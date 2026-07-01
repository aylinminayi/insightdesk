package insightdesk;

import java.util.Optional;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(
        name = "app.worker.enabled",
        havingValue = "true"
)
public class FeedbackAnalyzerWorker {

    private static final String FEEDBACK_QUEUE = "feedback:queue";
    private static final String PROCESSING_QUEUE = "feedback:processing";
    private static final String ATTEMPT_HASH = "feedback:attempts";
    private static final int MAX_ATTEMPTS = 3;

    private final FeedbackRepository feedbackRepository;
    private final StringRedisTemplate redisTemplate;
    private final GeminiAnalyzerService geminiAnalyzerService;
    private final DashboardService dashboardService;

    public FeedbackAnalyzerWorker(
            FeedbackRepository feedbackRepository,
            StringRedisTemplate redisTemplate,
            GeminiAnalyzerService geminiAnalyzerService,
            DashboardService dashboardService
    ) {
        this.feedbackRepository = feedbackRepository;
        this.redisTemplate = redisTemplate;
        this.geminiAnalyzerService = geminiAnalyzerService;
        this.dashboardService = dashboardService;
    }

    @PostConstruct
    public void recoverUnfinishedJobs() {
        while (true) {
            String feedbackId = redisTemplate.opsForList()
                    .rightPop(PROCESSING_QUEUE);

            if (feedbackId == null) {
                return;
            }

            redisTemplate.opsForList()
                    .rightPush(FEEDBACK_QUEUE, feedbackId);
        }
    }

    @Scheduled(fixedDelay = 1000)
    public void processNextFeedback() {
        String feedbackIdText = redisTemplate.opsForList()
                .rightPopAndLeftPush(
                        FEEDBACK_QUEUE,
                        PROCESSING_QUEUE
                );

        if (feedbackIdText == null) {
            return;
        }

        try {
            Long feedbackId = Long.parseLong(feedbackIdText);

            Optional<Feedback> feedbackOptional =
                    feedbackRepository.findById(feedbackId);

            if (feedbackOptional.isEmpty()) {
                removeFromProcessing(feedbackIdText);
                return;
            }

            Feedback feedback = feedbackOptional.get();

            if ("analyzed".equals(feedback.getStatus())) {
                removeFromProcessing(feedbackIdText);
                return;
            }

            feedback.setStatus("analyzing");
            feedbackRepository.save(feedback);
            dashboardService.clearDashboardCache();

            geminiAnalyzerService.analyze(feedback);

            feedbackRepository.save(feedback);
            dashboardService.clearDashboardCache();

            redisTemplate.opsForHash()
                    .delete(ATTEMPT_HASH, feedbackIdText);

            removeFromProcessing(feedbackIdText);

            System.out.println(
                    "Gemini analizi tamamlandı. Feedback ID: " + feedbackId
            );

        } catch (Exception exception) {
            handleFailure(feedbackIdText, exception);
        }
    }

    private void handleFailure(
            String feedbackIdText,
            Exception exception
    ) {
        System.err.println(
                "Worker hatası: " + exception.getMessage()
        );

        removeFromProcessing(feedbackIdText);

        Long attempts = redisTemplate.opsForHash()
                .increment(ATTEMPT_HASH, feedbackIdText, 1);

        if (attempts != null && attempts >= MAX_ATTEMPTS) {
            markAsFailed(feedbackIdText);
            return;
        }

        redisTemplate.opsForList()
                .rightPush(FEEDBACK_QUEUE, feedbackIdText);
    }

    private void markAsFailed(String feedbackIdText) {
        try {
            Long feedbackId = Long.parseLong(feedbackIdText);

            feedbackRepository.findById(feedbackId)
                    .ifPresent(feedback -> {
                        feedback.setStatus("failed");
                        feedbackRepository.save(feedback);
                    });

            dashboardService.clearDashboardCache();

            System.err.println(
                    "Feedback 3 denemeden sonra failed oldu: "
                            + feedbackIdText
            );

        } catch (NumberFormatException exception) {
            System.err.println(
                    "Geçersiz feedback ID: " + feedbackIdText
            );
        }
    }

    private void removeFromProcessing(String feedbackIdText) {
        redisTemplate.opsForList()
                .remove(PROCESSING_QUEUE, 1, feedbackIdText);
    }
}
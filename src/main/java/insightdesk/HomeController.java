package insightdesk;

import java.util.List;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    private static final String FEEDBACK_QUEUE = "feedback:queue";

    private final FeedbackRepository feedbackRepository;
    private final StringRedisTemplate redisTemplate;
    private final DashboardService dashboardService;

    public HomeController(
            FeedbackRepository feedbackRepository,
            StringRedisTemplate redisTemplate,
            DashboardService dashboardService
    ) {
        this.feedbackRepository = feedbackRepository;
        this.redisTemplate = redisTemplate;
        this.dashboardService = dashboardService;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/dashboard")
public String dashboard(
        @RequestParam(required = false) String status,
        @RequestParam(required = false) String category,
        @RequestParam(required = false) String sentiment,
        @RequestParam(required = false) String urgency,
        @RequestParam(defaultValue = "0") int page,
        Model model
) {
    var feedbackPage = dashboardService.getDashboardFeedback(
            status,
            category,
            sentiment,
            urgency,
            page
    );

    model.addAttribute("feedbackList", feedbackPage.getContent());
    model.addAttribute("currentPage", feedbackPage.getNumber());
    model.addAttribute("totalPages", feedbackPage.getTotalPages());

    model.addAttribute("status", status);
    model.addAttribute("category", category);
    model.addAttribute("sentiment", sentiment);
    model.addAttribute("urgency", urgency);

    return "dashboard";
}

    @PostMapping("/feedback")
    public String createFeedback(
            @RequestParam String title,
            @RequestParam String body,
            @RequestParam(required = false) String contactEmail
    ) {
        Feedback feedback = new Feedback(title, body, contactEmail);

        feedbackRepository.save(feedback);

        feedback.setStatus("queued");
        feedbackRepository.save(feedback);

        redisTemplate.opsForList()
                .rightPush(FEEDBACK_QUEUE, feedback.getId().toString());

        dashboardService.clearDashboardCache();

        return "redirect:/?success=true";
    }
}
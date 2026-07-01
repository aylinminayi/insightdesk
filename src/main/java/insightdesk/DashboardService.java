package insightdesk;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.Predicate;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    private static final String DASHBOARD_CACHE = "dashboard-feedback";

    private final FeedbackRepository feedbackRepository;

    public DashboardService(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    @Cacheable(
            value = DASHBOARD_CACHE,
            key = "#status + '-' + #category + '-' + #sentiment + '-' + #urgency + '-' + #page"
    )
    public Page<Feedback> getDashboardFeedback(
            String status,
            String category,
            String sentiment,
            String urgency,
            int page
    ) {
        System.out.println("Dashboard listesi PostgreSQL'den okunuyor.");

        Specification<Feedback> specification =
                buildSpecification(status, category, sentiment, urgency);

        PageRequest pageRequest = PageRequest.of(
                page,
                5,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        return feedbackRepository.findAll(specification, pageRequest);
    }

    @CacheEvict(value = DASHBOARD_CACHE, allEntries = true)
    public void clearDashboardCache() {
        System.out.println("Dashboard cache temizlendi.");
    }

    private Specification<Feedback> buildSpecification(
            String status,
            String category,
            String sentiment,
            String urgency
    ) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (hasText(status)) {
                predicates.add(
                        criteriaBuilder.equal(root.get("status"), status)
                );
            }

            if (hasText(category)) {
                predicates.add(
                        criteriaBuilder.equal(root.get("category"), category)
                );
            }

            if (hasText(sentiment)) {
                predicates.add(
                        criteriaBuilder.equal(root.get("sentiment"), sentiment)
                );
            }

            if (hasText(urgency)) {
                predicates.add(
                        criteriaBuilder.equal(root.get("urgency"), urgency)
                );
            }

            return criteriaBuilder.and(
                    predicates.toArray(new Predicate[0])
            );
        };
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
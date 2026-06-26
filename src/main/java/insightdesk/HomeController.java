package insightdesk;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    private final FeedbackRepository feedbackRepository;

    public HomeController(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @PostMapping("/feedback")
    public String createFeedback(
            @RequestParam String title,
            @RequestParam String body,
            @RequestParam(required = false) String contactEmail
    ) {
        Feedback feedback = new Feedback(title, body, contactEmail);

        feedbackRepository.save(feedback);

        return "redirect:/?success=true";
    }
}
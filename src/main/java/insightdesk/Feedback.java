package insightdesk;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "feedback")
public class Feedback implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String body;

    @Column(name = "contact_email")
    private String contactEmail;

    @Column(nullable = false)
    private String status = "received";

    @Column(name = "sentiment")
    private String sentiment;

    @Column(name = "category")
    private String category;

    @Column(name = "urgency")
    private String urgency;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(name = "suggested_reply", columnDefinition = "TEXT")
    private String suggestedReply;

    @Column(name = "model_used")
    private String modelUsed;

    @Column(name = "analyzed_at")
    private LocalDateTime analyzedAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Feedback() {
    }

    public Feedback(String title, String body, String contactEmail) {
        this.title = title;
        this.body = body;
        this.contactEmail = contactEmail;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public String getStatus() {
        return status;
    }

    public String getSentiment() {
        return sentiment;
    }

    public String getCategory() {
        return category;
    }

    public String getUrgency() {
        return urgency;
    }

    public String getSummary() {
        return summary;
    }

    public String getSuggestedReply() {
        return suggestedReply;
    }

    public String getModelUsed() {
        return modelUsed;
    }

    public LocalDateTime getAnalyzedAt() {
        return analyzedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAnalysis(
            String sentiment,
            String category,
            String urgency,
            String summary,
            String suggestedReply,
            String modelUsed
    ) {
        this.sentiment = sentiment;
        this.category = category;
        this.urgency = urgency;
        this.summary = summary;
        this.suggestedReply = suggestedReply;
        this.modelUsed = modelUsed;
        this.analyzedAt = LocalDateTime.now();
        this.status = "analyzed";
    }
}
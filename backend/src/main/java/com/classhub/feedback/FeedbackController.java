package com.classhub.feedback;

import com.classhub.feedback.dto.CriarFeedbackRequest;
import com.classhub.feedback.dto.FeedbackResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feedbacks")
@Tag(name = "Feedbacks")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FeedbackResponse criar(@Valid @RequestBody CriarFeedbackRequest request) {
        return feedbackService.criar(request);
    }

    @GetMapping("/{entregaId}")
    public FeedbackResponse buscarPorEntrega(@PathVariable Long entregaId) {
        return feedbackService.buscarPorEntrega(entregaId);
    }
}

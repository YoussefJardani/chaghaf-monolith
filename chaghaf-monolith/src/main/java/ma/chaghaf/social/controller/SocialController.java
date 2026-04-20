package ma.chaghaf.social.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.chaghaf.social.dto.SocialDtos.*;
import ma.chaghaf.social.service.SocialService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SocialController {

    private final SocialService service;

    @GetMapping("/api/posts")
    public ResponseEntity<Page<PostResponse>> getPosts(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Long userId = (Long) request.getAttribute("X-User-Id");
        return ResponseEntity.ok(service.getPosts(userId, page, size));
    }

    @PostMapping("/api/posts")
    public ResponseEntity<PostResponse> createPost(
            HttpServletRequest request,
            @Valid @RequestBody CreatePostRequest req) {
        Long userId   = (Long)   request.getAttribute("X-User-Id");
        String name   = (String) request.getAttribute("X-User-Name");
        String avatar = (String) request.getAttribute("X-User-Avatar");
        String role   = (String) request.getAttribute("X-User-Role");
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(service.createPost(userId,
                name   != null ? name   : "Membre",
                avatar != null ? avatar : "?",
                role   != null ? role   : "MEMBER",
                req));
    }

    @PostMapping("/api/posts/{id}/like")
    public ResponseEntity<PostResponse> toggleLike(
            HttpServletRequest request,
            @PathVariable Long id) {
        Long userId = (Long) request.getAttribute("X-User-Id");
        return ResponseEntity.ok(service.toggleLike(userId, id));
    }

    @PostMapping("/api/messages")
    public ResponseEntity<MessageResponse> sendMessage(
            HttpServletRequest request,
            @Valid @RequestBody SendMessageRequest req) {
        Long userId = (Long) request.getAttribute("X-User-Id");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.sendMessage(userId, req));
    }

    @GetMapping("/api/messages/{otherId}")
    public ResponseEntity<List<MessageResponse>> getConversation(
            HttpServletRequest request,
            @PathVariable Long otherId) {
        Long userId = (Long) request.getAttribute("X-User-Id");
        return ResponseEntity.ok(service.getConversation(userId, otherId));
    }
}

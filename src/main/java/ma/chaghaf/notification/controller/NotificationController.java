package ma.chaghaf.notification.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.chaghaf.notification.dto.NotificationDtos.*;
import ma.chaghaf.notification.service.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService service;

    @GetMapping
    public ResponseEntity<Page<NotificationResponse>> getNotifications(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Long userId = (Long) request.getAttribute("X-User-Id");
        return ResponseEntity.ok(service.getUserNotifications(userId, page, size));
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Map<String, Long>> unreadCount(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("X-User-Id");
        return ResponseEntity.ok(Map.of("count", service.getUnreadCount(userId)));
    }

    @PostMapping("/mark-read")
    public ResponseEntity<Map<String, String>> markAllRead(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("X-User-Id");
        service.markAllRead(userId);
        return ResponseEntity.ok(Map.of("message", "All notifications marked as read"));
    }

    @PostMapping("/send")
    public ResponseEntity<NotificationResponse> send(@Valid @RequestBody SendNotificationRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.send(req));
    }

    @PostMapping("/broadcast")
    public ResponseEntity<Map<String, String>> broadcast(@Valid @RequestBody BroadcastRequest req) {
        service.broadcast(req);
        return ResponseEntity.ok(Map.of("message", "Broadcast sent"));
    }
}

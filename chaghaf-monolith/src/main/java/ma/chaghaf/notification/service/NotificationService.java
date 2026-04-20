package ma.chaghaf.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.chaghaf.auth.repository.UserRepository;
import ma.chaghaf.notification.dto.NotificationDtos.*;
import ma.chaghaf.notification.entity.Notification;
import ma.chaghaf.notification.repository.NotificationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notifRepo;
    private final FirebaseService firebaseService;
    private final UserRepository userRepository; // appel direct dans le monolithe

    public Page<NotificationResponse> getUserNotifications(Long userId, int page, int size) {
        return notifRepo.findByUserIdOrderByCreatedAtDesc(userId, PageRequest.of(page, size))
            .map(this::toResponse);
    }

    public long getUnreadCount(Long userId) {
        return notifRepo.countByUserIdAndReadFalse(userId);
    }

    @Transactional
    public void markAllRead(Long userId) {
        notifRepo.markAllReadByUserId(userId);
    }

    @Transactional
    public NotificationResponse send(SendNotificationRequest req) {
        Notification notif = notifRepo.save(Notification.builder()
            .userId(req.getUserId())
            .title(req.getTitle())
            .body(req.getBody())
            .type(req.getType())
            .data(req.getData())
            .read(false)
            .build());

        // Envoyer le push Firebase si l'utilisateur a un FCM token
        userRepository.findById(req.getUserId()).ifPresent(user -> {
            if (user.getFcmToken() != null) {
                firebaseService.sendPushNotification(user.getFcmToken(), req.getTitle(), req.getBody());
            }
        });

        log.info("Notification envoyée à userId={}", req.getUserId());
        return toResponse(notif);
    }

    @Transactional
    public void broadcast(BroadcastRequest req) {
        List<Long> targetIds = req.getUserIds();

        if (targetIds == null || targetIds.isEmpty()) {
            // Envoyer à tous les utilisateurs actifs
            targetIds = userRepository.findAll().stream()
                .filter(u -> Boolean.TRUE.equals(u.getActive()))
                .map(u -> u.getId())
                .toList();
        }

        for (Long userId : targetIds) {
            send(new SendNotificationRequest(userId, req.getTitle(), req.getBody(), req.getType(), null));
        }

        log.info("Broadcast envoyé à {} utilisateurs", targetIds.size());
    }

    private NotificationResponse toResponse(Notification n) {
        return new NotificationResponse(n.getId(), n.getUserId(), n.getTitle(),
            n.getBody(), n.getType(), n.getData(), n.getRead(), n.getCreatedAt());
    }
}

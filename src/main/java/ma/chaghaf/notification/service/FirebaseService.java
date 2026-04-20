package ma.chaghaf.notification.service;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FirebaseService {

    public void sendPushNotification(String fcmToken, String title, String body) {
        if (fcmToken == null || fcmToken.isBlank()) return;
        if (FirebaseApp.getApps().isEmpty()) {
            log.warn("Firebase non initialisé, push ignoré pour token: {}...", fcmToken.substring(0, Math.min(10, fcmToken.length())));
            return;
        }

        try {
            Message message = Message.builder()
                .setToken(fcmToken)
                .setNotification(Notification.builder()
                    .setTitle(title)
                    .setBody(body)
                    .build())
                .build();

            String response = FirebaseMessaging.getInstance().send(message);
            log.info("Push envoyé: {}", response);
        } catch (Exception e) {
            log.error("Erreur envoi push: {}", e.getMessage());
        }
    }
}

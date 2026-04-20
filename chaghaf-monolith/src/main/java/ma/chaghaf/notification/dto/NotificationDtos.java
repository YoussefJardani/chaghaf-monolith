package ma.chaghaf.notification.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public class NotificationDtos {

    public record SendNotificationRequest(
        @NotNull Long userId,
        @NotBlank String title,
        @NotBlank String body,
        String type,
        String data
    ) {
        public Long getUserId()  { return userId; }
        public String getTitle() { return title; }
        public String getBody()  { return body; }
        public String getType()  { return type; }
        public String getData()  { return data; }
    }

    public record BroadcastRequest(
        @NotBlank String title,
        @NotBlank String body,
        String type,
        List<Long> userIds   // null = tous les utilisateurs
    ) {
        public String getTitle()     { return title; }
        public String getBody()      { return body; }
        public String getType()      { return type; }
        public List<Long> getUserIds() { return userIds; }
    }

    public record NotificationResponse(
        Long id,
        Long userId,
        String title,
        String body,
        String type,
        String data,
        boolean read,
        LocalDateTime createdAt
    ) {}
}

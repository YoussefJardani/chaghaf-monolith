package ma.chaghaf.social.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class SocialDtos {

    public record CreatePostRequest(@NotBlank String content) {
        public String getContent() { return content; }
    }

    public record SendMessageRequest(
        @NotNull Long receiverId,
        @NotBlank String content
    ) {
        public Long getReceiverId() { return receiverId; }
        public String getContent() { return content; }
    }

    public record PostResponse(
        Long id,
        Long userId,
        String userName,
        String userAvatar,
        String userRole,
        String content,
        int likesCount,
        boolean likedByMe,
        LocalDateTime createdAt
    ) {}

    public record MessageResponse(
        Long id,
        Long senderId,
        Long receiverId,
        String content,
        boolean read,
        LocalDateTime sentAt
    ) {}
}

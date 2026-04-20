package ma.chaghaf.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthDtos {

    public record RegisterRequest(
        @NotBlank String fullName,
        @NotBlank @Email String email,
        String phone,
        @NotBlank @Size(min = 6) String password,
        String fcmToken
    ) {
        public String getFullName() { return fullName; }
        public String getEmail() { return email; }
        public String getPhone() { return phone; }
        public String getPassword() { return password; }
        public String getFcmToken() { return fcmToken; }
    }

    public record LoginRequest(
        @NotBlank @Email String email,
        @NotBlank String password,
        String fcmToken
    ) {
        public String getEmail() { return email; }
        public String getPassword() { return password; }
        public String getFcmToken() { return fcmToken; }
    }

    public record RefreshRequest(@NotBlank String refreshToken) {
        public String getRefreshToken() { return refreshToken; }
    }

    public record FcmTokenRequest(@NotBlank String fcmToken) {
        public String getFcmToken() { return fcmToken; }
    }

    public record AuthResponse(
        Long id,
        String fullName,
        String email,
        String phone,
        String avatarLetter,
        String role,
        String accessToken,
        String refreshToken,
        long expiresIn
    ) {}

    public record UserProfileResponse(
        Long id,
        String fullName,
        String email,
        String phone,
        String avatarLetter,
        String role,
        Boolean active,
        String memberSince
    ) {}
}

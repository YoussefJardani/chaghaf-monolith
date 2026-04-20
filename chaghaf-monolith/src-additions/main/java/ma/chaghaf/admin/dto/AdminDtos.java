package ma.chaghaf.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class AdminDtos {

    // ─── Occupation ──────────────────────────────────────────────
    public record OccupationStats(
        int totalCapacity,
        int currentOccupied,
        double occupationPercent,
        long todayReservations,
        long todayDayAccess,
        long activeSubscriptions,
        List<RoomOccupation> rooms,
        LocalDateTime updatedAt
    ) {}

    public record RoomOccupation(
        String salleId,
        String salleName,
        String emoji,
        String capacity,
        long reservationsToday,
        boolean occupied
    ) {}

    // ─── QR ──────────────────────────────────────────────────────
    public record QrValidationResult(
        boolean valid,
        String token,
        Long userId,
        String memberName,
        String memberEmail,
        String memberAvatar,
        String accessType,
        LocalDate accessDate,
        boolean alreadyUsed,
        String message,
        LocalDateTime scannedAt
    ) {}

    // ─── Messaging ───────────────────────────────────────────────
    public record SendMessageRequest(
        @NotNull  Long targetUserId,
        @NotBlank String title,
        @NotBlank String body,
        String type              // defaults to SYSTEM
    ) {
        public Long getTargetUserId() { return targetUserId; }
        public String getTitle()      { return title; }
        public String getBody()       { return body; }
        public String getType()       { return type != null ? type : "SYSTEM"; }
    }

    // ─── Admin Social Post ────────────────────────────────────────
    public record CreateAdminPostRequest(
        @NotBlank String content,
        boolean notifyAll           // send push/in-app notification to all users
    ) {
        public String getContent()  { return content; }
        public boolean isNotifyAll(){ return notifyAll; }
    }

    // ─── Live Stats ───────────────────────────────────────────────
    public record LiveStats(
        long totalUsers,
        long activeUsers,
        long activeSubscriptions,
        long todayReservations,
        long todayDayAccess,
        long pendingSnackOrders,
        long unreadNotifications,
        int  connectedErpClients,
        LocalDateTime timestamp
    ) {}

    // ─── Client Management ────────────────────────────────────────
    public record ClientDetail(
        Long id,
        String fullName,
        String email,
        String phone,
        String role,
        String avatarLetter,
        Boolean active,
        String memberSince,
        boolean hasFcmToken,
        Object activeSubscription
    ) {}

    public record ClientUpdateRequest(
        String fullName,
        String phone,
        Boolean active,
        String newPassword
    ) {}

    // ─── Catalog Item ─────────────────────────────────────────────
    public record CatalogItemDto(
        Long id,
        String type,          // BOISSON | SNACK
        String name,
        String description,
        BigDecimal price,
        String emoji,
        String imageBase64,
        String imageMimeType,
        Boolean available,
        Integer stockQuantity,
        String category,
        LocalDateTime createdAt
    ) {}

    public record SaveCatalogItemRequest(
        @NotBlank String type,
        @NotBlank String name,
        String description,
        @NotNull BigDecimal price,
        String emoji,
        String imageBase64,    // full data-url or raw base64
        String imageMimeType,
        Boolean available,
        Integer stockQuantity,
        String category
    ) {}
}

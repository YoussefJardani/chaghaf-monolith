package ma.chaghaf.subscription.dto;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SubscriptionDtos {

    public record SubscribeRequest(
        @NotBlank String packType,
        @NotBlank String duration
    ) {
        public String getPackType() { return packType; }
        public String getDuration() { return duration; }
    }

    public record DayAccessRequest(@NotBlank String accessType) {
        public String getAccessType() { return accessType; }
    }

    public record SubscriptionResponse(
        Long id, Long userId,
        String packType, String duration,
        String status, BigDecimal price,
        LocalDate startDate, LocalDate endDate,
        Integer personsCount, long daysLeft
    ) {}

    public record DayAccessResponse(
        Long id, Long userId,
        String accessType, BigDecimal price,
        LocalDate accessDate, Boolean used, String qrToken
    ) {}
}

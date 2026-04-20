package ma.chaghaf.reservation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ReservationDtos {

    public record CreateReservationRequest(
        @NotBlank String salleId,
        @NotNull LocalDate reservationDate,
        @NotBlank String duration
    ) {
        public String getSalleId() { return salleId; }
        public LocalDate getReservationDate() { return reservationDate; }
        public String getDuration() { return duration; }
    }

    public record ReservationResponse(
        Long id, Long userId,
        String salleId, String salleName,
        LocalDate reservationDate, String duration,
        BigDecimal price, String status,
        LocalDateTime createdAt
    ) {}

    public record SalleInfo(
        String id, String name, String icon,
        String capacity, List<String> features,
        BigDecimal halfDayPrice, BigDecimal fullDayPrice
    ) {}
}

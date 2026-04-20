package ma.chaghaf.boisson.dto;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class BoissonDtos {

    public record ConsumeBoissonRequest(@NotBlank String boissonType) {
        public String getBoissonType() { return boissonType; }
    }

    public record BoissonSessionResponse(
        Long id, Long userId,
        String boissonType, String boissonName,
        BigDecimal price, LocalDateTime consumedAt
    ) {}

    public record BoissonInfo(
        String id, String name, String icon,
        String description, BigDecimal price, boolean available
    ) {}

    public record CafeGuideStep(int step, String title, String description, String icon) {}
}

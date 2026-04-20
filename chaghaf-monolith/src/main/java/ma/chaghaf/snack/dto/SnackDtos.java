package ma.chaghaf.snack.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class SnackDtos {

    public record CreateOrderRequest(@NotEmpty List<OrderItemRequest> items) {
        public List<OrderItemRequest> getItems() { return items; }
    }

    public record OrderItemRequest(
        @NotBlank String snackId,
        @Min(1) int quantity
    ) {
        public String getSnackId() { return snackId; }
        public int getQuantity() { return quantity; }
    }

    public record UpdateStatusRequest(String status) {
        public String getStatus() { return status; }
    }

    public record OrderResponse(
        Long id, Long userId,
        List<OrderItemResponse> items,
        BigDecimal total, String status,
        LocalDateTime createdAt
    ) {}

    public record OrderItemResponse(
        String snackId, String snackName,
        int quantity, BigDecimal unitPrice, BigDecimal subtotal
    ) {}

    public record SnackInfo(
        String id, String name, String icon,
        String description, BigDecimal price,
        String category, boolean available
    ) {}
}

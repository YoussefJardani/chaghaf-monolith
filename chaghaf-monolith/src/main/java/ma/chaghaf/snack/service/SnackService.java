package ma.chaghaf.snack.service;

import lombok.RequiredArgsConstructor;
import ma.chaghaf.snack.dto.SnackDtos.*;
import ma.chaghaf.snack.entity.OrderItem;
import ma.chaghaf.snack.entity.SnackOrder;
import ma.chaghaf.snack.repository.SnackOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SnackService {

    private final SnackOrderRepository repo;

    private static final Map<String, SnackInfo> CATALOG = Map.of(
        "SANDWICH",  new SnackInfo("SANDWICH",  "Sandwich",     "🥪", "Sandwich mixte",       new BigDecimal("25"), "Salé",  true),
        "CROISSANT", new SnackInfo("CROISSANT", "Croissant",    "🥐", "Croissant beurre",     new BigDecimal("12"), "Sucré", true),
        "BROWNIE",   new SnackInfo("BROWNIE",   "Brownie",      "🍫", "Brownie chocolat",     new BigDecimal("15"), "Sucré", true),
        "SALADE",    new SnackInfo("SALADE",    "Salade",       "🥗", "Salade fraîche",       new BigDecimal("30"), "Salé",  true),
        "CHIPS",     new SnackInfo("CHIPS",     "Chips",        "🍿", "Chips croustillantes", new BigDecimal("10"), "Salé",  true)
    );

    public List<SnackInfo> getCatalog() {
        return List.copyOf(CATALOG.values());
    }

    @Transactional
    public OrderResponse createOrder(Long userId, CreateOrderRequest req) {
        SnackOrder order = SnackOrder.builder().userId(userId).total(BigDecimal.ZERO).build();
        order = repo.save(order);

        final SnackOrder savedOrder = order;
        List<OrderItem> items = req.getItems().stream().map(itemReq -> {
            SnackInfo info = CATALOG.get(itemReq.getSnackId().toUpperCase());
            if (info == null) throw new IllegalArgumentException("Snack inconnu: " + itemReq.getSnackId());

            BigDecimal subtotal = info.price().multiply(new BigDecimal(itemReq.getQuantity()));
            return OrderItem.builder()
                .order(savedOrder)
                .snackId(itemReq.getSnackId().toUpperCase())
                .snackName(info.name())
                .quantity(itemReq.getQuantity())
                .unitPrice(info.price())
                .subtotal(subtotal)
                .build();
        }).toList();

        order.setItems(items);
        BigDecimal total = items.stream().map(OrderItem::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotal(total);
        return toResponse(repo.save(order));
    }

    public List<OrderResponse> getUserOrders(Long userId) {
        return repo.findByUserIdOrderByCreatedAtDesc(userId).stream().map(this::toResponse).toList();
    }

    public OrderResponse getOrder(Long id) {
        return repo.findById(id).map(this::toResponse)
            .orElseThrow(() -> new IllegalArgumentException("Order not found"));
    }

    @Transactional
    public OrderResponse updateStatus(Long id, String status) {
        SnackOrder order = repo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        order.setStatus(SnackOrder.Status.valueOf(status.toUpperCase()));
        return toResponse(repo.save(order));
    }

    private OrderResponse toResponse(SnackOrder o) {
        List<OrderItemResponse> items = o.getItems() == null ? List.of() :
            o.getItems().stream().map(i ->
                new OrderItemResponse(i.getSnackId(), i.getSnackName(),
                    i.getQuantity(), i.getUnitPrice(), i.getSubtotal())
            ).toList();
        return new OrderResponse(o.getId(), o.getUserId(), items, o.getTotal(),
            o.getStatus().name(), o.getCreatedAt());
    }
}

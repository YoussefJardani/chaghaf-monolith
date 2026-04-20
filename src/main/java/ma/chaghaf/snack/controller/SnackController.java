package ma.chaghaf.snack.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.chaghaf.snack.dto.SnackDtos.*;
import ma.chaghaf.snack.service.SnackService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/snacks")
@RequiredArgsConstructor
public class SnackController {

    private final SnackService service;

    @GetMapping("/catalog")
    public ResponseEntity<List<SnackInfo>> getCatalog() {
        return ResponseEntity.ok(service.getCatalog());
    }

    @PostMapping("/orders")
    public ResponseEntity<OrderResponse> createOrder(
            HttpServletRequest request,
            @Valid @RequestBody CreateOrderRequest req) {
        Long userId = (Long) request.getAttribute("X-User-Id");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createOrder(userId, req));
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponse>> getUserOrders(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("X-User-Id");
        return ResponseEntity.ok(service.getUserOrders(userId));
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok(service.getOrder(id));
    }

    @PatchMapping("/orders/{id}/status")
    public ResponseEntity<OrderResponse> updateStatus(
            @PathVariable Long id,
            @RequestBody UpdateStatusRequest req) {
        return ResponseEntity.ok(service.updateStatus(id, req.getStatus()));
    }
}

package ma.chaghaf.reservation.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.chaghaf.reservation.dto.ReservationDtos.*;
import ma.chaghaf.reservation.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService service;

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getUserReservations(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("X-User-Id");
        return ResponseEntity.ok(service.getUserReservations(userId));
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> create(
            HttpServletRequest request,
            @Valid @RequestBody CreateReservationRequest req) {
        Long userId = (Long) request.getAttribute("X-User-Id");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(userId, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> cancel(
            HttpServletRequest request,
            @PathVariable Long id) {
        Long userId = (Long) request.getAttribute("X-User-Id");
        service.cancel(userId, id);
        return ResponseEntity.ok(Map.of("message", "Reservation cancelled"));
    }

    @GetMapping("/salles")
    public ResponseEntity<List<SalleInfo>> getSalles() {
        List<SalleInfo> salles = List.of(
            new SalleInfo("s1", "Salle de Réunion", "🏛️", "1–8 personnes",
                List.of("Projecteur", "WiFi", "AC"), new BigDecimal("20"), new BigDecimal("30")),
            new SalleInfo("s2", "Salle Photo", "📸", "1–3 personnes",
                List.of("Fond blanc", "Éclairage", "Trépied"), new BigDecimal("20"), new BigDecimal("30")),
            new SalleInfo("s3", "Studio Podcast", "🎙️", "1–4 personnes",
                List.of("Micro", "Casques", "Isolation"), new BigDecimal("20"), new BigDecimal("30"))
        );
        return ResponseEntity.ok(salles);
    }
}

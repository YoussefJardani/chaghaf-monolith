package ma.chaghaf.boisson.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.chaghaf.boisson.dto.BoissonDtos.*;
import ma.chaghaf.boisson.service.BoissonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boissons")
@RequiredArgsConstructor
public class BoissonController {

    private final BoissonService service;

    @GetMapping
    public ResponseEntity<List<BoissonInfo>> getBoissons() {
        return ResponseEntity.ok(service.getAvailableBoissons());
    }

    @PostMapping("/consume")
    public ResponseEntity<BoissonSessionResponse> consume(
            HttpServletRequest request,
            @Valid @RequestBody ConsumeBoissonRequest req) {
        Long userId = (Long) request.getAttribute("X-User-Id");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.consume(userId, req));
    }

    @GetMapping("/history")
    public ResponseEntity<List<BoissonSessionResponse>> history(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("X-User-Id");
        return ResponseEntity.ok(service.getHistory(userId));
    }

    @GetMapping("/cafe-guide")
    public ResponseEntity<List<CafeGuideStep>> cafeGuide() {
        return ResponseEntity.ok(service.getCafeGuide());
    }
}

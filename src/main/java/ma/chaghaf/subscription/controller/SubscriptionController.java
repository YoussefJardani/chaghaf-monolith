package ma.chaghaf.subscription.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.chaghaf.subscription.dto.SubscriptionDtos.*;
import ma.chaghaf.subscription.service.SubscriptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService service;

    @GetMapping("/active")
    public ResponseEntity<SubscriptionResponse> getActive(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("X-User-Id");
        return ResponseEntity.ok(service.getActiveSubscription(userId));
    }

    @GetMapping("/history")
    public ResponseEntity<List<SubscriptionResponse>> getHistory(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("X-User-Id");
        return ResponseEntity.ok(service.getUserHistory(userId));
    }

    @PostMapping
    public ResponseEntity<SubscriptionResponse> subscribe(
            HttpServletRequest request,
            @Valid @RequestBody SubscribeRequest req) {
        Long userId = (Long) request.getAttribute("X-User-Id");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.subscribe(userId, req));
    }

    @PostMapping("/renew")
    public ResponseEntity<SubscriptionResponse> renew(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("X-User-Id");
        return ResponseEntity.ok(service.renew(userId));
    }

    @PostMapping("/day-access")
    public ResponseEntity<DayAccessResponse> purchaseDayAccess(
            HttpServletRequest request,
            @Valid @RequestBody DayAccessRequest req) {
        Long userId = (Long) request.getAttribute("X-User-Id");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.purchaseDayAccess(userId, req));
    }

    @GetMapping("/packs")
    public ResponseEntity<?> getAvailablePacks() {
        var packs = new ArrayList<>();

        var p1 = new HashMap<String, Object>();
        p1.put("id", "ONE_PERSON");
        p1.put("name", "1 Personne");
        p1.put("monthlyPrice", 350);
        p1.put("annualPrice", 3500);
        p1.put("personsCount", 1);
        p1.put("features", List.of("Accès illimité", "1 boisson/session", "WiFi premium"));
        p1.put("popular", false);
        packs.add(p1);

        var p2 = new HashMap<String, Object>();
        p2.put("id", "TWO_PERSONS");
        p2.put("name", "2 Personnes");
        p2.put("monthlyPrice", 600);
        p2.put("annualPrice", 6000);
        p2.put("personsCount", 2);
        p2.put("features", List.of("Accès illimité", "2 boissons/session", "WiFi premium", "Réservations prioritaires"));
        p2.put("popular", true);
        packs.add(p2);

        var p3 = new HashMap<String, Object>();
        p3.put("id", "THREE_PERSONS");
        p3.put("name", "3 Personnes");
        p3.put("monthlyPrice", 800);
        p3.put("annualPrice", 8000);
        p3.put("personsCount", 3);
        p3.put("features", List.of("Accès illimité", "3 boissons/session", "WiFi premium", "Réservations prioritaires", "Salle podcast 1h/mois"));
        p3.put("popular", false);
        packs.add(p3);

        return ResponseEntity.ok(packs);
    }
}

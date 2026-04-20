package ma.chaghaf.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.chaghaf.auth.dto.AuthDtos.*;
import ma.chaghaf.auth.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(req));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
        return ResponseEntity.ok(authService.login(req));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@Valid @RequestBody RefreshRequest req) {
        return ResponseEntity.ok(authService.refresh(req));
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getProfile(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("X-User-Id");
        return ResponseEntity.ok(authService.getProfile(userId));
    }

    @PutMapping("/fcm-token")
    public ResponseEntity<Map<String, String>> updateFcmToken(
            HttpServletRequest request,
            @Valid @RequestBody FcmTokenRequest req) {
        Long userId = (Long) request.getAttribute("X-User-Id");
        authService.updateFcmToken(userId, req);
        return ResponseEntity.ok(Map.of("message", "FCM token updated"));
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "chaghaf-monolith"));
    }
}

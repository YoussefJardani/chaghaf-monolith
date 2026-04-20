package ma.chaghaf.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.chaghaf.auth.dto.AuthDtos.*;
import ma.chaghaf.auth.entity.User;
import ma.chaghaf.auth.repository.UserRepository;
import ma.chaghaf.auth.security.JwtService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public AuthResponse register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("Email already registered: " + req.getEmail());
        }
        if (req.getPhone() != null && userRepository.existsByPhone(req.getPhone())) {
            throw new IllegalArgumentException("Phone already registered: " + req.getPhone());
        }

        String avatarLetter = req.getFullName().trim().isEmpty() ? "?"
            : String.valueOf(req.getFullName().trim().charAt(0)).toUpperCase();

        User user = User.builder()
            .fullName(req.getFullName())
            .email(req.getEmail().toLowerCase())
            .phone(req.getPhone())
            .password(passwordEncoder.encode(req.getPassword()))
            .avatarLetter(avatarLetter)
            .fcmToken(req.getFcmToken())
            .role(User.Role.MEMBER)
            .active(true)
            .build();

        user = userRepository.save(user);
        log.info("New user registered: {}", user.getEmail());
        return buildAuthResponse(user);
    }

    @Transactional
    public AuthResponse login(LoginRequest req) {
        User user = userRepository.findByEmail(req.getEmail().toLowerCase())
            .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        if (!user.getActive()) throw new BadCredentialsException("Account is deactivated");

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        if (req.getFcmToken() != null && !req.getFcmToken().isBlank()) {
            user.setFcmToken(req.getFcmToken());
            userRepository.save(user);
        }

        log.info("User logged in: {}", user.getEmail());
        return buildAuthResponse(user);
    }

    public AuthResponse refresh(RefreshRequest req) {
        String token = req.getRefreshToken();
        if (!jwtService.isTokenValid(token) || !jwtService.isRefreshToken(token)) {
            throw new BadCredentialsException("Invalid refresh token");
        }
        Long userId = Long.parseLong(jwtService.extractUserId(token));
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BadCredentialsException("User not found"));
        return buildAuthResponse(user);
    }

    @Transactional
    public void updateFcmToken(Long userId, FcmTokenRequest req) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setFcmToken(req.getFcmToken());
        userRepository.save(user);
    }

    public UserProfileResponse getProfile(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        String memberSince = user.getCreatedAt() != null
            ? user.getCreatedAt().format(DateTimeFormatter.ofPattern("MMMM yyyy")) : "N/A";
        return new UserProfileResponse(user.getId(), user.getFullName(), user.getEmail(),
            user.getPhone(), user.getAvatarLetter(), user.getRole().name(), user.getActive(), memberSince);
    }

    private AuthResponse buildAuthResponse(User user) {
        return new AuthResponse(user.getId(), user.getFullName(), user.getEmail(),
            user.getPhone(), user.getAvatarLetter(), user.getRole().name(),
            jwtService.generateAccessToken(user), jwtService.generateRefreshToken(user),
            jwtService.getAccessExpiration() / 1000);
    }
}

package ma.chaghaf.boisson.service;

import lombok.RequiredArgsConstructor;
import ma.chaghaf.boisson.dto.BoissonDtos.*;
import ma.chaghaf.boisson.entity.BoissonSession;
import ma.chaghaf.boisson.repository.BoissonSessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BoissonService {

    private final BoissonSessionRepository repo;

    private static final Map<String, BoissonInfo> CATALOG = Map.of(
        "CAFE",     new BoissonInfo("CAFE",     "Café",          "☕", "Espresso maison",         new BigDecimal("10"), true),
        "THE",      new BoissonInfo("THE",      "Thé",           "🍵", "Thé à la menthe",         new BigDecimal("8"),  true),
        "JUS",      new BoissonInfo("JUS",      "Jus frais",     "🍊", "Jus d'orange pressé",     new BigDecimal("15"), true),
        "EAU",      new BoissonInfo("EAU",      "Eau minérale",  "💧", "Eau fraîche",             new BigDecimal("5"),  true),
        "SMOOTHIE", new BoissonInfo("SMOOTHIE", "Smoothie",      "🥤", "Fruits de saison mixés",  new BigDecimal("20"), true)
    );

    public List<BoissonInfo> getAvailableBoissons() {
        return List.copyOf(CATALOG.values());
    }

    @Transactional
    public BoissonSessionResponse consume(Long userId, ConsumeBoissonRequest req) {
        BoissonInfo info = CATALOG.get(req.getBoissonType().toUpperCase());
        if (info == null) throw new IllegalArgumentException("Boisson inconnue: " + req.getBoissonType());

        BoissonSession session = repo.save(BoissonSession.builder()
            .userId(userId)
            .boissonType(req.getBoissonType().toUpperCase())
            .boissonName(info.name())
            .price(info.price())
            .build());

        return toResponse(session);
    }

    public List<BoissonSessionResponse> getHistory(Long userId) {
        return repo.findByUserIdOrderByConsumedAtDesc(userId).stream().map(this::toResponse).toList();
    }

    public List<CafeGuideStep> getCafeGuide() {
        return List.of(
            new CafeGuideStep(1, "Choisir votre boisson", "Sélectionnez parmi notre carte", "☕"),
            new CafeGuideStep(2, "Commander", "Appuyez sur consommer", "📱"),
            new CafeGuideStep(3, "Se diriger au comptoir", "Montrez votre confirmation", "🏃"),
            new CafeGuideStep(4, "Déguster", "Profitez de votre boisson !", "😊")
        );
    }

    private BoissonSessionResponse toResponse(BoissonSession s) {
        return new BoissonSessionResponse(s.getId(), s.getUserId(),
            s.getBoissonType(), s.getBoissonName(), s.getPrice(), s.getConsumedAt());
    }
}

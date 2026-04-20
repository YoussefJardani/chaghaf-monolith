package ma.chaghaf.reservation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.chaghaf.reservation.dto.ReservationDtos.*;
import ma.chaghaf.reservation.entity.Reservation;
import ma.chaghaf.reservation.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository repo;

    private static final Map<String, String> SALLE_NAMES = Map.of(
        "s1", "Salle de Réunion",
        "s2", "Salle Photo",
        "s3", "Studio Podcast"
    );

    public List<ReservationResponse> getUserReservations(Long userId) {
        return repo.findByUserId(userId).stream().map(this::toResponse).toList();
    }

    @Transactional
    public ReservationResponse create(Long userId, CreateReservationRequest req) {
        Reservation.Duration dur = Reservation.Duration.valueOf(req.getDuration());
        BigDecimal price = dur == Reservation.Duration.HALF_DAY
            ? new BigDecimal("20.00") : new BigDecimal("30.00");

        Reservation r = repo.save(Reservation.builder()
            .userId(userId)
            .salleId(req.getSalleId())
            .salleName(SALLE_NAMES.getOrDefault(req.getSalleId(), req.getSalleId()))
            .reservationDate(req.getReservationDate())
            .duration(dur)
            .price(price)
            .status(Reservation.Status.CONFIRMED)
            .build());

        log.info("Reservation {} created for user {}", r.getId(), userId);
        return toResponse(r);
    }

    @Transactional
    public void cancel(Long userId, Long reservationId) {
        Reservation r = repo.findById(reservationId)
            .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
        if (!r.getUserId().equals(userId))
            throw new IllegalArgumentException("Not your reservation");
        r.setStatus(Reservation.Status.CANCELLED);
        repo.save(r);
    }

    private ReservationResponse toResponse(Reservation r) {
        return new ReservationResponse(r.getId(), r.getUserId(), r.getSalleId(),
            r.getSalleName(), r.getReservationDate(), r.getDuration().name(),
            r.getPrice(), r.getStatus().name(), r.getCreatedAt());
    }
}

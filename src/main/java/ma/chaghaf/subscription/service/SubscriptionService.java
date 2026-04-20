package ma.chaghaf.subscription.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.chaghaf.subscription.dto.SubscriptionDtos.*;
import ma.chaghaf.subscription.entity.DayAccess;
import ma.chaghaf.subscription.entity.Subscription;
import ma.chaghaf.subscription.repository.DayAccessRepository;
import ma.chaghaf.subscription.repository.SubscriptionRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepo;
    private final DayAccessRepository dayAccessRepo;

    public SubscriptionResponse getActiveSubscription(Long userId) {
        return subscriptionRepo.findByUserIdAndStatus(userId, Subscription.Status.ACTIVE)
            .map(this::toResponse)
            .orElseThrow(() -> new IllegalArgumentException("No active subscription found"));
    }

    public List<SubscriptionResponse> getUserHistory(Long userId) {
        return subscriptionRepo.findByUserId(userId).stream().map(this::toResponse).toList();
    }

    @Transactional
    public SubscriptionResponse subscribe(Long userId, SubscribeRequest req) {
        subscriptionRepo.findByUserIdAndStatus(userId, Subscription.Status.ACTIVE)
            .ifPresent(s -> { s.setStatus(Subscription.Status.CANCELLED); subscriptionRepo.save(s); });

        Subscription.PackType packType = Subscription.PackType.valueOf(req.getPackType());
        Subscription.Duration duration = Subscription.Duration.valueOf(req.getDuration());
        var price = duration == Subscription.Duration.MONTHLY ? packType.getMonthlyPrice() : packType.getAnnualPrice();

        LocalDate start = LocalDate.now();
        LocalDate end = duration == Subscription.Duration.MONTHLY ? start.plusMonths(1) : start.plusYears(1);

        Subscription sub = subscriptionRepo.save(Subscription.builder()
            .userId(userId).packType(packType).duration(duration).price(price)
            .startDate(start).endDate(end).personsCount(packType.getPersonsCount())
            .status(Subscription.Status.ACTIVE).build());

        log.info("User {} subscribed to {} {}", userId, packType, duration);
        return toResponse(sub);
    }

    @Transactional
    public SubscriptionResponse renew(Long userId) {
        Subscription current = subscriptionRepo.findByUserIdAndStatus(userId, Subscription.Status.ACTIVE)
            .orElseThrow(() -> new IllegalArgumentException("No active subscription to renew"));

        LocalDate newStart = current.getEndDate().isAfter(LocalDate.now()) ? current.getEndDate() : LocalDate.now();
        LocalDate newEnd = current.getDuration() == Subscription.Duration.MONTHLY
            ? newStart.plusMonths(1) : newStart.plusYears(1);

        current.setStartDate(newStart);
        current.setEndDate(newEnd);
        current.setStatus(Subscription.Status.ACTIVE);
        subscriptionRepo.save(current);
        return toResponse(current);
    }

    @Transactional
    public DayAccessResponse purchaseDayAccess(Long userId, DayAccessRequest req) {
        DayAccess.AccessType type = DayAccess.AccessType.valueOf(req.getAccessType());
        String qrToken = "CHAGHAF-DAY-" + userId + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        DayAccess access = dayAccessRepo.save(DayAccess.builder()
            .userId(userId).accessType(type).price(type.getPrice())
            .accessDate(LocalDate.now()).qrToken(qrToken).used(false).build());

        return toDayAccessResponse(access);
    }

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void expireSubscriptions() {
        List<Subscription> expired = subscriptionRepo.findExpiredSubscriptions(LocalDate.now());
        expired.forEach(s -> s.setStatus(Subscription.Status.EXPIRED));
        subscriptionRepo.saveAll(expired);
        if (!expired.isEmpty()) log.info("Expired {} subscriptions", expired.size());
    }

    private SubscriptionResponse toResponse(Subscription s) {
        return new SubscriptionResponse(s.getId(), s.getUserId(), s.getPackType().name(),
            s.getDuration().name(), s.getStatus().name(), s.getPrice(),
            s.getStartDate(), s.getEndDate(), s.getPersonsCount(), s.getDaysLeft());
    }

    private DayAccessResponse toDayAccessResponse(DayAccess d) {
        return new DayAccessResponse(d.getId(), d.getUserId(), d.getAccessType().name(),
            d.getPrice(), d.getAccessDate(), d.getUsed(), d.getQrToken());
    }
}

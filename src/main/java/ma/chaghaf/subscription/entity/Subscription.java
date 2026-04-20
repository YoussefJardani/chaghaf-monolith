package ma.chaghaf.subscription.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "subscriptions")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PackType packType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Duration duration;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private Status status = Status.ACTIVE;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    private Integer personsCount;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public long getDaysLeft() {
        if (endDate == null) return 0;
        long days = ChronoUnit.DAYS.between(LocalDate.now(), endDate);
        return Math.max(0, days);
    }

    public enum PackType {
        ONE_PERSON(350, 3500, 1),
        TWO_PERSONS(600, 6000, 2),
        THREE_PERSONS(800, 8000, 3);

        private final int monthlyPrice;
        private final int annualPrice;
        private final int personsCount;

        PackType(int monthlyPrice, int annualPrice, int personsCount) {
            this.monthlyPrice = monthlyPrice;
            this.annualPrice = annualPrice;
            this.personsCount = personsCount;
        }

        public BigDecimal getMonthlyPrice() { return new BigDecimal(monthlyPrice); }
        public BigDecimal getAnnualPrice() { return new BigDecimal(annualPrice); }
        public int getPersonsCount() { return personsCount; }
    }

    public enum Duration { MONTHLY, ANNUAL }
    public enum Status { ACTIVE, EXPIRED, CANCELLED }
}

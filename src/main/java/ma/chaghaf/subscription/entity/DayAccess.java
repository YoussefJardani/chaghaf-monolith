package ma.chaghaf.subscription.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "day_accesses")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DayAccess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AccessType accessType;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private LocalDate accessDate;

    @Column(length = 100)
    private String qrToken;

    @Builder.Default
    private Boolean used = false;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public enum AccessType {
        HALF_DAY(50), FULL_DAY(80);

        private final int priceVal;
        AccessType(int priceVal) { this.priceVal = priceVal; }
        public BigDecimal getPrice() { return new BigDecimal(priceVal); }
    }
}

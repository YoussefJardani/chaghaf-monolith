package ma.chaghaf.boisson.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "boisson_sessions")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BoissonSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, length = 50)
    private String boissonType;

    @Column(nullable = false, length = 100)
    private String boissonName;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @CreationTimestamp
    private LocalDateTime consumedAt;
}

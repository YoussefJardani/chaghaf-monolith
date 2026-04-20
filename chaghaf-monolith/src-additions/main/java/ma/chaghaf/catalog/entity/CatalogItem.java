package ma.chaghaf.catalog.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "catalog_items")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CatalogItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ItemType type; // BOISSON or SNACK

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(length = 10)
    private String emoji;

    /** Full base64 image data (without data-url prefix). Can be large — stored as TEXT. */
    @Column(columnDefinition = "TEXT")
    private String imageBase64;

    /** e.g. image/jpeg, image/png */
    @Column(length = 50)
    private String imageMimeType;

    @Builder.Default
    private Boolean available = true;

    /** For snacks: track physical stock. */
    @Builder.Default
    private Integer stockQuantity = 0;

    /** Optional category label (e.g. "Sucré", "Salé", "Chaude", "Froide"). */
    @Column(length = 60)
    private String category;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public enum ItemType { BOISSON, SNACK }
}

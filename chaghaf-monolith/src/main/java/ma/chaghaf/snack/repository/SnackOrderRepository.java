package ma.chaghaf.snack.repository;

import ma.chaghaf.snack.entity.SnackOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SnackOrderRepository extends JpaRepository<SnackOrder, Long> {
    List<SnackOrder> findByUserIdOrderByCreatedAtDesc(Long userId);
}

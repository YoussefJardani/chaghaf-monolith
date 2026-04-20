package ma.chaghaf.boisson.repository;

import ma.chaghaf.boisson.entity.BoissonSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoissonSessionRepository extends JpaRepository<BoissonSession, Long> {
    List<BoissonSession> findByUserIdOrderByConsumedAtDesc(Long userId);
}

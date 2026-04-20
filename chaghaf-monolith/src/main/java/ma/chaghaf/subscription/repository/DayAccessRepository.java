package ma.chaghaf.subscription.repository;

import ma.chaghaf.subscription.entity.DayAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DayAccessRepository extends JpaRepository<DayAccess, Long> {}

package du.ac.kr.sb1204.repository;

import du.ac.kr.sb1204.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
}

package du.ac.kr.sb1204.repository;

import du.ac.kr.sb1204.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Location 엔티티에 대한 데이터 접근 인터페이스
 * JpaRepository를 상속받아 기본적인 CRUD 기능 제공
 */
public interface LocationRepository extends JpaRepository<Location, Long> {
}

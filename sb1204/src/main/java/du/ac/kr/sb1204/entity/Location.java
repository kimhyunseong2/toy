package du.ac.kr.sb1204.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * 입지 분석 정보를 저장하는 엔티티 클래스
 * JPA를 사용하여 데이터베이스와 매핑됨
 */
@Entity
@Data  // Lombok 어노테이션: Getter, Setter, toString 등 자동 생성
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 고유 식별자
    
    private String name;        // 위치 이름 (타임스탬프로 자동 생성)
    private double latitude;    // 위도 좌표
    private double longitude;   // 경도 좌표
    private int score;         // 입지 점수 (0-100점)
    private int publicFacilitiesCount;  // 1km 반경 내 공공기관 수
    private String analysis;    // 점수에 따른 분석 결과 설명
    @Column(columnDefinition = "TEXT")
    private String facilitiesData; // 공공기관 정보를 JSON 형태로 저장
} 
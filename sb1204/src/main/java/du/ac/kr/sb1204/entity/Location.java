package du.ac.kr.sb1204.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * 입지 분석 정보를 저장하는 JPA 엔티티
 * 데이터베이스 테이블과 매핑됨
 */
@Entity
@Data
public class Location {
    @Id @GeneratedValue
    private Long id;          // 고유 식별자
    
    private String name;      // 위치명 (타임스탬프 기반)
    private double latitude;  // 위도
    private double longitude; // 경도
    private int score;        // 입지 점수 (20-100)
    private int publicFacilitiesCount;  // 주변 시설 수
    private String analysis;  // 분석 결과 텍스트
    
    @Column(columnDefinition = "TEXT")
    private String facilitiesData;  // 시설 정보 JSON
} 
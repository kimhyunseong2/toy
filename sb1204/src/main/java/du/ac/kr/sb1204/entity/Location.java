package du.ac.kr.sb1204.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;        // 위치 이름
    private double latitude;    // 위도
    private double longitude;   // 경도
    private int score;         // 입지 점수
    private int publicFacilitiesCount;  // 주변 공공기관 수
    private String analysis;    // 분석 결과 설명
} 
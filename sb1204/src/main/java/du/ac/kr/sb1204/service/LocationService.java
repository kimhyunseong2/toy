package du.ac.kr.sb1204.service;

import du.ac.kr.sb1204.entity.Location;
import du.ac.kr.sb1204.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LocationService {
    private final LocationRepository locationRepository;
    
    public Location analyzeLocation(String name, double latitude, double longitude) {
        Location location = new Location();
        location.setName(name);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        
        // Google Places API를 사용하여 주변 1km 반경의 공공기관 수 계산
        int publicFacilitiesCount = calculateNearbyPublicFacilities(latitude, longitude);
        location.setPublicFacilitiesCount(publicFacilitiesCount);
        
        // 점수 계산 (0-100점)
        int score = calculateScore(publicFacilitiesCount);
        location.setScore(score);
        
        // 분석 결과 설명 생성
        String analysis = generateAnalysis(score, publicFacilitiesCount);
        location.setAnalysis(analysis);
        
        return locationRepository.save(location);
    }
    
    private int calculateNearbyPublicFacilities(double latitude, double longitude) {
        // Google Places API 구현
        // TODO: Google Places API 키를 사용하여 주변 공공기관 검색
        return 0; // 임시 반환값
    }
    
    private int calculateScore(int publicFacilitiesCount) {
        // 간단한 점수 계산 로직
        if (publicFacilitiesCount >= 10) return 100;
        else if (publicFacilitiesCount >= 7) return 80;
        else if (publicFacilitiesCount >= 5) return 60;
        else if (publicFacilitiesCount >= 3) return 40;
        else return 20;
    }
    
    private String generateAnalysis(int score, int publicFacilitiesCount) {
        if (score >= 80) {
            return "매우 좋은 입지입니다. 주변에 " + publicFacilitiesCount + "개의 공공기관이 있습니다.";
        } else if (score >= 60) {
            return "괜찮은 입지입니다. 주변에 " + publicFacilitiesCount + "개의 공공기관이 있습니다.";
        } else {
            return "개선이 필요한 입지입니다. 주변에 " + publicFacilitiesCount + "개의 공공기관이 있습니다.";
        }
    }
} 
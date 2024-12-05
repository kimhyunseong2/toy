package du.ac.kr.sb1204.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.model.*;
import du.ac.kr.sb1204.entity.Location;
import du.ac.kr.sb1204.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 입지 분석 비즈니스 로직을 처리하는 서비스 클래스
 * 주요 기능:
 * - Google Places API를 통한 주변 시설 검색
 * - 입지 점수 계산
 * - 분석 결과 생성
 */
@Service
@Transactional
@RequiredArgsConstructor
public class LocationService {
    private final LocationRepository locationRepository;

    @Value("${google.maps.api.key}")
    private String apiKey;

    /**
     * 입지 분석을 수행하는 메인 메서드
     * @param name 위치명
     * @param latitude 위도
     * @param longitude 경도
     * @param facilityType 시설 유형
     * @return 분석된 Location 엔티티
     */
    public Location analyzeLocation(String name, double latitude, double longitude, String facilityType) {
        Location location = new Location();
        location.setName(name);
        location.setLatitude(latitude);
        location.setLongitude(longitude);

        try {
            GeoApiContext context = new GeoApiContext.Builder()
                    .apiKey(apiKey)
                    .build();

            LatLng locationLatLng = new LatLng(latitude, longitude);

            // 모든 시설 유형 정의
            Map<PlaceType, String> placeTypes = new HashMap<>();
            placeTypes.put(PlaceType.HOSPITAL, "HOSPITAL");
            placeTypes.put(PlaceType.POLICE, "POLICE");
            placeTypes.put(PlaceType.FIRE_STATION, "FIRE_STATION");
            placeTypes.put(PlaceType.SCHOOL, "SCHOOL");
            placeTypes.put(PlaceType.UNIVERSITY, "UNIVERSITY");

            int totalCount = 0;
            List<Map<String, Object>> allFacilities = new ArrayList<>();

            // 각 시설 유형별로 검색 수행
            for (Map.Entry<PlaceType, String> entry : placeTypes.entrySet()) {
                PlacesSearchResponse response = PlacesApi.nearbySearchQuery(context, locationLatLng)
                        .radius(1000)
                        .type(entry.getKey())
                        .await();

                if (response.results != null) {
                    totalCount += response.results.length;

                    for (PlacesSearchResult result : response.results) {
                        Map<String, Object> facility = new HashMap<>();
                        facility.put("name", result.name);
                        facility.put("lat", result.geometry.location.lat);
                        facility.put("lng", result.geometry.location.lng);
                        facility.put("type", entry.getValue());
                        allFacilities.add(facility);
                    }
                }
            }

            context.shutdown();

            location.setFacilitiesData(new ObjectMapper().writeValueAsString(allFacilities));
            location.setPublicFacilitiesCount(totalCount);
            location.setScore(calculateScore(totalCount));
            location.setAnalysis(generateAnalysis(location.getScore(), totalCount));

            return locationRepository.save(location);

        } catch (Exception e) {
            e.printStackTrace();
            location.setPublicFacilitiesCount(0);
            location.setScore(20);
            location.setAnalysis("분석 중 오류가 발생했습니다.");
            return locationRepository.save(location);
        }
    }

    /**
     * 시설 수에 따른 점수 계산
     * 30개 이상: 100점
     * 20개 이상: 80점
     * 10개 이상: 60점
     * 5개 이상: 40점
     * 그 외: 20점
     */
    private int calculateScore(int publicFacilitiesCount) {
        System.out.println("Calculating score for " + publicFacilitiesCount + " facilities"); // 디버깅용 로그

        if (publicFacilitiesCount >= 30) {
            return 100;
        } else if (publicFacilitiesCount >= 20) {
            return 80;
        } else if (publicFacilitiesCount >= 10) {
            return 60;
        } else if (publicFacilitiesCount >= 5) {
            return 40;
        } else {
            return 20;
        }
    }

    /**
     * 점수에 따른 분석 결과 텍스트 생성
     */
    private String generateAnalysis(int score, int count) {
        StringBuilder analysis = new StringBuilder();
        analysis.append(String.format("이 위치에서 1km 반경 내에 총 %d개의 공공시설이 있습니다.\n", count));

        if (score >= 80) {
            analysis.append("매우 좋은 입지입니다. 다양한 공공시설과의 접근성이 매우 뛰어납니다.");
        } else if (score >= 60) {
            analysis.append("괜찮은 입지입니다. 공공시설과의 접근성이 좋은 편입니다.");
        } else if (score >= 40) {
            analysis.append("보통의 입지입니다. 공공시설과의 접근성이 적절합니다.");
        } else {
            analysis.append("개선이 필요한 입지입니다. 공공시설과의 접근성이 떨어집니다.");
        }

        return analysis.toString();
    }
} 
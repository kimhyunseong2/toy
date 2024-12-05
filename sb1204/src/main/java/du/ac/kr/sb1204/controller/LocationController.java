package du.ac.kr.sb1204.controller;

import du.ac.kr.sb1204.entity.Location;
import du.ac.kr.sb1204.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 입지 분석 관련 웹 요청을 처리하는 컨트롤러
 * 주요 기능:
 * - 메인 페이지 표시 (지도 선택 화면)
 * - 입지 분석 요청 처리
 * - 공공시설 검색 페이지 표시
 */
@Controller
@RequiredArgsConstructor  // 생성자 주입을 위한 Lombok 어노테이션
public class LocationController {
    private final LocationService locationService;

    /**
     * 메인 페이지 요청 처리
     * @return 위치 선택 페이지 뷰
     */
    @GetMapping("/")
    public String index() {
        return "location/createLocationForm";
    }

    /**
     * 입지 분석 요청 처리
     * @param latitude 위도
     * @param longitude 경도
     * @param facilityType 시설 유형
     * @param model 뷰 모델
     * @return 분석 결과 페이지 뷰
     */
    @PostMapping("/locations/new")
    public String create(@RequestParam double latitude,
                         @RequestParam double longitude,
                         @RequestParam String facilityType,
                         Model model) {
        Location location = locationService.analyzeLocation("위치 " + System.currentTimeMillis(),
                latitude,
                longitude,
                facilityType);
        model.addAttribute("location", location);
        return "location/analysisResult";
    }

} 
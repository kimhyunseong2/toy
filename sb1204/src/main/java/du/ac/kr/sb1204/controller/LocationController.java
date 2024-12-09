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
 */
@Controller
@RequiredArgsConstructor  // 생성자 주입을 위한 Lombok 어노테이션
public class LocationController {
    private final LocationService locationService;
    
    /**
     * 메인 페이지 요청 처리
     * 사용자가 위치를 선택할 수 있는 지도 페이지를 보여줌
     */
    @GetMapping("/")
    public String index() {
        return "location/createLocationForm";
    }
    
    /**
     * 입지 분석 요청 처리
     * 선택된 위치의 좌표를 받아 분석을 수행하고 결과를 보여줌
     * 
     * @param latitude 위도
     * @param longitude 경도
     * @param facilityType 공공시설 유형
     * @param model 뷰에 전달할 데이터를 담는 객체
     * @return 분석 결과 페이지
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
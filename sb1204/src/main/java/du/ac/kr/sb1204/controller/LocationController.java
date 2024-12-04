package du.ac.kr.sb1204.controller;

import du.ac.kr.sb1204.entity.Location;
import du.ac.kr.sb1204.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/locations")
public class LocationController {
    private final LocationService locationService;
    
    @GetMapping("/new")
    public String createForm() {
        return "location/createLocationForm";
    }
    
    @PostMapping("/new")
    public String create(@RequestParam String name,
                        @RequestParam double latitude,
                        @RequestParam double longitude,
                        Model model) {
        Location location = locationService.analyzeLocation(name, latitude, longitude);
        model.addAttribute("location", location);
        return "location/analysisResult";
    }
} 
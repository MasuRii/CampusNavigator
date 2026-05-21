package com.campusnavigator.Controller;

import com.campusnavigator.Entity.Building;
import com.campusnavigator.Entity.PointOfInterest;
import com.campusnavigator.Service.BuildingService;
import com.campusnavigator.Service.PointOfInterestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/pois")
public class PointOfInterestController {
    private final PointOfInterestService poiService;
    private final BuildingService buildingService;

    public PointOfInterestController(PointOfInterestService poiService, BuildingService buildingService) {
        this.poiService = poiService;
        this.buildingService = buildingService;
    }

    @PostMapping
    public ResponseEntity<?> createPointOfInterest(@RequestBody(required = false) Map<String, Object> request) {
        try {
            PointOfInterest poi = buildPointOfInterest(request);
            PointOfInterest createdPOI = poiService.createPointOfInterest(poi);
            return ResponseEntity.ok(createdPOI);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping
    public List<PointOfInterest> getAllPointsOfInterest() {
        return poiService.getAllPointsOfInterest();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PointOfInterest> getPointOfInterestById(@PathVariable Long id) {
        return poiService.getPointOfInterestById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePointOfInterest(@PathVariable Long id,
            @RequestBody(required = false) Map<String, Object> request) {
        try {
            PointOfInterest poiDetails = buildPointOfInterest(request);
            PointOfInterest updatedPOI = poiService.updatePointOfInterest(id, poiDetails);
            return ResponseEntity.ok(updatedPOI);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePointOfInterest(@PathVariable Long id) {
        try {
            poiService.deletePointOfInterest(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private PointOfInterest buildPointOfInterest(Map<String, Object> request) {
        Long buildingId = requiredLong(request, "buildingId");
        Building building = buildingService.getBuildingById(buildingId)
                .orElseThrow(() -> new NoSuchElementException("Building not found with id " + buildingId));

        PointOfInterest poi = new PointOfInterest();
        poi.setName(requiredString(request, "name"));
        poi.setDescription(requiredString(request, "description"));
        poi.setType(requiredString(request, "type"));
        poi.setBuilding(building);
        return poi;
    }

    private Long requiredLong(Map<String, Object> request, String fieldName) {
        String value = requiredString(request, fieldName);
        try {
            return Long.valueOf(value);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(fieldName + " must be a valid number.");
        }
    }

    private String requiredString(Map<String, Object> request, String fieldName) {
        if (request == null || !request.containsKey(fieldName) || request.get(fieldName) == null
                || request.get(fieldName).toString().trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " is required.");
        }
        return request.get(fieldName).toString().trim();
    }
}

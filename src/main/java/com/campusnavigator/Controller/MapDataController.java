package com.campusnavigator.Controller;

import com.campusnavigator.Entity.Building;
import com.campusnavigator.Entity.MapData;
import com.campusnavigator.Service.BuildingService;
import com.campusnavigator.Service.MapDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/maps")
public class MapDataController {
    private final MapDataService mapDataService;
    private final BuildingService buildingService;

    public MapDataController(MapDataService mapDataService, BuildingService buildingService) {
        this.mapDataService = mapDataService;
        this.buildingService = buildingService;
    }

    @PostMapping
    public ResponseEntity<?> createMapData(@RequestBody(required = false) Map<String, Object> request) {
        try {
            Long buildingId = requiredLong(request, "buildingId");
            String mapImageURL = requiredString(request, "mapImageURL");

            Building building = buildingService.getBuildingById(buildingId)
                    .orElseThrow(() -> new NoSuchElementException("Building not found with id " + buildingId));

            Optional<MapData> existingMapData = mapDataService.getMapDataByBuildingId(buildingId);
            if (existingMapData.isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Map data already exists for building id " + buildingId);
            }

            MapData mapData = new MapData();
            mapData.setMapImageURL(mapImageURL);
            mapData.setBuilding(building);

            MapData createdMapData = mapDataService.createMapData(mapData);
            return ResponseEntity.ok(createdMapData);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping
    public List<MapData> getAllMapData() {
        return mapDataService.getAllMapData();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MapData> getMapDataById(@PathVariable Long id) {
        return mapDataService.getMapDataById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMapData(@PathVariable Long id, @RequestBody(required = false) Map<String, Object> request) {
        try {
            Long buildingId = requiredLong(request, "buildingId");
            String mapImageURL = requiredString(request, "mapImageURL");

            Building building = buildingService.getBuildingById(buildingId)
                    .orElseThrow(() -> new NoSuchElementException("Building not found with id " + buildingId));

            MapData mapDataDetails = new MapData();
            mapDataDetails.setMapImageURL(mapImageURL);
            mapDataDetails.setBuilding(building);

            MapData updatedMapData = mapDataService.updateMapData(id, mapDataDetails);
            return ResponseEntity.ok(updatedMapData);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMapData(@PathVariable Long id) {
        try {
            mapDataService.deleteMapData(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
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

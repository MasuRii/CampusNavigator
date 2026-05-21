package com.campusnavigator.Service;

import com.campusnavigator.Entity.MapData;
import com.campusnavigator.Repository.MapDataRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class MapDataService {

    private final MapDataRepository mapDataRepository;

    public MapDataService(MapDataRepository mapDataRepository) {
        this.mapDataRepository = mapDataRepository;
    }

    public MapData createMapData(MapData mapData) {
        if (mapData == null || mapData.getBuilding() == null) {
            throw new IllegalArgumentException("MapData must be associated with a Building.");
        }
        return mapDataRepository.save(mapData);
    }

    public List<MapData> getAllMapData() {
        return mapDataRepository.findAll();
    }

    public Optional<MapData> getMapDataById(Long id) {
        return mapDataRepository.findById(id);
    }

    public Optional<MapData> getMapDataByBuildingId(Long buildingId) {
        return mapDataRepository.findByBuilding_BuildingID(buildingId);
    }

    public MapData updateMapData(Long id, MapData mapDataDetails) {
        MapData mapData = mapDataRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("MapData not found with id " + id));

        if (mapDataDetails == null || mapDataDetails.getBuilding() == null) {
            throw new IllegalArgumentException("MapData must be associated with a Building.");
        }

        mapData.setMapImageURL(mapDataDetails.getMapImageURL());
        mapData.setBuilding(mapDataDetails.getBuilding());

        return mapDataRepository.save(mapData);
    }

    public void deleteMapData(Long id) {
        if (!mapDataRepository.existsById(id)) {
            throw new NoSuchElementException("MapData not found with id " + id);
        }
        mapDataRepository.deleteById(id);
    }
}
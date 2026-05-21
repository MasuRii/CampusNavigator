package com.campusnavigator.Controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.campusnavigator.Entity.CampusService;
import com.campusnavigator.Service.CampusServiceService;

@RestController
@RequestMapping("/api/campusservice") // Simplified mapping
public class CampusServiceController {

    @Autowired
    private CampusServiceService campusServiceService;

    // Test API to verify the controller is working
    @GetMapping("/print")
    public String print() {
        return "Campus Service API is working!";
    }

    // Create a new CampusService
    @PostMapping
    public CampusService createCampusService(@RequestBody CampusService campusService) {
        return campusServiceService.postCampusService(campusService); // Use the correct method name
    }

    // Retrieve all CampusServices
    @GetMapping
    public List<CampusService> getAllCampusServices() {
        return campusServiceService.getAllCampusServices();
    }

    // Update an existing CampusService
    @PutMapping("/{serviceID}")
    public ResponseEntity<?> updateCampusService(
            @PathVariable int serviceID,
            @RequestBody CampusService updatedCampusService) {
        try {
            return ResponseEntity.ok(campusServiceService.putCampusService(serviceID, updatedCampusService));
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    // Delete a CampusService
    @DeleteMapping("/{serviceID}")
    public ResponseEntity<String> deleteCampusService(@PathVariable int serviceID) {
        try {
            return ResponseEntity.ok(campusServiceService.deleteCampusService(serviceID));
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
}

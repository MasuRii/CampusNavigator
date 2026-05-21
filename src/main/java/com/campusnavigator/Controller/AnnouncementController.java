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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.campusnavigator.Entity.AnnouncementEntity;
import com.campusnavigator.Service.AnnouncementService;

@RestController
@RequestMapping("/api/announcement")
public class AnnouncementController {
    @Autowired
    AnnouncementService aserv;

    //TEST RUN 
    @GetMapping("/print")
    public String print() {
        return "TEST TEST";
    }

    //READ
    @GetMapping
    public List<AnnouncementEntity> getAllAnnouncement() {
        return aserv.getAllAnnouncement();
    }

    //CREATE
    @PostMapping("/postAnnouncement")
    public ResponseEntity<?> postAnnouncement(@RequestBody(required = false) AnnouncementEntity announcement) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(aserv.postAnnouncement(announcement));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    //UPDATE
    @PutMapping("/putAnnouncement/{announcementID}")
    public ResponseEntity<?> putAnnouncement(@PathVariable int announcementID, @RequestBody(required = false) AnnouncementEntity newAnnouncementEntity) {
        return updateAnnouncement(announcementID, newAnnouncementEntity);
    }

    @PutMapping("/putAnnouncement")
    public ResponseEntity<?> putAnnouncementByQuery(@RequestParam int announcementID, @RequestBody(required = false) AnnouncementEntity newAnnouncementEntity) {
        return updateAnnouncement(announcementID, newAnnouncementEntity);
    }

    private ResponseEntity<?> updateAnnouncement(int announcementID, AnnouncementEntity newAnnouncementEntity) {
        try {
            return ResponseEntity.ok(aserv.putAnnouncement(announcementID, newAnnouncementEntity));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    //DELETE
    @DeleteMapping("/deleteAnnouncement/{announcementID}")
    public ResponseEntity<String> deleteAnnouncement(@PathVariable int announcementID) {
        try {
            return ResponseEntity.ok(aserv.deleteAnnouncement(announcementID));
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
}

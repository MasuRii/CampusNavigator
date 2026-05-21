package com.campusnavigator.Service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.campusnavigator.Entity.AnnouncementEntity;
import com.campusnavigator.Repository.AnnouncementRepository;

@Service
public class AnnouncementService {
    private static final int MAX_TEXT_LENGTH = 255;

    @Autowired
    AnnouncementRepository arepo;

    public AnnouncementService() {
        super();
    }

    //CREATE
    public AnnouncementEntity postAnnouncement(AnnouncementEntity announce) {
        validateAnnouncement(announce);
        return arepo.save(announce);
    }

    //READ
    public List<AnnouncementEntity> getAllAnnouncement() {
        return arepo.findAll();
    }

    //UPDATE
    public AnnouncementEntity putAnnouncement(int announcementID, AnnouncementEntity newAnnouncementEntity) {
        validateAnnouncement(newAnnouncementEntity);

        AnnouncementEntity announcement = arepo.findById(announcementID)
                .orElseThrow(() -> new NoSuchElementException("Announcement ID: " + announcementID + " not found!"));

        announcement.setTitle(newAnnouncementEntity.getTitle().trim());
        announcement.setContent(newAnnouncementEntity.getContent().trim());
        announcement.setCategory(trimToNull(newAnnouncementEntity.getCategory()));
        announcement.setPostedBy(newAnnouncementEntity.getPostedBy());
        announcement.setPostTimeStamp(newAnnouncementEntity.getPostTimeStamp());

        return arepo.save(announcement);
    }

    //DELETE
    public String deleteAnnouncement(int announcementID) {
        if (!arepo.existsById(announcementID)) {
            throw new NoSuchElementException("Announcement ID: " + announcementID + " not found!");
        }

        arepo.deleteById(announcementID);
        return "Announcement Successfully Deleted!";
    }

    private void validateAnnouncement(AnnouncementEntity announcement) {
        if (announcement == null) {
            throw new IllegalArgumentException("Announcement payload is required.");
        }
        validateRequiredText(announcement.getTitle(), "title");
        validateRequiredText(announcement.getContent(), "content");
        validateOptionalText(announcement.getCategory(), "category");
    }

    private void validateRequiredText(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Announcement " + fieldName + " is required.");
        }
        validateOptionalText(value, fieldName);
    }

    private void validateOptionalText(String value, String fieldName) {
        if (value != null && value.length() > MAX_TEXT_LENGTH) {
            throw new IllegalArgumentException("Announcement " + fieldName + " must be " + MAX_TEXT_LENGTH + " characters or less.");
        }
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}

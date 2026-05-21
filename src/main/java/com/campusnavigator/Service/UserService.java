package com.campusnavigator.Service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.campusnavigator.Entity.User;
import com.campusnavigator.Repository.UserRepository;

@Service
public class UserService {
    private static final int MAX_NAME_LENGTH = 100;
    private static final int MAX_EMAIL_LENGTH = 254;
    private static final int MAX_PASSWORD_LENGTH = 72;
    private static final int MAX_ROLE_LENGTH = 50;

    @Autowired
    UserRepository urepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    public UserService() {
        super();
    }

    //create
    public User postUserRecord(User user) {
        validateNewUser(user);

        String email = normalizeEmail(user.getEmail());
        if (urepo.existsByEmailIgnoreCase(email)) {
            throw new IllegalStateException("Email is already registered.");
        }

        user.setName(user.getName().trim());
        user.setEmail(email);
        user.setRole(trimToNull(user.getRole()));
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return urepo.save(user);
    }

    //read
    public List<User> getAllUser() {
        return urepo.findAll();
    }

    //update
    public User putUserRecord(int userID, User newUser) {
        return putUser(userID, newUser);
    }

    public User putUser(int userID, User newUser) {
        if (newUser == null) {
            throw new IllegalArgumentException("User payload is required.");
        }

        User existingUser = urepo.findById(userID)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + userID));

        if (newUser.getName() != null) {
            validateTextLength(newUser.getName(), "name", MAX_NAME_LENGTH);
            if (newUser.getName().trim().isEmpty()) {
                throw new IllegalArgumentException("Name cannot be blank.");
            }
            existingUser.setName(newUser.getName().trim());
        }

        if (newUser.getEmail() != null) {
            String email = normalizeEmail(newUser.getEmail());
            urepo.findFirstByEmailIgnoreCase(email)
                    .filter(user -> user.getUserID() != userID)
                    .ifPresent(user -> {
                        throw new IllegalStateException("Email is already registered.");
                    });
            existingUser.setEmail(email);
        }

        if (newUser.getPassword() != null) {
            validatePassword(newUser.getPassword());
            existingUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        }

        if (newUser.getRole() != null) {
            validateTextLength(newUser.getRole(), "role", MAX_ROLE_LENGTH);
            existingUser.setRole(trimToNull(newUser.getRole()));
        }

        existingUser.setAdmin(newUser.isAdmin());

        return urepo.save(existingUser);
    }

    public User authenticateUser(String email, String password) {
        if (email == null || password == null) {
            return null;
        }

        User user = urepo.findFirstByEmailIgnoreCase(email.trim()).orElse(null);
        if (user != null && user.getPassword() != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }

        return null;
    }

    public User authenticateAdmin(String email, String password) {
        User user = authenticateUser(email, password);
        return user != null && user.isAdmin() ? user : null;
    }

    //delete
    public String deleteUser(int userID) {
        if (!urepo.existsById(userID)) {
            throw new NoSuchElementException("User not found with id: " + userID);
        }

        urepo.deleteById(userID);
        return "User Successfully Deleted!";
    }

    private void validateNewUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User payload is required.");
        }
        validateRequiredText(user.getName(), "name", MAX_NAME_LENGTH);
        normalizeEmail(user.getEmail());
        validatePassword(user.getPassword());
        validateTextLength(user.getRole(), "role", MAX_ROLE_LENGTH);
    }

    private void validateRequiredText(String value, String fieldName, int maxLength) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(capitalize(fieldName) + " is required.");
        }
        validateTextLength(value, fieldName, maxLength);
    }

    private void validateTextLength(String value, String fieldName, int maxLength) {
        if (value != null && value.length() > maxLength) {
            throw new IllegalArgumentException(capitalize(fieldName) + " must be " + maxLength + " characters or less.");
        }
    }

    private String normalizeEmail(String email) {
        validateRequiredText(email, "email", MAX_EMAIL_LENGTH);
        String normalized = email.trim().toLowerCase();
        if (!normalized.contains("@") || normalized.startsWith("@") || normalized.endsWith("@")) {
            throw new IllegalArgumentException("Email must be valid.");
        }
        return normalized;
    }

    private void validatePassword(String password) {
        validateRequiredText(password, "password", MAX_PASSWORD_LENGTH);
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private String capitalize(String value) {
        return value.substring(0, 1).toUpperCase() + value.substring(1);
    }
}

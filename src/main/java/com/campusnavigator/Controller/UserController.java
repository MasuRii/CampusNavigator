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

import com.campusnavigator.Entity.User;
import com.campusnavigator.Service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userv;

    //test
    @GetMapping("/print")
    public String print() {
        return "TEST TEST";
    }
    
    //create
    @PostMapping("/postUserEntity")
    public ResponseEntity<?> postUserRecord(@RequestBody(required = false) User search) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(userv.postUserRecord(search));
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody(required = false) User loginRequest) {
        if (loginRequest == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User authenticatedUser = userv.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());
        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(authenticatedUser);
    }

    @PostMapping("/admin/login")
    public ResponseEntity<User> adminLogin(@RequestBody(required = false) User loginRequest) {
        if (loginRequest == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User authenticatedAdmin = userv.authenticateAdmin(loginRequest.getEmail(), loginRequest.getPassword());
        if (authenticatedAdmin == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(authenticatedAdmin);
    }

    //read
    @GetMapping("/getAllSearch")
    public List<User> getAllUser() {
        return userv.getAllUser();
    }

    //update
    @PutMapping("/putUserRecord/{userID}")
    public ResponseEntity<?> updateUser(@PathVariable int userID, @RequestBody(required = false) User newUser) {
        try {
            return ResponseEntity.ok(userv.putUser(userID, newUser));
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    //delete
    @DeleteMapping("/deleteUser/{userID}")
    public ResponseEntity<String> deleteUser(@PathVariable int userID) {
        try {
            return ResponseEntity.ok(userv.deleteUser(userID));
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
}

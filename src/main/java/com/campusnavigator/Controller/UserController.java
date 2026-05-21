package com.campusnavigator.Controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.campusnavigator.Entity.User;
import com.campusnavigator.Service.UserService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;



@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userv;
    //test
    @GetMapping("/print")
    public String print()
    {
        return "TEST TEST";
    }
    
    //create
    @PostMapping("/postUserEntity")
    public User postUserRecord(@RequestBody User search)
    {
        return userv.postUserRecord(search);
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User loginRequest) {
        User authenticatedUser = userv.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());
        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(authenticatedUser);
    }

    @PostMapping("/admin/login")
    public ResponseEntity<User> adminLogin(@RequestBody User loginRequest) {
        User authenticatedAdmin = userv.authenticateAdmin(loginRequest.getEmail(), loginRequest.getPassword());
        if (authenticatedAdmin == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(authenticatedAdmin);
    }
    //read
    @GetMapping("/getAllSearch")
    public List<User>getAllUser() {
        return userv.getAllUser();
    }
    //update
    @PutMapping("/putUserRecord/{userID}")  // Changed to match your frontend request
    public User updateUser(@PathVariable int userID, @RequestBody User newUser) {
        return userv.putUser(userID, newUser);
    }
    //delete
    @DeleteMapping("/deleteUser/{userID}")
    public String deleteUser(@PathVariable int userID)
    {
        return userv.deleteUser(userID);
    }
        
}
    


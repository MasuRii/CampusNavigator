package com.campusnavigator.Service;

import java.util.List;

import javax.naming.NameAlreadyBoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.campusnavigator.Entity.User;
import com.campusnavigator.Repository.UserRepository;

@Service
public class UserService {

    @Autowired
    UserRepository urepo;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    public UserService(){
        super();
    }
//create
    public User postUserRecord(User user){
        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return urepo.save(user);
    }
//read
    public List<User> getAllUser(){
        return urepo.findAll();
    }
//update
    @SuppressWarnings("finally")
    public User putUserRecord(int userID, User newUser)
{
    User search = new User();

    try {
        search = urepo.findById(userID).get();

       
        search.setPassword(encodePassword(newUser.getPassword()));

    } catch (NoClassDefFoundError nex){
        throw new NameAlreadyBoundException("Search UserID: " + userID + " not found");
    }finally
    {
        return urepo.save(search);
    }
}

public User putUser(int userID, User newUser) {
    // Find the existing user
    User existingUser = userRepository.findById(userID)
        .orElseThrow(() -> new RuntimeException("User not found with id: " + userID));
    
    // Update the existing user's fields if new values are provided
    if (newUser.getName() != null) {
        existingUser.setName(newUser.getName());
    }
    if (newUser.getEmail() != null) {
        existingUser.setEmail(newUser.getEmail());
    }
    if (newUser.getPassword() != null) {
        existingUser.setPassword(encodePassword(newUser.getPassword()));
    }
    if (newUser.getRole() != null) {
        existingUser.setRole(newUser.getRole());
    }
    // Update admin status
    existingUser.setAdmin(newUser.isAdmin());
    
    // Save and return the updated user
    return userRepository.save(existingUser);
    }
    public User authenticateUser(String email, String password) {
        if (email == null || password == null) {
            return null;
        }

        User user = userRepository.findByEmail(email);
        if (user != null && user.getPassword() != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }

        return null;
    }

    public User authenticateAdmin(String email, String password) {
        User user = authenticateUser(email, password);
        return user != null && user.isAdmin() ? user : null;
    }

    private String encodePassword(String password) {
        return password == null ? null : passwordEncoder.encode(password);
    }

//delete
public String deleteUser(int userID)
{
    String msg = "";

    if(urepo.findById(userID)!= null)
    {
        urepo.deleteById(userID);
        msg = "Search User Successfully Deleted!";
    }
    else
    {
        msg = userID + " NOT found.";
    }

    return msg;
}
}

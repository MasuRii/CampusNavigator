package com.campusnavigator.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.campusnavigator.Entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{

    public User findByUserID(int userID);
    public User findByEmail(String email);
    public Optional<User> findFirstByEmailIgnoreCase(String email);
    public boolean existsByEmailIgnoreCase(String email);
    
}
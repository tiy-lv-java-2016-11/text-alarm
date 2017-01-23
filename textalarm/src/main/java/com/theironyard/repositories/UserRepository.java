package com.theironyard.repositories;

import com.theironyard.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface UserRepository extends JpaRepository<User, Integer>{
    User findFirstByUsername(String username);
    List<User> findByAlarmTime(int hour);
}

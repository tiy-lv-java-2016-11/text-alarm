package com.comtherionyard.Repositories;

import com.comtherionyard.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by darionmoore on 1/20/17.
 */
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}

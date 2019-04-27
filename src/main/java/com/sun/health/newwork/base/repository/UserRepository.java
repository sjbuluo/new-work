package com.sun.health.newwork.base.repository;

import com.sun.health.newwork.base.dto.UserDTO;
import com.sun.health.newwork.base.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User findByUsernameAndPassword(String username, String password);

    int countByUsername(String username);

    int countByEmail(String email);

    int countByPhoneNumber(String phoneNumber);

}

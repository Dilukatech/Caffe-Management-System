package com.example.Cafe.Management.System.dao;

import com.example.Cafe.Management.System.POJO.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


public interface UserDao extends JpaRepository<User,Integer> {

    User findByEmailId(@Param("email") String email);
}

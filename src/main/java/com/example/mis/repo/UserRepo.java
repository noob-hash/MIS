package com.example.mis.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mis.entity.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    public Optional<User> findByUsername(String username);

    public List<User> findByRole(String role);

    public User findByUsernameAndRole(String username, String role);
}

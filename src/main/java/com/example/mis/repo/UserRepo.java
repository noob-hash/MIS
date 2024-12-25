package com.example.mis.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mis.entity.UsersDetails;

@Repository
public interface UserRepo extends JpaRepository<UsersDetails, Long> {

    public Optional<UsersDetails> findByUsername(String username);

    public List<UsersDetails> findByRoles(String role);

    public UsersDetails findByUsernameAndRoles(String username, String role);
}

package com.example.mis.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mis.entity.UsersDetails;
import com.example.mis.enums.UserRole;

@Repository
public interface UserRepo extends JpaRepository<UsersDetails, Long> {

    public Optional<UsersDetails> findByUsername(String username);

    public List<UsersDetails> findByRole(UserRole role);

    Optional<UsersDetails> findByEmailAddress(String email);

    public UsersDetails findByUsernameAndRole(String username, UserRole role);;
}

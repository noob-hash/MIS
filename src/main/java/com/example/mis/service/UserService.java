package com.example.mis.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.mis.entity.UsersDetails;
import com.example.mis.repo.UserRepo;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        UsersDetails user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList()));
    }

    public List<UsersDetails> getAllUsers() {
        return userRepository.findAll();
    }

    public UsersDetails getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public UsersDetails saveUser(UsersDetails user) {
        return userRepository.save(user);
    }

    public UsersDetails updateUser(Long userId, UsersDetails updatedUser) {
        UsersDetails user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return userRepository.save(updatedUser);
        } else {
            user.setAddress(updatedUser.getAddress());
            user.setContact(updatedUser.getContact());
            user.setUsername(updatedUser.getUsername());
            user.setPassword(updatedUser.getPassword());
            user.setName(updatedUser.getName());
            return userRepository.save(user);
        }
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
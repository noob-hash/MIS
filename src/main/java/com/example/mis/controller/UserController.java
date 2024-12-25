package com.example.mis.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mis.entity.UsersDetails;
import com.example.mis.service.UserService;

@RestController
@RequestMapping("/mis/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public List<UsersDetails> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UsersDetails getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public UsersDetails createUser(@PathVariable Long id, @RequestBody UsersDetails user) {
        return userService.updateUser(id,user);
    }

    @PutMapping("/{id}")
    public UsersDetails updateUser(@PathVariable Long id, @RequestBody UsersDetails user) {
        return userService.getUserById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userService.getUserById(id)!=null) {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

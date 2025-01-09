package com.example.mis.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    /**
     * Get users by role.
     * 
     * @param role The role to filter users by.
     * @return List of users with the specified role.
     */
    @GetMapping("/role/{role}")
    public ResponseEntity<List<UsersDetails>> getUsersByRole(@PathVariable String role) {
        try {
            List<UsersDetails> users = userService.getUsersByRole(role);
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Handle invalid role
        }
    }

    /**
     * Get supplier.
     * 
     * @param role The role to filter users by.
     * @return List of users with the specified role.
     */
    @GetMapping("/role")
    public ResponseEntity<List<UsersDetails>> getAllSuppliers() {
        try {
            List<UsersDetails> users = userService.getAllSuppliers();
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Handle invalid role
        }
    }

    @PostMapping("/save")
    public UsersDetails createUser(@RequestBody UsersDetails user) {
        return userService.saveUserWithCustomData(user);
    }
    // @PostMapping("/save")
    // public UsersDetails createUser(@RequestBody UsersDetails user) {
    // return userService.saveUser(user);
    // }

    @PutMapping("/{id}")
    public UsersDetails updateUser(@PathVariable Long id, @RequestBody UsersDetails user) {
        return userService.getUserById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userService.getUserById(id) != null) {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

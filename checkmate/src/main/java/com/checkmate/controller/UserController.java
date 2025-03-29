package com.checkmate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;

import com.checkmate.model.User;
import com.checkmate.service.UserService;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * Endpoint to get all users.
     *
     * @return List of all users.
     */
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * Endpoint to get a user by ID.
     *
     * @param id the ID of the user to retrieve.
     * @return the user with the specified ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        Optional<User> userOptional = userService.getUserById(id);
        return userOptional.map(user -> ResponseEntity.ok(user))
               .orElse(ResponseEntity.notFound().build());
    }
    /**
     * Endpoint to create a new user.
     *
     * @param user the user to create.
     * @return the created user.
     */
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }
}
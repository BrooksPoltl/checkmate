package com.checkmate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.checkmate.model.User;
import com.checkmate.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    /**
     * Method to get all users.
     *
     * @return List of all users.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    /**
     * Method to save a user.
     *
     * @param user the user to save.
     * @return the saved user.
     */
    public User saveUser(User user) {
        return userRepository.saveAndFlush(user);
    }
    /**
     * Method to get a user by ID.
     *
     * @param id the ID of the user to retrieve.
     * @return the user with the specified ID.
     */
    public Optional<User> getUserById(int id) {
        return userRepository.findById(id);
    }
}
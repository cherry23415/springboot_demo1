package com.ying.controller;

import com.ying.entity.User;
import com.ying.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author lyz
 */
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/add")
    public String addUser(@RequestBody User user) {
        userRepository.save(user);
        return "Saved";
    }

    @GetMapping("/list")
    public Iterable<User> getAll() {
        return userRepository.findAll();
    }
}

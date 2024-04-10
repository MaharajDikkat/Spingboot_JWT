package com.jwt.example.controller;

import com.jwt.example.domain.User;
import com.jwt.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private UserService userService;
    @GetMapping("/user")
    public String getUser(){

        System.out.println("getting Users..");
        return "Users";
    }

    @GetMapping("/users")
    public List<User> getUsers(){

        return userService.getAllUsers();
    }

    @PostMapping("/create/user")
    public User createUser(@RequestBody User user){

        return userService.createUser(user);
    }

}

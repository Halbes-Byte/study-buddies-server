package com.studybuddies.server.api.controllers;

import java.util.List;
import java.util.UUID;

import com.studybuddies.server.api.models.UserModel;
import com.studybuddies.server.api.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/")
    public ResponseEntity<List<UserModel>> getAllUsers() {
        return ResponseEntity.ok().body(userService.allUsers());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserModel> getUserById(@PathVariable String id) {
        return ResponseEntity.ok().body(userService.findById(UUID.fromString(id)));
    }

    @GetMapping("/users/new/{name}")
    public ResponseEntity<UserModel> createUser(@PathVariable String name) {
        return ResponseEntity.ok().body(userService.save(new UserModel(name, "1234")));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable String id) {
        userService.deleteById(UUID.fromString(id));

        return ResponseEntity.noContent().build();
    }
}

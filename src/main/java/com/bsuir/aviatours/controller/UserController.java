package com.bsuir.aviatours.controller;

import com.bsuir.aviatours.model.User;
import com.bsuir.aviatours.service.interfaces.EntityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    private final EntityService<User> userService;

    public UserController(EntityService<User> userService) {
        this.userService = userService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody User user) {
        userService.saveEntity(user);
        return ResponseEntity.ok("User saved successfully");
    }

    @GetMapping("/getAll")
    public List<User> getAll() {
        return userService.getAllEntities();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable int id) {
        User user = userService.findEntityById(id);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        User user = userService.findEntityById(id);
        userService.deleteEntity(user);
        return ResponseEntity.ok("User deleted successfully");
    }

    @PutMapping("/update")
    public ResponseEntity<User> update(@RequestBody User user) {
        User updatedUser = userService.updateEntity(user);
        return ResponseEntity.ok(updatedUser);
    }
}

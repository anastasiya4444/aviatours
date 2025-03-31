package com.bsuir.aviatours.controller;

import com.bsuir.aviatours.model.User;
import com.bsuir.aviatours.model.UserPersonalDatum;
import com.bsuir.aviatours.service.interfaces.EntityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user_personal_data")
@CrossOrigin
public class UserPersonalDataController {

    private final EntityService<UserPersonalDatum> userService;

    public UserPersonalDataController(EntityService<UserPersonalDatum> userService) {
        this.userService = userService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody UserPersonalDatum user) {
        userService.saveEntity(user);
        return ResponseEntity.ok("User saved successfully");
    }

    @GetMapping("/getAll")
    public List<UserPersonalDatum> getAll() {
        return userService.getAllEntities();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserPersonalDatum> getById(@PathVariable int id) {
        UserPersonalDatum user = userService.findEntityById(id);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        UserPersonalDatum user = userService.findEntityById(id);
        userService.deleteEntity(user);
        return ResponseEntity.ok("User deleted successfully");
    }

    @PutMapping("/update")
    public ResponseEntity<UserPersonalDatum> update(@RequestBody UserPersonalDatum user) {
        UserPersonalDatum updatedUser = userService.updateEntity(user);
        return ResponseEntity.ok(updatedUser);
    }
}

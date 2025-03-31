package com.bsuir.aviatours.controller;

import com.bsuir.aviatours.model.Role;
import com.bsuir.aviatours.service.interfaces.EntityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
@CrossOrigin
public class RoleController {

    private final EntityService<Role> entityService;

    public RoleController(EntityService<Role> entityService) {
        this.entityService = entityService;
    }

    @PostMapping("/add")
    public String add(@RequestBody Role role) {
        entityService.saveEntity(role);
        return "success";
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAll() {
        try {
            List<Role> roles = entityService.getAllEntities();
            return new ResponseEntity<>(roles, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

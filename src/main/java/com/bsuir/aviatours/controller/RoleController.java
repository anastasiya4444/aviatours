package com.bsuir.aviatours.controller;

import com.bsuir.aviatours.dto.ReviewDTO;
import com.bsuir.aviatours.dto.RoleDTO;
import com.bsuir.aviatours.model.Role;
import com.bsuir.aviatours.service.interfaces.EntityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/role")
@CrossOrigin
public class RoleController {

    private final EntityService<Role> entityService;

    public RoleController(EntityService<Role> entityService) {
        this.entityService = entityService;
    }

    @GetMapping("/getAll")
    public List<RoleDTO> getAll() {
        return entityService.getAllEntities()
                .stream()
                .map(RoleDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDTO> getById(@PathVariable int id) {
        RoleDTO review = RoleDTO.fromEntity(entityService.findEntityById(id));
        return ResponseEntity.ok(review);
    }
}

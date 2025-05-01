package com.bsuir.aviatours.controller;

import com.bsuir.aviatours.dto.RouteDTO;
import com.bsuir.aviatours.model.Route;
import com.bsuir.aviatours.model.User;
import com.bsuir.aviatours.service.interfaces.EntityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/route")
@CrossOrigin
public class RouteController {

    private final EntityService<Route> userService;

    public RouteController(EntityService<Route> userService) {
        this.userService = userService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody RouteDTO route) {
        userService.saveEntity(route.toEntity());
        return ResponseEntity.ok("Route saved successfully");
    }

    @GetMapping("/getAll")
    public List<RouteDTO> getAll() {
        return userService.getAllEntities()
                .stream()
                .map(RouteDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RouteDTO> getById(@PathVariable int id) {
        RouteDTO route = RouteDTO.fromEntity(userService.findEntityById(id));
        return ResponseEntity.ok(route);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        RouteDTO route = RouteDTO.fromEntity(userService.findEntityById(id));
        userService.deleteEntity(route.toEntity());
        return ResponseEntity.ok("Route deleted successfully");
    }

    @PutMapping("/update")
    public ResponseEntity<RouteDTO> update(@RequestBody RouteDTO route1) {
        RouteDTO route = RouteDTO.fromEntity(userService.updateEntity(route1.toEntity()));
        return ResponseEntity.ok(route);
    }
}

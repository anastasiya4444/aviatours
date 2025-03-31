package com.bsuir.aviatours.controller;

import com.bsuir.aviatours.model.Route;
import com.bsuir.aviatours.model.User;
import com.bsuir.aviatours.service.interfaces.EntityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/route")
@CrossOrigin
public class RouteController {

    private final EntityService<Route> userService;

    public RouteController(EntityService<Route> userService) {
        this.userService = userService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody Route route) {
        userService.saveEntity(route);
        return ResponseEntity.ok("Route saved successfully");
    }

    @GetMapping("/getAll")
    public List<Route> getAll() {
        return userService.getAllEntities();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Route> getById(@PathVariable int id) {
        Route route = userService.findEntityById(id);
        return ResponseEntity.ok(route);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        Route route = userService.findEntityById(id);
        userService.deleteEntity(route);
        return ResponseEntity.ok("Route deleted successfully");
    }

    @PutMapping("/update")
    public ResponseEntity<Route> update(@RequestBody Route route1) {
        Route route = userService.updateEntity(route1);
        return ResponseEntity.ok(route);
    }
}

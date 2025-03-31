package com.bsuir.aviatours.controller;

import com.bsuir.aviatours.model.Day;
import com.bsuir.aviatours.service.interfaces.EntityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/day")
@CrossOrigin
public class DayController {

    private final EntityService<Day> dayEntityService;

    public DayController(EntityService<Day> dayEntityService) {
        this.dayEntityService = dayEntityService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody Day day) {
        dayEntityService.saveEntity(day);
        return ResponseEntity.ok("Day saved successfully");
    }

    @GetMapping("/getAll")
    public List<Day> getAll() {
        return dayEntityService.getAllEntities();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Day> getById(@PathVariable int id) {
        Day day = dayEntityService.findEntityById(id);
        return ResponseEntity.ok(day);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        Day day = dayEntityService.findEntityById(id);
        dayEntityService.deleteEntity(day);
        return ResponseEntity.ok("Day deleted successfully");
    }

    @PutMapping("/update")
    public ResponseEntity<Day> update(@RequestBody Day user) {
        Day day = dayEntityService.updateEntity(user);
        return ResponseEntity.ok(day);
    }
}

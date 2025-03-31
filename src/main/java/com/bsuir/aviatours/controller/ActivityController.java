package com.bsuir.aviatours.controller;

import com.bsuir.aviatours.model.Activity;
import com.bsuir.aviatours.service.interfaces.EntityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/activity")
@CrossOrigin
public class ActivityController {

    private final EntityService<Activity> activityEntityService;

    public ActivityController(EntityService<Activity> activityEntityService) {
        this.activityEntityService = activityEntityService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody Activity activity) {
        activityEntityService.saveEntity(activity);
        return ResponseEntity.ok("Activity saved successfully");
    }

    @GetMapping("/getAll")
    public List<Activity> getAll() {
        return activityEntityService.getAllEntities();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Activity> getById(@PathVariable int id) {
        Activity activity = activityEntityService.findEntityById(id);
        return ResponseEntity.ok(activity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        Activity activity = activityEntityService.findEntityById(id);
        activityEntityService.deleteEntity(activity);
        return ResponseEntity.ok("Activity deleted successfully");
    }

    @PutMapping("/update")
    public ResponseEntity<Activity> update(@RequestBody Activity activity) {
        Activity updatedUser = activityEntityService.updateEntity(activity);
        return ResponseEntity.ok(updatedUser);
    }
}

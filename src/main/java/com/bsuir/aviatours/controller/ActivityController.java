package com.bsuir.aviatours.controller;

import com.bsuir.aviatours.dto.ActivityDTO;
import com.bsuir.aviatours.model.Activity;
import com.bsuir.aviatours.service.interfaces.EntityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/activity")
@CrossOrigin
public class ActivityController {

    private final EntityService<Activity> activityEntityService;

    public ActivityController(EntityService<Activity> activityEntityService) {
        this.activityEntityService = activityEntityService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody ActivityDTO activity) {
        activityEntityService.saveEntity(activity.toEntity());
        return ResponseEntity.ok("Activity saved successfully");
    }

    @GetMapping("/getAll")
    public List<ActivityDTO> getAll() {
        return activityEntityService.getAllEntities()
                .stream()
                .map(ActivityDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivityDTO> getById(@PathVariable int id) {
        Activity activity = activityEntityService.findEntityById(id);
        return ResponseEntity.ok(ActivityDTO.fromEntity(activity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        Activity activity = activityEntityService.findEntityById(id);
        activityEntityService.deleteEntity(activity);
        return ResponseEntity.ok("Activity deleted successfully");
    }

    @PutMapping("/update")
    public ResponseEntity<ActivityDTO> update(@RequestBody ActivityDTO activity) {
        ActivityDTO updatedUser = ActivityDTO.fromEntity(activityEntityService.updateEntity(activity.toEntity()));
        return ResponseEntity.ok(updatedUser);
    }
}

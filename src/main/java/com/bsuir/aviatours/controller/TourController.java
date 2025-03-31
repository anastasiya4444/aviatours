package com.bsuir.aviatours.controller;

import com.bsuir.aviatours.model.Tour;
import com.bsuir.aviatours.service.interfaces.EntityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tour")
@CrossOrigin
public class TourController {

    private final EntityService<Tour> tourEntityService;

    public TourController(EntityService<Tour> tourEntityService) {
        this.tourEntityService = tourEntityService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody Tour tour) {
        tourEntityService.saveEntity(tour);
        return ResponseEntity.ok("Tour saved successfully");
    }

    @GetMapping("/getAll")
    public List<Tour> getAll() {
        return tourEntityService.getAllEntities();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tour> getById(@PathVariable int id) {
        Tour tour = tourEntityService.findEntityById(id);
        return ResponseEntity.ok(tour);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        Tour tour = tourEntityService.findEntityById(id);
        tourEntityService.deleteEntity(tour);
        return ResponseEntity.ok("Tour deleted successfully");
    }

    @PutMapping("/update")
    public ResponseEntity<Tour> update(@RequestBody Tour tour1) {
        Tour tour = tourEntityService.updateEntity(tour1);
        return ResponseEntity.ok(tour);
    }
}

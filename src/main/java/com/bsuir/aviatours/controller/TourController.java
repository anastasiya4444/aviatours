package com.bsuir.aviatours.controller;

import com.bsuir.aviatours.dto.TourDTO;
import com.bsuir.aviatours.model.Tour;
import com.bsuir.aviatours.service.interfaces.EntityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tour")
@CrossOrigin
public class TourController {

    private final EntityService<Tour> tourEntityService;

    public TourController(EntityService<Tour> tourEntityService) {
        this.tourEntityService = tourEntityService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody TourDTO tour) {
        tourEntityService.saveEntity(tour.toEntity());
        return ResponseEntity.ok("Tour saved successfully");
    }

    @GetMapping("/getAll")
    public List<TourDTO> getAll() {
        return tourEntityService.getAllEntities()
                .stream()
                .map(TourDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TourDTO> getById(@PathVariable int id) {
        TourDTO tour = TourDTO.fromEntity(tourEntityService.findEntityById(id));
        return ResponseEntity.ok(tour);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        TourDTO tour = TourDTO.fromEntity(tourEntityService.findEntityById(id));
        tourEntityService.deleteEntity(tour.toEntity());
        return ResponseEntity.ok("Tour deleted successfully");
    }

    @PutMapping("/update")
    public ResponseEntity<TourDTO> update(@RequestBody TourDTO tour1) {
        TourDTO tour = TourDTO.fromEntity(tourEntityService.updateEntity(tour1.toEntity()));
        return ResponseEntity.ok(tour);
    }
}

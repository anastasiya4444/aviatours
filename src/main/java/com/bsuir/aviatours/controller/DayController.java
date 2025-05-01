package com.bsuir.aviatours.controller;

import com.bsuir.aviatours.dto.DayDTO;
import com.bsuir.aviatours.model.Day;
import com.bsuir.aviatours.service.interfaces.EntityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/day")
@CrossOrigin
public class DayController {

    private final EntityService<Day> dayEntityService;

    public DayController(EntityService<Day> dayEntityService) {
        this.dayEntityService = dayEntityService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody DayDTO day) {
        dayEntityService.saveEntity(day.toEntity());
        return ResponseEntity.ok("Day saved successfully");
    }

    @GetMapping("/getAll")
    public List<DayDTO> getAll() {
        return dayEntityService.getAllEntities()
                .stream()
                .map(DayDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DayDTO> getById(@PathVariable int id) {
        DayDTO day = DayDTO.fromEntity(dayEntityService.findEntityById(id));
        return ResponseEntity.ok(day);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        DayDTO day = DayDTO.fromEntity(dayEntityService.findEntityById(id));
        dayEntityService.deleteEntity(day.toEntity());
        return ResponseEntity.ok("Day deleted successfully");
    }

    @PutMapping("/update")
    public ResponseEntity<DayDTO> update(@RequestBody DayDTO user) {
        DayDTO day = DayDTO.fromEntity(dayEntityService.updateEntity(user.toEntity()));
        return ResponseEntity.ok(day);
    }
}

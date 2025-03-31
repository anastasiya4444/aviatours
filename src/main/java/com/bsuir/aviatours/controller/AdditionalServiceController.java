package com.bsuir.aviatours.controller;

import com.bsuir.aviatours.model.AdditionalService;
import com.bsuir.aviatours.service.interfaces.EntityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/additional_service")
@CrossOrigin
public class AdditionalServiceController {

    private final EntityService<AdditionalService> additionalServiceEntityService;

    public AdditionalServiceController(EntityService<AdditionalService> additionalServiceEntityService) {
        this.additionalServiceEntityService = additionalServiceEntityService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody AdditionalService additionalService) {
        additionalServiceEntityService.saveEntity(additionalService);
        return ResponseEntity.ok("AdditionalService saved successfully");
    }

    @GetMapping("/getAll")
    public List<AdditionalService> getAll() {
        return additionalServiceEntityService.getAllEntities();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdditionalService> getById(@PathVariable int id) {
        AdditionalService additionalService = additionalServiceEntityService.findEntityById(id);
        return ResponseEntity.ok(additionalService);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        AdditionalService additionalService = additionalServiceEntityService.findEntityById(id);
        additionalServiceEntityService.deleteEntity(additionalService);
        return ResponseEntity.ok("AdditionalService deleted successfully");
    }

    @PutMapping("/update")
    public ResponseEntity<AdditionalService> update(@RequestBody AdditionalService user) {
        AdditionalService additionalService = additionalServiceEntityService.updateEntity(user);
        return ResponseEntity.ok(additionalService);
    }
}

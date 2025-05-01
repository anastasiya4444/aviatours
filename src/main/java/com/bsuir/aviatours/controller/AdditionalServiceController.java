package com.bsuir.aviatours.controller;

import com.bsuir.aviatours.dto.AdditionalServiceDTO;
import com.bsuir.aviatours.model.AdditionalService;
import com.bsuir.aviatours.service.interfaces.EntityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/additional_service")
@CrossOrigin
public class AdditionalServiceController {

    private final EntityService<AdditionalService> additionalServiceEntityService;

    public AdditionalServiceController(EntityService<AdditionalService> additionalServiceEntityService) {
        this.additionalServiceEntityService = additionalServiceEntityService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody AdditionalServiceDTO additionalService) {
        additionalServiceEntityService.saveEntity(additionalService.toEntity());
        return ResponseEntity.ok("AdditionalService saved successfully");
    }

    @GetMapping("/getAll")
    public List<AdditionalServiceDTO> getAll() {
        return additionalServiceEntityService.getAllEntities()
                .stream()
                .map(AdditionalServiceDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdditionalServiceDTO> getById(@PathVariable int id) {
        AdditionalServiceDTO additionalService = AdditionalServiceDTO
                .fromEntity(additionalServiceEntityService.findEntityById(id));
        return ResponseEntity.ok(additionalService);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        AdditionalServiceDTO additionalService = AdditionalServiceDTO
                .fromEntity(additionalServiceEntityService.findEntityById(id));
        additionalServiceEntityService.deleteEntity(additionalService.toEntity());
        return ResponseEntity.ok("AdditionalService deleted successfully");
    }

    @PutMapping("/update")
    public ResponseEntity<AdditionalServiceDTO> update(@RequestBody AdditionalServiceDTO user) {
        AdditionalServiceDTO additionalService = AdditionalServiceDTO
                .fromEntity(additionalServiceEntityService.updateEntity(user.toEntity()));
        return ResponseEntity.ok(additionalService);
    }
}

package com.bsuir.aviatours.controller;

import com.bsuir.aviatours.model.VisaRequirement;
import com.bsuir.aviatours.service.interfaces.EntityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/visa_requirement")
@CrossOrigin
public class VisaRequirementController {

    private final EntityService<VisaRequirement> visaRequirementEntityService;

    public VisaRequirementController(EntityService<VisaRequirement> visaRequirementEntityService) {
        this.visaRequirementEntityService = visaRequirementEntityService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody VisaRequirement visaRequirement) {
        visaRequirementEntityService.saveEntity(visaRequirement);
        return ResponseEntity.ok("Requirement saved successfully");
    }

    @GetMapping("/getAll")
    public List<VisaRequirement> getAll() {
        return visaRequirementEntityService.getAllEntities();
    }

    @GetMapping("/{id}")
    public ResponseEntity<VisaRequirement> getById(@PathVariable int id) {
        VisaRequirement requirement = visaRequirementEntityService.findEntityById(id);
        return ResponseEntity.ok(requirement);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        VisaRequirement requirement = visaRequirementEntityService.findEntityById(id);
        visaRequirementEntityService.deleteEntity(requirement);
        return ResponseEntity.ok("Requirement deleted successfully");
    }

    @PutMapping("/update")
    public ResponseEntity<VisaRequirement> update(@RequestBody VisaRequirement visaRequirement) {
        VisaRequirement requirement = visaRequirementEntityService.updateEntity(visaRequirement);
        return ResponseEntity.ok(requirement);
    }
}

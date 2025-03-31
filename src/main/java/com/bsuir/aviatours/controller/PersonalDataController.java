package com.bsuir.aviatours.controller;

import com.bsuir.aviatours.model.Payment;
import com.bsuir.aviatours.model.PersonalDatum;
import com.bsuir.aviatours.service.interfaces.EntityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/personal_data")
@CrossOrigin
public class PersonalDataController {

    private final EntityService<PersonalDatum> paymentEntityService;

    public PersonalDataController(EntityService<PersonalDatum> paymentEntityService) {
        this.paymentEntityService = paymentEntityService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody PersonalDatum personalDatum) {
        paymentEntityService.saveEntity(personalDatum);
        return ResponseEntity.ok("PersonalDatum saved successfully");
    }

    @GetMapping("/getAll")
    public List<PersonalDatum> getAll() {
        return paymentEntityService.getAllEntities();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonalDatum> getById(@PathVariable int id) {
        PersonalDatum personalDatum = paymentEntityService.findEntityById(id);
        return ResponseEntity.ok(personalDatum);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        PersonalDatum personalDatum = paymentEntityService.findEntityById(id);
        paymentEntityService.deleteEntity(personalDatum);
        return ResponseEntity.ok("PersonalDatum deleted successfully");
    }

    @PutMapping("/update")
    public ResponseEntity<PersonalDatum> update(@RequestBody PersonalDatum personalDatum) {
        PersonalDatum personalDatum1 = paymentEntityService.updateEntity(personalDatum);
        return ResponseEntity.ok(personalDatum1);
    }
}

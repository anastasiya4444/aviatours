package com.bsuir.aviatours.controller;

import com.bsuir.aviatours.dto.PersonalDatumDTO;
import com.bsuir.aviatours.model.Payment;
import com.bsuir.aviatours.model.PersonalDatum;
import com.bsuir.aviatours.service.interfaces.EntityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/personal_data")
@CrossOrigin
public class PersonalDataController {

    private final EntityService<PersonalDatum> paymentEntityService;

    public PersonalDataController(EntityService<PersonalDatum> paymentEntityService) {
        this.paymentEntityService = paymentEntityService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody PersonalDatumDTO personalDatum) {
        paymentEntityService.saveEntity(personalDatum.toEntity());
        return ResponseEntity.ok("PersonalDatum saved successfully");
    }

    @GetMapping("/getAll")
    public List<PersonalDatumDTO> getAll() {
        return paymentEntityService.getAllEntities()
                .stream()
                .map(PersonalDatumDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonalDatumDTO> getById(@PathVariable int id) {
        PersonalDatumDTO personalDatum = PersonalDatumDTO.fromEntity(paymentEntityService.findEntityById(id));
        return ResponseEntity.ok(personalDatum);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        PersonalDatumDTO personalDatum = PersonalDatumDTO.fromEntity(paymentEntityService.findEntityById(id));
        paymentEntityService.deleteEntity(personalDatum.toEntity());
        return ResponseEntity.ok("PersonalDatum deleted successfully");
    }

    @PutMapping("/update")
    public ResponseEntity<PersonalDatumDTO> update(@RequestBody PersonalDatumDTO personalDatum) {
        PersonalDatumDTO personalDatum1 = PersonalDatumDTO.fromEntity(paymentEntityService.updateEntity(personalDatum.toEntity()));
        return ResponseEntity.ok(personalDatum1);
    }
}

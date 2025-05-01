package com.bsuir.aviatours.controller;

import com.bsuir.aviatours.service.interfaces.EntityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client_visa")
@CrossOrigin
public class ClientVisaController {

    private final EntityService<ClientVisa> clientVisaEntityService;

    public ClientVisaController(EntityService<ClientVisa> clientVisaEntityService) {
        this.clientVisaEntityService = clientVisaEntityService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody ClientVisa clientVisa) {
        clientVisaEntityService.saveEntity(clientVisa);
        return ResponseEntity.ok("ClientVisa saved successfully");
    }

    @GetMapping("/getAll")
    public List<ClientVisa> getAll() {
        return clientVisaEntityService.getAllEntities();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientVisa> getById(@PathVariable int id) {
        ClientVisa clientVisa = clientVisaEntityService.findEntityById(id);
        return ResponseEntity.ok(clientVisa);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        ClientVisa clientVisa = clientVisaEntityService.findEntityById(id);
        clientVisaEntityService.deleteEntity(clientVisa);
        return ResponseEntity.ok("ClientVisa deleted successfully");
    }

    @PutMapping("/update")
    public ResponseEntity<ClientVisa> update(@RequestBody ClientVisa clientVisa) {
        ClientVisa clientVisa1 = clientVisaEntityService.updateEntity(clientVisa);
        return ResponseEntity.ok(clientVisa1);
    }
}

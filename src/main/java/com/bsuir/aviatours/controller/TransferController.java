package com.bsuir.aviatours.controller;

import com.bsuir.aviatours.model.Transfer;
import com.bsuir.aviatours.service.interfaces.EntityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transfer")
@CrossOrigin
public class TransferController {

    private final EntityService<Transfer> transferEntityService;

    public TransferController(EntityService<Transfer> transferEntityService) {
        this.transferEntityService = transferEntityService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody Transfer transfer) {
        transferEntityService.saveEntity(transfer);
        return ResponseEntity.ok("Transfer saved successfully");
    }

    @GetMapping("/getAll")
    public List<Transfer> getAll() {
        return transferEntityService.getAllEntities();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transfer> getById(@PathVariable int id) {
        Transfer transfer = transferEntityService.findEntityById(id);
        return ResponseEntity.ok(transfer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        Transfer transfer = transferEntityService.findEntityById(id);
        transferEntityService.deleteEntity(transfer);
        return ResponseEntity.ok("Transfer deleted successfully");
    }

    @PutMapping("/update")
    public ResponseEntity<Transfer> update(@RequestBody Transfer transfer1) {
        Transfer transfer = transferEntityService.updateEntity(transfer1);
        return ResponseEntity.ok(transfer);
    }
}

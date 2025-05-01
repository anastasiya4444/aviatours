package com.bsuir.aviatours.controller;

import com.bsuir.aviatours.service.interfaces.EntityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/transfer")
@CrossOrigin
public class TransferController {

    private final EntityService<Transfer> transferEntityService;

    public TransferController(EntityService<Transfer> transferEntityService) {
        this.transferEntityService = transferEntityService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody TransferDTO transfer) {
        transferEntityService.saveEntity(transfer.toEntity());
        return ResponseEntity.ok("Transfer saved successfully");
    }

    @GetMapping("/getAll")
    public List<TransferDTO> getAll() {
        return transferEntityService.getAllEntities()
                .stream()
                .map(TransferDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransferDTO> getById(@PathVariable int id) {
        TransferDTO transfer = TransferDTO.fromEntity(transferEntityService.findEntityById(id));
        return ResponseEntity.ok(transfer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        TransferDTO transfer = TransferDTO.fromEntity(transferEntityService.findEntityById(id));
        transferEntityService.deleteEntity(transfer.toEntity());
        return ResponseEntity.ok("Transfer deleted successfully");
    }

    @PutMapping("/update")
    public ResponseEntity<TransferDTO> update(@RequestBody TransferDTO transfer1) {
        TransferDTO transfer = TransferDTO.fromEntity(transferEntityService.updateEntity(transfer1.toEntity()));
        return ResponseEntity.ok(transfer);
    }
}

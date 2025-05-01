package com.bsuir.aviatours.controller;

import com.bsuir.aviatours.dto.PaymentDTO;
import com.bsuir.aviatours.model.Payment;
import com.bsuir.aviatours.service.interfaces.EntityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/payment")
@CrossOrigin
public class PaymentController {

    private final EntityService<Payment> paymentEntityService;

    public PaymentController(EntityService<Payment> paymentEntityService) {
        this.paymentEntityService = paymentEntityService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody PaymentDTO payment) {
        paymentEntityService.saveEntity(payment.toEntity());
        return ResponseEntity.ok("Payment saved successfully");
    }

    @GetMapping("/getAll")
    public List<PaymentDTO> getAll() {
        return paymentEntityService.getAllEntities()
                .stream()
                .map(PaymentDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDTO> getById(@PathVariable int id) {
        PaymentDTO payment = PaymentDTO.fromEntity(paymentEntityService.findEntityById(id));
        return ResponseEntity.ok(payment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        PaymentDTO payment = PaymentDTO.fromEntity(paymentEntityService.findEntityById(id));
        paymentEntityService.deleteEntity(payment.toEntity());
        return ResponseEntity.ok("Payment deleted successfully");
    }

    @PutMapping("/update")
    public ResponseEntity<PaymentDTO> update(@RequestBody PaymentDTO payment) {
        PaymentDTO payment1 = PaymentDTO.fromEntity(paymentEntityService.updateEntity(payment.toEntity()));
        return ResponseEntity.ok(payment1);
    }
}

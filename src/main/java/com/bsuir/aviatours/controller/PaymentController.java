package com.bsuir.aviatours.controller;

import com.bsuir.aviatours.model.Payment;
import com.bsuir.aviatours.service.interfaces.EntityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payment")
@CrossOrigin
public class PaymentController {

    private final EntityService<Payment> paymentEntityService;

    public PaymentController(EntityService<Payment> paymentEntityService) {
        this.paymentEntityService = paymentEntityService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody Payment payment) {
        paymentEntityService.saveEntity(payment);
        return ResponseEntity.ok("Payment saved successfully");
    }

    @GetMapping("/getAll")
    public List<Payment> getAll() {
        return paymentEntityService.getAllEntities();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getById(@PathVariable int id) {
        Payment payment = paymentEntityService.findEntityById(id);
        return ResponseEntity.ok(payment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        Payment payment = paymentEntityService.findEntityById(id);
        paymentEntityService.deleteEntity(payment);
        return ResponseEntity.ok("Payment deleted successfully");
    }

    @PutMapping("/update")
    public ResponseEntity<Payment> update(@RequestBody Payment payment) {
        Payment payment1 = paymentEntityService.updateEntity(payment);
        return ResponseEntity.ok(payment1);
    }
}

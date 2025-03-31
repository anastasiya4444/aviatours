package com.bsuir.aviatours.service.implementations;

import com.bsuir.aviatours.model.Payment;
import com.bsuir.aviatours.model.Role;
import com.bsuir.aviatours.repository.PaymentRepository;
import com.bsuir.aviatours.service.interfaces.EntityService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentServiceImpl implements EntityService<Payment> {

    private final PaymentRepository entityRepository;

    public PaymentServiceImpl(PaymentRepository entityRepository) {
        this.entityRepository = entityRepository;
    }

    @Override
    public Payment saveEntity(Payment obj) {
        return entityRepository.save(obj);
    }

    @Override
    public List<Payment> getAllEntities() {
        return entityRepository.findAll();
    }

    @Override
    public Payment updateEntity(Payment obj) {
        if (!entityRepository.existsById(obj.getId())) {
            throw new EntityNotFoundException("Payment with ID " + obj.getId() + " not found");
        }
        return entityRepository.save(obj);
    }

    @Override
    public void deleteEntity(Payment obj) {
        entityRepository.delete(obj);
    }

    @Override
    public Payment findEntityById(int id) {
        return entityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Payment with ID " + id + " not found"));
    }

}

package com.bsuir.aviatours.service.implementations;

import com.bsuir.aviatours.model.Role;
import com.bsuir.aviatours.model.Transfer;
import com.bsuir.aviatours.repository.TransferRepository;
import com.bsuir.aviatours.service.interfaces.EntityService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransferServiceImpl implements EntityService<Transfer> {

    private final TransferRepository entityRepository;

    public TransferServiceImpl(TransferRepository entityRepository) {
        this.entityRepository = entityRepository;
    }

    @Override
    public Transfer saveEntity(Transfer obj) {
        return entityRepository.save(obj);
    }

    @Override
    public List<Transfer> getAllEntities() {
        return entityRepository.findAll();
    }

    @Override
    public Transfer updateEntity(Transfer obj) {
        if (!entityRepository.existsById(obj.getId())) {
            throw new EntityNotFoundException("Transfer with ID " + obj.getId() + " not found");
        }
        return entityRepository.save(obj);
    }

    @Override
    public void deleteEntity(Transfer obj) {
        entityRepository.delete(obj);
    }

    @Override
    public Transfer findEntityById(int id) {
        return entityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transfer with ID " + id + " not found"));
    }

}

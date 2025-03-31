package com.bsuir.aviatours.service.implementations;

import com.bsuir.aviatours.model.AdditionalService;
import com.bsuir.aviatours.repository.AdditionalServiceRepository;
import com.bsuir.aviatours.service.interfaces.EntityService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdditionalServiceServiceImpl implements EntityService<AdditionalService> {

    private final AdditionalServiceRepository entityRepository;

    public AdditionalServiceServiceImpl(AdditionalServiceRepository entityRepository) {
        this.entityRepository = entityRepository;
    }

    @Override
    public AdditionalService saveEntity(AdditionalService obj) {
        return entityRepository.save(obj);
    }

    @Override
    public List<AdditionalService> getAllEntities() {
        return entityRepository.findAll();
    }

    @Override
    public AdditionalService updateEntity(AdditionalService obj) {
        if (!entityRepository.existsById(obj.getId())) {
            throw new EntityNotFoundException("AdditionalService with ID " + obj.getId() + " not found");
        }
        return entityRepository.save(obj);
    }

    @Override
    public void deleteEntity(AdditionalService obj) {
        entityRepository.delete(obj);
    }

    @Override
    public AdditionalService findEntityById(int id) {
        return entityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("AdditionalService with ID " + id + " not found"));
    }

}

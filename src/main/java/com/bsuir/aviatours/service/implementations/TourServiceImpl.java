package com.bsuir.aviatours.service.implementations;

import com.bsuir.aviatours.model.Role;
import com.bsuir.aviatours.model.Tour;
import com.bsuir.aviatours.repository.TourRepository;
import com.bsuir.aviatours.service.interfaces.EntityService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TourServiceImpl implements EntityService<Tour> {

    private final TourRepository entityRepository;

    public TourServiceImpl(TourRepository entityRepository) {
        this.entityRepository = entityRepository;
    }

    @Override
    public Tour saveEntity(Tour obj) {
        return entityRepository.save(obj);
    }

    @Override
    public List<Tour> getAllEntities() {
        return entityRepository.findAll();
    }

    @Override
    public Tour updateEntity(Tour obj) {
        if (!entityRepository.existsById(obj.getId())) {
            throw new EntityNotFoundException("Role with ID " + obj.getId() + " not found");
        }
        return entityRepository.save(obj);
    }

    @Override
    public void deleteEntity(Tour obj) {
        entityRepository.delete(obj);
    }

    @Override
    public Tour findEntityById(int id) {
        return entityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Role with ID " + id + " not found"));
    }

}

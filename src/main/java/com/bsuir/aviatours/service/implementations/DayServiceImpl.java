package com.bsuir.aviatours.service.implementations;

import com.bsuir.aviatours.model.Day;
import com.bsuir.aviatours.model.Role;
import com.bsuir.aviatours.repository.DayRepository;
import com.bsuir.aviatours.service.interfaces.EntityService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DayServiceImpl implements EntityService<Day> {

    private final DayRepository entityRepository;

    public DayServiceImpl(DayRepository entityRepository) {
        this.entityRepository = entityRepository;
    }

    @Override
    public Day saveEntity(Day obj) {
        return entityRepository.save(obj);
    }

    @Override
    public List<Day> getAllEntities() {
        return entityRepository.findAll();
    }

    @Override
    public Day updateEntity(Day obj) {
        if (!entityRepository.existsById(obj.getId())) {
            throw new EntityNotFoundException("Day with ID " + obj.getId() + " not found");
        }
        return entityRepository.save(obj);
    }

    @Override
    public void deleteEntity(Day obj) {
        entityRepository.delete(obj);
    }

    @Override
    public Day findEntityById(int id) {
        return entityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Day with ID " + id + " not found"));
    }

}

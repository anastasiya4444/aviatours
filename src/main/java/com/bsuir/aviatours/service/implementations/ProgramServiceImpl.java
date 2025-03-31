package com.bsuir.aviatours.service.implementations;

import com.bsuir.aviatours.model.Program;
import com.bsuir.aviatours.model.Role;
import com.bsuir.aviatours.repository.ProgramRepository;
import com.bsuir.aviatours.service.interfaces.EntityService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProgramServiceImpl implements EntityService<Program> {

    private final ProgramRepository entityRepository;

    public ProgramServiceImpl(ProgramRepository entityRepository) {
        this.entityRepository = entityRepository;
    }

    @Override
    public Program saveEntity(Program obj) {
        return entityRepository.save(obj);
    }

    @Override
    public List<Program> getAllEntities() {
        return entityRepository.findAll();
    }

    @Override
    public Program updateEntity(Program obj) {
        if (!entityRepository.existsById(obj.getId())) {
            throw new EntityNotFoundException("Program with ID " + obj.getId() + " not found");
        }
        return entityRepository.save(obj);
    }

    @Override
    public void deleteEntity(Program obj) {
        entityRepository.delete(obj);
    }

    @Override
    public Program findEntityById(int id) {
        return entityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Program with ID " + id + " not found"));
    }

}

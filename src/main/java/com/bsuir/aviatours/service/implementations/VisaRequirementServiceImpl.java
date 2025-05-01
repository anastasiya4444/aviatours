package com.bsuir.aviatours.service.implementations;

import com.bsuir.aviatours.repository.VisaRequirementRepository;
import com.bsuir.aviatours.service.interfaces.EntityService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VisaRequirementServiceImpl implements EntityService<VisaRequirement> {

    private final VisaRequirementRepository entityRepository;

    public VisaRequirementServiceImpl(VisaRequirementRepository entityRepository) {
        this.entityRepository = entityRepository;
    }

    @Override
    public VisaRequirement saveEntity(VisaRequirement obj) {
        return entityRepository.save(obj);
    }

    @Override
    public List<VisaRequirement> getAllEntities() {
        return entityRepository.findAll();
    }

    @Override
    public VisaRequirement updateEntity(VisaRequirement obj) {
        if (!entityRepository.existsById(obj.getId())) {
            throw new EntityNotFoundException("VisaRequirement with ID " + obj.getId() + " not found");
        }
        return entityRepository.save(obj);
    }

    @Override
    public void deleteEntity(VisaRequirement obj) {
        entityRepository.delete(obj);
    }

    @Override
    public VisaRequirement findEntityById(int id) {
        return entityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("VisaRequirement with ID " + id + " not found"));
    }

}

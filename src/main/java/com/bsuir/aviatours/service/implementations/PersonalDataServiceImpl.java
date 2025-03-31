package com.bsuir.aviatours.service.implementations;

import com.bsuir.aviatours.model.PersonalDatum;
import com.bsuir.aviatours.model.Role;
import com.bsuir.aviatours.repository.PersonalDataRepository;
import com.bsuir.aviatours.service.interfaces.EntityService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonalDataServiceImpl implements EntityService<PersonalDatum> {

    private final PersonalDataRepository entityRepository;

    public PersonalDataServiceImpl(PersonalDataRepository entityRepository) {
        this.entityRepository = entityRepository;
    }

    @Override
    public PersonalDatum saveEntity(PersonalDatum obj) {
        return entityRepository.save(obj);
    }

    @Override
    public List<PersonalDatum> getAllEntities() {
        return entityRepository.findAll();
    }

    @Override
    public PersonalDatum updateEntity(PersonalDatum obj) {
        if (!entityRepository.existsById(obj.getId())) {
            throw new EntityNotFoundException("PersonalDatum with ID " + obj.getId() + " not found");
        }
        return entityRepository.save(obj);
    }

    @Override
    public void deleteEntity(PersonalDatum obj) {
        entityRepository.delete(obj);
    }

    @Override
    public PersonalDatum findEntityById(int id) {
        return entityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("PersonalDatum with ID " + id + " not found"));
    }

}

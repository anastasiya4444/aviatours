package com.bsuir.aviatours.service.implementations;

import com.bsuir.aviatours.model.Role;
import com.bsuir.aviatours.model.UserPersonalDatum;
import com.bsuir.aviatours.repository.UserPersonalDataRepository;
import com.bsuir.aviatours.service.interfaces.EntityService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserPersonalDataServiceImpl implements EntityService<UserPersonalDatum> {

    private final UserPersonalDataRepository entityRepository;

    public UserPersonalDataServiceImpl(UserPersonalDataRepository entityRepository) {
        this.entityRepository = entityRepository;
    }

    @Override
    public UserPersonalDatum saveEntity(UserPersonalDatum obj) {
        return entityRepository.save(obj);
    }

    @Override
    public List<UserPersonalDatum> getAllEntities() {
        return entityRepository.findAll();
    }

    @Override
    public UserPersonalDatum updateEntity(UserPersonalDatum obj) {
        if (!entityRepository.existsById(obj.getUser().getId())) {
            throw new EntityNotFoundException("UserPersonalDatum with ID " + obj.getId() + " not found");
        }
        return entityRepository.save(obj);
    }

    @Override
    public void deleteEntity(UserPersonalDatum obj) {
        entityRepository.delete(obj);
    }

    @Override
    public UserPersonalDatum findEntityById(int id) {
        return entityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("UserPersonalDatum with ID " + id + " not found"));
    }

}

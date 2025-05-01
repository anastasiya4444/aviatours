package com.bsuir.aviatours.service.implementations;

import com.bsuir.aviatours.repository.ClientVisaRepository;
import com.bsuir.aviatours.service.interfaces.EntityService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientVisaServiceImpl implements EntityService<ClientVisa> {

    private final ClientVisaRepository entityRepository;

    public ClientVisaServiceImpl(ClientVisaRepository entityRepository) {
        this.entityRepository = entityRepository;
    }

    @Override
    public ClientVisa saveEntity(ClientVisa obj) {
        return entityRepository.save(obj);
    }

    @Override
    public List<ClientVisa> getAllEntities() {
        return entityRepository.findAll();
    }

    @Override
    public ClientVisa updateEntity(ClientVisa obj) {
        if (!entityRepository.existsById(obj.getId())) {
            throw new EntityNotFoundException("ClientVisa with ID " + obj.getId() + " not found");
        }
        return entityRepository.save(obj);
    }

    @Override
    public void deleteEntity(ClientVisa obj) {
        entityRepository.delete(obj);
    }

    @Override
    public ClientVisa findEntityById(int id) {
        return entityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ClientVisa with ID " + id + " not found"));
    }

}

package com.bsuir.aviatours.service.implementations;

import com.bsuir.aviatours.model.Role;
import com.bsuir.aviatours.repository.RoleRepository;
import com.bsuir.aviatours.service.interfaces.EntityService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements EntityService<Role> {

    private final RoleRepository entityRepository;

    public RoleServiceImpl(RoleRepository entityRepository) {
        this.entityRepository = entityRepository;
    }

    @Override
    public Role saveEntity(Role obj) {
        return entityRepository.save(obj);
    }

    @Override
    public List<Role> getAllEntities() {
        return entityRepository.findAll();
    }

    @Override
    public Role updateEntity(Role obj) {
        if (!entityRepository.existsById(obj.getId())) {
            throw new EntityNotFoundException("Role with ID " + obj.getId() + " not found");
        }
        return entityRepository.save(obj);
    }

    @Override
    public void deleteEntity(Role obj) {
        entityRepository.delete(obj);
    }

    @Override
    public Role findEntityById(int id) {
        return entityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Role with ID " + id + " not found"));
    }

}

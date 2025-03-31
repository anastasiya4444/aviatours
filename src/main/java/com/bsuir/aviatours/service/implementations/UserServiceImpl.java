package com.bsuir.aviatours.service.implementations;

import com.bsuir.aviatours.model.Role;
import com.bsuir.aviatours.model.User;
import com.bsuir.aviatours.repository.UserRepository;
import com.bsuir.aviatours.service.interfaces.EntityService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements EntityService<User> {

    private final UserRepository entityRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.entityRepository = userRepository;
    }

    @Override
    public User saveEntity(User obj) {
        return entityRepository.save(obj);
    }

    @Override
    public List<User> getAllEntities() {
        return entityRepository.findAll();
    }

    @Override
    public User updateEntity(User obj) {
        if (!entityRepository.existsById(obj.getId())) {
            throw new EntityNotFoundException("User with ID " + obj.getId() + " not found");
        }
        return entityRepository.save(obj);
    }

    @Override
    public void deleteEntity(User obj) {
        entityRepository.delete(obj);
    }

    @Override
    public User findEntityById(int id) {
        return entityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + id + " not found"));
    }

}

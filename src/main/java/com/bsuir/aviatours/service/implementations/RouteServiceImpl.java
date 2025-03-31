package com.bsuir.aviatours.service.implementations;

import com.bsuir.aviatours.model.Role;
import com.bsuir.aviatours.model.Route;
import com.bsuir.aviatours.repository.RouteRepository;
import com.bsuir.aviatours.service.interfaces.EntityService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteServiceImpl implements EntityService<Route> {

    private final RouteRepository entityRepository;

    public RouteServiceImpl(RouteRepository entityRepository) {
        this.entityRepository = entityRepository;
    }

    @Override
    public Route saveEntity(Route obj) {
        return entityRepository.save(obj);
    }

    @Override
    public List<Route> getAllEntities() {
        return entityRepository.findAll();
    }

    @Override
    public Route updateEntity(Route obj) {
        if (!entityRepository.existsById(obj.getId())) {
            throw new EntityNotFoundException("Route with ID " + obj.getId() + " not found");
        }
        return entityRepository.save(obj);
    }

    @Override
    public void deleteEntity(Route obj) {
        entityRepository.delete(obj);
    }

    @Override
    public Route findEntityById(int id) {
        return entityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Route with ID " + id + " not found"));
    }

}

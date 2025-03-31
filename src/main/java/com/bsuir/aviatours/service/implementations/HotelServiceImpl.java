package com.bsuir.aviatours.service.implementations;

import com.bsuir.aviatours.model.Hotel;
import com.bsuir.aviatours.model.Role;
import com.bsuir.aviatours.repository.HotelRepository;
import com.bsuir.aviatours.service.interfaces.EntityService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelServiceImpl implements EntityService<Hotel> {

    private final HotelRepository entityRepository;

    public HotelServiceImpl(HotelRepository entityRepository) {
        this.entityRepository = entityRepository;
    }

    @Override
    public Hotel saveEntity(Hotel obj) {
        return entityRepository.save(obj);
    }

    @Override
    public List<Hotel> getAllEntities() {
        return entityRepository.findAll();
    }

    @Override
    public Hotel updateEntity(Hotel obj) {
        if (!entityRepository.existsById(obj.getId())) {
            throw new EntityNotFoundException("Hotel with ID " + obj.getId() + " not found");
        }
        return entityRepository.save(obj);
    }

    @Override
    public void deleteEntity(Hotel obj) {
        entityRepository.delete(obj);
    }

    @Override
    public Hotel findEntityById(int id) {
        return entityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Hotel with ID " + id + " not found"));
    }

}

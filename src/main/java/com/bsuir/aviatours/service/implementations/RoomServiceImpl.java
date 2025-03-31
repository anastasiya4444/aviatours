package com.bsuir.aviatours.service.implementations;

import com.bsuir.aviatours.model.Role;
import com.bsuir.aviatours.model.Room;
import com.bsuir.aviatours.repository.RoomRepository;
import com.bsuir.aviatours.service.interfaces.EntityService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomServiceImpl implements EntityService<Room> {

    private final RoomRepository entityRepository;

    public RoomServiceImpl(RoomRepository entityRepository) {
        this.entityRepository = entityRepository;
    }

    @Override
    public Room saveEntity(Room obj) {
        return entityRepository.save(obj);
    }

    @Override
    public List<Room> getAllEntities() {
        return entityRepository.findAll();
    }

    @Override
    public Room updateEntity(Room obj) {
        if (!entityRepository.existsById(obj.getId())) {
            throw new EntityNotFoundException("Room with ID " + obj.getId() + " not found");
        }
        return entityRepository.save(obj);
    }

    @Override
    public void deleteEntity(Room obj) {
        entityRepository.delete(obj);
    }

    @Override
    public Room findEntityById(int id) {
        return entityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Room with ID " + id + " not found"));
    }

}

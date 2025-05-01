package com.bsuir.aviatours.service.implementations;

import com.bsuir.aviatours.model.AirTicket;
import com.bsuir.aviatours.model.Role;
import com.bsuir.aviatours.repository.AirTicketRepository;
import com.bsuir.aviatours.service.interfaces.EntityService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AirTicketServiceImpl implements EntityService<AirTicket> {

    private final AirTicketRepository entityRepository;

    public AirTicketServiceImpl(AirTicketRepository entityRepository) {
        this.entityRepository = entityRepository;
    }

    @Override
    public AirTicket saveEntity(AirTicket obj) {
        return entityRepository.save(obj);
    }

    @Override
    public List<AirTicket> getAllEntities() {
        return entityRepository.findAll();
    }

    @Override
    public AirTicket updateEntity(AirTicket obj) {
        if (!entityRepository.existsById(obj.getId())) {
            throw new EntityNotFoundException("AirTicket with ID " + obj.getId() + " not found");
        }
        System.out.println(obj.getSeatNumber());
        return entityRepository.save(obj);
    }

    @Override
    public void deleteEntity(AirTicket obj) {
        entityRepository.delete(obj);
    }

    @Override
    public AirTicket findEntityById(int id) {
        return entityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("AirTicket with ID " + id + " not found"));
    }

}

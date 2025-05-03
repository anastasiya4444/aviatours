package com.bsuir.aviatours.service.implementations;

import com.bsuir.aviatours.model.AirTicket;
import com.bsuir.aviatours.model.Role;
import com.bsuir.aviatours.repository.AirTicketRepository;
import com.bsuir.aviatours.service.business.adapter.DateTimeAdapter;
import com.bsuir.aviatours.service.interfaces.EntityService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
public class AirTicketServiceImpl implements EntityService<AirTicket> {

    private final AirTicketRepository entityRepository;
    private DateTimeAdapter dateTimeAdapter;

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
    public List<AirTicket> filterTickets(
            String flightNumber,
            String departureAirportCode,
            String arrivalAirportCode,
            Instant departureTimeFrom,
            Instant departureTimeTo,
            BigDecimal minCost,
            BigDecimal maxCost,
            Integer seatNumber,
            String status) {

        Specification<AirTicket> spec = Specification.where(null);

        if (flightNumber != null) {
            spec = spec.and((root, query, cb) ->
                    cb.like(root.get("flightNumber"), "%" + flightNumber + "%"));
        }

        if (departureAirportCode != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("departureAirportCode"), departureAirportCode));
        }

        if (arrivalAirportCode != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("arrivalAirportCode"), arrivalAirportCode));
        }

        if (departureTimeFrom != null) {
            spec = spec.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("departureTime"), departureTimeFrom));
        }

        if (departureTimeTo != null) {
            spec = spec.and((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("departureTime"), departureTimeTo));
        }

        if (minCost != null) {
            spec = spec.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("cost"), minCost));
        }

        if (maxCost != null) {
            spec = spec.and((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("cost"), maxCost));
        }

        if (seatNumber != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("seatNumber"), seatNumber));
        }

        if (status != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("status"), status));
        }

        return entityRepository.findAll(spec);
    }
}

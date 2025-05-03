package com.bsuir.aviatours.controller;

import com.bsuir.aviatours.dto.AirTicketDTO;
import com.bsuir.aviatours.model.AirTicket;
import com.bsuir.aviatours.service.business.adapter.DateTimeAdapter;
import com.bsuir.aviatours.service.business.adapter.IsoDateTimeAdapter;
import com.bsuir.aviatours.service.implementations.AirTicketServiceImpl;
import com.bsuir.aviatours.service.interfaces.EntityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/air_ticket")
@CrossOrigin
public class AirTicketController {

    private final AirTicketServiceImpl airTicketEntityService;

    public AirTicketController(AirTicketServiceImpl airTicketEntityService) {
        this.airTicketEntityService = airTicketEntityService;
    }

    @PostMapping("/add")
    public ResponseEntity<AirTicketDTO> add(@RequestBody AirTicketDTO airTicketDTO) {
        AirTicket savedTicket = airTicketEntityService.saveEntity(airTicketDTO.toEntity());
        return ResponseEntity.ok(AirTicketDTO.fromEntity(savedTicket));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<AirTicketDTO>> getAll() {
        List<AirTicketDTO> tickets = airTicketEntityService.getAllEntities().stream()
                .map(AirTicketDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AirTicketDTO> getById(@PathVariable int id) {
        AirTicket ticket = airTicketEntityService.findEntityById(id);
        return ResponseEntity.ok(AirTicketDTO.fromEntity(ticket));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        airTicketEntityService.deleteEntity(airTicketEntityService.findEntityById(id));
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update")
    public ResponseEntity<AirTicketDTO> update(@RequestBody AirTicketDTO airTicketDTO) {
        AirTicket updatedTicket = airTicketEntityService.updateEntity(airTicketDTO.toEntity());
        return ResponseEntity.ok(AirTicketDTO.fromEntity(updatedTicket));
    }

    @PostMapping("/batch_create")
    public ResponseEntity<List<AirTicketDTO>> batchCreate(@RequestBody BatchCreateRequest request) {
        List<AirTicket> newTickets = new ArrayList<>();
        AirTicket prototype = request.getPrototype().toEntity();

        for (int seat = request.getSeatFrom(); seat <= request.getSeatTo(); seat++) {
            AirTicket newTicket = new AirTicket();
            newTicket.setFlightNumber(prototype.getFlightNumber());
            newTicket.setDepartureAirportCode(prototype.getDepartureAirportCode());
            newTicket.setArrivalAirportCode(prototype.getArrivalAirportCode());
            newTicket.setDepartureTime(prototype.getDepartureTime());
            newTicket.setArrivalTime(prototype.getArrivalTime());
            newTicket.setCost(prototype.getCost());
            newTicket.setStatus(prototype.getStatus());
            newTicket.setSeatNumber(seat);

            newTickets.add(airTicketEntityService.saveEntity(newTicket));
        }

        List<AirTicketDTO> result = newTickets.stream()
                .map(AirTicketDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }
    @GetMapping("/filter")
    public ResponseEntity<List<AirTicketDTO>> filterTickets(
            @RequestParam(required = false) String flightNumber,
            @RequestParam(required = false) String departureAirportCode,
            @RequestParam(required = false) String arrivalAirportCode,
            @RequestParam(required = false) String departureTimeFrom,
            @RequestParam(required = false) String departureTimeTo,
            @RequestParam(required = false) BigDecimal minCost,
            @RequestParam(required = false) BigDecimal maxCost,
            @RequestParam(required = false) Integer seatNumber,
            @RequestParam(required = false) String status) {

        DateTimeAdapter dateAdapter = new IsoDateTimeAdapter();

        Instant fromInstant = departureTimeFrom != null
                ? dateAdapter.fromFrontendFormat(departureTimeFrom)
                : null;
        Instant toInstant = departureTimeTo != null
                ? dateAdapter.fromFrontendFormat(departureTimeTo)
                : null;

        List<AirTicket> filteredTickets = airTicketEntityService.filterTickets(
                flightNumber, departureAirportCode, arrivalAirportCode,
                fromInstant, toInstant, minCost, maxCost,
                seatNumber, status);

        List<AirTicketDTO> result = filteredTickets.stream()
                .map(AirTicketDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }
    static class BatchCreateRequest {
        private int seatFrom;
        private int seatTo;
        private AirTicketDTO prototype;

        public int getSeatFrom() {
            return seatFrom;
        }

        public void setSeatFrom(int seatFrom) {
            this.seatFrom = seatFrom;
        }

        public int getSeatTo() {
            return seatTo;
        }

        public void setSeatTo(int seatTo) {
            this.seatTo = seatTo;
        }

        public AirTicketDTO getPrototype() {
            return prototype;
        }

        public void setPrototype(AirTicketDTO prototype) {
            this.prototype = prototype;
        }
    }
}


package com.bsuir.aviatours.service.business.prototype;

import com.bsuir.aviatours.dto.AirTicketDTO;
import com.bsuir.aviatours.model.AirTicket;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class AirTicketPrototypeService {
    private final Map<String, AirTicketDTO> ticketPrototypes = new ConcurrentHashMap<>();

    public void registerPrototype(String flightNumber, AirTicketDTO prototype) {
        ticketPrototypes.put(flightNumber, prototype);
    }

    public AirTicketDTO createTicket(String flightNumber, int seatNumber) {
        AirTicketDTO prototype = ticketPrototypes.get(flightNumber);
        if (prototype == null) {
            throw new IllegalArgumentException("No prototype registered for flight " + flightNumber);
        }

        AirTicketDTO newTicket = prototype.copy();
        newTicket.setSeatNumber(seatNumber);
        return newTicket;
    }

    public List<AirTicketDTO> createMultipleTickets(String flightNumber, List<Integer> seatNumbers) {
        return seatNumbers.stream()
                .map(seat -> createTicket(flightNumber, seat))
                .collect(Collectors.toList());
    }
}
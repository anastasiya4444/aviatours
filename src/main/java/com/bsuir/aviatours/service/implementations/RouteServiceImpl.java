package com.bsuir.aviatours.service.implementations;

import com.bsuir.aviatours.model.AirTicket;
import com.bsuir.aviatours.model.Role;
import com.bsuir.aviatours.model.Route;
import com.bsuir.aviatours.repository.RouteRepository;
import com.bsuir.aviatours.service.interfaces.EntityService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Service
public class RouteServiceImpl implements EntityService<Route> {

    private final RouteRepository entityRepository;
    private final AirTicketServiceImpl airTicketService;

    public RouteServiceImpl(RouteRepository entityRepository, AirTicketServiceImpl airTicketService) {
        this.entityRepository = entityRepository;
        this.airTicketService = airTicketService;
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
    public List<List<AirTicket>> findAvailableRoutes(String departureAirportCode, String arrivalAirportCode, LocalDateTime departureDateTime, int maxStops) {
        List<List<AirTicket>> routes = new ArrayList<>();
        findRoutesRecursive(departureAirportCode, arrivalAirportCode, departureDateTime, maxStops, new ArrayList<>(), routes);
        return routes;
    }

    private void findRoutesRecursive(String currentDepartureAirportCode, String finalArrivalAirportCode, LocalDateTime currentDepartureDateTime, int maxStops, List<AirTicket> currentRoute, List<List<AirTicket>> routes) {
        if (maxStops < 0) {
            return; // Превышено максимальное количество пересадок
        }

        List<AirTicket> availableTickets = airTicketService.findAvailableTickets(currentDepartureAirportCode, finalArrivalAirportCode, currentDepartureDateTime);

        for (AirTicket ticket : availableTickets) {
            // Проверяем, что билет начинается из нужного аэропорта
            if (!ticket.getDepartureAirportCode().equals(currentDepartureAirportCode)) {
                continue; // Пропускаем билет, если аэропорт отправления не совпадает
            }

            List<AirTicket> newRoute = new ArrayList<>(currentRoute);
            newRoute.add(ticket);

            if (ticket.getArrivalAirportCode().equals(finalArrivalAirportCode)) {
                // Маршрут найден!
                routes.add(newRoute);
            } else {
                // Ищем дальше с пересадкой
                // Use LocalDateTime for plusHours
                LocalDateTime nextDepartureDateTime = LocalDateTime.ofInstant(ticket.getDepartureTime(), ZoneOffset.UTC).plusHours(2);
                findRoutesRecursive(ticket.getArrivalAirportCode(), finalArrivalAirportCode, nextDepartureDateTime, maxStops - 1, newRoute, routes); // Добавляем время на пересадку (2 часа)
            }
        }
    }
}

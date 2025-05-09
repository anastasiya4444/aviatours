package com.bsuir.aviatours.dto;

import com.bsuir.aviatours.model.AirTicket;
import com.bsuir.aviatours.model.Route;
import com.bsuir.aviatours.model.Tour;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class RouteDTO {
    private Integer id;
    private String departureAirportCode;
    private String arrivalAirportCode;
    private Instant departureDate;
    private Instant arrivalDate;
    private TourDTO tour;
    private Set<AirTicketDTO> airTickets = new LinkedHashSet<>();

    public RouteDTO() {}

    public RouteDTO(Integer id, String departureAirportCode, String arrivalAirportCode, Instant departureDate, Instant arrivalDate, TourDTO tour) {
        this.id = id;
        this.departureAirportCode = departureAirportCode;
        this.arrivalAirportCode = arrivalAirportCode;
        this.departureDate = departureDate;
        this.arrivalDate = arrivalDate;
        this.tour = tour;
    }
    public RouteDTO(Integer id, String departureAirportCode, String arrivalAirportCode,
                    Instant departureDate, Instant arrivalDate, TourDTO tour,
                    Set<AirTicketDTO> airTickets) {
        this(id, departureAirportCode, arrivalAirportCode, departureDate, arrivalDate, tour);
        this.airTickets = airTickets != null ? airTickets : new LinkedHashSet<>();
    }
    public Route toEntity(){
        Route route = new Route();
        route.setId(this.id);
        route.setDepartureAirportCode(this.departureAirportCode);
        route.setArrivalAirportCode(this.arrivalAirportCode);
        route.setDepartureDate(this.departureDate);
        route.setArrivalDate(this.arrivalDate);
//        if(this.tour != null){
//            route.setTour(this.tour.toEntity());
//        }
//        if (this.airTickets != null && !this.airTickets.isEmpty()) {
//            Set<AirTicket> tickets = this.airTickets.stream()
//                    .map(AirTicketDTO::toEntity)
//                    .collect(Collectors.toSet());
//            route.setAirTickets(tickets);
//        }
        return route;
    }

    public static RouteDTO fromEntity(Route route){
        RouteDTO routeDTO = new RouteDTO();
        if(route.getId() != null){
            routeDTO.setId(route.getId());
            routeDTO.setDepartureAirportCode(route.getDepartureAirportCode());
            routeDTO.setArrivalAirportCode(route.getArrivalAirportCode());
            routeDTO.setDepartureDate(route.getDepartureDate());
            routeDTO.setArrivalDate(route.getArrivalDate());
//            if(route.getTour().getId() != null){
//                routeDTO.setTour(TourDTO.fromEntity(route.getTour()));
//            }
//            if (route.getAirTickets() != null && !route.getAirTickets().isEmpty()) {
//                Set<AirTicketDTO> ticketDTOs = route.getAirTickets().stream()
//                        .map(AirTicketDTO::fromEntity)
//                        .collect(Collectors.toSet());
//                routeDTO.setAirTickets(ticketDTOs);
//            }
        }
        return routeDTO;
    }

    public Set<AirTicketDTO> getAirTickets() {
        return airTickets;
    }

    public void setAirTickets(Set<AirTicketDTO> airTickets) {
        this.airTickets = airTickets;
    }

    public TourDTO getTour() {
        return tour;
    }

    public void setTour(TourDTO tour) {
        this.tour = tour;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDepartureAirportCode() {
        return departureAirportCode;
    }

    public void setDepartureAirportCode(String departureAirportCode) {
        this.departureAirportCode = departureAirportCode;
    }

    public String getArrivalAirportCode() {
        return arrivalAirportCode;
    }

    public void setArrivalAirportCode(String arrivalAirportCode) {
        this.arrivalAirportCode = arrivalAirportCode;
    }

    public Instant getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Instant departureDate) {
        this.departureDate = departureDate;
    }

    public Instant getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(Instant arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

}
package com.bsuir.aviatours.dto;

import com.bsuir.aviatours.model.AirTicket;
import com.bsuir.aviatours.model.PersonalDatum;
import com.bsuir.aviatours.model.Route;
import jakarta.persistence.Column;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;

public class AirTicketDTO {
    private Integer id;
    private String flightNumber;
    private String departureAirportCode;
    private String arrivalAirportCode;
    private Instant departureTime;
    private Instant arrivalTime;
    private PersonalDatumDTO passenger;
    private RouteDTO route;
    private BigDecimal cost;

    public AirTicketDTO() {}

    public AirTicketDTO(Integer id, RouteDTO route, PersonalDatumDTO passenger, Instant arrivalTime, Instant departureTime,
                        String arrivalAirportCode, String departureAirportCode, String flightNumber, BigDecimal cost) {
        this.id = id;
        this.route = route;
        this.passenger = passenger;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
        this.arrivalAirportCode = arrivalAirportCode;
        this.departureAirportCode = departureAirportCode;
        this.flightNumber = flightNumber;
        this.cost = cost;
    }

    public AirTicket toEntity(){
        AirTicket airTicket = new AirTicket();
        airTicket.setId(id);
        airTicket.setDepartureAirportCode(departureAirportCode);
        airTicket.setArrivalAirportCode(arrivalAirportCode);
        airTicket.setDepartureTime(departureTime);
        airTicket.setArrivalTime(arrivalTime);
        if(passenger.getId() != null){
            airTicket.setPassenger(passenger.toEntity());
        }
        if(route.getId() != null){
            airTicket.setRoute(route.toEntity());
        }
        airTicket.setFlightNumber(flightNumber);
        airTicket.setCost(cost);
        return airTicket;
    }

    public static AirTicketDTO fromEntity(AirTicket airTicket){
        AirTicketDTO airTicketDTO = new AirTicketDTO();
        if(airTicket.getId() != null){
            airTicketDTO.setId(airTicket.getId());
            airTicketDTO.setDepartureAirportCode(airTicket.getDepartureAirportCode());
            airTicketDTO.setArrivalAirportCode(airTicket.getArrivalAirportCode());
            airTicketDTO.setDepartureTime(airTicket.getDepartureTime());
            airTicketDTO.setArrivalTime(airTicket.getArrivalTime());
            if(airTicket.getPassenger() != null){
                airTicketDTO.setPassenger(PersonalDatumDTO.fromEntity(airTicket.getPassenger()));
            }
            if(airTicket.getRoute() != null){
                airTicketDTO.setRoute(RouteDTO.fromEntity(airTicket.getRoute()));
            }
            airTicketDTO.setFlightNumber(airTicket.getFlightNumber());
            airTicketDTO.setCost(airTicket.getCost());
        }
        return airTicketDTO;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
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

    public Instant getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Instant departureTime) {
        this.departureTime = departureTime;
    }

    public Instant getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Instant arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public PersonalDatumDTO getPassenger() {
        return passenger;
    }

    public void setPassenger(PersonalDatumDTO passenger) {
        this.passenger = passenger;
    }

    public RouteDTO getRoute() {
        return route;
    }

    public void setRoute(RouteDTO route) {
        this.route = route;
    }
}
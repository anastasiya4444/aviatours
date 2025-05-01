package com.bsuir.aviatours.dto;

import com.bsuir.aviatours.model.AirTicket;
import com.bsuir.aviatours.service.business.adapter.DateTimeAdapter;
import com.bsuir.aviatours.service.business.adapter.IsoDateTimeAdapter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AirTicketDTO {
    private Integer id;
    private String flightNumber;
    private String departureAirportCode;
    private String arrivalAirportCode;
    private String departureTime;
    private String arrivalTime;
    private PersonalDatumDTO passenger;
    private RouteDTO route;
    private BigDecimal cost;
    private Integer seatNumber;
    private String status;
    private static final DateTimeAdapter DATE_ADAPTER = new IsoDateTimeAdapter();

    public AirTicketDTO() {}

    public AirTicketDTO(Integer id, RouteDTO route, PersonalDatumDTO passenger, String arrivalTime,
                        String departureTime, String arrivalAirportCode, String departureAirportCode,
                        String flightNumber, BigDecimal cost, Integer seatNumber, String status) {
        this.id = id;
        this.route = route;
        this.passenger = passenger;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
        this.arrivalAirportCode = arrivalAirportCode;
        this.departureAirportCode = departureAirportCode;
        this.flightNumber = flightNumber;
        this.cost = cost;
        this.seatNumber = seatNumber;
        this.status = status;
    }

    public AirTicketDTO(AirTicketDTO source) {
        this.id = source.id;
        this.flightNumber = source.flightNumber;
        this.departureAirportCode = source.departureAirportCode;
        this.arrivalAirportCode = source.arrivalAirportCode;
        this.departureTime = source.departureTime;
        this.arrivalTime = source.arrivalTime;
        this.route = source.route;
        this.cost = source.cost;
        this.seatNumber = source.seatNumber;
        if(source.passenger != null){
            this.passenger = source.passenger;
        }
        if(source.route != null){
            this.status = source.status;
        }


    }

    public AirTicketDTO copy() {
        return new AirTicketDTO(this);
    }

    public AirTicket toEntity(){
        AirTicket airTicket = new AirTicket();
        airTicket.setId(id);
        airTicket.setDepartureTime(DATE_ADAPTER.fromFrontendFormat(this.departureTime));
        airTicket.setArrivalTime(DATE_ADAPTER.fromFrontendFormat(this.arrivalTime));
        if(passenger != null){
            airTicket.setPassenger(passenger.toEntity());
        }
        if(route != null){
            airTicket.setRoute(route.toEntity());
        }
        System.out.println(seatNumber);
        if(seatNumber != null){
            airTicket.setSeatNumber(seatNumber);
        }
        airTicket.setFlightNumber(flightNumber);
        airTicket.setCost(cost);
        airTicket.setStatus(status);
        airTicket.setDepartureAirportCode(departureAirportCode);
        airTicket.setArrivalAirportCode(arrivalAirportCode);
        return airTicket;
    }

    public static AirTicketDTO fromEntity(AirTicket airTicket){
        AirTicketDTO airTicketDTO = new AirTicketDTO();
        if(airTicket.getId() != null){
            airTicketDTO.setId(airTicket.getId());
            airTicketDTO.setDepartureTime(DATE_ADAPTER.toFrontendFormat(airTicket.getDepartureTime()));
            airTicketDTO.setArrivalTime(DATE_ADAPTER.toFrontendFormat(airTicket.getArrivalTime()));
            if(airTicket.getPassenger() != null){
                airTicketDTO.setPassenger(PersonalDatumDTO.fromEntity(airTicket.getPassenger()));
            }
            if(airTicket.getRoute() != null){
                airTicketDTO.setRoute(RouteDTO.fromEntity(airTicket.getRoute()));
            }
            airTicketDTO.setFlightNumber(airTicket.getFlightNumber());
            airTicketDTO.setCost(airTicket.getCost());
            airTicketDTO.setSeatNumber(airTicket.getSeatNumber());
            airTicketDTO.setStatus(airTicket.getStatus());
            airTicketDTO.setDepartureAirportCode(airTicket.getDepartureAirportCode());
            airTicketDTO.setArrivalAirportCode(airTicket.getArrivalAirportCode());
        }
        return airTicketDTO;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(Integer seatNumber) {
        this.seatNumber = seatNumber;
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

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
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
package com.bsuir.aviatours.dto;

import com.bsuir.aviatours.model.Booking;
import com.bsuir.aviatours.model.Payment;
import com.bsuir.aviatours.model.Room;
import jakarta.persistence.Column;

import java.math.BigDecimal;
import java.time.Instant;

public class BookingDTO {
    private Integer id;
    private RouteDTO route;
    private UserDTO user;
    private PaymentDTO payment;
    private Instant createdAt;
    private Room room;

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    private BigDecimal totalCost;

    public BookingDTO() {}

    public BookingDTO(Integer id, Instant createdAt, PaymentDTO payment, UserDTO user, RouteDTO route) {
        this.id = id;
        this.createdAt = createdAt;
        this.payment = payment;
        this.user = user;
        this.route = route;
    }

    public Booking toEntity(){
        Booking booking = new Booking();
        booking.setId(id);
        booking.setCreatedAt(createdAt);
        if(payment != null){
            booking.setPayment(payment.toEntity());
        }
        if(user != null){
            booking.setUser(user.toEntity());
        }
        if(route != null){
            booking.setRoute(route.toEntity());
        }
        booking.setTotalCost(totalCost);
        return booking;
    }

    public static BookingDTO fromEntity(Booking booking){
        BookingDTO bookingDTO = new BookingDTO();
        if(booking.getId() != null){
            bookingDTO.setId(booking.getId());
            bookingDTO.setCreatedAt(booking.getCreatedAt());
            if(booking.getPayment() != null){
                bookingDTO.setPayment(PaymentDTO.fromEntity(booking.getPayment()));
            }
            if(booking.getUser() != null){
                bookingDTO.setUser(UserDTO.fromEntity(booking.getUser()));
            }
            if(booking.getRoute() != null){
                bookingDTO.setRoute(RouteDTO.fromEntity(booking.getRoute()));
            }
            if(booking.getTotalCost() != null){
                bookingDTO.setTotalCost(booking.getTotalCost());
            }
        }
        return bookingDTO;
    }

    public RouteDTO getRoute() {
        return route;
    }

    public void setRoute(RouteDTO route) {
        this.route = route;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public PaymentDTO getPayment() {
        return payment;
    }

    public void setPayment(PaymentDTO payment) {
        this.payment = payment;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

}
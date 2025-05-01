package com.bsuir.aviatours.dto;

import com.bsuir.aviatours.model.Booking;
import com.bsuir.aviatours.model.Payment;

import java.time.Instant;

public class BookingDTO {
    private Integer id;
    private RouteDTO route;
    private UserDTO user;
    private Payment payment;
    private Instant createdAt;

    public BookingDTO() {}

    public BookingDTO(Integer id, Instant createdAt, Payment payment, UserDTO user, RouteDTO route) {
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
        if(payment.getId() != null){
            booking.setPayment(payment);
        }
        if(user.getId() != null){
            booking.setUser(user.toEntity());
        }
        if(route.getId() != null){
            booking.setRoute(route.toEntity());
        }
        return booking;
    }

    public static BookingDTO fromEntity(Booking booking){
        BookingDTO bookingDTO = new BookingDTO();
        if(booking.getId() != null){
            bookingDTO.setId(booking.getId());
            bookingDTO.setCreatedAt(booking.getCreatedAt());
            if(booking.getPayment() != null){
                bookingDTO.setPayment(booking.getPayment());
            }
            if(booking.getUser() != null){
                bookingDTO.setUser(UserDTO.fromEntity(booking.getUser()));
            }
            if(booking.getRoute() != null){
                bookingDTO.setRoute(RouteDTO.fromEntity(booking.getRoute()));
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

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

}
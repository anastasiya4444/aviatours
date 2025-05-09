package com.bsuir.aviatours.service.business.booking;

import com.bsuir.aviatours.model.Booking;
import com.bsuir.aviatours.model.Payment;
import com.bsuir.aviatours.model.Route;
import com.bsuir.aviatours.model.User;

import java.time.Instant;

public class BookingBuilder {

    private Booking booking = new Booking();

    public BookingBuilder setRoute(Route route) {
        booking.setRoute(route);
        return this;
    }

    public BookingBuilder setUser(User user) {
        booking.setUser(user);
        return this;
    }

    public BookingBuilder setPayment(Payment payment) {
        booking.setPayment(payment);
        return this;
    }

    public BookingBuilder setCreatedAt(Instant createdAt) {
        booking.setCreatedAt(createdAt);
        return this;
    }

    public Booking build() {
        return booking;
    }
}
package com.bsuir.aviatours.service.business.booking;

import com.bsuir.aviatours.model.Booking;
import com.bsuir.aviatours.repository.BookingRepository;

import java.math.BigDecimal;
import java.util.List;

public class CreateBookingCommand implements BookingCommand {

    private final Booking booking;
    private final BookingRepository bookingRepository;

    public CreateBookingCommand(Booking booking, BookingRepository bookingRepository) {
        this.booking = booking;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public void execute() {
        bookingRepository.save(booking);
    }

    @Override
    public void undo() {
        bookingRepository.delete(booking);
    }
}


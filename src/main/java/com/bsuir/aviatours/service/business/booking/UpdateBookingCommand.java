package com.bsuir.aviatours.service.business.booking;

import com.bsuir.aviatours.model.Booking;
import com.bsuir.aviatours.repository.BookingRepository;

// UpdateBookingCommand
public class UpdateBookingCommand implements BookingCommand {

    private final Booking booking;
    private final BookingRepository bookingRepository;
    private final Booking oldBooking;

    public UpdateBookingCommand(Booking booking, BookingRepository bookingRepository) {
        this.booking = booking;
        this.bookingRepository = bookingRepository;
        this.oldBooking = bookingRepository.findById(booking.getId()).orElse(null);
    }

    @Override
    public void execute() {
        bookingRepository.save(booking);
    }

    @Override
    public void undo() {
        if (oldBooking != null) {
            bookingRepository.save(oldBooking);
        } else {
            bookingRepository.delete(booking);
        }
    }
}

package com.bsuir.aviatours.service.business.booking;

import com.bsuir.aviatours.model.Booking;
import com.bsuir.aviatours.repository.BookingRepository;

public class CancelBookingCommand implements BookingCommand {

    private final int bookingId;
    private final BookingRepository bookingRepository;
    private Booking booking;

    public CancelBookingCommand(int bookingId, BookingRepository bookingRepository) {
        this.bookingId = bookingId;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public void execute() {
        booking = bookingRepository.findById(bookingId).orElse(null);
        if (booking != null) {
            bookingRepository.delete(booking);
        }
    }

    @Override
    public void undo() {
        if (booking != null) {
            bookingRepository.save(booking);
        }
    }
}

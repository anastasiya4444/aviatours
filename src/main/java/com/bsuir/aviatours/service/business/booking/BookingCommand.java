package com.bsuir.aviatours.service.business.booking;

public interface BookingCommand {
    void execute();
    void undo();
}

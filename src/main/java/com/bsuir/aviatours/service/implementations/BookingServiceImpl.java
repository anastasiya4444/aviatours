package com.bsuir.aviatours.service.implementations;

import com.bsuir.aviatours.model.Booking;
import com.bsuir.aviatours.repository.BookingRepository;
import com.bsuir.aviatours.service.interfaces.EntityService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingServiceImpl implements EntityService<Booking> {

    private final BookingRepository bookingRepository;

    public BookingServiceImpl(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public Booking saveEntity(Booking obj) {
        return bookingRepository.save(obj);
    }

    @Override
    public List<Booking> getAllEntities() {
        return bookingRepository.findAll();
    }

    @Override
    public Booking updateEntity(Booking obj) {
        if (!bookingRepository.existsById(obj.getId())) {
            throw new EntityNotFoundException("Booking with ID " + obj.getId() + " not found");
        }
        return bookingRepository.save(obj);
    }

    @Override
    public void deleteEntity(Booking obj) {
        bookingRepository.delete(obj);
    }

    @Override
    public Booking findEntityById(int id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking with ID " + id + " not found"));
    }

}

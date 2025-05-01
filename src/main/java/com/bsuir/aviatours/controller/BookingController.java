package com.bsuir.aviatours.controller;

import com.bsuir.aviatours.dto.BookingDTO;
import com.bsuir.aviatours.model.Booking;
import com.bsuir.aviatours.service.interfaces.EntityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/booking")
@CrossOrigin
public class BookingController {

    private final EntityService<Booking> activityEntityService;

    public BookingController(EntityService<Booking> bookingEntityService) {
        this.activityEntityService = bookingEntityService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody BookingDTO booking) {
        activityEntityService.saveEntity(booking.toEntity());
        return ResponseEntity.ok("Booking saved successfully");
    }

    @GetMapping("/getAll")
    public List<BookingDTO> getAll() {
        return activityEntityService.getAllEntities()
                .stream()
                .map(BookingDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDTO> getById(@PathVariable int id) {
        BookingDTO booking = BookingDTO.fromEntity(activityEntityService.findEntityById(id));
        return ResponseEntity.ok(booking);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        BookingDTO booking = BookingDTO.fromEntity(activityEntityService.findEntityById(id));
        activityEntityService.deleteEntity(booking.toEntity());
        return ResponseEntity.ok("Booking deleted successfully");
    }

    @PutMapping("/update")
    public ResponseEntity<BookingDTO> update(@RequestBody BookingDTO booking1) {
        BookingDTO booking = BookingDTO.fromEntity(activityEntityService.updateEntity(booking1.toEntity()));
        return ResponseEntity.ok(booking);
    }
}

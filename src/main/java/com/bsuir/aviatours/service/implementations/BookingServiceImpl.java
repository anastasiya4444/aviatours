package com.bsuir.aviatours.service.implementations;

import com.bsuir.aviatours.model.*;
import com.bsuir.aviatours.repository.*;
import com.bsuir.aviatours.service.business.booking.CancelBookingCommand;
import com.bsuir.aviatours.service.business.booking.CreateBookingCommand;
import com.bsuir.aviatours.service.business.booking.UpdateBookingCommand;
import com.bsuir.aviatours.service.interfaces.EntityService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingServiceImpl implements EntityService<Booking> {

    private final BookingRepository bookingRepository;
    private final AirTicketRepository airTicketRepository;
    private final RoomRepository roomRepository;
    private final ActivityRepository activityRepository;
    private final PaymentServiceImpl paymentService;
    private final RouteRepository routeRepository;

    public BookingServiceImpl(BookingRepository bookingRepository, AirTicketRepository airTicketRepository, RoomRepository roomRepository, ActivityRepository activityRepository, PaymentServiceImpl paymentService, RouteRepository routeRepository) {
        this.bookingRepository = bookingRepository;
        this.airTicketRepository = airTicketRepository;
        this.roomRepository = roomRepository;
        this.activityRepository = activityRepository;
        this.paymentService = paymentService;
        this.routeRepository = routeRepository;
    }

    @Override
    public Booking saveEntity(Booking obj) {
        CreateBookingCommand command = new CreateBookingCommand(obj, bookingRepository);
        command.execute();
        return obj;
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
        UpdateBookingCommand command = new UpdateBookingCommand(obj, bookingRepository);
        command.execute();
        return obj;
    }

    @Override
    public void deleteEntity(Booking obj) {
        CancelBookingCommand command = new CancelBookingCommand(obj.getId(), bookingRepository);
        command.execute();
    }

    @Override
    public Booking findEntityById(int id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking with ID " + id + " not found"));
    }

    public BigDecimal calculateTotalPrice(
            List<Integer> airTicketIds,
            Integer roomId,
            List<Integer> activityIds) {

        BigDecimal total = BigDecimal.ZERO;

        if (airTicketIds != null && !airTicketIds.isEmpty()) {
            BigDecimal ticketsSum = airTicketRepository.findAllById(airTicketIds).stream()
                    .map(AirTicket::getCost)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            total = total.add(ticketsSum);
        }

        if (roomId != null) {
            Room room = roomRepository.findById(roomId)
                    .orElseThrow(() -> new EntityNotFoundException("Room not found"));
            total = total.add(room.getCost());
        }

        if (activityIds != null && !activityIds.isEmpty()) {
            BigDecimal activitiesSum = activityRepository.findAllById(activityIds).stream()
                    .map(Activity::getCost)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            total = total.add(activitiesSum);
        }

        return total;
    }

    public Booking createBooking(
            Integer routeId,
            Integer roomId,
            List<Integer> airTicketIds,
            List<Integer> activityIds,
            Payment payment,
            User user) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new EntityNotFoundException("Route not found for ID: " + routeId));

        Room room = null;
        if (roomId != null) {
            room = roomRepository.findById(roomId)
                    .orElseThrow(() -> new EntityNotFoundException("Room not found for ID: " + roomId));
        }
        Booking booking = new Booking();
        booking.setRoute(route);
        if (user != null) {
            booking.setUser(user);
        }

        booking.setPayment(payment);

        booking.setRoom(room);

        booking.setTotalCost(payment.getAmount());

        return bookingRepository.save(booking);
    }
    }


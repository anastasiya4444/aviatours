package com.bsuir.aviatours.controller;

import com.bsuir.aviatours.dto.BookingDTO;
import com.bsuir.aviatours.dto.BookingRequestDTO;
import com.bsuir.aviatours.dto.PaymentDTO;
import com.bsuir.aviatours.dto.PriceCalculationDTO;
import com.bsuir.aviatours.model.Booking;
import com.bsuir.aviatours.model.Payment;
import com.bsuir.aviatours.model.Route;
import com.bsuir.aviatours.model.User;
import com.bsuir.aviatours.service.business.BookingPdfService;
import com.bsuir.aviatours.service.business.booking.BookingBuilder;
import com.bsuir.aviatours.service.implementations.BookingServiceImpl;
import com.bsuir.aviatours.service.implementations.PaymentServiceImpl;
import com.bsuir.aviatours.service.implementations.RouteServiceImpl;
import com.bsuir.aviatours.service.implementations.UserServiceImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/booking")
@CrossOrigin
public class BookingController {

    private final BookingServiceImpl bookingEntityService;
    private final RouteServiceImpl routeRepository;
    private final UserServiceImpl userRepository;
    private final PaymentServiceImpl paymentService;
    private final BookingPdfService bookingPdfService;

    public BookingController(BookingServiceImpl bookingEntityService, RouteServiceImpl routeRepository, UserServiceImpl userRepository, PaymentServiceImpl paymentService, BookingPdfService bookingPdfService) {
        this.bookingEntityService = bookingEntityService;
        this.routeRepository = routeRepository;
        this.userRepository = userRepository;
        this.paymentService = paymentService;
        this.bookingPdfService = bookingPdfService;
    }

    @PostMapping("/add")
    public ResponseEntity<BookingDTO> add(
            @RequestBody BookingDTO bookingDTO,
            Authentication authentication) {

        String username = authentication.getName();
        User user = userRepository.findByUsername(username);

        Route route = routeRepository.findEntityById(bookingDTO.getRoute().getId());

        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setPaymentMethod("CREDIT_CARD");
        paymentDTO.setPaymentStatus("PENDING");
        paymentDTO.setAmount(BigDecimal.valueOf(11));

        Payment payment = paymentService.saveEntity(paymentDTO.toEntity());

        Booking booking = new BookingBuilder()
                .setRoute(route)
                .setUser(user)
                .setPayment(payment)
                .setCreatedAt(Instant.now())
                .build();

        Booking savedBooking = bookingEntityService.saveEntity(booking);

        return ResponseEntity.ok(BookingDTO.fromEntity(savedBooking));
    }

    @GetMapping("/getAll")
    public List<BookingDTO> getAll() {
        return bookingEntityService.getAllEntities()
                .stream()
                .map(BookingDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDTO> getById(@PathVariable int id) {
        Booking booking = bookingEntityService.findEntityById(id);
        return ResponseEntity.ok(BookingDTO.fromEntity(booking));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        Booking booking = bookingEntityService.findEntityById(id);
        bookingEntityService.deleteEntity(BookingDTO.fromEntity(booking).toEntity());
        return ResponseEntity.ok("Booking deleted successfully");
    }

    @PutMapping("/update")
    public ResponseEntity<BookingDTO> update(@RequestBody BookingDTO booking1) {
        Booking booking = bookingEntityService.updateEntity(booking1.toEntity());
        return ResponseEntity.ok(BookingDTO.fromEntity(booking));
    }

    @PostMapping("/calculate-price")
    public ResponseEntity<BigDecimal> calculateTotalPrice(
            @RequestBody PriceCalculationDTO calculationDTO,
            @AuthenticationPrincipal User user) {

        BigDecimal totalPrice = bookingEntityService.calculateTotalPrice(
                calculationDTO.getAirTicketIds(),
                calculationDTO.getRoomId(),
                calculationDTO.getActivityIds()
        );

        return ResponseEntity.ok(totalPrice);
    }

    @PostMapping("/book")
    public ResponseEntity<byte[]> createBooking(
            @RequestBody BookingRequestDTO bookingRequest) {

        User currentUser = bookingRequest.getUser();
        currentUser = userRepository.findByUsername(currentUser.getUsername());

        BigDecimal totalCost = bookingEntityService.calculateTotalPrice(
                bookingRequest.getAirTicketIds(),
                bookingRequest.getRoomId(),
                bookingRequest.getActivityIds()
        );

        Payment payment = new Payment();
        payment.setAmount(totalCost);
        payment.setPaymentMethod(bookingRequest.getPaymentMethod());
        payment.setPaymentStatus("PENDING");
        payment = paymentService.saveEntity(payment);

        Booking booking = bookingEntityService.createBooking(
                bookingRequest.getRouteId(),
                bookingRequest.getRoomId(),
                bookingRequest.getAirTicketIds(),
                bookingRequest.getActivityIds(),
                payment,
                currentUser
        );

        ByteArrayOutputStream pdfContent = bookingPdfService.generateBookingReportPdf(booking.getId());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "booking_report_" + booking.getId() + ".pdf");
        headers.setContentLength(pdfContent.size());

        return new ResponseEntity<>(pdfContent.toByteArray(), headers, HttpStatus.OK);
    }
}

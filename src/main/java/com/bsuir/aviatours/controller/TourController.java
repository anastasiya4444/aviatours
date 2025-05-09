package com.bsuir.aviatours.controller;

import com.bsuir.aviatours.dto.*;
import com.bsuir.aviatours.model.*;
import com.bsuir.aviatours.service.business.adapter.IsoDateTimeAdapter;
import com.bsuir.aviatours.service.implementations.TourServiceImpl;
import com.bsuir.aviatours.service.interfaces.EntityService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tour")
@CrossOrigin
public class TourController {

    private final TourServiceImpl tourEntityService;
    private final EntityService<Route> routeService;
    private final EntityService<AirTicket> airTicketService;
    private final EntityService<Program> programService;
    private final EntityService<Day> dayService;
    private final EntityService<Activity> activityService;
    private final EntityService<Hotel> hotelService;
    private final EntityService<TourHotel> tourHotelService;
    private final EntityService<AdditionalService> additionalServiceService;
    private final EntityService<TourAdditionalService> tourAdditionalServiceService;
    private final DateTimeFormatter dateTimeFormatter;

    public TourController(TourServiceImpl tourEntityService, EntityService<Route> routeService,
                          EntityService<AirTicket> airTicketService, EntityService<Program> programService,
                          EntityService<Day> dayService, EntityService<Activity> activityService,
                          EntityService<Hotel> hotelService, EntityService<TourHotel> tourHotelService,
                          EntityService<AdditionalService> additionalServiceService,
                          EntityService<TourAdditionalService> tourAdditionalServiceService,
                          IsoDateTimeAdapter dateTimeFormatter) {
        this.tourEntityService = tourEntityService;
        this.routeService = routeService;
        this.airTicketService = airTicketService;
        this.programService = programService;
        this.dayService = dayService;
        this.activityService = activityService;
        this.hotelService = hotelService;
        this.tourHotelService = tourHotelService;
        this.additionalServiceService = additionalServiceService;
        this.tourAdditionalServiceService = tourAdditionalServiceService;
        this.dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;
    }
    private Instant parseDateTime(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.trim().isEmpty()) {
            return null;
        }
        try {
            return Instant.from(dateTimeFormatter.parse(dateTimeStr));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Expected ISO-8601 format: " + dateTimeStr);
        }
    }

    @PostMapping("/createBasic")
    public ResponseEntity<?> createBasicTour(@RequestBody TourDTO tourDTO) {
        try {
            Tour tour = new Tour();
            tour.setTourName(tourDTO.getTourName());
            tour.setDescription(tourDTO.getDescription());
            tour.setCreatedAt(Instant.now());

            Tour savedTour = tourEntityService.saveEntity(tour);

            TourDTO responseDto = new TourDTO();
            responseDto.setId(savedTour.getId());
            responseDto.setTourName(savedTour.getTourName());
            responseDto.setDescription(savedTour.getDescription());
            responseDto.setCreatedAt(savedTour.getCreatedAt().toString());

            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{tourId}/addRoutes")
    public ResponseEntity<?> addRoutes(
            @PathVariable Integer tourId,
            @RequestBody Set<RouteWithTicketsDTO> routesWithTickets) {
        try {
            Tour tour = tourEntityService.findEntityById(tourId);
            Set<RouteDTO> addedRoutes = new LinkedHashSet<>();

            for (RouteWithTicketsDTO routeWithTickets : routesWithTickets) {
                Route route = new Route();
                route.setTour(tour);
                route.setDepartureAirportCode(routeWithTickets.getDepartureAirportCode());
                route.setArrivalAirportCode(routeWithTickets.getArrivalAirportCode());
                route.setDepartureDate(routeWithTickets.getParsedDepartureDate());
                route.setArrivalDate(routeWithTickets.getParsedArrivalDate());
                Route savedRoute = routeService.saveEntity(route);

                if (routeWithTickets.getTicketIds() != null && !routeWithTickets.getTicketIds().isEmpty()) {
                    Set<AirTicket> tickets = routeWithTickets.getTicketIds().stream()
                            .map(ticketId -> {
                                AirTicket ticket = airTicketService.findEntityById(ticketId);
                                if (ticket == null) {
                                    throw new IllegalArgumentException("Air ticket not found with ID: " + ticketId);
                                }
                                ticket.setRoute(savedRoute);
                                return airTicketService.saveEntity(ticket);
                            })
                            .collect(Collectors.toSet());
                }

                addedRoutes.add(RouteDTO.fromEntity(savedRoute));
            }

            return ResponseEntity.ok(TourDTO.fromEntity(tour));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/{tourId}/addProgram")
    public ResponseEntity<?> addProgram(
            @PathVariable Integer tourId,
            @RequestBody ProgramDTO programDTO) {
        try {
            Tour tour = tourEntityService.findEntityById(tourId);

            Program program = new Program();
            program.setCreatedAt(Instant.now());
            Program savedProgram = programService.saveEntity(program);
            tour.setProgram(savedProgram);
            tourEntityService.updateEntity(tour);

            for (DayDTO dayDTO : programDTO.getDays()) {
                Day day = new Day();
                day.setProgram(savedProgram);
                day.setDayNumber(dayDTO.getDayNumber());
                day.setCreatedAt(Instant.now());

                Day savedDay = dayService.saveEntity(day);

                if (dayDTO.getActivities() != null && !dayDTO.getActivities().isEmpty()) {
                    Set<Activity> activities = dayDTO.getActivities().stream()
                            .map(activityDTO -> activityService.findEntityById(activityDTO.getId()))
                            .filter(Objects::nonNull)
                            .collect(Collectors.toSet());

                    savedDay.setActivities(activities);
                    dayService.saveEntity(savedDay);
                }
            }

            return ResponseEntity.ok(TourDTO.fromEntity(tour));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{tourId}/addServices")
    public ResponseEntity<?> addServices(
            @PathVariable Integer tourId,
            @RequestBody TourServicesDTO servicesDTO) {
        try {
            Tour tour = tourEntityService.findEntityById(tourId);

            if (servicesDTO.getHotelIds() != null && !servicesDTO.getHotelIds().isEmpty()) {
                for (Integer hotelId : servicesDTO.getHotelIds()) {
                    TourHotel tourHotel = new TourHotel();

                   TourHotelId tourHotelId = new TourHotelId();
                    tourHotelId.setTourId(tour.getId());
                    tourHotelId.setHotelId(hotelId);
                    tourHotel.setId(tourHotelId);

                    tourHotel.setTour(tour);
                    tourHotel.setHotel(hotelService.findEntityById(hotelId));

                    tourHotelService.saveEntity(tourHotel);
                }
            }

            if (servicesDTO.getAdditionalServiceIds() != null) {
                for (Integer serviceId : servicesDTO.getAdditionalServiceIds()) {
                    TourAdditionalService tourService = new TourAdditionalService();
                    tourService.setTour(tour);
                    tourService.setService(additionalServiceService.findEntityById(serviceId));
                    tourAdditionalServiceService.saveEntity(tourService);
                }
            }

            return ResponseEntity.ok(TourDTO.fromEntity(tour));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/add")
    public ResponseEntity<TourDTO> add(@RequestBody TourDTO tourDTO) {
        tourEntityService.createTour(tourDTO);
        return ResponseEntity.ok(tourDTO);
    }

    @GetMapping("/getAll")
    public List<TourDTO> getAll() {
        return tourEntityService.getAllEntities()
                .stream()
                .map(TourDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TourDTO> getById(@PathVariable int id) {
        TourDTO tour = TourDTO.fromEntity(tourEntityService.findEntityById(id));
        return ResponseEntity.ok(tour);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        TourDTO tour = TourDTO.fromEntity(tourEntityService.findEntityById(id));
        tourEntityService.deleteEntity(tour.toEntity());
        return ResponseEntity.ok("Tour deleted successfully");
    }

    @PutMapping("/update")
    public ResponseEntity<TourDTO> update(@RequestBody TourDTO tour1) {
        TourDTO tour = TourDTO.fromEntity(tourEntityService.updateEntity(tour1.toEntity()));
        return ResponseEntity.ok(tour);
    }
    @GetMapping("/filter")
    public ResponseEntity<List<TourDTO>> filterTours(
            @RequestParam(required = false) String tourName,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String mealType,
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) Double maxRating,
            @RequestParam(required = false) String hotelFeatures,
            @RequestParam(required = false) String roomView,
            @RequestParam(required = false) Integer minBeds,
            @RequestParam(required = false) Integer maxBeds,
            @RequestParam(required = false) Double minRoomSize,
            @RequestParam(required = false) Double maxRoomSize,
            @RequestParam(required = false) String roomType,
            @RequestParam(required = false) Boolean hasBath,
            @RequestParam(required = false) Boolean hasTerrace,
            @RequestParam(required = false) BigDecimal minRoomCost,
            @RequestParam(required = false) BigDecimal maxRoomCost,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDirection) {

        List<Tour> filteredTours = tourEntityService.filterTours(
                tourName, description,
                mealType, minRating, maxRating, hotelFeatures,
                roomView, minBeds, maxBeds, minRoomSize, maxRoomSize,
                roomType, hasBath, hasTerrace, minRoomCost, maxRoomCost,
                sortBy, sortDirection);

        List<TourDTO> result = filteredTours.stream()
                .map(TourDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }
    @GetMapping("/search")
    public ResponseEntity<List<TourDTO>> searchTours(
            @RequestParam(required = false) String tourName,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String mealType,
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) Double maxRating,
            @RequestParam(required = false) String hotelFeatures,
            @RequestParam(required = false) String roomView,
            @RequestParam(required = false) Integer minBeds,
            @RequestParam(required = false) Integer maxBeds,
            @RequestParam(required = false) Double minRoomSize,
            @RequestParam(required = false) Double maxRoomSize,
            @RequestParam(required = false) String roomType,
            @RequestParam(required = false) Boolean hasBath,
            @RequestParam(required = false) Boolean hasTerrace,
            @RequestParam(required = false) BigDecimal minRoomCost,
            @RequestParam(required = false) BigDecimal maxRoomCost,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDirection) {

        List<Tour> filteredTours = tourEntityService.filterTours(
                tourName, description,
                mealType, minRating, maxRating, hotelFeatures,
                roomView, minBeds, maxBeds, minRoomSize, maxRoomSize,
                roomType, hasBath, hasTerrace, minRoomCost, maxRoomCost,
                sortBy, sortDirection);

        List<TourDTO> result = filteredTours.stream()
                .map(TourDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }
    public static class RouteWithTicketsDTO {
        private String departureAirportCode;
        private String arrivalAirportCode;
        private String departureDate;
        private String arrivalDate;
        private Set<Integer> ticketIds;

        public Instant getParsedDepartureDate() {
            return parseDateTimeSafe(departureDate);
        }

        public Instant getParsedArrivalDate() {
            return parseDateTimeSafe(arrivalDate);
        }

        private Instant parseDateTimeSafe(String dateTimeStr) {
            if (dateTimeStr == null || dateTimeStr.isEmpty()) {
                return null;
            }
            try {
               return Instant.parse(dateTimeStr);
            } catch (DateTimeParseException e1) {
                try {
                    LocalDateTime localDateTime = LocalDateTime.parse(dateTimeStr);
                    return localDateTime.atZone(ZoneId.systemDefault()).toInstant();
                } catch (DateTimeParseException e2) {
                    throw new IllegalArgumentException("Invalid date format: " + dateTimeStr);
                }
            }
        }

        public String getDepartureAirportCode() {
            return departureAirportCode;
        }

        public void setDepartureAirportCode(String departureAirportCode) {
            this.departureAirportCode = departureAirportCode;
        }

        public String getArrivalAirportCode() {
            return arrivalAirportCode;
        }

        public void setArrivalAirportCode(String arrivalAirportCode) {
            this.arrivalAirportCode = arrivalAirportCode;
        }

        public String getDepartureDate() {
            return departureDate;
        }

        public void setDepartureDate(String departureDate) {
            this.departureDate = departureDate;
        }

        public String getArrivalDate() {
            return arrivalDate;
        }

        public void setArrivalDate(String arrivalDate) {
            this.arrivalDate = arrivalDate;
        }

        public Set<Integer> getTicketIds() {
            return ticketIds;
        }

        public void setTicketIds(Set<Integer> ticketIds) {
            this.ticketIds = ticketIds;
        }
    }


}

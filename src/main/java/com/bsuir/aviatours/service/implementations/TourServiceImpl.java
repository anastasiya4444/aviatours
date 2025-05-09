package com.bsuir.aviatours.service.implementations;

import com.bsuir.aviatours.dto.*;
import com.bsuir.aviatours.model.*;
import com.bsuir.aviatours.repository.*;
import com.bsuir.aviatours.service.interfaces.EntityService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TourServiceImpl implements EntityService<Tour> {

    private final TourRepository entityRepository;
    private final ProgramRepository programRepository;
    private final DayRepository dayRepository;
    private final ActivityRepository activityRepository;
    private final RouteRepository routeRepository;
    private final HotelRepository hotelRepository;
    private final AdditionalServiceRepository additionalServiceRepository;
    private final TourHotelRepository tourHotelRepository;
    private final TourAdditionalServiceRepository tourAdditionalServiceRepository;

    public TourServiceImpl(TourRepository entityRepository, ProgramRepository programRepository, DayRepository dayRepository, ActivityRepository activityRepository, RouteRepository routeRepository, HotelRepository hotelRepository, AdditionalServiceRepository additionalServiceRepository, TourHotelRepository tourHotelRepository, TourAdditionalServiceRepository tourAdditionalServiceRepository) {
        this.entityRepository = entityRepository;
        this.programRepository = programRepository;
        this.dayRepository = dayRepository;
        this.activityRepository = activityRepository;
        this.routeRepository = routeRepository;
        this.hotelRepository = hotelRepository;
        this.additionalServiceRepository = additionalServiceRepository;
        this.tourHotelRepository = tourHotelRepository;
        this.tourAdditionalServiceRepository = tourAdditionalServiceRepository;
    }

    @Override
    public Tour saveEntity(Tour obj) {
        return entityRepository.save(obj);
    }

    @Override
    public List<Tour> getAllEntities() {
        return entityRepository.findAll();
    }

    @Override
    public Tour updateEntity(Tour obj) {
        if (!entityRepository.existsById(obj.getId())) {
            throw new EntityNotFoundException("Role with ID " + obj.getId() + " not found");
        }
        return entityRepository.save(obj);
    }

    @Override
    public void deleteEntity(Tour obj) {
        entityRepository.delete(obj);
    }

    @Override
    public Tour findEntityById(int id) {
        return entityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Role with ID " + id + " not found"));
    }
    public TourDTO createTour(TourDTO tourDTO) {
        // 1. Сохраняем тур
        Tour tour = new Tour();
        tour.setTourName(tourDTO.getTourName());
        tour.setDescription(tourDTO.getDescription());
        tour.setCreatedAt(Instant.from(LocalDateTime.now()));
        Tour savedTour = entityRepository.save(tour);

        // 2. Создаем программу (если нужно)
        if (tourDTO.getProgram().getId() == null) {
            Program program = new Program();
            program.setCreatedAt(Instant.from(LocalDateTime.now()));
            Program savedProgram = programRepository.save(program);
            savedTour.getProgram().setId(savedProgram.getId());
            entityRepository.save(savedTour);
        }

        if (tourDTO.getProgram().getDays() != null) {
            for (DayDTO dayDTO : tourDTO.getProgram().getDays()) {
                Day day = new Day();
                day.getProgram().setId(tour.getProgram().getId());
                day.setDayNumber(dayDTO.getDayNumber());
                day.setCreatedAt(Instant.from(LocalDateTime.now()));
                Day savedDay = dayRepository.save(day);

                // 4. Сохраняем мероприятия для дня
                if (dayDTO.getActivities() != null) {
                    for (ActivityDTO activityDTO : dayDTO.getActivities()) {
                        Activity activity = new Activity();
                        activity.setActivityType(activityDTO.getActivityType());
                        activity.setStartDateTime(activityDTO.getStartDateTime());
                        activity.setEndDateTime(activityDTO.getEndDateTime());
                        activity.setDescription(activityDTO.getDescription());
                        activity.setCost(activityDTO.getCost());
                        activity.setImageUrls(activityDTO.getImageUrls());
                        activity.setInitialCapacity(activityDTO.getInitialCapacity());
                        activity.setBooked(0);
                        activity.setCreatedAt(Instant.from(LocalDateTime.now()));
                        activityRepository.save(activity);
                    }
                }
            }
        }

        // 5. Сохраняем маршруты
        if (tourDTO.getRoutes() != null) {
            for (RouteDTO routeDTO : tourDTO.getRoutes()) {
                Route route = new Route();
                route.getTour().setId(savedTour.getId());
                route.setDepartureAirportCode(routeDTO.getDepartureAirportCode());
                route.setArrivalAirportCode(routeDTO.getArrivalAirportCode());
                route.setDepartureDate(routeDTO.getDepartureDate());
                route.setArrivalDate(routeDTO.getArrivalDate());
                routeRepository.save(route);
            }
        }

        // 6. Добавляем отели через tour_hotel
        if (tourDTO.getHotels() != null) {
            tourDTO.getHotels().stream()
                    .map(hotelId -> {
                        TourHotel tourHotel = new TourHotel();
                        tourHotel.setTour(savedTour);
                        tourHotel.getHotel().setId(hotelId.getId());
                        return tourHotel;
                    })
                    .forEach(tourHotelRepository::save); // Save each TourHotel
        }

// 7. Добавляем доп. сервисы через tour_additional_service
        if (tourDTO.getAdditionalServices() != null) {
            tourDTO.getAdditionalServices().stream()
                    .map(serviceId -> {
                        TourAdditionalService tourService = new TourAdditionalService();
                        tourService.setTour(savedTour);
                        tourService.setService(serviceId.toEntity());
                        return tourService;
                    })
                    .forEach(tourAdditionalServiceRepository::save); // Save each TourAdditionalService
        }

        return TourDTO.fromEntity(savedTour);
    }

    public List<Tour> filterTours(
            String tourName,
            String description,
            String mealType,
            Double minRating,
            Double maxRating,
            String hotelFeatures,
            String roomView,
            Integer minBeds,
            Integer maxBeds,
            Double minRoomSize,
            Double maxRoomSize,
            String roomType,
            Boolean hasBath,
            Boolean hasTerrace,
            BigDecimal minRoomCost,
            BigDecimal maxRoomCost,
            String sortBy,
            String sortDirection) {

        Specification<Tour> spec = Specification.where(null);

        spec = spec.and(TourSpecifications.withTourName(tourName));
        spec = spec.and(TourSpecifications.withDescription(description));
        spec = spec.and(TourSpecifications.withMealType(mealType));
        spec = spec.and(TourSpecifications.withRatingBetween(minRating, maxRating));
        spec = spec.and(TourSpecifications.withHotelFeatures(hotelFeatures));
        spec = spec.and(TourSpecifications.withRoomView(roomView));
        spec = spec.and(TourSpecifications.withBedsBetween(minBeds, maxBeds));
        spec = spec.and(TourSpecifications.withRoomSizeBetween(minRoomSize, maxRoomSize));
        spec = spec.and(TourSpecifications.withRoomType(roomType));
        spec = spec.and(TourSpecifications.hasBath(hasBath));
        spec = spec.and(TourSpecifications.hasTerrace(hasTerrace));
        spec = spec.and(TourSpecifications.withRoomCostBetween(minRoomCost, maxRoomCost));

        Sort sort;
        if (sortBy != null && !sortBy.isEmpty()) {
            if ("rating".equalsIgnoreCase(sortBy)) {
                return sortByHotelRating(spec, sortDirection);
            } else if ("price".equalsIgnoreCase(sortBy)) {
                return sortByRoomPrice(spec, sortDirection);
            } else {
                sort = Sort.by("asc".equalsIgnoreCase(sortDirection) ?
                        Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
            }
        } else {
            sort = Sort.unsorted();
        }

        return entityRepository.findAll(spec, sort);
    }

    private List<Tour> sortByHotelRating(Specification<Tour> spec, String sortDirection) {
        return entityRepository.findAll(spec, Sort.by(
                "asc".equalsIgnoreCase(sortDirection) ?
                        Sort.Direction.ASC : Sort.Direction.DESC,
                "hotels.hotel.rating"
        ));
    }

    private List<Tour> sortByRoomPrice(Specification<Tour> spec, String sortDirection) {
        List<Tour> tours = entityRepository.findAll(spec);

         tours.sort((t1, t2) -> {
            BigDecimal price1 = t1.getHotels().stream()
                    .flatMap(th -> th.getRooms().stream())
                    .map(Room::getCost)
                    .min(BigDecimal::compareTo)
                    .orElse(BigDecimal.ZERO);

            BigDecimal price2 = t2.getHotels().stream()
                    .flatMap(th -> th.getRooms().stream())
                    .map(Room::getCost)
                    .min(BigDecimal::compareTo)
                    .orElse(BigDecimal.ZERO);

            return "asc".equalsIgnoreCase(sortDirection) ?
                    price1.compareTo(price2) : price2.compareTo(price1);
        });

        return tours;
    }
}

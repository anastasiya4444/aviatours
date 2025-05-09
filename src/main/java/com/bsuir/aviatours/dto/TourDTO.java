package com.bsuir.aviatours.dto;

import com.bsuir.aviatours.model.*;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class TourDTO {
    private Integer id;
    private String tourName;
    private String description;
    private String createdAt;  // Изменено на String для фронтенда
    private ProgramDTO program;
    private Set<RouteDTO> routes = new LinkedHashSet<>();
    private Set<AdditionalServiceDTO> additionalServices = new LinkedHashSet<>();
    private Set<HotelDTO> hotels = new LinkedHashSet<>();

    public TourDTO() {}

    public TourDTO(Integer id, String tourName, String description, ProgramDTO program) {
        this.id = id;
        this.tourName = tourName;
        this.description = description;
        this.program = program;
    }

    public Tour toEntity() {
        Tour tour = new Tour();
        tour.setId(this.id);
        tour.setTourName(this.tourName);
        tour.setDescription(this.description);

        // Преобразование строки в Instant, если createdAt не null
        if (this.createdAt != null && !this.createdAt.isEmpty()) {
            try {
                tour.setCreatedAt(Instant.parse(this.createdAt));
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Invalid date format for createdAt", e);
            }
        }

        if (this.program != null) {
            tour.setProgram(this.program.toEntity());
        }
        return tour;
    }

    public static TourDTO fromEntity(Tour tour) {
        if (tour == null) {
            return null;
        }

        TourDTO tourDTO = new TourDTO();
        tourDTO.setId(tour.getId());
        tourDTO.setTourName(tour.getTourName());
        tourDTO.setDescription(tour.getDescription());

        // Преобразование Instant в строку ISO-формата
        if (tour.getCreatedAt() != null) {
            tourDTO.setCreatedAt(tour.getCreatedAt().toString());
        }

        if (tour.getProgram() != null) {
            tourDTO.setProgram(ProgramDTO.fromEntity(tour.getProgram()));
        }
        if (tour.getHotels() != null) {
            Set<HotelDTO> hotelDTOs = tour.getHotels().stream()
                    .map(tourHotel -> {
                        return tourHotel != null ? HotelDTO.fromEntity(tourHotel) : null;
                    })
                    .filter(Objects::nonNull) // Фильтруем null значения
                    .collect(Collectors.toCollection(LinkedHashSet::new));

            tourDTO.setHotels(hotelDTOs);
        }

        // Конвертация дополнительных сервисов (если нужно)
        if (tour.getAdditionalServices() != null) {
            Set<AdditionalServiceDTO> serviceDTOs = tour.getAdditionalServices().stream()
                    .map(tourService -> {
                        return tourService != null ? AdditionalServiceDTO.fromEntity(tourService) : null;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toCollection(LinkedHashSet::new));

            tourDTO.setAdditionalServices(serviceDTOs);
        }

        // Конвертация маршрутов (если нужно)
        if (tour.getRoutes() != null) {
            Set<RouteDTO> routeDTOs = tour.getRoutes().stream()
                    .map(RouteDTO::fromEntity)
                    .collect(Collectors.toCollection(LinkedHashSet::new));

            tourDTO.setRoutes(routeDTOs);
        }


        return tourDTO;
    }

    public Set<RouteDTO> getRoutes() {
        return routes;
    }

    public void setRoutes(Set<RouteDTO> routes) {
        this.routes = routes;
    }

    public Set<AdditionalServiceDTO> getAdditionalServices() {
        return additionalServices;
    }

    public void setAdditionalServices(Set<AdditionalServiceDTO> additionalServices) {
        this.additionalServices = additionalServices;
    }

    public Set<HotelDTO> getHotels() {
        return hotels;
    }

    public void setHotels(Set<HotelDTO> hotels) {
        this.hotels = hotels;
    }

    // Геттеры и сеттеры
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTourName() {
        return tourName;
    }

    public void setTourName(String tourName) {
        this.tourName = tourName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public ProgramDTO getProgram() {
        return program;
    }

    public void setProgram(ProgramDTO program) {
        this.program = program;
    }
}
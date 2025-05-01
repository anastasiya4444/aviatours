package com.bsuir.aviatours.dto;

import com.bsuir.aviatours.model.Program;
import com.bsuir.aviatours.model.Tour;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

public class TourDTO {
    private Integer id;
    private String tourName;
    private String description;
    private Instant createdAt;
    private ProgramDTO program;

    public TourDTO() {}

    public TourDTO(Integer id, String tourName, String description, Instant createdAt, ProgramDTO program) {
        this.id = id;
        this.tourName = tourName;
        this.description = description;
        this.createdAt = createdAt;
        this.program = program;
    }

    public Tour toEntity(){
        Tour tour = new Tour();
        tour.setId(this.id);
        tour.setTourName(this.tourName);
        tour.setDescription(this.description);
        tour.setCreatedAt(this.createdAt);
        if(program.getId() != null){
            tour.setProgram(this.program.toEntity());
        }
        return tour;
    }

    public static TourDTO fromEntity(Tour tour){
        TourDTO tourDTO = new TourDTO();
        if(tour.getId() != null){
            tourDTO.setId(tour.getId());
            tourDTO.setTourName(tour.getTourName());
            tourDTO.setDescription(tour.getDescription());
            tourDTO.setCreatedAt(tour.getCreatedAt());
            if(tour.getProgram() != null){
                tourDTO.setProgram(ProgramDTO.fromEntity(tour.getProgram()));
            }
        }
        return tourDTO;
    }

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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public ProgramDTO getProgram() {
        return program;
    }

    public void setProgram(ProgramDTO program) {
        this.program = program;
    }
}
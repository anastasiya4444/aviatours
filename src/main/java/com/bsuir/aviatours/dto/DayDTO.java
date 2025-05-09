package com.bsuir.aviatours.dto;

import com.bsuir.aviatours.model.Activity;
import com.bsuir.aviatours.model.Day;
import com.bsuir.aviatours.model.Program;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

public class DayDTO {
    private Integer id;
    private ProgramDTO program;
    private Integer dayNumber;
    private Instant createdAt;
    private Set<ActivityDTO> activities = new LinkedHashSet<>(); // Assuming ActivityDTO exists

    public DayDTO() {}

    public DayDTO(Integer id, ProgramDTO program, Integer dayNumber, Instant createdAt) {
        this.id = id;
        this.program = program;
        this.dayNumber = dayNumber;
        this.createdAt = createdAt;
    }

    public Day toEntity() {
        Day day = new Day();
        day.setId(this.id);
        if (this.program != null && this.program.getId() != null) {
            day.setProgram(this.program.toEntity());
        }
        day.setDayNumber(this.dayNumber);
        day.setCreatedAt(this.createdAt);
        return day;
    }

    public static DayDTO fromEntity(Day day) {
        DayDTO dto = new DayDTO();
        dto.setId(day.getId());
        if (day.getProgram() != null) {
            dto.setProgram(ProgramDTO.fromEntity(day.getProgram()));
        }
        dto.setDayNumber(day.getDayNumber());
        dto.setCreatedAt(day.getCreatedAt());
        return dto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ProgramDTO getProgram() {
        return program;
    }

    public void setProgram(ProgramDTO program) {
        this.program = program;
    }

    public Integer getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(Integer dayNumber) {
        this.dayNumber = dayNumber;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Set<ActivityDTO> getActivities() {
        return activities;
    }

    public void setActivities(Set<ActivityDTO> activities) {
        this.activities = activities;
    }
}
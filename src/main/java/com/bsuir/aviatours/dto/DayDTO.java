package com.bsuir.aviatours.dto;

import com.bsuir.aviatours.model.Day;
import com.bsuir.aviatours.model.Program;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

public class DayDTO {
    private Integer id;
    private ProgramDTO program;
    private Integer dayNumber;
    private Instant createdAt;

    public DayDTO() {}

    public DayDTO(Integer id, ProgramDTO program, Integer dayNumber, Instant createdAt) {
        this.id = id;
        this.program = program;
        this.dayNumber = dayNumber;
        this.createdAt = createdAt;
    }

    public Day toEntity(){
        Day day = new Day();
        day.setId(this.id);
        if(this.program.getId() != null){
            day.setProgram(this.program.toEntity());
        }
        day.setDayNumber(this.dayNumber);
        day.setCreatedAt(this.createdAt);
        return day;
    }

    public static DayDTO fromEntity(Day day) {
        DayDTO dto = new DayDTO();
        if(dto.getId() != null){
            dto.setId(day.getId());
            if(day.getProgram() != null){
                dto.setProgram(ProgramDTO.fromEntity(day.getProgram()));
            }
            dto.setDayNumber(day.getDayNumber());
            dto.setCreatedAt(day.getCreatedAt());
        }
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

}
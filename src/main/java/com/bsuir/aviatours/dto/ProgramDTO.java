package com.bsuir.aviatours.dto;

import com.bsuir.aviatours.model.Program;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

public class ProgramDTO {
    private Integer id;
    private String description;
    private Instant createdAt;

    public ProgramDTO() {}

    public ProgramDTO(Integer id, String description, Instant createdAt) {
        this.id = id;
        this.description = description;
        this.createdAt = createdAt;
    }

    public Program toEntity(){
        Program program = new Program();
        program.setId(id);
        program.setDescription(description);
        program.setCreatedAt(createdAt);
        return program;
    }

    public static ProgramDTO fromEntity(Program program){
        ProgramDTO programDTO = new ProgramDTO();
        if(program.getId() != null){
            programDTO.setId(program.getId());
            programDTO.setDescription(program.getDescription());
            programDTO.setCreatedAt(program.getCreatedAt());
        }
        return programDTO;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

}
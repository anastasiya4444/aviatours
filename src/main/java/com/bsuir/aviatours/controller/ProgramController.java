package com.bsuir.aviatours.controller;

import com.bsuir.aviatours.dto.ProgramDTO;
import com.bsuir.aviatours.model.Program;
import com.bsuir.aviatours.service.interfaces.EntityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/program")
@CrossOrigin
public class ProgramController {

    private final EntityService<Program> programEntityService;

    public ProgramController(EntityService<Program> programEntityService) {
        this.programEntityService = programEntityService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody ProgramDTO program) {
        programEntityService.saveEntity(program.toEntity());
        return ResponseEntity.ok("Program saved successfully");
    }

    @GetMapping("/getAll")
    public List<ProgramDTO> getAll() {
        return programEntityService.getAllEntities()
                .stream()
                .map(ProgramDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProgramDTO> getById(@PathVariable int id) {
        ProgramDTO program = ProgramDTO.fromEntity(programEntityService.findEntityById(id));
        return ResponseEntity.ok(program);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        ProgramDTO program = ProgramDTO.fromEntity(programEntityService.findEntityById(id));
        programEntityService.deleteEntity(program.toEntity());
        return ResponseEntity.ok("Program deleted successfully");
    }

    @PutMapping("/update")
    public ResponseEntity<ProgramDTO> update(@RequestBody ProgramDTO program) {
        ProgramDTO program1 = ProgramDTO.fromEntity(programEntityService.updateEntity(program.toEntity()));
        return ResponseEntity.ok(program1);
    }
}

package com.bsuir.aviatours.controller;

import com.bsuir.aviatours.model.Program;
import com.bsuir.aviatours.service.interfaces.EntityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/program")
@CrossOrigin
public class ProgramController {

    private final EntityService<Program> programEntityService;

    public ProgramController(EntityService<Program> programEntityService) {
        this.programEntityService = programEntityService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody Program program) {
        programEntityService.saveEntity(program);
        return ResponseEntity.ok("Program saved successfully");
    }

    @GetMapping("/getAll")
    public List<Program> getAll() {
        return programEntityService.getAllEntities();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Program> getById(@PathVariable int id) {
        Program program = programEntityService.findEntityById(id);
        return ResponseEntity.ok(program);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        Program program = programEntityService.findEntityById(id);
        programEntityService.deleteEntity(program);
        return ResponseEntity.ok("Program deleted successfully");
    }

    @PutMapping("/update")
    public ResponseEntity<Program> update(@RequestBody Program program) {
        Program program1 = programEntityService.updateEntity(program);
        return ResponseEntity.ok(program1);
    }
}

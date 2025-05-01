package com.bsuir.aviatours.controller;

import com.bsuir.aviatours.dto.AirTicketDTO;
import com.bsuir.aviatours.model.AirTicket;
import com.bsuir.aviatours.service.interfaces.EntityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/air_ticket")
@CrossOrigin
public class AirTicketController {

    private final EntityService<AirTicket> airTicketEntityService;

    public AirTicketController(EntityService<AirTicket> airTicketEntityService) {
        this.airTicketEntityService = airTicketEntityService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody AirTicket airTicket) {
        airTicketEntityService.saveEntity(airTicket);
        return ResponseEntity.ok("AirTicket saved successfully");
    }

    @GetMapping("/getAll")
    public List<AirTicketDTO> getAll() {
        return airTicketEntityService.getAllEntities()
                .stream()
                .map(AirTicketDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AirTicketDTO> getById(@PathVariable int id) {
        AirTicketDTO user = AirTicketDTO.fromEntity(airTicketEntityService.findEntityById(id));
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        AirTicketDTO airTicket = AirTicketDTO.fromEntity(airTicketEntityService.findEntityById(id));
        airTicketEntityService.deleteEntity(airTicket.toEntity());
        return ResponseEntity.ok("AirTicket deleted successfully");
    }

    @PutMapping("/update")
    public ResponseEntity<AirTicketDTO> update(@RequestBody AirTicketDTO airTicket1) {
        AirTicketDTO airTicket = AirTicketDTO.fromEntity(airTicketEntityService.updateEntity(airTicket1.toEntity()));
        return ResponseEntity.ok(airTicket);
    }
}

package com.bsuir.aviatours.controller;

import com.bsuir.aviatours.model.AirTicket;
import com.bsuir.aviatours.service.interfaces.EntityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<AirTicket> getAll() {
        return airTicketEntityService.getAllEntities();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AirTicket> getById(@PathVariable int id) {
        AirTicket user = airTicketEntityService.findEntityById(id);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        AirTicket airTicket = airTicketEntityService.findEntityById(id);
        airTicketEntityService.deleteEntity(airTicket);
        return ResponseEntity.ok("AirTicket deleted successfully");
    }

    @PutMapping("/update")
    public ResponseEntity<AirTicket> update(@RequestBody AirTicket airTicket1) {
        AirTicket airTicket = airTicketEntityService.updateEntity(airTicket1);
        return ResponseEntity.ok(airTicket);
    }
}

package com.bsuir.aviatours.controller;

import com.bsuir.aviatours.dto.AirTicketDTO;
import com.bsuir.aviatours.dto.RouteDTO;
import com.bsuir.aviatours.model.Route;
import com.bsuir.aviatours.service.implementations.RouteServiceImpl;
import com.bsuir.aviatours.service.interfaces.EntityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/route")
@CrossOrigin
public class RouteController {

    private final RouteServiceImpl routeEntityService;

    public RouteController(RouteServiceImpl routeEntityService) {
        this.routeEntityService = routeEntityService;
    }


    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody RouteDTO route) {
        routeEntityService.saveEntity(route.toEntity());
        return ResponseEntity.ok("Route saved successfully");
    }
    @PostMapping("/add_get")
    public ResponseEntity<RouteDTO> add_get(@RequestBody RouteDTO route) {
        Route savedRoute = routeEntityService.saveEntity(route.toEntity());
        return ResponseEntity.ok(RouteDTO.fromEntity(savedRoute)); // Return the ID of the created route
    }
    @GetMapping("/getAll")
    public List<RouteDTO> getAll() {
        return routeEntityService.getAllEntities()
                .stream()
                .map(RouteDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RouteDTO> getById(@PathVariable int id) {
        RouteDTO route = RouteDTO.fromEntity(routeEntityService.findEntityById(id));
        return ResponseEntity.ok(route);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        RouteDTO route = RouteDTO.fromEntity(routeEntityService.findEntityById(id));
        routeEntityService.deleteEntity(route.toEntity());
        return ResponseEntity.ok("Route deleted successfully");
    }

    @PutMapping("/update")
    public ResponseEntity<RouteDTO> update(@RequestBody RouteDTO route1) {
        RouteDTO route = RouteDTO.fromEntity(routeEntityService.updateEntity(route1.toEntity()));
        return ResponseEntity.ok(route);
    }
    @GetMapping("/search")
    public ResponseEntity<?> searchAvailableRoutes(
            @RequestParam String departureAirportCode,
            @RequestParam String arrivalAirportCode,
            @RequestParam LocalDateTime departureDateTime,
            @RequestParam(defaultValue = "3") int maxStops) {

        if (departureAirportCode == null || departureAirportCode.isEmpty() ||
                arrivalAirportCode == null || arrivalAirportCode.isEmpty() ||
                departureDateTime == null) {
            return ResponseEntity.badRequest().body("Invalid input data");
        }

        List<List<AirTicketDTO>> availableRoutes = routeEntityService.findAvailableRoutes(
                        departureAirportCode,
                        arrivalAirportCode,
                        departureDateTime,
                        maxStops
                ).stream()
                .map(route -> route.stream().map(AirTicketDTO::fromEntity).collect(Collectors.toList()))
                .collect(Collectors.toList());

        if (availableRoutes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No routes found for the specified criteria.");
        }

        return ResponseEntity.ok(availableRoutes);
    }

}

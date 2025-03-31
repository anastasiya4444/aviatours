package com.bsuir.aviatours.controller;

import com.bsuir.aviatours.model.Hotel;
import com.bsuir.aviatours.service.interfaces.EntityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotel")
@CrossOrigin
public class HotelController {

    private final EntityService<Hotel> hotelEntityService;

    public HotelController(EntityService<Hotel> hotelEntityService) {
        this.hotelEntityService = hotelEntityService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody Hotel hotel) {
        hotelEntityService.saveEntity(hotel);
        return ResponseEntity.ok("Hotel saved successfully");
    }

    @GetMapping("/getAll")
    public List<Hotel> getAll() {
        return hotelEntityService.getAllEntities();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Hotel> getById(@PathVariable int id) {
        Hotel hotel = hotelEntityService.findEntityById(id);
        return ResponseEntity.ok(hotel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        Hotel hotel = hotelEntityService.findEntityById(id);
        hotelEntityService.deleteEntity(hotel);
        return ResponseEntity.ok("Hotel deleted successfully");
    }

    @PutMapping("/update")
    public ResponseEntity<Hotel> update(@RequestBody Hotel hotel) {
        Hotel updatedUser = hotelEntityService.updateEntity(hotel);
        return ResponseEntity.ok(updatedUser);
    }
}

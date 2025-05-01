package com.bsuir.aviatours.controller;

import com.bsuir.aviatours.dto.HotelDTO;
import com.bsuir.aviatours.model.Hotel;
import com.bsuir.aviatours.service.interfaces.EntityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/hotel")
@CrossOrigin
public class HotelController {

    private final EntityService<Hotel> hotelEntityService;

    public HotelController(EntityService<Hotel> hotelEntityService) {
        this.hotelEntityService = hotelEntityService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody HotelDTO hotel) {
        hotelEntityService.saveEntity(hotel.toEntity());
        return ResponseEntity.ok("Hotel saved successfully");
    }

    @GetMapping("/getAll")
    public List<HotelDTO> getAll() {
        return hotelEntityService.getAllEntities()
                .stream()
                .map(HotelDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelDTO> getById(@PathVariable int id) {
        HotelDTO hotel = HotelDTO.fromEntity(hotelEntityService.findEntityById(id));
        return ResponseEntity.ok(hotel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        HotelDTO hotel = HotelDTO.fromEntity(hotelEntityService.findEntityById(id));
        hotelEntityService.deleteEntity(hotel.toEntity());
        return ResponseEntity.ok("Hotel deleted successfully");
    }

    @PutMapping("/update")
    public ResponseEntity<HotelDTO> update(@RequestBody HotelDTO hotel) {
        HotelDTO updatedUser = HotelDTO.fromEntity(hotelEntityService.updateEntity(hotel.toEntity()));
        return ResponseEntity.ok(updatedUser);
    }
}

package com.bsuir.aviatours.controller;

import com.bsuir.aviatours.dto.RoomDTO;
import com.bsuir.aviatours.model.Hotel;
import com.bsuir.aviatours.model.Room;
import com.bsuir.aviatours.service.implementations.HotelServiceImpl;
import com.bsuir.aviatours.service.implementations.RoomServiceImpl;
import com.bsuir.aviatours.service.interfaces.EntityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/room")
@CrossOrigin
public class RoomController {

    private final RoomServiceImpl roomEntityService;
    private final HotelServiceImpl hotelEntityService;

    public RoomController(RoomServiceImpl roomEntityService, HotelServiceImpl hotelEntityService) {
        this.roomEntityService = roomEntityService;
        this.hotelEntityService = hotelEntityService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody RoomDTO room) {
        Room newRoom = room.toEntity();
        Hotel hotel = hotelEntityService.findEntityById(room.getHotel().getId());
        newRoom.setHotel(hotel);
        roomEntityService.saveEntity(newRoom);
        return ResponseEntity.ok("Room saved successfully");
    }
    @GetMapping("/getByHotel/{hotelId}")
    public List<RoomDTO> getByHotel(@PathVariable Integer hotelId) {
        return roomEntityService.findByHotelId(hotelId).stream()
                .map(RoomDTO::fromEntity)
                .collect(Collectors.toList());
    }
    @GetMapping("/getAll")
    public List<RoomDTO> getAll() {
        return roomEntityService.getAllEntities()
                .stream()
                .map(RoomDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomDTO> getById(@PathVariable int id) {
        RoomDTO room = RoomDTO.fromEntity(roomEntityService.findEntityById(id));
        return ResponseEntity.ok(room);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        RoomDTO room = RoomDTO.fromEntity(roomEntityService.findEntityById(id));
        roomEntityService.deleteEntity(room.toEntity());
        return ResponseEntity.ok("Room deleted successfully");
    }

    @PutMapping("/update")
    public ResponseEntity<RoomDTO> update(@RequestBody RoomDTO room) {
        Room editRoom = room.toEntity();
        Hotel hotel = hotelEntityService.findEntityById(room.getHotel().getId());
        editRoom.setHotel(hotel);
        RoomDTO room1 = RoomDTO.fromEntity(roomEntityService.updateEntity(editRoom));
        return ResponseEntity.ok(room1);
    }
}

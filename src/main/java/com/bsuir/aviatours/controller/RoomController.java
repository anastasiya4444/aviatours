package com.bsuir.aviatours.controller;

import com.bsuir.aviatours.dto.RoomDTO;
import com.bsuir.aviatours.model.Room;
import com.bsuir.aviatours.service.interfaces.EntityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/room")
@CrossOrigin
public class RoomController {

    private final EntityService<Room> roomEntityService;

    public RoomController(EntityService<Room> roomEntityService) {
        this.roomEntityService = roomEntityService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody RoomDTO room) {
        roomEntityService.saveEntity(room.toEntity());
        return ResponseEntity.ok("Room saved successfully");
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
        RoomDTO room1 = RoomDTO.fromEntity(roomEntityService.updateEntity(room.toEntity()));
        return ResponseEntity.ok(room1);
    }
}

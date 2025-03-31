package com.bsuir.aviatours.controller;

import com.bsuir.aviatours.model.Room;
import com.bsuir.aviatours.service.interfaces.EntityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/room")
@CrossOrigin
public class RoomController {

    private final EntityService<Room> roomEntityService;

    public RoomController(EntityService<Room> roomEntityService) {
        this.roomEntityService = roomEntityService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody Room room) {
        roomEntityService.saveEntity(room);
        return ResponseEntity.ok("Room saved successfully");
    }

    @GetMapping("/getAll")
    public List<Room> getAll() {
        return roomEntityService.getAllEntities();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> getById(@PathVariable int id) {
        Room room = roomEntityService.findEntityById(id);
        return ResponseEntity.ok(room);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        Room room = roomEntityService.findEntityById(id);
        roomEntityService.deleteEntity(room);
        return ResponseEntity.ok("Room deleted successfully");
    }

    @PutMapping("/update")
    public ResponseEntity<Room> update(@RequestBody Room room) {
        Room room1 = roomEntityService.updateEntity(room);
        return ResponseEntity.ok(room1);
    }
}

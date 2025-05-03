package com.bsuir.aviatours.controller;

import com.bsuir.aviatours.dto.RoomDTO;
import com.bsuir.aviatours.model.Hotel;
import com.bsuir.aviatours.model.Room;
import com.bsuir.aviatours.service.implementations.HotelServiceImpl;
import com.bsuir.aviatours.service.implementations.RoomServiceImpl;
import com.bsuir.aviatours.service.interfaces.EntityService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
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
    @Value("${upload.path}")
    private String uploadPath;

    @PostMapping("/{roomId}/uploadImage")
    public ResponseEntity<String> uploadImage(
            @PathVariable int roomId,
            @RequestParam("file") MultipartFile file) {

        try {
            Room room = roomEntityService.findEntityById(roomId);
            if (room == null) {
                return ResponseEntity.notFound().build();
            }

            // Удаляем старое изображение если есть
            if (room.getImageUrls() != null) {
                Files.deleteIfExists(Paths.get(uploadPath, room.getImageUrls()));
            }

            // Сохраняем новое изображение
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadPath, filename);
            Files.write(filePath, file.getBytes());

            room.setImageUrls(filename);
            roomEntityService.updateEntity(room);

            return ResponseEntity.ok(filename);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to upload image");
        }
    }

    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(uploadPath).resolve(filename);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(filePath))
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
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

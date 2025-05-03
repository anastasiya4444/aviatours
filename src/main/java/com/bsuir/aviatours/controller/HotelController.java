package com.bsuir.aviatours.controller;

import com.bsuir.aviatours.dto.HotelDTO;
import com.bsuir.aviatours.model.Hotel;
import com.bsuir.aviatours.service.business.ImageService;
import com.bsuir.aviatours.service.interfaces.EntityService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/hotel")
@CrossOrigin
public class HotelController {

    private final EntityService<Hotel> hotelEntityService;
    private final ImageService imageService;

    public HotelController(EntityService<Hotel> hotelEntityService, ImageService imageService) {
        this.hotelEntityService = hotelEntityService;
        this.imageService = imageService;
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
    @PostMapping("/{hotelId}/uploadImage")
    public ResponseEntity<String> uploadImage(
            @PathVariable int hotelId,
            @RequestParam("file") MultipartFile file) {

        try {
            Hotel hotel = hotelEntityService.findEntityById(hotelId);
            if (hotel == null) {
                return ResponseEntity.notFound().build();
            }

            if (hotel.getImageUrls() != null) {
                imageService.deleteImage(hotel.getImageUrls());
            }

            String filename = imageService.saveImage(file);
            hotel.setImageUrls(filename);
            hotelEntityService.updateEntity(hotel);

            return ResponseEntity.ok(filename);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to upload image");
        }
    }

    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            Resource resource = imageService.loadImage(filename);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(Paths.get(filename)))
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}

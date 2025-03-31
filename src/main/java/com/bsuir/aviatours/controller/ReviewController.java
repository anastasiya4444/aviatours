package com.bsuir.aviatours.controller;

import com.bsuir.aviatours.model.Review;
import com.bsuir.aviatours.service.interfaces.EntityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/review")
@CrossOrigin
public class ReviewController {

    private final EntityService<Review> reviewEntityService;

    public ReviewController(EntityService<Review> reviewEntityService) {
        this.reviewEntityService = reviewEntityService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody Review review) {
        reviewEntityService.saveEntity(review);
        return ResponseEntity.ok("Review saved successfully");
    }

    @GetMapping("/getAll")
    public List<Review> getAll() {
        return reviewEntityService.getAllEntities();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Review> getById(@PathVariable int id) {
        Review review = reviewEntityService.findEntityById(id);
        return ResponseEntity.ok(review);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        Review review = reviewEntityService.findEntityById(id);
        reviewEntityService.deleteEntity(review);
        return ResponseEntity.ok("Review deleted successfully");
    }

    @PutMapping("/update")
    public ResponseEntity<Review> update(@RequestBody Review review) {
        Review review1 = reviewEntityService.updateEntity(review);
        return ResponseEntity.ok(review1);
    }
}

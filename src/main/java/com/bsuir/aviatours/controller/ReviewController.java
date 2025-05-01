package com.bsuir.aviatours.controller;

import com.bsuir.aviatours.dto.ReviewDTO;
import com.bsuir.aviatours.model.Review;
import com.bsuir.aviatours.service.interfaces.EntityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/review")
@CrossOrigin
public class ReviewController {

    private final EntityService<Review> reviewEntityService;

    public ReviewController(EntityService<Review> reviewEntityService) {
        this.reviewEntityService = reviewEntityService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody ReviewDTO review) {
        reviewEntityService.saveEntity(review.toEntity());
        return ResponseEntity.ok("Review saved successfully");
    }

    @GetMapping("/getAll")
    public List<ReviewDTO> getAll() {
        return reviewEntityService.getAllEntities()
                .stream()
                .map(ReviewDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDTO> getById(@PathVariable int id) {
        ReviewDTO review = ReviewDTO.fromEntity(reviewEntityService.findEntityById(id));
        return ResponseEntity.ok(review);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        ReviewDTO review = ReviewDTO.fromEntity(reviewEntityService.findEntityById(id));
        reviewEntityService.deleteEntity(review.toEntity());
        return ResponseEntity.ok("Review deleted successfully");
    }

    @PutMapping("/update")
    public ResponseEntity<ReviewDTO> update(@RequestBody ReviewDTO review) {
        ReviewDTO review1 = ReviewDTO.fromEntity(reviewEntityService.updateEntity(review.toEntity()));
        return ResponseEntity.ok(review1);
    }
}

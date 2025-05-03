package com.bsuir.aviatours.controller;

import com.bsuir.aviatours.dto.ReviewDTO;
import com.bsuir.aviatours.dto.UserDTO;
import com.bsuir.aviatours.model.Review;
import com.bsuir.aviatours.model.User;
import com.bsuir.aviatours.security.UserDetailsConf;
import com.bsuir.aviatours.service.implementations.ReviewServiceImpl;
import com.bsuir.aviatours.service.implementations.UserServiceImpl;
import com.bsuir.aviatours.service.interfaces.EntityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/review")
@CrossOrigin
public class ReviewController {

    private final ReviewServiceImpl reviewEntityService;
    private final UserServiceImpl userService;

    public ReviewController(ReviewServiceImpl reviewEntityService, UserServiceImpl userService) {
        this.reviewEntityService = reviewEntityService;
        this.userService = userService;
    }

    @GetMapping("/my")
    public ResponseEntity<List<ReviewDTO>> getMyReviews(Authentication authentication) {
        try {
            String username = authentication.getName();
            User currentUser = userService.findByUsername(username);

            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            List<Review> reviews = reviewEntityService.findByUserId(currentUser.getId());
            List<ReviewDTO> reviewDTOs = reviews.stream()
                    .map(ReviewDTO::fromEntity)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(reviewDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<ReviewDTO> createReview(@RequestBody ReviewDTO reviewDTO,
                                                  Authentication authentication) {
        try {
            String username = authentication.getName();
            User currentUser = userService.findByUsername(username);

            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            reviewDTO.setUser(UserDTO.fromEntity(currentUser));
            Review review = reviewDTO.toEntity();
            Review savedReview = reviewEntityService.saveEntity(review);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ReviewDTO.fromEntity(savedReview));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewDTO> updateReview(
            @PathVariable Integer id,
            @RequestBody ReviewDTO reviewDTO,
            Authentication authentication) {
        try {
            String username = authentication.getName();
            User currentUser = userService.findByUsername(username);

            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            Review existingReview = reviewEntityService.findEntityById(id);
            if (existingReview == null || !existingReview.getUser().getId().equals(currentUser.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            existingReview.setRating(reviewDTO.getRating());
            existingReview.setReviewText(reviewDTO.getReviewText());

            Review updatedReview = reviewEntityService.updateEntity(existingReview);
            return ResponseEntity.ok(ReviewDTO.fromEntity(updatedReview));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Integer id,
                                             Authentication authentication) {
        try {
            String username = authentication.getName();
            User currentUser = userService.findByUsername(username);

            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            Review review = reviewEntityService.findEntityById(id);
            if (review == null || (!review.getUser().getId().equals(currentUser.getId())
                    && !currentUser.getRole().getName().equals("ADMIN"))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            reviewEntityService.deleteEntity(review);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<ReviewDTO>> getAll() {
        List<Review> reviews = reviewEntityService.getAllEntities();
        List<ReviewDTO> reviewDTOs = reviews.stream()
                .map(ReviewDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(reviewDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDTO> getById(@PathVariable Integer id) {
        Review review = reviewEntityService.findEntityById(id);
        if (review == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ReviewDTO.fromEntity(review));
    }

}
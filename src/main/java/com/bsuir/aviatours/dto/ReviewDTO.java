package com.bsuir.aviatours.dto;

import com.bsuir.aviatours.model.Review;
import com.bsuir.aviatours.model.User;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

public class ReviewDTO {
    private Integer id;
    private UserDTO user;
    private Byte rating;
    private String reviewText;
    private Instant createdAt;

    public ReviewDTO() {}

    public ReviewDTO(UserDTO user, Byte rating, String reviewText, Instant createdAt) {
        this.user = user;
        this.rating = rating;
        this.reviewText = reviewText;
        this.createdAt = createdAt;
    }

    public Review toEntity(){
        Review review = new Review();
        if(user.getId() != null){
            review.setUser(user.toEntity());
        }
        review.setRating(rating);
        review.setReviewText(reviewText);
        review.setCreatedAt(createdAt);
        return review;
    }

    public static ReviewDTO fromEntity(Review review){
        ReviewDTO reviewDTO = new ReviewDTO();
        if(review.getId() != null){
            if(review.getUser() != null){
                reviewDTO.setUser(UserDTO.fromEntity(review.getUser()));
            }
            reviewDTO.setRating(review.getRating());
            reviewDTO.setReviewText(review.getReviewText());
            reviewDTO.setCreatedAt(review.getCreatedAt());
        }
        return reviewDTO;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public Byte getRating() {
        return rating;
    }

    public void setRating(Byte rating) {
        this.rating = rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

}
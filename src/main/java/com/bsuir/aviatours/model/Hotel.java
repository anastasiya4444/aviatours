package com.bsuir.aviatours.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "hotel")
public class Hotel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hotel_id", nullable = false)
    private Integer id;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "address")
    private String address;

    @Column(name = "meal_type", length = 50)
    private String mealType;

    @Column(name = "rating", precision = 3, scale = 2)
    private BigDecimal rating;

    @Lob
    @Column(name = "hotel_features")
    private String hotelFeatures;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

    public String getHotelFeatures() {
        return hotelFeatures;
    }

    public void setHotelFeatures(String hotelFeatures) {
        this.hotelFeatures = hotelFeatures;
    }

}
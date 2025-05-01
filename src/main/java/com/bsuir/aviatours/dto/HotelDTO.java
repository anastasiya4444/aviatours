package com.bsuir.aviatours.dto;

import com.bsuir.aviatours.model.Hotel;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;

public class HotelDTO {
    private Integer id;
    private String description;
    private String address;
    private String mealType;
    private BigDecimal rating;
    private String hotelFeatures;
    private Instant createdAt;

    public HotelDTO() {}

    public HotelDTO(Integer id, String description, String address, String mealType, BigDecimal rating, String hotelFeatures, Instant createdAt) {
        this.id = id;
        this.description = description;
        this.address = address;
        this.mealType = mealType;
        this.rating = rating;
        this.hotelFeatures = hotelFeatures;
        this.createdAt = createdAt;
    }

    public Hotel toEntity(){
        Hotel hotel = new Hotel();
        hotel.setId(id);
        hotel.setDescription(description);
        hotel.setAddress(address);
        hotel.setMealType(mealType);
        hotel.setRating(rating);
        hotel.setHotelFeatures(hotelFeatures);
        hotel.setCreatedAt(createdAt);
        return hotel;
    }

    public static HotelDTO fromEntity(Hotel hotel){
        HotelDTO hotelDTO = new HotelDTO();
        if(hotel.getId() != null){
            hotelDTO.setId(hotel.getId());
            hotelDTO.setDescription(hotel.getDescription());
            hotelDTO.setAddress(hotel.getAddress());
            hotelDTO.setMealType(hotel.getMealType());
            hotelDTO.setRating(hotel.getRating());
            hotelDTO.setHotelFeatures(hotel.getHotelFeatures());
            hotelDTO.setCreatedAt(hotel.getCreatedAt());
        }
        return hotelDTO;
    }

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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

}
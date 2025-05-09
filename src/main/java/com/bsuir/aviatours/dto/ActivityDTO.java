package com.bsuir.aviatours.dto;

import com.bsuir.aviatours.model.Activity;
import com.bsuir.aviatours.model.Day;
import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;

public class ActivityDTO {
    private Integer id;
    private String activityType;
    private Instant startDateTime;
    private Instant endDateTime;
    private String description;
    private Integer initialCapacity;
    private Integer booked;
    private Instant createdAt;
    private BigDecimal cost;
    private String imageUrls;

    public ActivityDTO() {}

    public ActivityDTO(Integer id, String activityType, Instant startDateTime, Instant endDateTime,
                       String description, Integer initialCapacity, Integer booked, Instant createdAt, BigDecimal cost, String imageUrls) {
        this.id = id;
        this.activityType = activityType;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.description = description;
        this.initialCapacity = initialCapacity;
        this.booked = booked;
        this.createdAt = createdAt;
        this.cost = cost;
        this.imageUrls = imageUrls;
    }

    public Activity toEntity(){
        Activity activity = new Activity();
        activity.setId(this.id);
        activity.setActivityType(this.activityType);
        activity.setStartDateTime(this.startDateTime);
        activity.setEndDateTime(this.endDateTime);
        activity.setDescription(this.description);
        activity.setInitialCapacity(this.initialCapacity);
        activity.setBooked(this.booked);
        activity.setCreatedAt(this.createdAt);
        activity.setCost(this.cost);
        activity.setImageUrls(this.imageUrls);
        return activity;
    }

    public static ActivityDTO fromEntity(Activity activity){
        ActivityDTO activityDTO = new ActivityDTO();
        if(activity.getId() != null){
            activityDTO.setId(activity.getId());
            activityDTO.setActivityType(activity.getActivityType());
            activityDTO.setStartDateTime(activity.getStartDateTime());
            activityDTO.setEndDateTime(activity.getEndDateTime());
            activityDTO.setDescription(activity.getDescription());
            activityDTO.setInitialCapacity(activity.getInitialCapacity());
            activityDTO.setBooked(activity.getBooked());
            activityDTO.setCreatedAt(activity.getCreatedAt());
            activityDTO.setCost(activity.getCost());
            activityDTO.setImageUrls(activity.getImageUrls());
        }
        return activityDTO;
    }

    public String getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(String imageUrls) {
        this.imageUrls = imageUrls;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public Instant getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Instant startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Instant getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Instant endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getInitialCapacity() {
        return initialCapacity;
    }

    public void setInitialCapacity(Integer initialCapacity) {
        this.initialCapacity = initialCapacity;
    }

    public Integer getBooked() {
        return booked;
    }

    public void setBooked(Integer booked) {
        this.booked = booked;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

}
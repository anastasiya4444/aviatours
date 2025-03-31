package com.bsuir.aviatours.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "activity")
public class Activity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activity_id", nullable = false)
    private Integer id;

    @Column(name = "activity_type", nullable = false, length = 45)
    private String activityType;

    @Column(name = "start_date_time", nullable = false)
    private Instant startDateTime;

    @Column(name = "end_date_time", nullable = false)
    private Instant endDateTime;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "initial_capacity", nullable = false)
    private Integer initialCapacity;

    @Column(name = "booked", nullable = false)
    private Integer booked;

    @Column(name = "additional_service_id")
    private Integer additionalServiceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

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

    public Integer getAdditionalServiceId() {
        return additionalServiceId;
    }

    public void setAdditionalServiceId(Integer additionalServiceId) {
        this.additionalServiceId = additionalServiceId;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

}
package com.bsuir.aviatours.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "room")
public class Room implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @Column(name = "view", length = 50)
    private String view;

    @Column(name = "beds")
    private Integer beds;

    @Column(name = "max_guests")
    private Integer maxGuests;

    @Column(name = "size_m2", precision = 5, scale = 2)
    private BigDecimal sizeM2;

    @Column(name = "type", length = 50)
    private String type;

    @Column(name = "bath")
    private Boolean bath;

    @Column(name = "terrace")
    private Boolean terrace;

    @Column(name = "cost", precision = 10, scale = 2)
    private BigDecimal cost;

    @Column(name = "available_rooms")
    private Integer availableRooms;

    @Column(name = "booking_count")
    private Integer bookingCount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public Integer getBeds() {
        return beds;
    }

    public void setBeds(Integer beds) {
        this.beds = beds;
    }

    public Integer getMaxGuests() {
        return maxGuests;
    }

    public void setMaxGuests(Integer maxGuests) {
        this.maxGuests = maxGuests;
    }

    public BigDecimal getSizeM2() {
        return sizeM2;
    }

    public void setSizeM2(BigDecimal sizeM2) {
        this.sizeM2 = sizeM2;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getBath() {
        return bath;
    }

    public void setBath(Boolean bath) {
        this.bath = bath;
    }

    public Boolean getTerrace() {
        return terrace;
    }

    public void setTerrace(Boolean terrace) {
        this.terrace = terrace;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public Integer getAvailableRooms() {
        return availableRooms;
    }

    public void setAvailableRooms(Integer availableRooms) {
        this.availableRooms = availableRooms;
    }

    public Integer getBookingCount() {
        return bookingCount;
    }

    public void setBookingCount(Integer bookingCount) {
        this.bookingCount = bookingCount;
    }

}
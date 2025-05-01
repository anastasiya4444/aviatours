package com.bsuir.aviatours.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tour_hotel")
public class TourHotel {
    @EmbeddedId
    private TourHotelId id;

    @MapsId("tourId")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "tour_id", nullable = false)
    private Tour tour;

    @MapsId("hotelId")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    public TourHotelId getId() {
        return id;
    }

    public void setId(TourHotelId id) {
        this.id = id;
    }

    public Tour getTour() {
        return tour;
    }

    public void setTour(Tour tour) {
        this.tour = tour;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

}
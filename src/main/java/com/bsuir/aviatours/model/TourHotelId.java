package com.bsuir.aviatours.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.hibernate.Hibernate;

import java.util.Objects;

@Embeddable
public class TourHotelId implements java.io.Serializable {
    private static final long serialVersionUID = 8273121448808751028L;
    @Column(name = "tour_id", nullable = false)
    private Integer tourId;

    @Column(name = "hotel_id", nullable = false)
    private Integer hotelId;

    public TourHotelId(Integer tourId, Integer hotelId) {
        this.tourId = tourId;
        this.hotelId = hotelId;
    }

    public TourHotelId() {}

    public Integer getTourId() {
        return tourId;
    }

    public void setTourId(Integer tourId) {
        this.tourId = tourId;
    }

    public Integer getHotelId() {
        return hotelId;
    }

    public void setHotelId(Integer hotelId) {
        this.hotelId = hotelId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TourHotelId entity = (TourHotelId) o;
        return Objects.equals(this.tourId, entity.tourId) &&
                Objects.equals(this.hotelId, entity.hotelId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tourId, hotelId);
    }

}
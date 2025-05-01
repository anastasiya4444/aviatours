package com.bsuir.aviatours.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.hibernate.Hibernate;

import java.util.Objects;

@Embeddable
public class TourAdditionalServiceId implements java.io.Serializable {
    private static final long serialVersionUID = -7890743696844133631L;
    @Column(name = "tour_id", nullable = false)
    private Integer tourId;

    @Column(name = "service_id", nullable = false)
    private Integer serviceId;

    public Integer getTourId() {
        return tourId;
    }

    public void setTourId(Integer tourId) {
        this.tourId = tourId;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TourAdditionalServiceId entity = (TourAdditionalServiceId) o;
        return Objects.equals(this.tourId, entity.tourId) &&
                Objects.equals(this.serviceId, entity.serviceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tourId, serviceId);
    }

}
package com.bsuir.aviatours.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.hibernate.Hibernate;

import java.util.Objects;

@Embeddable
public class TourVisaRequirementId implements java.io.Serializable {
    private static final long serialVersionUID = -3488657204817768498L;
    @Column(name = "tour_id", nullable = false)
    private Integer tourId;

    @Column(name = "requirement_id", nullable = false)
    private Integer requirementId;

    public Integer getTourId() {
        return tourId;
    }

    public void setTourId(Integer tourId) {
        this.tourId = tourId;
    }

    public Integer getRequirementId() {
        return requirementId;
    }

    public void setRequirementId(Integer requirementId) {
        this.requirementId = requirementId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TourVisaRequirementId entity = (TourVisaRequirementId) o;
        return Objects.equals(this.tourId, entity.tourId) &&
                Objects.equals(this.requirementId, entity.requirementId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tourId, requirementId);
    }

}
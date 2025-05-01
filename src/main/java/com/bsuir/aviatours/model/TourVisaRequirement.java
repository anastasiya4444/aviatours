package com.bsuir.aviatours.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tour_visa_requirement")
public class TourVisaRequirement {
    @EmbeddedId
    private TourVisaRequirementId id;

    @MapsId("tourId")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "tour_id", nullable = false)
    private Tour tour;

    @MapsId("requirementId")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "requirement_id", nullable = false)
    private VisaRequirement requirement;

    public TourVisaRequirementId getId() {
        return id;
    }

    public void setId(TourVisaRequirementId id) {
        this.id = id;
    }

    public Tour getTour() {
        return tour;
    }

    public void setTour(Tour tour) {
        this.tour = tour;
    }

    public VisaRequirement getRequirement() {
        return requirement;
    }

    public void setRequirement(VisaRequirement requirement) {
        this.requirement = requirement;
    }

}
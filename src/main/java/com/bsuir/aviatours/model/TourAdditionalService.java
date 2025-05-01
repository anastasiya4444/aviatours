package com.bsuir.aviatours.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tour_additional_service")
public class TourAdditionalService {
    @EmbeddedId
    private TourAdditionalServiceId id;

    @MapsId("tourId")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "tour_id", nullable = false)
    private Tour tour;

    @MapsId("serviceId")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "service_id", nullable = false)
    private AdditionalService service;

    public TourAdditionalServiceId getId() {
        return id;
    }

    public void setId(TourAdditionalServiceId id) {
        this.id = id;
    }

    public Tour getTour() {
        return tour;
    }

    public void setTour(Tour tour) {
        this.tour = tour;
    }

    public AdditionalService getService() {
        return service;
    }

    public void setService(AdditionalService service) {
        this.service = service;
    }

}
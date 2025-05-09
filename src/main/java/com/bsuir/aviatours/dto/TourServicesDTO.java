package com.bsuir.aviatours.dto;

import java.util.Collections;
import java.util.Set;

public class TourServicesDTO {
    private Set<Integer> hotelIds;
    private Set<Integer> additionalServiceIds;

    // Геттеры и сеттеры
    public Set<Integer> getHotelIds() {
        return hotelIds != null ? hotelIds : Collections.emptySet();
    }

    public Set<Integer> getAdditionalServiceIds() {
        return additionalServiceIds != null ? additionalServiceIds : Collections.emptySet();
    }
}
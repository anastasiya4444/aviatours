package com.bsuir.aviatours.repository;

import com.bsuir.aviatours.model.TourAdditionalService;
import com.bsuir.aviatours.model.TourHotel;
import com.bsuir.aviatours.model.TourHotelId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TourHotelRepository extends JpaRepository<TourHotel, TourHotelId> {
}
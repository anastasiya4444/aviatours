package com.bsuir.aviatours.repository;

import com.bsuir.aviatours.model.DayActivity;
import com.bsuir.aviatours.model.DayActivityId;
import com.bsuir.aviatours.model.TourHotel;
import com.bsuir.aviatours.model.TourHotelId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DayActivityRepository extends JpaRepository<DayActivity, DayActivityId> {
}
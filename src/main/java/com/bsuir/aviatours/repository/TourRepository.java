package com.bsuir.aviatours.repository;

import com.bsuir.aviatours.model.Tour;
import com.bsuir.aviatours.model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TourRepository extends JpaRepository<Tour, Integer> {
}

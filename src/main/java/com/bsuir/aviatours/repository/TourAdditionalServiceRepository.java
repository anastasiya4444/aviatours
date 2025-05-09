package com.bsuir.aviatours.repository;

import com.bsuir.aviatours.model.Activity;
import com.bsuir.aviatours.model.TourAdditionalService;
import com.bsuir.aviatours.model.TourAdditionalServiceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TourAdditionalServiceRepository extends JpaRepository<TourAdditionalService, TourAdditionalServiceId> {
}

package com.bsuir.aviatours.repository;

import com.bsuir.aviatours.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TourVisaRequirementRepository extends JpaRepository<Activity, Integer> {
}

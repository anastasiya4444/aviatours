package com.bsuir.aviatours.repository;

import com.bsuir.aviatours.model.AdditionalService;
import com.bsuir.aviatours.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdditionalServiceRepository extends JpaRepository<AdditionalService, Integer> {
}

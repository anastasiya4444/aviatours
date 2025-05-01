package com.bsuir.aviatours.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientVisaRepository extends JpaRepository<ClientVisa, Integer> {
}

package com.bsuir.aviatours.repository;

import com.bsuir.aviatours.model.PersonalDatum;
import com.bsuir.aviatours.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalDataRepository extends JpaRepository<PersonalDatum, Integer> {
}

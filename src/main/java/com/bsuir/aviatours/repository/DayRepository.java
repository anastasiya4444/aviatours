package com.bsuir.aviatours.repository;

import com.bsuir.aviatours.model.Day;
import com.bsuir.aviatours.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DayRepository extends JpaRepository<Day, Integer> {
}

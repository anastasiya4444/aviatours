package com.bsuir.aviatours.repository;

import com.bsuir.aviatours.model.Role;
import com.bsuir.aviatours.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteRepository extends JpaRepository<Route, Integer> {
}

package com.bsuir.aviatours.repository;

import com.bsuir.aviatours.model.AirTicket;
import com.bsuir.aviatours.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface AirTicketRepository extends JpaRepository<AirTicket, Integer>, JpaSpecificationExecutor<AirTicket> {
    @Query("SELECT t FROM AirTicket t WHERE " +
            "(:flightNumber IS NULL OR t.flightNumber = :flightNumber) AND " +
            "(:departureAirportCode IS NULL OR t.departureAirportCode = :departureAirportCode) AND " +
            "(:arrivalAirportCode IS NULL OR t.arrivalAirportCode = :arrivalAirportCode) AND " +
            "(:departureTimeFrom IS NULL OR t.departureTime >= :departureTimeFrom) AND " +
            "(:departureTimeTo IS NULL OR t.departureTime <= :departureTimeTo) AND " +
            "(:minCost IS NULL OR t.cost >= :minCost) AND " +
            "(:maxCost IS NULL OR t.cost <= :maxCost) AND " +
            "(:seatNumber IS NULL OR t.seatNumber = :seatNumber) AND " +
            "(:status IS NULL OR t.status = :status)")
    List<AirTicket> findFilteredTickets(
            @Param("flightNumber") String flightNumber,
            @Param("departureAirportCode") String departureAirportCode,
            @Param("arrivalAirportCode") String arrivalAirportCode,
            @Param("departureTimeFrom") String departureTimeFrom,
            @Param("departureTimeTo") String departureTimeTo,
            @Param("minCost") BigDecimal minCost,
            @Param("maxCost") BigDecimal maxCost,
            @Param("seatNumber") Integer seatNumber,
            @Param("status") String status);
}

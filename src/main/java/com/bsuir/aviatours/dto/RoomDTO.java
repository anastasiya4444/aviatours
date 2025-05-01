package com.bsuir.aviatours.dto;

import com.bsuir.aviatours.model.Hotel;
import com.bsuir.aviatours.model.Room;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;

public class RoomDTO {
    private Integer id;
    private HotelDTO hotel;
    private String view;
    private Integer beds;
    private Integer maxGuests;
    private BigDecimal sizeM2;
    private String type;
    private Boolean bath;
    private Boolean terrace;
    private BigDecimal cost;
    private Integer availableRooms;
    private Integer bookingCount;
    private Instant createdAt;

    public RoomDTO() {}

    public RoomDTO(Integer id, HotelDTO hotel, String view, Integer beds, Integer maxGuests, BigDecimal sizeM2,
                   String type, Boolean bath, Boolean terrace, BigDecimal cost, Integer availableRooms,
                   Integer bookingCount, Instant createdAt) {
        this.id = id;
        this.hotel = hotel;
        this.view = view;
        this.beds = beds;
        this.maxGuests = maxGuests;
        this.sizeM2 = sizeM2;
        this.type = type;
        this.bath = bath;
        this.terrace = terrace;
        this.cost = cost;
        this.availableRooms = availableRooms;
        this.bookingCount = bookingCount;
        this.createdAt = createdAt;
    }

    public Room toEntity(){
        Room room = new Room();
        room.setId(this.id);
        if(hotel.getId() != null){
            room.setHotel(this.hotel.toEntity());
        }
        room.setView(this.view);
        room.setBeds(this.beds);
        room.setMaxGuests(this.maxGuests);
        room.setSizeM2(this.sizeM2);
        room.setType(this.type);
        room.setBath(this.bath);
        room.setTerrace(this.terrace);
        room.setCost(this.cost);
        room.setAvailableRooms(this.availableRooms);
        room.setBookingCount(this.bookingCount);
        room.setCreatedAt(this.createdAt);
        return room;
    }

    public static RoomDTO fromEntity(Room room){
        RoomDTO roomDTO = new RoomDTO();
        if(room.getId() != null){
            roomDTO.setId(room.getId());
            if(room.getHotel() != null){
                roomDTO.setHotel(HotelDTO.fromEntity(room.getHotel()));
            }
            roomDTO.setView(room.getView());
            roomDTO.setBeds(room.getBeds());
            roomDTO.setMaxGuests(room.getMaxGuests());
            roomDTO.setSizeM2(room.getSizeM2());
            roomDTO.setType(room.getType());
            roomDTO.setBath(room.getBath());
            roomDTO.setTerrace(room.getTerrace());
            roomDTO.setCost(room.getCost());
            roomDTO.setAvailableRooms(room.getAvailableRooms());
            roomDTO.setBookingCount(room.getBookingCount());
            roomDTO.setCreatedAt(room.getCreatedAt());
        }
        return roomDTO;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public HotelDTO getHotel() {
        return hotel;
    }

    public void setHotel(HotelDTO hotel) {
        this.hotel = hotel;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public Integer getBeds() {
        return beds;
    }

    public void setBeds(Integer beds) {
        this.beds = beds;
    }

    public Integer getMaxGuests() {
        return maxGuests;
    }

    public void setMaxGuests(Integer maxGuests) {
        this.maxGuests = maxGuests;
    }

    public BigDecimal getSizeM2() {
        return sizeM2;
    }

    public void setSizeM2(BigDecimal sizeM2) {
        this.sizeM2 = sizeM2;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getBath() {
        return bath;
    }

    public void setBath(Boolean bath) {
        this.bath = bath;
    }

    public Boolean getTerrace() {
        return terrace;
    }

    public void setTerrace(Boolean terrace) {
        this.terrace = terrace;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public Integer getAvailableRooms() {
        return availableRooms;
    }

    public void setAvailableRooms(Integer availableRooms) {
        this.availableRooms = availableRooms;
    }

    public Integer getBookingCount() {
        return bookingCount;
    }

    public void setBookingCount(Integer bookingCount) {
        this.bookingCount = bookingCount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

}
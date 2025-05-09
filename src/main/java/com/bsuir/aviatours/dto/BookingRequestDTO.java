package com.bsuir.aviatours.dto;

import com.bsuir.aviatours.model.User;

import java.util.List;

public class BookingRequestDTO {
    private Integer routeId;
    private Integer roomId;
    private List<Integer> airTicketIds;
    private List<Integer> activityIds;
    private String paymentMethod;
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // Getters and Setters
    public Integer getRouteId() { return routeId; }
    public void setRouteId(Integer routeId) { this.routeId = routeId; }

    public Integer getRoomId() { return roomId; }
    public void setRoomId(Integer roomId) { this.roomId = roomId; }

    public List<Integer> getAirTicketIds() { return airTicketIds; }
    public void setAirTicketIds(List<Integer> airTicketIds) { this.airTicketIds = airTicketIds; }

    public List<Integer> getActivityIds() { return activityIds; }
    public void setActivityIds(List<Integer> activityIds) { this.activityIds = activityIds; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}
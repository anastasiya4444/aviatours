package com.bsuir.aviatours.dto;

import java.util.List;

public class PriceCalculationDTO {
    private List<Integer> airTicketIds;
    private Integer roomId;
    private List<Integer> activityIds;

    public List<Integer> getAirTicketIds() {
        return airTicketIds;
    }

    public void setAirTicketIds(List<Integer> airTicketIds) {
        this.airTicketIds = airTicketIds;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public List<Integer> getActivityIds() {
        return activityIds;
    }

    public void setActivityIds(List<Integer> activityIds) {
        this.activityIds = activityIds;
    }
}
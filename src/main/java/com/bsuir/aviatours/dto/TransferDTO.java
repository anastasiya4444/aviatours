package com.bsuir.aviatours.dto;

import com.bsuir.aviatours.model.Transfer;

import java.time.Instant;

public class TransferDTO {
    private Integer id;
    private String fromLocation;
    private String toLocation;
    private Instant transferDate;
    private UserDTO user;
    private String description;

    public TransferDTO() {}

    public TransferDTO(Integer id, String fromLocation, String toLocation, Instant transferDate, UserDTO user, String description) {
        this.id = id;
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
        this.transferDate = transferDate;
        this.user = user;
        this.description = description;
    }

    public Transfer toEntity(){
        Transfer transfer = new Transfer();
        if(transfer.getId() != null) {
            transfer.setId(this.id);
            transfer.setFromLocation(this.fromLocation);
            transfer.setToLocation(this.toLocation);
            transfer.setTransferDate(this.transferDate);
            if(transfer.getUser() != null) {
                transfer.setUser(this.user.toEntity());
            }
            transfer.setDescription(this.description);
        }
        return transfer;
    }

    public static TransferDTO fromEntity(Transfer transfer){
        TransferDTO transferDTO = new TransferDTO();
        if(transfer.getId() != null) {
            transferDTO.setId(transfer.getId());
            transferDTO.setFromLocation(transfer.getFromLocation());
            transferDTO.setToLocation(transfer.getToLocation());
            transferDTO.setTransferDate(transfer.getTransferDate());
            if(transfer.getUser() != null) {
                transferDTO.setUser(UserDTO.fromEntity(transfer.getUser()));
            }
            transferDTO.setDescription(transfer.getDescription());
        }
        return transferDTO;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(String fromLocation) {
        this.fromLocation = fromLocation;
    }

    public String getToLocation() {
        return toLocation;
    }

    public void setToLocation(String toLocation) {
        this.toLocation = toLocation;
    }

    public Instant getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(Instant transferDate) {
        this.transferDate = transferDate;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
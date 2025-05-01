package com.bsuir.aviatours.dto;

import com.bsuir.aviatours.model.AdditionalService;
import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;

public class AdditionalServiceDTO {
    private Integer id;
    private String serviceType;
    private String description;
    private Instant createdAt;
    private BigDecimal cost;
    private String imageUrl;

    public AdditionalServiceDTO() {}

    public AdditionalServiceDTO(Integer id, String serviceType, String description, Instant createdAt, BigDecimal cost, String imageUrl) {
        this.id = id;
        this.serviceType = serviceType;
        this.description = description;
        this.createdAt = createdAt;
        this.cost = cost;
        this.imageUrl = imageUrl;
    }

    public AdditionalService toEntity(){
        AdditionalService additionalService = new AdditionalService();
        additionalService.setId(id);
        additionalService.setServiceType(serviceType);
        additionalService.setDescription(description);
        additionalService.setCreatedAt(createdAt);
        additionalService.setCost(cost);
        additionalService.setImageUrl(imageUrl);
        return additionalService;
    }

    public static AdditionalServiceDTO fromEntity(AdditionalService additionalService){
        AdditionalServiceDTO additionalServiceDTO = new AdditionalServiceDTO();
        if(additionalService != null){
            additionalServiceDTO.setId(additionalService.getId());
            additionalServiceDTO.setServiceType(additionalService.getServiceType());
            additionalServiceDTO.setDescription(additionalService.getDescription());
            additionalServiceDTO.setCreatedAt(additionalService.getCreatedAt());
            additionalServiceDTO.setCost(additionalService.getCost());
            additionalServiceDTO.setImageUrl(additionalService.getImageUrl());
        }
        return additionalServiceDTO;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

}
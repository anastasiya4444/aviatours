package com.bsuir.aviatours.dto;

import com.bsuir.aviatours.model.AdditionalService;
import java.time.Instant;

public class AdditionalServiceDTO {
    private Integer id;
    private String serviceType;
    private String description;
    private Instant createdAt;

    public AdditionalServiceDTO() {}

    public AdditionalServiceDTO(Integer id, String serviceType, String description, Instant createdAt) {
        this.id = id;
        this.serviceType = serviceType;
        this.description = description;
        this.createdAt = createdAt;
    }

    public AdditionalService toEntity(){
        AdditionalService additionalService = new AdditionalService();
        additionalService.setId(id);
        additionalService.setServiceType(serviceType);
        additionalService.setDescription(description);
        additionalService.setCreatedAt(createdAt);
        return additionalService;
    }

    public static AdditionalServiceDTO fromEntity(AdditionalService additionalService){
        AdditionalServiceDTO additionalServiceDTO = new AdditionalServiceDTO();
        if(additionalService != null){
            additionalServiceDTO.setId(additionalService.getId());
            additionalServiceDTO.setServiceType(additionalService.getServiceType());
            additionalServiceDTO.setDescription(additionalService.getDescription());
            additionalServiceDTO.setCreatedAt(additionalService.getCreatedAt());
        }
        return additionalServiceDTO;
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
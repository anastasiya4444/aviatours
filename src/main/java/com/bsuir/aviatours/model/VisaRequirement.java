package com.bsuir.aviatours.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "visa_requirement")
public class VisaRequirement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "requirement_id", nullable = false)
    private Integer id;

    @Column(name = "country", nullable = false, length = 100)
    private String country;

    @Column(name = "visa_type", nullable = false, length = 50)
    private String visaType;

    @Column(name = "consular_fee", nullable = false, precision = 10, scale = 2)
    private BigDecimal consularFee;

    @Lob
    @Column(name = "requirement_description", nullable = false)
    private String requirementDescription;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getVisaType() {
        return visaType;
    }

    public void setVisaType(String visaType) {
        this.visaType = visaType;
    }

    public BigDecimal getConsularFee() {
        return consularFee;
    }

    public void setConsularFee(BigDecimal consularFee) {
        this.consularFee = consularFee;
    }

    public String getRequirementDescription() {
        return requirementDescription;
    }

    public void setRequirementDescription(String requirementDescription) {
        this.requirementDescription = requirementDescription;
    }

}
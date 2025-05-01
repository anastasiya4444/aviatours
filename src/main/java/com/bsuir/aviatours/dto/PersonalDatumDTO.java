package com.bsuir.aviatours.dto;

import com.bsuir.aviatours.model.PersonalDatum;
import jakarta.persistence.Column;
import jakarta.persistence.Lob;

import java.time.LocalDate;

public class PersonalDatumDTO {
    private Integer id;
    private String lastName;
    private String firstName;
    private String middleName;
    private String citizenship;
    private LocalDate birthDate;
    private String gender;
    private String passportType;
    private String passportNumber;
    private LocalDate passportIssueDate;
    private LocalDate passportExpiryDate;
    private String issuingAuthority;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getCitizenship() {
        return citizenship;
    }

    public void setCitizenship(String citizenship) {
        this.citizenship = citizenship;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPassportType() {
        return passportType;
    }

    public void setPassportType(String passportType) {
        this.passportType = passportType;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public LocalDate getPassportIssueDate() {
        return passportIssueDate;
    }

    public void setPassportIssueDate(LocalDate passportIssueDate) {
        this.passportIssueDate = passportIssueDate;
    }

    public LocalDate getPassportExpiryDate() {
        return passportExpiryDate;
    }

    public void setPassportExpiryDate(LocalDate passportExpiryDate) {
        this.passportExpiryDate = passportExpiryDate;
    }

    public String getIssuingAuthority() {
        return issuingAuthority;
    }

    public void setIssuingAuthority(String issuingAuthority) {
        this.issuingAuthority = issuingAuthority;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public PersonalDatum toEntity() {
        PersonalDatum personalDatum = new PersonalDatum();
        personalDatum.setId(this.id);
        personalDatum.setBirthDate(this.birthDate);
        personalDatum.setFirstName(this.firstName);
        personalDatum.setMiddleName(this.middleName);
        personalDatum.setLastName(this.lastName);
        personalDatum.setPassportType(this.passportType);
        personalDatum.setPassportNumber(this.passportNumber);
        personalDatum.setPassportIssueDate(this.passportIssueDate);
        personalDatum.setPassportExpiryDate(this.passportExpiryDate);
        personalDatum.setIssuingAuthority(this.issuingAuthority);
        return personalDatum;
    }

    public static PersonalDatumDTO fromEntity(PersonalDatum personalDatum) {
        PersonalDatumDTO dto = new PersonalDatumDTO();
        if(personalDatum.getId() != null) {
            dto.setId(personalDatum.getId());
            dto.setBirthDate(personalDatum.getBirthDate());
            dto.setFirstName(personalDatum.getFirstName());
            dto.setMiddleName(personalDatum.getMiddleName());
            dto.setLastName(personalDatum.getLastName());
            dto.setPassportType(personalDatum.getPassportType());
            dto.setPassportNumber(personalDatum.getPassportNumber());
            dto.setPassportIssueDate(personalDatum.getPassportIssueDate());
            dto.setPassportExpiryDate(personalDatum.getPassportExpiryDate());
            dto.setIssuingAuthority(personalDatum.getIssuingAuthority());
        }

        return dto;
    }
}

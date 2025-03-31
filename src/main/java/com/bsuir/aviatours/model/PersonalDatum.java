package com.bsuir.aviatours.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "personal_data")
public class PersonalDatum implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "personal_data_id", nullable = false)
    private Integer id;

    @Column(name = "last_name", nullable = false, length = 45)
    private String lastName;

    @Column(name = "first_name", nullable = false, length = 45)
    private String firstName;

    @Column(name = "middle_name", nullable = false, length = 45)
    private String middleName;

    @Column(name = "citizenship", nullable = false, length = 45)
    private String citizenship;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Lob
    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "passport_type", nullable = false, length = 45)
    private String passportType;

    @Column(name = "passport_number", nullable = false, length = 45)
    private String passportNumber;

    @Column(name = "passport_issue_date", nullable = false)
    private LocalDate passportIssueDate;

    @Column(name = "passport_expiry_date", nullable = false)
    private LocalDate passportExpiryDate;

    @Column(name = "issuing_authority", nullable = false, length = 225)
    private String issuingAuthority;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
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

}
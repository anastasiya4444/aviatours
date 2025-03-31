package com.bsuir.aviatours.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "user_personal_data")
public class UserPersonalDatum implements Serializable {
    @EmbeddedId
    private UserPersonalDatumId id;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @MapsId("personalDataId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "personal_data_id", nullable = false)
    private PersonalDatum personalData;

    public UserPersonalDatumId getId() {
        return id;
    }

    public void setId(UserPersonalDatumId id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public PersonalDatum getPersonalData() {
        return personalData;
    }

    public void setPersonalData(PersonalDatum personalData) {
        this.personalData = personalData;
    }

}
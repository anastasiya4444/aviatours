package com.bsuir.aviatours.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserPersonalDatumId implements Serializable {
    private static final long serialVersionUID = -8641428751736007319L;
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "personal_data_id", nullable = false)
    private Integer personalDataId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getPersonalDataId() {
        return personalDataId;
    }

    public void setPersonalDataId(Integer personalDataId) {
        this.personalDataId = personalDataId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserPersonalDatumId entity = (UserPersonalDatumId) o;
        return Objects.equals(this.userId, entity.userId) &&
                Objects.equals(this.personalDataId, entity.personalDataId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, personalDataId);
    }

}
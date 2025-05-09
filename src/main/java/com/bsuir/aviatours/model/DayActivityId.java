package com.bsuir.aviatours.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.hibernate.Hibernate;

import java.util.Objects;

@Embeddable
public class DayActivityId implements java.io.Serializable {
    private static final long serialVersionUID = -5096659212943130815L;
    @Column(name = "day_id", nullable = false)
    private Integer dayId;

    @Column(name = "activity_id", nullable = false)
    private Integer activityId;

    public Integer getDayId() {
        return dayId;
    }

    public void setDayId(Integer dayId) {
        this.dayId = dayId;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        DayActivityId entity = (DayActivityId) o;
        return Objects.equals(this.activityId, entity.activityId) &&
                Objects.equals(this.dayId, entity.dayId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(activityId, dayId);
    }

}
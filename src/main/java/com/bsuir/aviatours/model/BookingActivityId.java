package com.bsuir.aviatours.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.hibernate.Hibernate;

import java.util.Objects;

@Embeddable
public class BookingActivityId implements java.io.Serializable {
    private static final long serialVersionUID = 1709025303245342086L;
    @Column(name = "booking_id", nullable = false)
    private Integer bookingId;

    @Column(name = "activity_id", nullable = false)
    private Integer activityId;

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
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
        BookingActivityId entity = (BookingActivityId) o;
        return Objects.equals(this.activityId, entity.activityId) &&
                Objects.equals(this.bookingId, entity.bookingId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(activityId, bookingId);
    }

}
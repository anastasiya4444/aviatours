package com.bsuir.aviatours.service.implementations;

import com.bsuir.aviatours.model.Hotel;
import com.bsuir.aviatours.model.Room;
import com.bsuir.aviatours.model.Tour;
import com.bsuir.aviatours.model.TourHotel;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TourSpecifications {

    public static Specification<Tour> withTourName(String tourName) {
        return (root, query, cb) ->
                tourName == null ? null :
                        cb.like(cb.lower(root.get("tourName")), "%" + tourName.toLowerCase() + "%");
    }

    public static Specification<Tour> withDescription(String description) {
        return (root, query, cb) ->
                description == null ? null :
                        cb.like(cb.lower(root.get("description")), "%" + description.toLowerCase() + "%");
    }

    public static Specification<Tour> withMealType(String mealType) {
        return (root, query, cb) -> {
            if (mealType == null) return null;
            Join<Tour, TourHotel> tourHotelJoin = root.join("hotels");
            Join<TourHotel, Hotel> hotelJoin = tourHotelJoin.join("hotel");
            return cb.equal(hotelJoin.get("mealType"), mealType);
        };
    }

    public static Specification<Tour> withRatingBetween(Double minRating, Double maxRating) {
        return (root, query, cb) -> {
            if (minRating == null && maxRating == null) return null;
            Join<Tour, TourHotel> tourHotelJoin = root.join("hotels");
            Join<TourHotel, Hotel> hotelJoin = tourHotelJoin.join("hotel");

            List<Predicate> predicates = new ArrayList<>();
            if (minRating != null) {
                predicates.add(cb.ge(hotelJoin.get("rating"), minRating));
            }
            if (maxRating != null) {
                predicates.add(cb.le(hotelJoin.get("rating"), maxRating));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Tour> withHotelFeatures(String hotelFeatures) {
        return (root, query, cb) -> {
            if (hotelFeatures == null) return null;
            Join<Tour, TourHotel> tourHotelJoin = root.join("hotels");
            Join<TourHotel, Hotel> hotelJoin = tourHotelJoin.join("hotel");
            return cb.like(cb.lower(hotelJoin.get("hotelFeatures")), "%" + hotelFeatures.toLowerCase() + "%");
        };
    }

    public static Specification<Tour> withRoomView(String roomView) {
        return (root, query, cb) -> {
            if (roomView == null) return null;
            Join<Tour, TourHotel> tourHotelJoin = root.join("hotels");
            Join<TourHotel, Hotel> hotelJoin = tourHotelJoin.join("hotel");
            Join<Hotel, Room> roomJoin = hotelJoin.join("rooms");
            return cb.like(cb.lower(roomJoin.get("view")), "%" + roomView.toLowerCase() + "%");
        };
    }

    public static Specification<Tour> withBedsBetween(Integer minBeds, Integer maxBeds) {
        return (root, query, cb) -> {
            if (minBeds == null && maxBeds == null) return null;
            Join<Tour, TourHotel> tourHotelJoin = root.join("hotels");
            Join<TourHotel, Hotel> hotelJoin = tourHotelJoin.join("hotel");
            Join<Hotel, Room> roomJoin = hotelJoin.join("rooms");

            List<Predicate> predicates = new ArrayList<>();
            if (minBeds != null) {
                predicates.add(cb.ge(roomJoin.get("beds"), minBeds));
            }
            if (maxBeds != null) {
                predicates.add(cb.le(roomJoin.get("beds"), maxBeds));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Tour> withRoomSizeBetween(Double minSize, Double maxSize) {
        return (root, query, cb) -> {
            if (minSize == null && maxSize == null) return null;
            Join<Tour, TourHotel> tourHotelJoin = root.join("hotels");
            Join<TourHotel, Hotel> hotelJoin = tourHotelJoin.join("hotel");
            Join<Hotel, Room> roomJoin = hotelJoin.join("rooms");

            List<Predicate> predicates = new ArrayList<>();
            if (minSize != null) {
                predicates.add(cb.ge(roomJoin.get("sizeM2"), minSize));
            }
            if (maxSize != null) {
                predicates.add(cb.le(roomJoin.get("sizeM2"), maxSize));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Tour> withRoomType(String roomType) {
        return (root, query, cb) -> {
            if (roomType == null) return null;
            Join<Tour, TourHotel> tourHotelJoin = root.join("hotels");
            Join<TourHotel, Hotel> hotelJoin = tourHotelJoin.join("hotel");
            Join<Hotel, Room> roomJoin = hotelJoin.join("rooms");
            return cb.equal(roomJoin.get("type"), roomType);
        };
    }

    public static Specification<Tour> hasBath(Boolean hasBath) {
        return (root, query, cb) -> {
            if (hasBath == null) return null;
            Join<Tour, TourHotel> tourHotelJoin = root.join("hotels");
            Join<TourHotel, Hotel> hotelJoin = tourHotelJoin.join("hotel");
            Join<Hotel, Room> roomJoin = hotelJoin.join("rooms");
            return cb.equal(roomJoin.get("bath"), hasBath);
        };
    }

    public static Specification<Tour> hasTerrace(Boolean hasTerrace) {
        return (root, query, cb) -> {
            if (hasTerrace == null) return null;
            Join<Tour, TourHotel> tourHotelJoin = root.join("hotels");
            Join<TourHotel, Hotel> hotelJoin = tourHotelJoin.join("hotel");
            Join<Hotel, Room> roomJoin = hotelJoin.join("rooms");
            return cb.equal(roomJoin.get("terrace"), hasTerrace);
        };
    }

    public static Specification<Tour> withRoomCostBetween(BigDecimal minCost, BigDecimal maxCost) {
        return (root, query, cb) -> {
            if (minCost == null && maxCost == null) return null;
            Join<Tour, TourHotel> tourHotelJoin = root.join("hotels");
            Join<TourHotel, Hotel> hotelJoin = tourHotelJoin.join("hotel");
            Join<Hotel, Room> roomJoin = hotelJoin.join("rooms");

            List<Predicate> predicates = new ArrayList<>();
            if (minCost != null) {
                predicates.add(cb.greaterThanOrEqualTo(roomJoin.get("cost"), minCost));
            }
            if (maxCost != null) {
                predicates.add(cb.lessThanOrEqualTo(roomJoin.get("cost"), maxCost));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
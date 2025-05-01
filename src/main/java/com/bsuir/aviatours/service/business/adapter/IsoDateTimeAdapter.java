package com.bsuir.aviatours.service.business.adapter;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class IsoDateTimeAdapter implements DateTimeAdapter {
    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final DateTimeFormatter HTML_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    private static final ZoneId ZONE_ID = ZoneId.systemDefault();

    @Override
    public Instant parse(String dateTime) {
        if (dateTime == null) return null;
        return LocalDateTime.parse(dateTime, ISO_FORMATTER)
                .atZone(ZONE_ID)
                .toInstant();
    }

    @Override
    public String format(Instant instant) {
        if (instant == null) return null;
        return ISO_FORMATTER.format(instant.atZone(ZONE_ID));
    }

    @Override
    public String toFrontendFormat(Instant instant) {
        if (instant == null) return null;
        return HTML_FORMATTER.format(instant.atZone(ZONE_ID));
    }

    @Override
    public Instant fromFrontendFormat(String dateTime) {
        if (dateTime == null) return null;
        String normalized = dateTime.length() == 16 ? dateTime + ":00" : dateTime;
        return parse(normalized);
    }
}
package com.bsuir.aviatours.service.business.adapter;

import java.time.Instant;

public interface DateTimeAdapter {
    Instant parse(String dateTime);
    String format(Instant instant);
    String toFrontendFormat(Instant instant);
    Instant fromFrontendFormat(String dateTime);
}
package com.example.payment.util;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class OrderUtil {
    public static String makeOrderNumber(final String sequence) {
        return calculateDateFormat(0L, "yyMMddHHmmss") + sequence;
    }

    public static String calculateDateFormat(final long interval, final String format) {
        LocalDateTime dateTime = null;
        if (interval > 0) {
            dateTime = LocalDateTime.now().plusDays(interval);
        } else if (interval < 0) {
            dateTime = LocalDateTime.now().minusDays(Math.abs(interval));
        } else {
            dateTime = LocalDateTime.now();
        }

        return dateTime.format(DateTimeFormatter.ofPattern(format));
    }

    public static final synchronized String getyyyyMMddHHmmss(){
        SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss");
        return yyyyMMddHHmmss.format(new Date());
    }
}

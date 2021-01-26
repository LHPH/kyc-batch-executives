package com.kyc.batchs.util;

import javax.swing.text.DateFormatter;
import java.text.DateFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtil {

    public static Date parseStringToDate(String date, String format) {
        LocalDate ld = LocalDate.parse(date, DateTimeFormatter.ofPattern(format));
        return Date.from(ld.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

}

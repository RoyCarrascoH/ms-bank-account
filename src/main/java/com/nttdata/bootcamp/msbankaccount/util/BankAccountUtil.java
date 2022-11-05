package com.nttdata.bootcamp.msbankaccount.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import lombok.AllArgsConstructor;

/**
 * Class BankAccountUtil.
 * BankAccount microservice class BankAccountUtil.
 */
@AllArgsConstructor
@Component
public class BankAccountUtil {

    public static final String CLIENT_CB = "clientCB";
    public static final String CREDIT_CB = "creditCB";
    public static final String DEBITCARD_CB = "debitcardCB";
    public static final String MOVEMENT_CB = "movementCB";

    public LocalDate dateToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public Date localDateToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public Date cutTimeFromDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}

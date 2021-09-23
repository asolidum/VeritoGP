package com.alansolidum.veritogp;

import com.google.type.Date;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

public class VeritoDateTest
{
    VeritoDate vd;

    @Before
    public void setUp() {
        vd = new VeritoDate();
    }

    @Test
    public void testConvertLocalDateToGoogleDate() {
        LocalDate localDate = LocalDate.now();
        Date googleDate = Date.newBuilder()
                .setDay(localDate.getDayOfMonth())
                .setMonth(localDate.getMonthValue())
                .setYear(localDate.getYear())
                .build();

        Assert.assertEquals(googleDate, vd.convertLocalDateToGoogleDate(localDate));
    }
}

package com.alansolidum.veritogp;

import com.google.type.Date;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

public class VeritoDateTest {
    VeritoDate vd;

    @Before
    public void setUp() {
        vd = new VeritoDate();
        vd.setOffsetDays(0);
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

    @Test
    public void testInitDateCorrect() {
        Assert.assertEquals(vd.getStartDate(), vd.convertLocalDateToGoogleDate(LocalDate.MAX));
        Assert.assertEquals(vd.getEndDate(), vd.convertLocalDateToGoogleDate(LocalDate.MIN));
    }

    @Test
    public void testDetermineFileDateRanges() {
        LocalDate now = LocalDate.now();
        Date temp = vd.convertLocalDateToGoogleDate(now);
        Date current = temp.toBuilder().setDay(15).build();
        Date earliest = temp.toBuilder().setDay(14).build();
        Date latest = temp.toBuilder().setDay(16).build();

        vd.determineDateRanges(vd.convertGoogleDateToLocalDate(current));
        Assert.assertEquals(vd.getStartDate(), current);
        Assert.assertEquals(vd.getEndDate(), current);

        vd.determineDateRanges(vd.convertGoogleDateToLocalDate(earliest));
        Assert.assertEquals(vd.getStartDate(), earliest);
        Assert.assertEquals(vd.getEndDate(), current);

        vd.determineDateRanges(vd.convertGoogleDateToLocalDate(latest));
        Assert.assertEquals(vd.getStartDate(), earliest);
        Assert.assertEquals(vd.getEndDate(), latest);
    }

    @Test
    public void testDetermineFileDateRangesWithOffset() {
        LocalDate now = LocalDate.now();
        Date temp = vd.convertLocalDateToGoogleDate(now);
        Date current = temp.toBuilder().setDay(15).build();
        Date earliest = temp.toBuilder().setDay(14).build();
        Date earliestMinusOne = temp.toBuilder().setDay(13).build();
        Date latest = temp.toBuilder().setDay(16).build();
        Date latestPlusOne = temp.toBuilder().setDay(17).build();

        vd.setOffsetDays(1);

        vd.determineDateRanges(vd.convertGoogleDateToLocalDate(current));
        Assert.assertEquals(vd.getStartDate(), earliest);
        Assert.assertEquals(vd.getEndDate(), latest);

        vd.determineDateRanges(vd.convertGoogleDateToLocalDate(earliest));
        Assert.assertEquals(vd.getStartDate(), earliestMinusOne);
        Assert.assertEquals(vd.getEndDate(), latest);

        vd.determineDateRanges(vd.convertGoogleDateToLocalDate(latest));
        Assert.assertEquals(vd.getStartDate(), earliestMinusOne);
        Assert.assertEquals(vd.getEndDate(), latestPlusOne);
    }

}

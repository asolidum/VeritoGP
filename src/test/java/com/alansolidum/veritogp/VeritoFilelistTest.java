package com.alansolidum.veritogp;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import com.google.type.Date;

public class VeritoFilelistTest {
    VeritoFilelist fl;

    @Before
    public void setUp() {
        fl = new VeritoFilelist();
    }

    @Test
    public void VeritoTestFilenameExtensions() {
        // Check valid extensions
        Assert.assertTrue(fl.isValidExtension("test.jpg"));
        Assert.assertTrue(fl.isValidExtension("test.JPG"));

        // Check invalid extensions
        Assert.assertFalse(fl.isValidExtension("test.java"));
    }

    @Test
    public void VeritoTestConvertLocalDateToGoogleDate() {
        LocalDate localDate = LocalDate.now();
        Date googleDate = Date.newBuilder()
                .setDay(localDate.getDayOfMonth())
                .setMonth(localDate.getMonthValue())
                .setYear(localDate.getYear())
                .build();

        Date tempDate = fl.convertLocalDateToGoogleDate(localDate);

        Assert.assertEquals(tempDate, googleDate);
    }
}

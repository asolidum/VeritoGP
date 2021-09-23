package com.alansolidum.veritogp;

import com.google.photos.library.v1.proto.DateFilter;
import com.google.photos.library.v1.proto.Filters;
import com.google.type.Date;
import org.junit.Assert;
import org.junit.Test;

public class VeritoAPITest {
    @Test
    public void testValidateDateRangeFilter() {
        Date startDate = Date.newBuilder()
                .setMonth(9)
                .setDay(21)
                .setYear(2021)
                .build();
        Date endDate = Date.newBuilder()
                .setMonth(10)
                .setDay(28)
                .setYear(2022)
                .build();
        Filters filters = VeritoAPI.setupDateRangeFilters(startDate, endDate);

        DateFilter dateFilter = filters.getDateFilter();
        Assert.assertEquals(1, dateFilter.getRangesCount());
        Assert.assertEquals(startDate, dateFilter.getRangesList().get(0).getStartDate());
        Assert.assertEquals(endDate, dateFilter.getRangesList().get(0).getEndDate());
    }
}

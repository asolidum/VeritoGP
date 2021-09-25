package com.alansolidum.veritogp;

import com.google.type.Date;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class VeritoDate {
    private LocalDate startDate = LocalDate.MAX;
    private LocalDate endDate = LocalDate.MIN;

    private LocalDate convertFileDateToLocalDate(File file) throws IOException {
        BasicFileAttributes attr = Files.readAttributes(Paths.get(file.getAbsolutePath()),
                BasicFileAttributes.class);

        return LocalDate.ofInstant(attr.creationTime().toInstant(), ZoneId.systemDefault().normalized());
    }

    // This code will convert the LocalDate type used in this class to the type
    // needed in the Google Photos API filter date range
    public Date convertLocalDateToGoogleDate(LocalDate date) {
        return Date.newBuilder()
                .setDay(date.getDayOfMonth())
                .setMonth(date.getMonthValue())
                .setYear(date.getYear())
                .build();
    }

    public LocalDate convertGoogleDateToLocalDate(Date date) {
        return LocalDate.of(date.getYear(), date.getMonth(), date.getDay());
    }

    public Date getStartDate() {
        return convertLocalDateToGoogleDate(startDate);
    }

    public Date getEndDate() {
        return convertLocalDateToGoogleDate(endDate);
    }

    public String getDateString(LocalDate date) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ISO_LOCAL_DATE;
        return date.format(dateFormat);
    }

    public String getStartDateString() { return getDateString(startDate); }

    public String getEndDateString() { return getDateString(endDate); }

    public void determineDateRanges(LocalDate fileDate) {
        // LocalDate supports date comparisons so best to use this date format
        if (fileDate.compareTo(startDate) < 0)
            startDate = fileDate;
        if (fileDate.compareTo(endDate) > 0)
            endDate = fileDate;
    }

    public void determineDateRangesFromFile(File file) throws IOException {
        LocalDate fileDate = convertFileDateToLocalDate(file);
        determineDateRanges(fileDate);
    }
}

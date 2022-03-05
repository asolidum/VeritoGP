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

/** Class responsible for date related operations
 * @author Alan Solidum
 * @author hi@alansolidum.com
 * @author https://alansolidum.com
 */
public class VeritoDate {
    public static long offsetDays = 0;

    // Local variables to store the earliest and latest file creation dates
    private LocalDate startDate = LocalDate.MAX;
    private LocalDate endDate = LocalDate.MIN;

    private LocalDate convertFileDateToLocalDate(File file) throws IOException {
        BasicFileAttributes attr = Files.readAttributes(Paths.get(file.getAbsolutePath()),
                BasicFileAttributes.class);

        return LocalDate.ofInstant(attr.creationTime().toInstant(), ZoneId.systemDefault().normalized());
    }

    /** This function converts the LocalDate type used in this class
     * to the type used in the Google Photos API filter date range
     * @param date Local date to be converted to Google date type
     * @return date converted to Google date type
     */
    public Date convertLocalDateToGoogleDate(LocalDate date) {
        return Date.newBuilder()
                .setDay(date.getDayOfMonth())
                .setMonth(date.getMonthValue())
                .setYear(date.getYear())
                .build();
    }

    /** This function is the inverse of the convertLocalDateToGoogleDate
     * function. It converts the Google date type to Local date type
     * @param date Google date type to be converted to Local date type
     * @return date converted to Local date type
     */
    public LocalDate convertGoogleDateToLocalDate(Date date) {
        return LocalDate.of(date.getYear(), date.getMonth(), date.getDay());
    }

    public void setOffsetDays(long numOffsetDays) { offsetDays = numOffsetDays; }

    public Date getStartDate() {
        return convertLocalDateToGoogleDate(startDate.minusDays(offsetDays));
    }

    public Date getEndDate() {
        return convertLocalDateToGoogleDate(endDate.plusDays(offsetDays));
    }

    public String getDateString(LocalDate date) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ISO_LOCAL_DATE;
        return date.format(dateFormat);
    }

    public String getStartDateString() {
        return getDateString(startDate.minusDays(offsetDays));
    }

    public String getEndDateString() {
        return getDateString(endDate.plusDays(offsetDays));
    }

    /** This function will set the earliest and latest file creation date
     * from the fileDate variable
     *
     * @param fileDate file creation date used to determine earliest and
     *                 latest date range
     */
    public void determineDateRanges(LocalDate fileDate) {
        // LocalDate supports date comparisons so best to use this date format
        if (fileDate.compareTo(startDate) < 0)
            startDate = fileDate;
        if (fileDate.compareTo(endDate) > 0)
            endDate = fileDate;
    }

    /** This function will set the earliest and latest file creation date
     * from the specified file
     * @param file File object the will be used to determine earliest and
     *             latest date range
     * @throws IOException
     */
    public void determineDateRangesFromFile(File file) throws IOException {
        LocalDate fileDate = convertFileDateToLocalDate(file);
        determineDateRanges(fileDate);
    }
}

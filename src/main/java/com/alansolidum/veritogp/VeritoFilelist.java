package com.alansolidum.veritogp;

import com.google.type.Date;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;

public class VeritoFilelist {
    private LocalDate startDate = LocalDate.MAX;
    private LocalDate endDate = LocalDate.MIN;

    public boolean isValidExtension(String filename) {
        String fmtFilename = filename.toLowerCase();

        // Valid extension list
        // If file extension is valid return true, else false
        return fmtFilename.endsWith(".mp4") ||
                fmtFilename.endsWith(".mov") ||
                fmtFilename.endsWith(".jpg") ||
                fmtFilename.endsWith(".png") ||
                fmtFilename.endsWith(".dng");
    }

    private LocalDate convertFileDateToLocalDate(File file) throws IOException {
        BasicFileAttributes attr = Files.readAttributes(Paths.get(file.getAbsolutePath()),
                BasicFileAttributes.class);

        return LocalDate.ofInstant(attr.creationTime().toInstant(), ZoneId.systemDefault().normalized());
    }

    // This code will convert the LocalDate type used in this class to the type
    // needed in the Google Photos API filter date range
    public Date convertLocalDateToGoogleDate (LocalDate date) {
        return Date.newBuilder()
                .setDay(date.getDayOfMonth())
                .setMonth(date.getMonthValue())
                .setYear(date.getYear())
                .build();
    }

    public Date getStartDate() {
        return convertLocalDateToGoogleDate(startDate);
    }

    public Date getEndDate() {
        return convertLocalDateToGoogleDate(endDate);
    }

    public Set<String> getAllFilesInFolder(File folder) throws IOException {
        Set<String> files = new HashSet<>();
        FileFilter extFilter = new FileFilter() {
            // Override accept method
            public boolean accept(File file) {
                if (file.isDirectory()) {
                    return true;
                }
                return isValidExtension(file.getName());
            }
        };

        for (File file : folder.listFiles(extFilter)) {
            if (!file.isDirectory()) {
                files.add(file.getName());
                LocalDate fileDate = convertFileDateToLocalDate(file);
                if (fileDate.compareTo(startDate) < 0)
                    startDate = fileDate;
                if (fileDate.compareTo(endDate) > 0)
                    endDate = fileDate;
            } else {
                // Recurse through nested folders
                files.addAll(getAllFilesInFolder(file));
            }
        }
        return files;
    }
}

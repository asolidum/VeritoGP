package com.alansolidum.veritogp;

import com.google.type.Date;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class VeritoFilelist {
    VeritoDate veritoDate = new VeritoDate();

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

    public Date getStartDate() { return veritoDate.getStartDate(); }
    public Date getEndDate() { return veritoDate.getEndDate(); }
    public String getStartDateString() { return veritoDate.getStartDateString(); }
    public String getEndDateString() { return veritoDate.getEndDateString(); }

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
                veritoDate.determineFileDateRanges(file);
            } else {
                // Recurse through nested folders
                files.addAll(getAllFilesInFolder(file));
            }
        }
        return files;
    }
}

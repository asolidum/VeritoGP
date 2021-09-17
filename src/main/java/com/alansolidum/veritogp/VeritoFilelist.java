package com.alansolidum.veritogp;

import java.io.File;
import java.io.FileFilter;

public class VeritoFilelist {
    public boolean isValidExtension(String filename) {
        String fmtFilename = filename.toLowerCase();

        // Valid extension list
        // If file extension is valid return true, else false
        return fmtFilename.endsWith(".mp4") ||
                fmtFilename.endsWith(".jpg") ||
                fmtFilename.endsWith(".png") ||
                fmtFilename.endsWith(".dng");
    }

    public void getAllFilesInFolder(File folder) {
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
                System.out.println(file.getName());
            } else {
                // Recurse through nested folders
                getAllFilesInFolder(file);
            }
        }
    }
}

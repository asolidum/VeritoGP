package com.alansolidum.veritogp;

import java.io.File;
import java.io.FileFilter;
import java.util.HashSet;
import java.util.Set;

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

    public Set<String> getAllFilesInFolder(File folder) {
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
            } else {
                // Recurse through nested folders
                files.addAll(getAllFilesInFolder(file));
            }
        }
        return files;
    }
}

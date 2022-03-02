package com.alansolidum.veritogp;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import java.security.GeneralSecurityException;
import io.grpc.StatusRuntimeException;

public class VeritoGP {
    static Logger logger = LoggerFactory.getLogger(VeritoGP.class.getName());

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";

    public static void main(String[] args) {
        PropertyConfigurator.configure("./log4j.properties");

        // Default path is current directory unless otherwise specified in command line arg
        String path = "./";
        if (args.length > 0)
            path = args[0];
        try {
            VeritoFilelist fl = new VeritoFilelist();

            File folder = new File(path);
            logger.info("Compiling filelist in folder: {}", path);

            Set<String> dirFilelist = fl.getAllFilesInFolder(folder);
            int numFiles = dirFilelist.size();
            if (numFiles == 0) {
                logger.info("{}0 local files found (Exiting...){}", ANSI_RED, ANSI_RESET);
                System.exit(0);
            }
            logger.info("{} local file(s) found ({} to {})",
                    numFiles,
                    fl.getStartDateString(),
                    fl.getEndDateString());

            logger.info("Checking Google Photos (GP) servers");
            Set<String> apiPhotoList = VeritoAPI
                    .getPhotolistByRange(fl.getStartDate(), fl.getEndDate());

            logger.info("{} remote file(s) found in GP", apiPhotoList.size());

            // Remove overlapping filenames in dirFileList and apiPhotoList
            dirFilelist.removeAll(apiPhotoList);

            String textColor = ANSI_RED;
            if (dirFilelist.size() == 0)
                textColor = ANSI_GREEN;
            logger.info("{}{} UNMATCHED local file(s) ({}){}", textColor, dirFilelist.size(), dirFilelist, ANSI_RESET);
        } catch (StatusRuntimeException e) {
            logger.info("HTTP {} - {}", e.getMessage(), e.getLocalizedMessage());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
    }
}

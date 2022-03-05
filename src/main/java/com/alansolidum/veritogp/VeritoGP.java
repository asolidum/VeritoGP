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

    static final String ANSI_RESET = "\u001B[0m";
    static final String ANSI_RED = "\u001B[31m";
    static final String ANSI_GREEN = "\u001B[32m";

    static final int STATUS_NORMAL = 0;
    static final int STATUS_ERROR = 1;

    static void outputErrorMessage(String message) {
        outputStatusMessage(message, STATUS_ERROR);
    }

    static void outputStatusMessage(String message, int status) {
        String textColor = ANSI_RED;

        if (status == STATUS_NORMAL)
            textColor = ANSI_GREEN;

        logger.info("{}{}{}", textColor, message, ANSI_RESET);
    }

    public static void main(String[] args) {
        PropertyConfigurator.configure("./log4j.properties");

        // Default path is current directory unless otherwise specified in command line arg
        String path = "./";
        if (args.length > 0)
            path = args[0];
        try {
            VeritoFilelist fl = new VeritoFilelist();
            fl.setOffsetDays(1);

            File folder = new File(path);
            logger.info("Compiling filelist in folder: {}", path);

            Set<String> dirFilelist = fl.getAllFilesInFolder(folder);
            int numFiles = dirFilelist.size();
            if (numFiles == 0) {
                outputErrorMessage("0 local files found (Exiting...)");
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

            numFiles = dirFilelist.size();

            outputStatusMessage(
                    String.format("%s UNMATCHED local file(s) (%s)", numFiles, dirFilelist),
                    numFiles);
        } catch (StatusRuntimeException e) {
            logger.info("HTTP {} - {}", e.getMessage(), e.getLocalizedMessage());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
    }
}

package com.alansolidum.veritogp;

import java.io.File;
import java.io.IOException;

public class VeritoGP {
    public static void main(String[] args) {
        try {
            File folder = new File("./");
            VeritoFilelist fl = new VeritoFilelist();
            fl.getAllFilesInFolder(folder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

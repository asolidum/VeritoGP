package com.alansolidum.veritogp;

import java.io.File;

public class VeritoGP {
    public static void main(String[] args) {
        File folder = new File("./");
        VeritoFilelist fl = new VeritoFilelist();
        fl.getAllFilesInFolder(folder);
    }
}

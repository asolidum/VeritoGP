package com.alansolidum.veritogp;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class VeritoFilelistTest {
    VeritoFilelist fl;

    @Before
    public void setUp() {
        fl = new VeritoFilelist();
    }

    @Test
    public void testValidFilenames() {
        // Check skipping of hidden files (files that start with '.')
        Assert.assertFalse(fl.isValidFile(".test.java"));

        // Check valid extensions
        Assert.assertTrue(fl.isValidFile("test.jpg"));
        Assert.assertTrue(fl.isValidFile("test.JPG"));

        // Check invalid extensions
        Assert.assertFalse(fl.isValidFile("test.java"));
    }
}

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
    public void VeritoTestFilenameExtensions() {
        // Check valid extensions
        Assert.assertTrue(fl.isValidExtension("test.jpg"));
        Assert.assertTrue(fl.isValidExtension("test.JPG"));

        // Check invalid extensions
        Assert.assertFalse(fl.isValidExtension("test.java"));

    }
}

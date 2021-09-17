import com.alansolidum.veritogp.VeritoFilelist;
import org.junit.Assert;
import org.junit.Test;

public class VeritoFilelistTest {
    @Test
    public void VeritoTestFilenameExtensions() {
        VeritoFilelist fl = new VeritoFilelist();

        // Check valid extensions
        Assert.assertTrue(fl.isValidExtension("test.jpg"));
        Assert.assertTrue(fl.isValidExtension("test.JPG"));

        // Check invalid extensions
        Assert.assertFalse(fl.isValidExtension("test.java"));

    }
}

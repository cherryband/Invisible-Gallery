package neue.project.invisiblegallery.test;

import org.junit.Test;

import neue.project.invisiblegallery.util.Util;

import static org.junit.Assert.*;

public class UtilTest {
    @Test
    public void extensionRemovalTest(){
        assertEquals(Util.removeExtension("asdf.png.jpg"), "asdf.png");
        assertEquals(Util.removeExtension("qrdaf.jpeg"), "qrdaf");
        assertEquals(Util.removeExtension("z781iuvjmdxo9.heif"), "z781iuvjmdxo9");
        assertEquals(Util.removeExtension("Hello world.gif"), "Hello world");
    }
}

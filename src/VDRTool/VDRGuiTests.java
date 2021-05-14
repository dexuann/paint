package VDRTool;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.awt.*;

public class VDRGuiTests {

    VDRGui vdrgui;
    Canvas canvas;

    @BeforeEach
    public void setVDRgui() {
        vdrgui = null;
    }


    @Test
    public void testConstruction() {
        vdrgui = new VDRGui("Test");
    }

    @Test
    public void testGetCoordinate1() {
        vdrgui = new VDRGui("Test");
        assertEquals(vdrgui.canvas.getCoordinate("0.110000"), 77 );
    }

    @Test
    public void testGetCoordinate2() {
        vdrgui = new VDRGui("Test");
        assertEquals(vdrgui.canvas.getCoordinate("0.051000"), 36 );
    }

    @Test
    public void testGetCoordinate3() {
        vdrgui = new VDRGui("Test");
        assertEquals(vdrgui.canvas.getCoordinate("1.110000"), 777 );
    }

    @Test
    public void testConvertCoordinate1() {
        vdrgui = new VDRGui("Test");
        assertEquals(vdrgui.canvas.convertCoordinate(222), "0.317143" );
    }

    @Test
    public void testConvertCoordinate2() {
        vdrgui = new VDRGui("Test");
        assertEquals(vdrgui.canvas.convertCoordinate(765), "1.092857" );
    }

    @Test
    public void testConvertCoordinate3() {
        vdrgui = new VDRGui("Test");
        assertEquals(vdrgui.canvas.convertCoordinate(17), "0.024286" );
    }

    @Test
    public void testGetHex1() {
        vdrgui = new VDRGui("Test");
        assertEquals(vdrgui.canvas.getHex(Color.WHITE), "#ffffff");
    }

    @Test
    public void testGetHex2() {
        vdrgui = new VDRGui("Test");
        assertEquals(vdrgui.canvas.getHex(Color.BLACK), "#000000");
    }

    @Test
    public void testGetHex3() {
        vdrgui = new VDRGui("Test");
        assertEquals(vdrgui.canvas.getHex(Color.PINK), "#ffafaf");
    }
}

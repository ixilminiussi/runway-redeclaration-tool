/**
 * Test class for import XML function
 */

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;

public class testImportXML {

    @Test (expected = IllegalArgumentException.class)
    public void testNegValue() throws Exception{
        importXML testing = new importXML("src/test_xml/testRunwayNegValue.xml");

        ArrayList<Runway> test = testing.importRunwaysFromXML();
        Runway runway = test.get(0);
        assertEquals(1, test.size());
    }

    @Test
    public void testRunway27R() {
        try {
            importXML testing = new importXML("src/test_xml/test27R.xml");
            ArrayList<Runway> test = testing.importRunwaysFromXML();
            Runway runway = test.get(0);
            assertEquals(1, test.size());
            assertEquals("27R", runway.getName());
            assertEquals(3884, runway.getTORA());
            assertEquals(3962, runway.getTODA());
        } catch (Exception e) {
            fail("File could not be read");
        }
    }


    @Test
    public void testBlankXML() throws Exception{
        importXML testing = new importXML("src/test_xml/testRunwayBlanks.xml");
        try {
            ArrayList<Runway> runwaysTest = testing.importRunwaysFromXML();
            fail("Exception not thrown");
        } catch (NumberFormatException e){
            assertEquals("For input string: \"\"", e.getMessage());
        }
    }

    @Test
    public void testImportingFourRunways() throws Exception{
        importXML testing = new importXML("src/test_xml/heathrowTestData.xml");
        ArrayList<Runway> test = testing.importRunwaysFromXML();
        assertEquals(4, test.size());
    }
}

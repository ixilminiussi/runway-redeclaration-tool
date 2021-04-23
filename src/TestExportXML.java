import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class TestExportXML {

	@Test
    public void testScenarioObstructionsExport() {
        try {       	
        	exportXML testXML = new exportXML();
        	
        	ArrayList<Obstruction> obstructions = new ArrayList<Obstruction>();
        	
        	Obstruction obs1 = new Obstruction("One", 12, 0, -50, 0);
        	Obstruction obs2 = new Obstruction("Two", 12, 0, 3646, 0);
        	Obstruction obs3 = new Obstruction("Three", 25, 0, 2853, -20);
        	Obstruction obs4 = new Obstruction("Four", 25, 0, 500, -20);
        	Obstruction obs5 = new Obstruction("Five", 15, 0, 150, 60);
        	Obstruction obs6 = new Obstruction("Six", 15, 0, 3203, 60);
        	Obstruction obs7 = new Obstruction("Seven", 20, 0, 3546, 20);
        	Obstruction obs8 = new Obstruction("Eight", 20, 0, 50, 20);
        	
        	obstructions.add(obs1);
        	obstructions.add(obs2);
        	obstructions.add(obs3);
        	obstructions.add(obs4);
        	obstructions.add(obs5);
        	obstructions.add(obs6);
        	obstructions.add(obs7);
        	obstructions.add(obs8);
        	
        	String scenarioObstructions = "src/test_xml/scenarioObstructionsTemp.xml";
        	
        	testXML.exportObstructionsToXML(scenarioObstructions, obstructions);
        	
        	File file = new File("src/test_xml/scenarioObstructionsTemp.xml");
        	
        	assertTrue(file.exists());
        	
            importXML testFile = new importXML("src/test_xml/scenarioObstructionsTemp.xml");

            ArrayList<Obstruction> testSavedFile = testFile.importObstructionsFromXML();
            
            
        	for (int i = 0; i < testSavedFile.size(); i++) {
                assertEquals(obstructions.get(i).getName(), testSavedFile.get(i).getName());
                assertEquals(obstructions.get(i).getHeight(), testSavedFile.get(i).getHeight());
                assertEquals(obstructions.get(i).getLength(), testSavedFile.get(i).getLength());
                assertEquals(obstructions.get(i).getDistanceFromCentre(), testSavedFile.get(i).getDistanceFromCentre());
                assertEquals(obstructions.get(i).getDistanceFromThreshold(), testSavedFile.get(i).getDistanceFromThreshold());
        	}
           
        	file.delete();
        } catch (Exception e) {
            fail("Error exporting obstruction: " + e.getMessage());
        }
    }
	
	@Test
    public void testScenarioRunwaysExport() {
        try {       	  	
        	exportXML testXML = new exportXML();
        	
        	ArrayList<Runway> runways = new ArrayList<Runway>();
        	
        	Runway x09R = new Runway("09R", "Heathrow", 3660, 3660, 3660, 3353, 307, 60, 50, 240, 300, 0, 0);
        	Runway x27L = new Runway("27L", "Heathrow", 3660, 3660, 3660, 3660, 0, 60, 50, 240, 300, 0, 0);
        	Runway x09L = new Runway("09L", "Heathrow", 3902, 3902, 3902, 3595, 306, 60, 50, 240, 300, 0, 0);
        	Runway x27R = new Runway("27R", "Heathrow", 3884, 3962, 3884, 3884, 0, 60, 50, 240, 300, 0, 78);

            x09R.setSlopeRatio(x09R.getEGR());
            x27L.setSlopeRatio(x27L.getEGR());
            x09L.setSlopeRatio(x09L.getEGR());
            x27R.setSlopeRatio(x27R.getEGR());
            
            runways.add(x09R);
            runways.add(x27L);
            runways.add(x09L);
            runways.add(x27R);
        	
        	String scenarioRunways = "src/test_xml/scenarioRunwaysTemp.xml";
        	
        	testXML.exportRunwaysToXML(scenarioRunways, runways);
        	
        	File file = new File("src/test_xml/scenarioRunwaysTemp.xml");
        	
        	assertTrue(file.exists());
        	
            importXML testFile = new importXML("src/test_xml/scenarioRunwaysTemp.xml");

            ArrayList<Runway> testSavedFile = testFile.importRunwaysFromXML();

        	for (int i = 0; i < testSavedFile.size(); i++) {
                assertEquals(runways.get(i).getName(), testSavedFile.get(i).getName());
                assertEquals(runways.get(i).getAirport(), testSavedFile.get(i).getAirport());
                assertEquals(runways.get(i).getTORA(), testSavedFile.get(i).getTORA());
                assertEquals(runways.get(i).getTODA(), testSavedFile.get(i).getTODA());
                assertEquals(runways.get(i).getASDA(), testSavedFile.get(i).getASDA());
                assertEquals(runways.get(i).getLDA(), testSavedFile.get(i).getLDA());
                assertEquals(runways.get(i).getDisplacedThreshold(), testSavedFile.get(i).getDisplacedThreshold());
                assertEquals(runways.get(i).getStripEnd(), testSavedFile.get(i).getStripEnd());
                assertEquals(runways.get(i).getEGR(), testSavedFile.get(i).getEGR());
                assertEquals(runways.get(i).getRESA(), testSavedFile.get(i).getRESA());
                assertEquals(runways.get(i).getName(), testSavedFile.get(i).getName());
                assertEquals(runways.get(i).getBlastAllowance(), testSavedFile.get(i).getBlastAllowance());
                assertEquals(runways.get(i).getStopway(), testSavedFile.get(i).getStopway());
                assertEquals(runways.get(i).getClearway(), testSavedFile.get(i).getClearway());
        	} 
           
        	file.delete();
        } catch (Exception e) {
            fail("Error exporting runway: " + e.getMessage());
        }
    }
	
	@Test
	public void testExportBoth() {
		try {
			exportXML testXML = new exportXML();
			
			ArrayList<Obstruction> obstructions = new ArrayList<Obstruction>();
    	
			Obstruction obs1 = new Obstruction("One", 12, 0, -50, 0);
			Obstruction obs2 = new Obstruction("Two", 12, 0, 3646, 0);
			Obstruction obs3 = new Obstruction("Three", 25, 0, 2853, -20);
			Obstruction obs4 = new Obstruction("Four", 25, 0, 500, -20);
			Obstruction obs5 = new Obstruction("Five", 15, 0, 150, 60);
			Obstruction obs6 = new Obstruction("Six", 15, 0, 3203, 60);
			Obstruction obs7 = new Obstruction("Seven", 20, 0, 3546, 20);
    		Obstruction obs8 = new Obstruction("Eight", 20, 0, 50, 20);
    	
    		obstructions.add(obs1);
    		obstructions.add(obs2);
    		obstructions.add(obs3);
    		obstructions.add(obs4);
    		obstructions.add(obs5);
    		obstructions.add(obs6);
    		obstructions.add(obs7);
    		obstructions.add(obs8);
    		
    		ArrayList<Runway> runways = new ArrayList<Runway>();
    		
    		Runway x09R = new Runway("09R", "Heathrow", 3660, 3660, 3660, 3353, 307, 60, 50, 240, 300, 0, 0);
    		Runway x27L = new Runway("27L", "Heathrow", 3660, 3660, 3660, 3660, 0, 60, 50, 240, 300, 0, 0);
    		Runway x09L = new Runway("09L", "Heathrow", 3902, 3902, 3902, 3595, 306, 60, 50, 240, 300, 0, 0);
    		Runway x27R = new Runway("27R", "Heathrow", 3884, 3962, 3884, 3884, 0, 60, 50, 240, 300, 0, 78);
    		
    		x09R.setSlopeRatio(x09R.getEGR());
    		x27L.setSlopeRatio(x27L.getEGR());
    		x09L.setSlopeRatio(x09L.getEGR());
    		x27R.setSlopeRatio(x27R.getEGR());
    		
    		runways.add(x09R);
    		runways.add(x27L);
    		runways.add(x09L);
        	runways.add(x27R);
           
        	String scenarioObstructions = "src/test_xml/scenarioTemp.xml";
    	
        	testXML.exportBothToXML("src/test_xml/scenarioTemp.xml", obstructions, runways);
    	
        	File file = new File("src/test_xml/scenarioTemp.xml");
    	
        	assertTrue(file.exists());
  	
        	file.delete();
        } catch (Exception e) {
            fail("Error exporting obstructions or runways: " + e.getMessage());
        }		
	}
}


import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

class TestExportImage {
	
	static ArrayList<Obstruction> obstructions = new ArrayList<Obstruction>();
	static ArrayList<Runway> runways = new ArrayList<Runway>();
	
	
	@BeforeAll
	public static void setup() {
    	Obstruction obs1 = new Obstruction("One", 12, 40, -50, 0);
    	Obstruction obs2 = new Obstruction("Two", 12, 40, 3646, 0);
    	Obstruction obs3 = new Obstruction("Three", 25, 40, 2853, -20);
    	Obstruction obs4 = new Obstruction("Four", 25, 40, 500, -20);
    	Obstruction obs5 = new Obstruction("Five", 15, 40, 150, 60);
    	Obstruction obs6 = new Obstruction("Six", 15, 40, 3203, 60);
    	Obstruction obs7 = new Obstruction("Seven", 20, 40, 3546, 20);
    	Obstruction obs8 = new Obstruction("Eight", 20, 40, 50, 20);
    	
    	obstructions.add(obs1);
    	obstructions.add(obs2);
    	obstructions.add(obs3);
    	obstructions.add(obs4);
    	obstructions.add(obs5);
    	obstructions.add(obs6);
    	obstructions.add(obs7);
    	obstructions.add(obs8);
		
		Runway x09R = new Runway("09R", "Heathrow", 3660, 3660, 3660, 3353, 307, 60, 50, 240, 300, 0, 0);
		Runway x27L = new Runway("27L", "Heathrow", 3660, 3660, 3660, 3660, 0, 60, 50, 240, 300, 0, 0);
		Runway x09L = new Runway("09L", "Heathrow", 3902, 3902, 3902, 3595, 306, 60, 50, 240, 300, 0, 0);
		Runway x27R = new Runway("27R", "Heathrow", 3884, 3962, 3884, 3884, 0, 60, 50, 240, 300, 0, 78);
				
		runways.add(x09R);
		runways.add(x27L);
		runways.add(x09L);
    	runways.add(x27R);
	}
	
	//Comment out or remove @AfterAll to keep files after test
	@AfterAll
	public static void deleteTempFiles() {
    	for (int i = 0; i < obstructions.size(); i++) {
    		File file1 = new File("src/test_image/scenarioTestTopView" + (i + 1) + "Temp.png");
    		file1.delete();
    		
    		File file2 = new File("src/test_image/scenarioTestTopView" + (i + 1) + "Temp.jpg");
    		file1.delete();
    		
    		File file3 = new File("src/test_image/scenarioTestTopView" + (i + 1) + "Temp.docx");
    		file1.delete();
    		
    		/**File file4 = new File("src/test_image/scenarioTestSideView" + (i + 1) + "Temp.png");
    		file2.delete();**/
    		
    		/**File file5 = new File("src/test_image/scenarioTestSideView" + (i + 1) + "Temp.jpg");
    		file2.delete();**/
    		
    		/**File file6 = new File("src/test_image/scenarioTestSideView" + (i + 1) + "Temp.docx");
    		file2.delete();**/
    	}
	}
	
    @Test
    public void testExportPNG() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                new JFXPanel();
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                    	
                    	class Test extends Application {
                    	Stage stage;
                    	RunwayGraphics runwayGraphics;
                    	Scene scene;
                    	    @Override
                    	    public void start(Stage stage) throws Exception {
                    	    	this.stage = stage;
                    	    }                    	  
                    	}
                    	
                    	Test test = new Test();
                    	try {
							test.start(new Stage());
						} catch (Exception e1) {
							e1.printStackTrace();
						}
                    	
                    	RunwayGraphics testImage = new RunwayGraphics(new Stage(), new Theme("dark"));
                        test.runwayGraphics = testImage;
            	    	test.scene = new Scene(test.runwayGraphics.getRunwayGraphics(), 1000, 1000);
            	    	test.stage.setScene(test.scene);
            
            	        //test.stage.show();
            	                                	
                        for (int i = 0; i < obstructions.size(); i++) {
                        		
                        	Runway runway = runways.get(0);
                        		
                        	switch(i) {
                        		case 3:
                        		case 5:
                        			runway = runways.get(0); //09R
                        			break;
                        		case 4:
                        		case 6:
                        			runway = runways.get(1); //27L
                        			break;
                        		case 1:
                        		case 7:
                        			runway = runways.get(2); //09L
                        			break;
                        		case 2:
                        		case 8:
                        			runway = runways.get(3); //27R
                        			break;
                        	}
                        	
                            test.runwayGraphics.draw(new AffectedRunway(runway, obstructions.get(i)));
                        	testImage.saveCanvasToPNG("src/test_image/scenarioTestTopView" + (i + 1) + "Temp.png");
                        	
                        	//test.runwayGraphics.currentView = test.runwayGraphics.sideViewCanvas;
                        	//testImage.saveCanvasToPNG("src/test_image/scenarioTestSideView" + (i + 1) + "Temp.png");
                        	
                        }
                        latch.countDown();	
                    }
                });
            }
        });
        
        thread.start();
        latch.await();
              
        try {
        	for (int i = 0; i < obstructions.size(); i++) {
        		File file1 = new File("src/test_image/scenarioTestTopView" + (i + 1) + "Temp.png");
        		assertTrue(file1.exists());
        		
        		//File file2 = new File("src/test_image/scenarioTestSideView" + (i + 1) + "Temp.png");
        		//assertTrue(file2.exists());
        	}
        } catch (Exception e) {
        	fail("Error exporting to PNG: " + e.getMessage());
        }
    }

    @Test
    public void testExportJPEG() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                new JFXPanel();
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                    	
                    	class Test extends Application {
                    	Stage stage;
                    	RunwayGraphics runwayGraphics;
                    	Scene scene;
                    	    @Override
                    	    public void start(Stage stage) throws Exception {
                    	    	this.stage = stage;
                    	    }                    	  
                    	}
                    	
                    	Test test = new Test();
                    	try {
							test.start(new Stage());
						} catch (Exception e1) {
							e1.printStackTrace();
						}
                    	
                    	RunwayGraphics testImage = new RunwayGraphics(new Stage(), new Theme("dark"));
                        test.runwayGraphics = testImage;
            	    	test.scene = new Scene(test.runwayGraphics.getRunwayGraphics(), 1000, 1000);
            	    	test.stage.setScene(test.scene);
            
            	        //test.stage.show();
            	                                	
                        for (int i = 0; i < obstructions.size(); i++) {
                        		
                        	Runway runway = runways.get(0);
                        		
                        	switch(i) {
                        		case 3:
                        		case 5:
                        			runway = runways.get(0); //09R
                        			break;
                        		case 4:
                        		case 6:
                        			runway = runways.get(1); //27L
                        			break;
                        		case 1:
                        		case 7:
                        			runway = runways.get(2); //09L
                        			break;
                        		case 2:
                        		case 8:
                        			runway = runways.get(3); //27R
                        			break;
                        	}
                        	
                            test.runwayGraphics.draw(new AffectedRunway(runway, obstructions.get(i)));
                        	//testImage.saveCanvasToJPEG("src/test_image/scenarioTestTopView" + (i + 1) + "Temp.jpg");
                        	
                        	//test.runwayGraphics.currentView = test.runwayGraphics.sideViewCanvas;
                        	//testImage.saveCanvasToJPEG("src/test_image/scenarioTestSideView" + (i + 1) + "Temp.jpg");
                        }
                        latch.countDown();	
                    }
                });
            }
        });
        
        thread.start();
        latch.await();
              
        try {
        	for (int i = 0; i < obstructions.size(); i++) {
        		File file1 = new File("src/test_image/scenarioTestTopView" + (i + 1) + "Temp.jpg");
        		assertTrue(file1.exists());
        		
        		//File file2 = new File("src/test_image/scenarioTestSideView" + (i + 1) + "Temp.jpg");
        		//assertTrue(file2.exists());
        	}
        } catch (Exception e) {
        	fail("Error exporting to JPEG: " + e.getMessage());
        }
    }
}

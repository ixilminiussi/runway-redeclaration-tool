import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.embed.swing.JFXPanel;
import javafx.scene.control.TextField;

class TestUserInput {
	
	@BeforeAll
	public static void setUp() {
    	JFXPanel fxPanel = new JFXPanel();
	}

    @Test
    public void validationTestsStrings() {
    	
    	ConfigPanel test = new ConfigPanel(new HistoryPanel());
    	
    	//Testing valid runway name
    	test.getRunwayStringTextFields().get(0).setText("01");
    	assertEquals(true, test.areFieldsValid());
    	
 	    //Testing empty string
    	test.getRunwayStringTextFields().get(0).setText("");
 	    assertEquals(false, test.areFieldsValid());
    	
    	//Testing maximum
    	test.getRunwayStringTextFields().get(0).setText("36");
    	assertEquals(true, test.areFieldsValid());
    	
    	//Testing above bound
    	test.getRunwayStringTextFields().get(0).setText("37");
    	assertEquals(false, test.areFieldsValid());
    	
    	//Set valid for testing airpot name
    	test.getRunwayStringTextFields().get(0).setText("01");
    	
    	//Testing valid airport name
    	test.getRunwayStringTextFields().get(1).setText("abcdef");;
    	assertEquals(true, test.areFieldsValid());
    		 
 	    //Testing empty string
    	test.getRunwayStringTextFields().get(1).setText("");
 	    assertEquals(false, test.areFieldsValid());
    		   		 	    	    	   	    	
    	//Testing maximum
 	    test.getRunwayStringTextFields().get(1).setText("684axpDWWa5zbmcueTDshXhBYgpNUYu");
    	assertEquals(true, test.areFieldsValid());
    	    	
    	//Testing above bound
    	test.getRunwayStringTextFields().get(1).setText("684axpDWWa5zbmcueTDshXhBYgpNUYui");
    	assertEquals(false, test.areFieldsValid());
    }
    
    //This test no longer applicable after changing validation to require TODA = TORA + CLEARYWAY and ASDA = TORA + STOPWAY
    /**
    @Test
    public void validationTestsPositiveIntFields() {
    	
    	ConfigPanel test = new ConfigPanel(new HistoryPanel());
    	
    	 for (TextField textField : test.getRunwayIntTextFields()) {

    	    	//Testing positive integer
    	    	textField.setText("1");
    	    	assertEquals(true, test.areFieldsValid());
    	    	
    	    	//Testing zero
    	    	textField.setText("0");
    	    	assertEquals(true, test.areFieldsValid());
    	    	
    	    	//Testing negative integer
    	    	textField.setText("-1");
    	    	assertEquals(false, test.areFieldsValid());
    	    	
    	    	//Testing maximum
    	    	textField.setText("999998");
    	    	assertEquals(true, test.areFieldsValid());
    	    	
    	    	//Testing above bound
    	    	textField.setText("999999");
    	    	assertEquals(false, test.areFieldsValid());
    	    	
    	    	//Testing non integer
    	    	textField.setText("1.24842149041");
    	    	assertEquals(false, test.areFieldsValid());
    	    	
    	    	textField.setText("1");
         }
    } **/
    
    
    @Test
    public void validationTestsHeight() throws Exception {
    	
    	ConfigPanel test = new ConfigPanel(new HistoryPanel());
    	
    	//Testing positive integer
    	test.getObstIntTextFields().get(0).setText("1");
    	assertEquals(true, test.areFieldsValid());
    	
    	//Testing zero
    	test.getObstIntTextFields().get(0).setText("0");
    	assertEquals(false, test.areFieldsValid());
    	
    	//Testing negative integer
    	test.getObstIntTextFields().get(0).setText("-1");
    	assertEquals(false, test.areFieldsValid());
    	
    	//Testing maximum
    	test.getObstIntTextFields().get(0).setText("999998");
    	assertEquals(true, test.areFieldsValid());
    	
    	//Testing above bound
    	test.getObstIntTextFields().get(0).setText("999999");
    	assertEquals(false, test.areFieldsValid());
    	
    	//Testing non integer
    	test.getObstIntTextFields().get(0).setText("1.24842149041");
    	assertEquals(false, test.areFieldsValid());
    	
    }
    	
    @Test
    public void validationTestsLength() throws Exception {
    	
    	ConfigPanel test = new ConfigPanel(new HistoryPanel());
    	
    	//Testing positive integer
    	test.getObstIntTextFields().get(1).setText("1");
    	assertEquals(true, test.areFieldsValid());
    	
    	//Testing zero
    	test.getObstIntTextFields().get(1).setText("0");
    	assertEquals(false, test.areFieldsValid());
    	
    	//Testing negative integer
    	test.getObstIntTextFields().get(1).setText("-1");
    	assertEquals(false, test.areFieldsValid());
    	
    	//Testing maximum
    	test.getObstIntTextFields().get(1).setText("999998");
    	assertEquals(true, test.areFieldsValid());
    	
    	//Testing above bound
    	test.getObstIntTextFields().get(1).setText("999999");
    	assertEquals(false, test.areFieldsValid());
    	
    	//Testing non integer
    	test.getObstIntTextFields().get(1).setText("1.24842149041");
    	assertEquals(false, test.areFieldsValid());
    	
    }
    
    @Test
    public void validationTestsThresholdDistance() throws Exception {
    	
    	ConfigPanel test = new ConfigPanel(new HistoryPanel());
    	
    	//Testing positive integer
    	test.getObstIntTextFields().get(2).setText("1");
    	assertEquals(true, test.areFieldsValid());
    	
    	//Testing zero
    	test.getObstIntTextFields().get(2).setText("0");
    	assertEquals(true, test.areFieldsValid());
    	
    	//Testing negative integer
    	test.getObstIntTextFields().get(2).setText("-1");
    	assertEquals(true, test.areFieldsValid());
    	
    	//Testing maximum
    	test.getObstIntTextFields().get(2).setText("999998");
    	assertEquals(true, test.areFieldsValid());
    	
    	//Testing above bound
    	test.getObstIntTextFields().get(2).setText("999999");
    	assertEquals(false, test.areFieldsValid());
    	
    	//Testing non integer
    	test.getObstIntTextFields().get(2).setText("1.24842149041");
    	assertEquals(false, test.areFieldsValid());
    	
    }
    	
    @Test
    public void validationTestsCentreDistance() throws Exception {
    	
    	ConfigPanel test = new ConfigPanel(new HistoryPanel());
    	
    	//Testing positive integer
    	test.getObstIntTextFields().get(3).setText("1");
    	assertEquals(true, test.areFieldsValid());
    	
    	//Testing zero
    	test.getObstIntTextFields().get(3).setText("0");
    	assertEquals(true, test.areFieldsValid());
    	
    	//Testing negative integer
    	test.getObstIntTextFields().get(3).setText("-1");
    	assertEquals(true, test.areFieldsValid());
    	
    	//Testing maximum
    	test.getObstIntTextFields().get(3).setText("999998");
    	assertEquals(true, test.areFieldsValid());
    	
    	//Testing above bound
    	test.getObstIntTextFields().get(3).setText("999999");
    	assertEquals(false, test.areFieldsValid());
    	
    	//Testing non integer
    	test.getObstIntTextFields().get(3).setText("1.24842149041");
    	assertEquals(false, test.areFieldsValid());
    	
    }
}

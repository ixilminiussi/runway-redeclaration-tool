import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class TestCalculations {

	private static Runway x09R = new Runway("09R", "Heathrow", 3660, 3660, 3660, 3353, 307, 60, 50, 240, 300, 0, 0);
	private static Runway x27L = new Runway("27L", "Heathrow", 3660, 3660, 3660, 3660, 0, 60, 50, 240, 300, 0, 0);
	private static Runway x09L = new Runway("09L", "Heathrow", 3902, 3902, 3902, 3595, 306, 60, 50, 240, 300, 0, 0);
	private static Runway x27R = new Runway("27R", "Heathrow", 3884, 3962, 3884, 3884, 0, 60, 50, 240, 300, 0, 78);
		
    @Test
    public void testScenario1() {
        try {        	
        	Obstruction obs1 = new Obstruction("One", 12, 0, -50, 0);
        	Obstruction obs2 = new Obstruction("Two", 12, 0, 3646, 0);
        	
        	AffectedRunway test1 = new AffectedRunway(x09L, obs1);
        	AffectedRunway test2 = new AffectedRunway(x27R, obs2);
        	
        	//Testing 09L (Take off Away, Landing Over)
            assertEquals(3346, test1.getTORA());
            assertEquals(3346, test1.getASDA());
            assertEquals(3346, test1.getTODA());
            assertEquals(2985, test1.getLDA());
                   
            //Testing 27R (Take off Towards, Landing Towards)
            assertEquals(2986, test2.getTORA());
            assertEquals(2986, test2.getASDA());
            assertEquals(2986, test2.getTODA());
            assertEquals(3346, test2.getLDA());
        } catch (Exception e) {
            fail("Error creating obstruction or runway: " + e.getMessage());
        }
    }

    @Test
    public void testScenario2() {
        try {        	
        	Obstruction obs3 = new Obstruction("Three", 25, 0, 2853, -20);
        	Obstruction obs4 = new Obstruction("Four", 25, 0, 500, -20);
        	
        	AffectedRunway test3 = new AffectedRunway(x09R, obs3);
        	AffectedRunway test4 = new AffectedRunway(x27L, obs4);
       	
        	//Testing 09R (Take off Towards, Landing Towards)
            assertEquals(1850, test3.getTORA());
            assertEquals(1850, test3.getASDA());
            assertEquals(1850, test3.getTODA());
            assertEquals(2553, test3.getLDA());
                   
            //Testing 27L (Take off Away, Landing Over)
            assertEquals(2860, test4.getTORA());
            assertEquals(2860, test4.getASDA());
            assertEquals(2860, test4.getTODA());
            assertEquals(1850, test4.getLDA());
        } catch (Exception e) {
            fail("Error creating obstruction or runway: " + e.getMessage());
        }
    }
    
    @Test
    public void testScenario3() {
        try {        	
        	Obstruction obs5 = new Obstruction("Five", 15, 0, 150, 60);
        	Obstruction obs6 = new Obstruction("Six", 15, 0, 3203, 60);
        	
        	AffectedRunway test5 = new AffectedRunway(x09R, obs5);    	
        	AffectedRunway test6 = new AffectedRunway(x27L, obs6);
       	
        	//Testing 09R (Take off Away, Landing Over)
            assertEquals(2903, test5.getTORA());
            assertEquals(2903, test5.getASDA());
            assertEquals(2903, test5.getTODA());
            assertEquals(2393, test5.getLDA());
                   
            //Testing 27L (Take off Towards, Landing Towards)
            assertEquals(2393, test6.getTORA());
            assertEquals(2393, test6.getASDA());
            assertEquals(2393, test6.getTODA());
            assertEquals(2903, test6.getLDA());
        } catch (Exception e) {
            fail("Error creating obstruction or runway: " + e.getMessage());
        }
    }
    
    @Test
    public void testScenario4() {
        try {        	
        	Obstruction obs7 = new Obstruction("Seven", 20, 0, 3546, 20);
        	Obstruction obs8 = new Obstruction("Eight", 20, 0, 50, 20);
   
        	AffectedRunway test7 = new AffectedRunway(x09L, obs7);   	
        	AffectedRunway test8 = new AffectedRunway(x27R, obs8);
       	
        	//Testing 09L (Take off Towards, Landing Towards)
            assertEquals(2792, test7.getTORA());
            assertEquals(2792, test7.getASDA());
            assertEquals(2792, test7.getTODA());
            assertEquals(3246, test7.getLDA());
                   
            //Testing 27R (Take off Away, Landing Over)
            assertEquals(3534, test8.getTORA());
            assertEquals(3534, test8.getASDA());
            assertEquals(3612, test8.getTODA());
            assertEquals(2774, test8.getLDA());
        } catch (Exception e) {
            fail("Error creating obstruction or runway: " + e.getMessage());
        }
    }
}


public class Tests {

    public static void main(String[] args) {
    	//Setup Runways
    	Runway x09R = new Runway("09R", "Heathrow", 3660, 3660, 3660, 3353, 307, 60, 50, 240, 300, 0, 0, null);
    	Runway x27L = new Runway("27L", "Heathrow", 3660, 3660, 3660, 3660, 0, 60, 50, 240, 300, 0, 0, null);
    	Runway x09L = new Runway("09L", "Heathrow", 3902, 3902, 3902, 3595, 306, 60, 50, 240, 300, 0, 0, null);
    	Runway x27R = new Runway("27R", "Heathrow", 3884, 3962, 3884, 3884, 0, 60, 50, 240, 300, 0, 78, null);
    	
    	x09R.setSlopeRatio(x09R.getEGR());
    	x27L.setSlopeRatio(x27L.getEGR());
    	x09L.setSlopeRatio(x09L.getEGR());
    	x27R.setSlopeRatio(x27R.getEGR());
    	
    	//Scenario 1
    	Obstruction obs1 = new Obstruction("One", 12, 0, -50, 0);
    	Obstruction obs2 = new Obstruction("Two", 12, 0, 3646, 0);
    	
    	AffectedRunway test1 = new AffectedRunway(x09L, obs1);
    	AffectedRunway test2 = new AffectedRunway(x27R, obs2);
    	
    	System.out.println("Testing Scenario 1");
    	System.out.println();
    	System.out.println("Testing 09L (Take off Away, Landing Over)");
    	testReCalculations(3346, 3346, 3346, 2985, test1);
    	System.out.println();
    	System.out.println("Testing 27R (Take off Towards, Landing Towards)");
    	testReCalculations(2986, 2986, 2986, 3346, test2);
    	System.out.println();
    	
    	//Scenario 2
    	Obstruction obs3 = new Obstruction("Three", 25, 0, 2853, 20);
    	Obstruction obs4 = new Obstruction("Four", 25, 0, 500, 20);
    	
    	AffectedRunway test3 = new AffectedRunway(x09R, obs3);
    	AffectedRunway test4 = new AffectedRunway(x27L, obs4);
    	
    	System.out.println("Testing Scenario 2");
    	System.out.println();
    	System.out.println("Testing 09R (Take off Towards, Landing Towards)");
    	testReCalculations(1850, 1850, 1850, 2553, test3);
    	System.out.println();
    	System.out.println("Testing 27L (Take off Away, Landing Over)");
    	testReCalculations(2860, 2860, 2860, 1850, test4);
    	System.out.println();
    	
    	//Scenario 3
    	Obstruction obs5 = new Obstruction("Five", 15, 0, 150, 60);
    	Obstruction obs6 = new Obstruction("Six", 15, 0, 3203, 60);
    	
    	AffectedRunway test5 = new AffectedRunway(x09R, obs5);    	
    	AffectedRunway test6 = new AffectedRunway(x27L, obs6);
    	
    	System.out.println("Testing Scenario 3");
    	System.out.println();
    	System.out.println("Testing 09R (Take off Away, Landing Over)");
    	testReCalculations(2903, 2903, 2903, 2393, test5);
    	System.out.println();
    	System.out.println("Testing 27L (Take off Towards, Landing Towards)");
    	testReCalculations(2393, 2393, 2393, 2903, test6);
    	System.out.println();
    	
    	//Scenario 4
    	Obstruction obs7 = new Obstruction("Seven", 20, 0, 3546, 20);
    	Obstruction obs8 = new Obstruction("Eight", 20, 0, 50, 20);
    	
    	AffectedRunway test7 = new AffectedRunway(x09L, obs7);   	
    	AffectedRunway test8 = new AffectedRunway(x27R, obs8);
    	
    	System.out.println("Testing Scenario 4");
    	System.out.println();
    	System.out.println("Testing 09L (Take off Towards, Landing Towards)");
    	testReCalculations(2792, 2792, 2792, 3246, test7);
    	System.out.println();
    	System.out.println("Testing 27R (Take off Away, Landing Over)");
    	testReCalculations(3534, 3534, 3612, 2774, test8);
    }
    
    public static void testReCalculations(int TORA, int ASDA, int TODA, int LDA, AffectedRunway runway) {
    	System.out.print("Comparing TORA with expected value:");
    	if (TORA == runway.getTORA()) {
    		System.out.print(" Passed");
    	} else {
    		System.out.print(" Failed");
    	}
    	
    	System.out.println();
    	System.out.print("Comparing ASDA with expected value:");
    	if (ASDA == runway.getASDA()) {
    		System.out.print(" Passed");
    	} else {
    		System.out.print(" Failed");
    	}
    	
    	System.out.println();
    	System.out.print("Comparing TODA with expected value:");
    	if (TODA == runway.getTODA()) {
    		System.out.print(" Passed");
    	} else {
    		System.out.print(" Failed");
    	}
    	
    	System.out.println();
    	System.out.print("Comparing LDA with expected value:");
    	if (LDA == runway.getLDA()) {
    		System.out.print(" Passed");
    	} else {
    		System.out.print(" Failed");
    	}
    	System.out.println();
    	
    	/* System.out.println("TORA = " + runway.getTORA());
    	System.out.println("ASDA = " + runway.getASDA());
    	System.out.println("TODA = " + runway.getTODA());
    	System.out.println("LDA = " + runway.getLDA());
    	*/
    }
}

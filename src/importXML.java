// XML file importer using DOM Structure

import java.io.File;
import javax.xml.parsers.*;
import org.w3c.dom.*;

public class importXML {
    private String filename;

    // TODO: implement error catching here - not XML, file not found, etc.
    importXML(String filename){
        this.filename = filename;
    }

    public Obstruction importObsFromXML() {
        // try opening file - if invalid filename provided, throw error.
        // error - get
        File inputFile = new File(filename);
        Obstruction newObstruction;

        try {
            // create a DocumentBuilder and load XML file as a document
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            // get a list of all obstructions from the XML file
            NodeList nodeList = doc.getElementsByTagName("obstruction");

            // get first node from list. throws exception if no nodes exist for it.
            Node n = nodeList.item(0);
            if (n == null) {
                throw new NullPointerException("Empty list");
            }

            // parse and open
            newObstruction = newObsFromElement((Element) n);

            // testing: print new obstruction
            System.out.println(newObstruction.getHeight());
            return newObstruction;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Runway importRunwayFromXML() {
        // try opening file - if invalid filename provided, throw error.
        File inputFile = new File(filename);

        try {
            // create a DocumentBuilder and load XML file as a document
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            // get a list of all obstructions from the XML file
            NodeList nodeList = doc.getElementsByTagName("runway");

            // get first node from list. throws exception if no nodes exist for it.
            Node n = nodeList.item(0);
            if (n == null) {
                throw new NullPointerException("Empty list");
            }

            return newRunwayFromElement((Element) n);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // function that, given an obstruction element, returns an obstruction object
    private Obstruction newObsFromElement (Element n) {
        String name = getTagValue(n,"name");
        int height = getIntTagValue(n,"height");
        int length = getIntTagValue(n,"length");
        int distanceFromThreshold = getIntTagValue(n,"distFromThreshold");
        int distanceFromCentre = getIntTagValue(n,"distFromCentre");
        return new Obstruction(name, height, length, distanceFromThreshold, distanceFromCentre);
    }

    // function that, given a runway element, returns a runway element
    private Runway newRunwayFromElement (Element n) {
        String name = getTagValue(n,"name");
        String airport = getTagValue(n, "airport");
        int TORA = getIntTagValue(n,"TORA");
        int TODA = getIntTagValue(n,"TODA");
        int ASDA = getIntTagValue(n,"ASDA");
        int LDA = getIntTagValue(n,"LDA");
        int displacedThreshold = getIntTagValue(n,"displacedThreshold");
        int stripEnd = getIntTagValue(n,"stripEnd");
        int EGR = getIntTagValue(n,"EGR");
        int RESA = getIntTagValue(n,"RESA");
        int blastAllowance = getIntTagValue(n,"blastAllowance");
        int stopway = getIntTagValue(n,"stopway");
        int clearway = getIntTagValue(n,"clearway");
        Directions direction = null; // TODO: what to do with these?

        return new Runway(name, airport, TORA, TODA, ASDA, LDA, displacedThreshold, stripEnd, EGR, RESA,
                blastAllowance, stopway, clearway, direction);
    }

    private String getTagValue(Element n, String tag) {
        return n.getElementsByTagName(tag).item(0).getTextContent();
    }

    // TODO: Current error handling sets fields to -1 if empty. Handle this properly.
    private int getIntTagValue(Element n, String tag) {
        try {
            return Integer.parseInt(getTagValue(n, tag));
        } catch (NumberFormatException e) {
            return -1;
        }
    }


    public static void main(String args[]) {
        importXML testing = new importXML("src/testrunway1.xml");
        System.out.println(testing.importRunwayFromXML().getAirport());
    }
}

// XML file importer using DOM Structure

import java.io.File;
import java.util.ArrayList;
import javax.xml.parsers.*;
import org.w3c.dom.*;

public class importXML {
    private String filename;
    private DocumentBuilderFactory dbFactory;
    private DocumentBuilder dBuilder;
    private Document doc;


    // TODO: implement error catching here - not XML, file not found, etc.
    importXML(String filename) throws Exception {
        this.filename = filename;

        // throws file not found exception if
        File inputFile = new File(filename);

        // initialise document
        // create a DocumentBuilder and load XML file as a document
        try {
            this.dbFactory = DocumentBuilderFactory.newInstance();
            this.dBuilder = dbFactory.newDocumentBuilder();
            this.doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
        } catch (Exception e) {
            throw new Exception("Could not create document builder");
        }
    }

    public Obstruction importObsFromXML() {
        Obstruction newObstruction;

        try {
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

    public ArrayList<Runway> importRunwaysFromXML() {
        // get a list of all runways from the XML files
        NodeList runwayNodes = doc.getElementsByTagName("runway");

        // if list empty, throw exception
        if (runwayNodes.getLength() == 0) {
            throw new NullPointerException("Emptyu list - XML file contains no runways");
        }

        // iterate over all runways and add them as objects to array list
        ArrayList<Runway> runwayObjects = new ArrayList<Runway>();
        for (int i = 0, len = runwayNodes.getLength(); i < len; i++) {
            runwayObjects.add(newRunwayFromElement((Element) runwayNodes.item(i)));
        }

        return runwayObjects;
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
        Directions direction = Directions.valueOf(getTagValue(n, "direction"));

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

    public static void main(String args[]) throws Exception {
        importXML testing = new importXML("src/test3runways.xml");
        ArrayList<Runway> runwaysTest = testing.importRunwaysFromXML();
        for (Runway runway : runwaysTest) {
            System.out.println(runway.getName() + " - TORA: " + runway.getTORA());
        }
    }
}

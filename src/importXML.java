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


    importXML(String filename) throws Exception {
        this.filename = filename;

        // throws file not found exception if fails
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

    public ArrayList<Runway> importRunwaysFromXML() {
        // get a list of all runways from the XML files
        NodeList runwayNodes = doc.getElementsByTagName("runway");

        // if list empty, throw exception
        if (runwayNodes.getLength() == 0) {
            throw new NullPointerException("Empty list - XML file contains no runways");
        }

        // iterate over all runways and add them as objects to array list
        ArrayList<Runway> runwayObjects = new ArrayList<Runway>();
        for (int i = 0, len = runwayNodes.getLength(); i < len; i++) {
            runwayObjects.add(newRunwayFromElement((Element) runwayNodes.item(i)));
        }

        return runwayObjects;
    }


    public ArrayList<Obstruction> importObstructionsFromXML() {
        // get a list of all runways from the XML files
        NodeList obstructionNodes = doc.getElementsByTagName("obstruction");

        // if list empty, throw exception
        if (obstructionNodes.getLength() == 0) {
            throw new NullPointerException("Empty list - XML file contains no obstructions");
        }

        // iterate over all runways and add them as objects to array list
        ArrayList<Obstruction> obstructionObjects = new ArrayList<Obstruction>();
        for (int i = 0, len = obstructionNodes.getLength(); i < len; i++) {
            obstructionObjects.add(newObsFromElement((Element) obstructionNodes.item(i)));
        }

        return obstructionObjects;
    }

    // function that, given an obstruction element, returns an obstruction object
    private Obstruction newObsFromElement (Element n) {
        String name = getTagValue(n,"name");
        int height = getPositiveIntTagValue(n,"height");
        int length = getPositiveIntTagValue(n,"length");
        int distanceFromThreshold = getPositiveIntTagValue(n,"distFromThreshold");
        int distanceFromCentre = getPositiveIntTagValue(n,"distFromCentre");
        return new Obstruction(name, height, length, distanceFromThreshold, distanceFromCentre);
    }

    // function that, given a runway element, returns a runway element
    private Runway newRunwayFromElement (Element n) {
        String name = getTagValue(n,"name");
        String airport = getTagValue(n, "airport");
        int TORA = getPositiveIntTagValue(n,"TORA");
        int TODA = getPositiveIntTagValue(n,"TODA");
        int ASDA = getPositiveIntTagValue(n,"ASDA");
        int LDA = getPositiveIntTagValue(n,"LDA");
        int displacedThreshold = getPositiveIntTagValue(n,"displacedThreshold");
        int stripEnd = getPositiveIntTagValue(n,"stripEnd");
        int EGR = getPositiveIntTagValue(n,"EGR");
        int RESA = getPositiveIntTagValue(n,"RESA");
        int blastAllowance = getPositiveIntTagValue(n,"blastAllowance");
        int stopway = getPositiveIntTagValue(n,"stopway");
        int clearway = getPositiveIntTagValue(n,"clearway");
        Directions direction = Directions.valueOf(getTagValue(n, "direction"));

        return new Runway(name, airport, TORA, TODA, ASDA, LDA, displacedThreshold, stripEnd, EGR, RESA,
                blastAllowance, stopway, clearway, direction);
    }

    private String getTagValue(Element n, String tag) {
        return n.getElementsByTagName(tag).item(0).getTextContent();
    }

    private int getPositiveIntTagValue(Element n, String tag) {
        int value =  Integer.parseInt(getTagValue(n, tag));
        if (value < 0) {
            throw new IllegalArgumentException(tag + " value cannot be negative");
        }
        return value;
    }

    private int getIntTagValue(Element n, String tag) {
        return Integer.parseInt(getTagValue(n, tag));
    }

    public static void main(String args[]) throws Exception {
        importXML testing = new importXML("src/test3runways.xml");
        ArrayList<Runway> runwaysTest = testing.importRunwaysFromXML();
        for (Runway runway : runwaysTest) {
            System.out.println(runway.getName() + " - TORA: " + runway.getTORA());
        }
    }
}

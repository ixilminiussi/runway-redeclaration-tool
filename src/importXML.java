/**
 * SEGP 2021
 * Group 44
 * Class for importing Runways and Obstructions from an XML file in the DOM structure
 */


import java.io.File;
import java.util.ArrayList;
import javax.xml.parsers.*;
import org.w3c.dom.*;


public class importXML {
    private String filename;
    private DocumentBuilderFactory dbFactory;
    private DocumentBuilder dBuilder;
    private Document doc;


    /**
     * Constructor
     * XML file can contain either Runways, Obstructions OR both
     *
     * @param filename The filename of the XML file to be imported
     * @throws Exception If file could not be opened
     */
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
            throw new Exception("File not found");
        }
    }


    /**
     * Function that imports all runways from an XML file
     *
     * @return An ArrayList of Runway objects
     */
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


    /**
     * Function that imports all runways from an XML file
     *
     * @return An ArrayList of Runway objects
     */
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


    /**
     * Function that, given an obstruction node, returns an obstruction object
     *
     * @param n The node from the XML file
     * @return An Obstruction object
     */
    private Obstruction newObsFromElement(Element n) {
        String name = getTagValue(n, "name");
        int height = getPositiveIntTagValue(n, "height");
        int length = getPositiveIntTagValue(n, "length");
        int distanceFromThreshold = getPositiveIntTagValue(n, "distFromThreshold");
        int distanceFromCentre = getPositiveIntTagValue(n, "distFromCentre");
        return new Obstruction(name, height, length, distanceFromThreshold, distanceFromCentre);
    }


    /**
     * Function that, given a runway node, returns a runway object
     *
     * @param n The node from the XML file
     * @return A Runway object
     */
    private Runway newRunwayFromElement(Element n) {
        String name = getTagValue(n, "name");
        String airport = getTagValue(n, "airport");
        int TORA = getPositiveIntTagValue(n, "TORA");
        int TODA = getPositiveIntTagValue(n, "TODA");
        int ASDA = getPositiveIntTagValue(n, "ASDA");
        int LDA = getPositiveIntTagValue(n, "LDA");
        int displacedThreshold = getIntTagValue(n, "displacedThreshold");
        int stripEnd = getPositiveIntTagValue(n, "stripEnd");
        int EGR = getPositiveIntTagValue(n, "EGR");
        int RESA = getPositiveIntTagValue(n, "RESA");
        int blastAllowance = getPositiveIntTagValue(n, "blastAllowance");
        int stopway = getPositiveIntTagValue(n, "stopway");
        int clearway = getPositiveIntTagValue(n, "clearway");

        return new Runway(name, airport, TORA, TODA, ASDA, LDA, displacedThreshold, stripEnd, EGR, RESA,
                blastAllowance, stopway, clearway);
    }


    /**
     * Parses and returns the String value held in the XML file for a given tag
     * If a node has two of the same tag, the first will be returned.
     *
     * @param n   The element parsed from the XML file
     * @param tag The XML tag that is being searched for
     * @return The String value in the tag for the element
     */
    private String getTagValue(Element n, String tag) {
        return n.getElementsByTagName(tag).item(0).getTextContent();
    }


    /**
     * Parses the value stored in the XML file and returns a positive integer.
     *
     * @param n   The element parsed from the XML file
     * @param tag The XML tag that is being searched for
     * @return The positive integer (or zero) stored in the XML file
     * @throws IllegalArgumentException If the parsed integer value is negative
     */
    private int getPositiveIntTagValue(Element n, String tag) throws IllegalArgumentException {
        int value = Integer.parseInt(getTagValue(n, tag));
        if (value < 0) {
            throw new IllegalArgumentException(tag + " value cannot be negative");
        }
        return value;
    }


    /**
     * Parses the value stored in the XML file and returns a  integer.
     *
     * @param n   The element parsed from the XML file
     * @param tag The XML tag that is being searched for
     * @return The integer value stored in the XML file
     * @throws IllegalArgumentException If the parsed integer value is negative
     */
    private int getIntTagValue(Element n, String tag) {
        return Integer.parseInt(getTagValue(n, tag));
    }

    public static void main(String args[]) throws Exception {
        importXML testing = new importXML("src/testing/testRunwayBlanks.xml");
        ArrayList<Runway> runwaysTest = testing.importRunwaysFromXML();
    }
}
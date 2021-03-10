import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.io.File;
import java.util.ArrayList;
import javax.xml.parsers.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;


public class exportXML {
    public static void exportObstructionsToXML(String filename, ArrayList<Obstruction> obstructions) throws IllegalArgumentException, Exception{
        // check filename is .xml
        try {
            if (!filename.substring(filename.length() - 4).equals(".xml")) {
                throw new IllegalArgumentException("file extension not XML");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("file name not long enough");
        }

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            // root element
            Element rootElement = doc.createElement("obstructions");
            doc.appendChild(rootElement);

            for (int i = 0, len = obstructions.size(); i < len; i++) {
                // obstruction element
                Element obstruction = doc.createElement("obstruction");
                rootElement.appendChild(obstruction);

                Element e = null;
                e = doc.createElement("name");
                e.appendChild(doc.createTextNode(obstructions.get(i).getName()));
                obstruction.appendChild(e);

                e = doc.createElement("height");
                e.appendChild(doc.createTextNode(Integer.toString(obstructions.get(i).getHeight())));
                obstruction.appendChild(e);

                e = doc.createElement("length");
                e.appendChild(doc.createTextNode(Integer.toString(obstructions.get(i).getLength())));
                obstruction.appendChild(e);

                e = doc.createElement("distFromThreshold");
                e.appendChild(doc.createTextNode(Integer.toString(obstructions.get(i).getDistanceFromThreshold())));
                obstruction.appendChild(e);

                e = doc.createElement("distFromCentre");
                e.appendChild(doc.createTextNode(Integer.toString(obstructions.get(i).getDistanceFromCentre())));
                obstruction.appendChild(e);
            }

            // write to XML
            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filename));
            transformer.transform(source, result);

        } catch (Exception e) {
            throw new Exception("Could not export");
        }
    }

    public static void exportRunwaysToXML(String filename, ArrayList<Runway> runways) throws IllegalArgumentException, Exception {
    // check filename is .xml
        try {
            if (!filename.substring(filename.length() - 4).equals(".xml")) {
                throw new IllegalArgumentException("file extension not XML");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("invalid file name");
        }

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            // root element
            Element rootElement = doc.createElement("runways");
            doc.appendChild(rootElement);

            for (int i = 0, len = runways.size(); i < len; i++) {
                // runway element
                Element runway = doc.createElement("runway");
                rootElement.appendChild(runway);

                Element e = null;
                e = doc.createElement("name");
                e.appendChild(doc.createTextNode(runways.get(i).getName()));
                runway.appendChild(e);

                e = doc.createElement("airport");
                e.appendChild(doc.createTextNode(runways.get(i).getAirport()));
                runway.appendChild(e);

                e = doc.createElement("TORA");
                e.appendChild(doc.createTextNode(Integer.toString(runways.get(i).getTORA())));
                runway.appendChild(e);

                e = doc.createElement("TODA");
                e.appendChild(doc.createTextNode(Integer.toString(runways.get(i).getTODA())));
                runway.appendChild(e);

                e = doc.createElement("ASDA");
                e.appendChild(doc.createTextNode(Integer.toString(runways.get(i).getASDA())));
                runway.appendChild(e);

                e = doc.createElement("LDA");
                e.appendChild(doc.createTextNode(Integer.toString(runways.get(i).getLDA())));
                runway.appendChild(e);

                e = doc.createElement("displacedThreshold");
                e.appendChild(doc.createTextNode(Integer.toString(runways.get(i).getDisplacedThreshold())));
                runway.appendChild(e);

                e = doc.createElement("stripEnd");
                e.appendChild(doc.createTextNode(Integer.toString(runways.get(i).getStripEnd())));
                runway.appendChild(e);

                e = doc.createElement("EGR");
                e.appendChild(doc.createTextNode(Integer.toString(runways.get(i).getEGR())));
                runway.appendChild(e);

                e = doc.createElement("RESA");
                e.appendChild(doc.createTextNode(Integer.toString(runways.get(i).getRESA())));
                runway.appendChild(e);

                e = doc.createElement("blastAllowance");
                e.appendChild(doc.createTextNode(Integer.toString(runways.get(i).getBlastAllowance())));
                runway.appendChild(e);

                e = doc.createElement("stopway");
                e.appendChild(doc.createTextNode(Integer.toString(runways.get(i).getStopway())));
                runway.appendChild(e);

                e = doc.createElement("clearway");
                e.appendChild(doc.createTextNode(Integer.toString(runways.get(i).getClearway())));
                runway.appendChild(e);

                e = doc.createElement("direction");
                e.appendChild(doc.createTextNode(runways.get(i).getDirection().name()));
                runway.appendChild(e);
            }

            // write to XML
            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filename));
            transformer.transform(source, result);

        } catch (Exception e) {
            throw new Exception("Could not export");
        }
    }
}

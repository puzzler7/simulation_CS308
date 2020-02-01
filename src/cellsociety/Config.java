package cellsociety;

import javafx.scene.paint.Color;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

/**
 * Reads an XML file and sets up the Grid.
 * @author Alex Xu aqx
 */
public class Config {
    //public static final String XML_CONFIG_FILEPATH = "GameofLife.xml"; //Delete Later, since the filepath is now passed in.
    private String ConfigNodeName = "ConfigInfo";

    private Grid myGrid;

    private String myFilePath;
    private File xmlFile;
    private Document doc;

    private String myTitle;
    private String myAuthor;

    private Map<String, Color> myStates;
    private String defaultState;

    private int mySpeed;
    private int myWidth;
    private int myHeight;

    /**
     * Constructor for the Config object. Sets the filepath and sets up the documentBuilder.
     * @param filePath
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public Config(String filePath) throws ParserConfigurationException, SAXException, IOException {
        myFilePath = filePath;
        setupDocument();
        System.out.println("Document Setup Complete");                                                                  //Debugging Purposes Only.
        extractConfigInfo();
    }

    private void setupDocument() throws IOException, SAXException, ParserConfigurationException {
        xmlFile = new File(myFilePath);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        doc = builder.parse(xmlFile);
        doc.getDocumentElement().normalize();                                                                           //optional, might remove later.
    }

    /**
     * Extracts all information in the XML Document that lies within <ConfigInfo>.
     */
    private void extractConfigInfo(){
        NodeList configNodeList = doc.getElementsByTagName(ConfigNodeName);
        Node configNode = configNodeList.item(0);

        if(configNode.getNodeType() == Node.ELEMENT_NODE) {
            Element configElement = (Element) configNode;
            extractTitle(configElement);
            extractAuthor(configElement);
            extractDimensions(configElement);
            extractStates(configElement);
        }
    }

    private void extractTitle(Element startingElement){
        myTitle = startingElement.getElementsByTagName("Title").item(0).getTextContent();
        System.out.println("Simulation Name: "+ myTitle);                                                               //For Debugging Purposes.
    }
    private void extractAuthor(Element startingElement){
        myAuthor = startingElement.getElementsByTagName("Author").item(0).getTextContent();
        System.out.println("Author: "+ myAuthor);
    }

    private void extractDimensions(Element startingElement){
        Node dimensionsNode = startingElement.getElementsByTagName("Dimensions").item(0);
        if(dimensionsNode.getNodeType() == Node.ELEMENT_NODE){
            Element dimensionsElement = (Element) dimensionsNode;
            extractHeight(dimensionsElement);
            extractWidth(dimensionsElement);
            extractSpeed(dimensionsElement);
        }
    }

    private void extractSpeed(Element dimensionsElement) {
        mySpeed = Integer.parseInt(dimensionsElement.getElementsByTagName("Speed").item(0).getTextContent().trim());
    }

    private void extractWidth(Element dimensionsElement) {
        myWidth = Integer.parseInt(dimensionsElement.getElementsByTagName("Width").item(0).getTextContent().trim());
    }

    private void extractHeight(Element dimensionsElement) {
        myHeight = Integer.parseInt(dimensionsElement.getElementsByTagName("Height").item(0).getTextContent().trim());
    }

    private void extractStates(Element startingElement){
        
    }



    public String getTitle(){
        return myTitle;
    }
    public String getAuthor(){
        return myAuthor;
    }
    public int getWidth(){
        return myWidth;
    }
    public int getHeight(){
        return myHeight;
    }
    public Map<String, Color> getStates(){
        return myStates;
    }
    public String getDefaultState(){
        return defaultState;
    }
    public int getSpeed(){
        return mySpeed;
    }


    public Grid loadFile(String path){
        Grid newGrid = new Grid();
        return newGrid;
    }

    //Take file input in
    //private void readFile throws FileNotFoundException(String path){

    //}

    private Grid createGrid(){
        Grid newGrid = new Grid();
        return newGrid;
    }
}
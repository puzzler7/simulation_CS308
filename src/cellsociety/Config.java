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
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Reads an XML file and sets up the Grid.
 * @author Alex Xu aqx
 */
public class Config {
    private String packagePrefixName = "cellsociety.";

    private String configNodeName = "ConfigInfo";
    private String titleNodeName = "Title";
    private String authorNodeName = "Author";
    private String parametersNodeName = "SpecialParameters";
    private String statesNodeName = "States";
    private String singleParameterNodeName = "Parameter";
    private String singleStateNodeName = "State";
    private String parameterNameAttributeName = "name";
    private String rowNodeName = "Row";
    private String stateIDNodeName = "ID";
    private String colorNodeName = "Color";
    private String dimensionsNodeName = "Dimensions";
    private String speedNodeName = "Speed";
    private String widthNodeName = "Width";
    private String heightNodeName = "Height";
    private String cellNodeName = "Cell";

    private String docSetUpConfirmationMessage = "Document Setup Complete";
    private String configSetUpConfirmationMessage = "Config Info Load Complete";
    private String gridConfirmationMessage = "Grid Created";

    private File myFile;
    private Document doc;

    private Grid myGrid;
    private String myTitle;
    private String myAuthor;
    private double mySpeed;
    private int myWidth;
    private int myHeight;
    private Map<Integer, Color> myStates;
    private Map<String, Double> myParameters;
    private int defaultState = 0;

    /**
     * Constructor for the Config object. Sets the filepath and sets up the documentBuilder.
     * @param xmlFile File object passed in, in XML format
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public Config(File xmlFile) throws ParserConfigurationException, SAXException, IOException {
        myFile = xmlFile;
        setupDocument();
        System.out.println(docSetUpConfirmationMessage);
    }

    /**
     * Create and set up the Grid based on stored information, and then return it.
     * @return
     */
    public Grid loadFile() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        extractConfigInfo();
        System.out.println(configSetUpConfirmationMessage);
        createGrid();
        System.out.println(gridConfirmationMessage);
        return myGrid;
    }

    /**
     * Returns the update speed of the simulation, as defined within the initial config XML document.
     * @return speed of the simulation
     */
    public double getSpeed(){
        return mySpeed;
    }

    private void setupDocument() throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        doc = builder.parse(myFile);
        doc.getDocumentElement().normalize();
    }

    /**
     * Extracts all information in the XML Document that lies within <ConfigInfo>.
     */
    private void extractConfigInfo(){
        NodeList configNodeList = doc.getElementsByTagName(configNodeName);
        Node configNode = configNodeList.item(0);

        if(configNode.getNodeType() == Node.ELEMENT_NODE) {
            Element configElement = (Element) configNode;
            extractTitle(configElement);
            extractAuthor(configElement);
            extractDimensions(configElement);
            extractStates(configElement);
            extractParameters(configElement);
        }
    }
                                                                                                                        //Note to self: VERY SIMILAR CODE TO EXTRACT STATES
    private void extractParameters(Element configElement) {
        myParameters = new HashMap<>();

        Node parametersNode = configElement.getElementsByTagName(parametersNodeName).item(0);
        if(parametersNode.getNodeType() == Node.ELEMENT_NODE){
            Element parametersElement = (Element) parametersNode;

            NodeList parametersNodeList = parametersElement.getElementsByTagName(singleParameterNodeName);

            for(int i = 0; i<parametersNodeList.getLength(); i++){
                Node singleParameterNode = parametersNodeList.item(i);
                if(singleParameterNode.getNodeType() == Node.ELEMENT_NODE){
                    Element singleParameterElement = (Element) singleParameterNode;
                    String parameterName = singleParameterElement.getAttribute(parameterNameAttributeName);
                    Double parameterValue = Double.valueOf(singleParameterElement.getTextContent());
                    myParameters.put(parameterName, parameterValue);
                }
            }
        }
        printParameters();
    }

    private void extractStates(Element startingElement){
        myStates = new HashMap<>();

        Node statesNode = startingElement.getElementsByTagName(statesNodeName).item(0);
        if(statesNode.getNodeType() == Node.ELEMENT_NODE){
            Element statesElement = (Element) statesNode;

            NodeList statesNodeList = statesElement.getElementsByTagName(singleStateNodeName);

            for(int i=0; i<statesNodeList.getLength(); i++) {
                Node singleStateNode = statesNodeList.item(i);
                if (singleStateNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element singleStateElement = (Element) singleStateNode;
                    Integer stateID = Integer.valueOf(singleStateElement.getElementsByTagName(stateIDNodeName).item(0).getTextContent());
                    String stateColor = singleStateElement.getElementsByTagName(colorNodeName).item(0).getTextContent();
                    myStates.put(stateID, Color.web(stateColor));
                }
            }
        }
        printStates();
    }

    private void extractDimensions(Element startingElement){
        Node dimensionsNode = startingElement.getElementsByTagName(dimensionsNodeName).item(0);
        if(dimensionsNode.getNodeType() == Node.ELEMENT_NODE){
            Element dimensionsElement = (Element) dimensionsNode;
            extractHeight(dimensionsElement);
            extractWidth(dimensionsElement);
            extractSpeed(dimensionsElement);
            printDimensions();
        }
    }

    private void extractTitle(Element startingElement){
        myTitle = startingElement.getElementsByTagName(titleNodeName).item(0).getTextContent();
        System.out.println("Simulation Name: "+ myTitle);
    }
    private void extractAuthor(Element startingElement){
        myAuthor = startingElement.getElementsByTagName(authorNodeName).item(0).getTextContent();
        System.out.println("Author: "+ myAuthor);
    }

    private void extractSpeed(Element dimensionsElement) {
        mySpeed = Double.parseDouble(dimensionsElement.getElementsByTagName(speedNodeName).item(0).getTextContent().trim());
    }

    private void extractWidth(Element dimensionsElement) {
        myWidth = Integer.parseInt(dimensionsElement.getElementsByTagName(widthNodeName).item(0).getTextContent().trim());
    }

    private void extractHeight(Element dimensionsElement) {
        myHeight = Integer.parseInt(dimensionsElement.getElementsByTagName(heightNodeName).item(0).getTextContent().trim());
    }

    private void printParameters() {                                                                                    //For Debug Purposes
        System.out.println("All Parameters Set (Debug):");
        for (Map.Entry name : myParameters.entrySet()) {
            System.out.println("Name: "+name.getKey() + " & Value: " + name.getValue());
        }
    }
    private void printStates() {
        System.out.println("All States (Debug):");
        for (Map.Entry stateID : myStates.entrySet()) {
            System.out.println("State: "+stateID.getKey() + " & Value: " + stateID.getValue());
        }
    }

    private void printDimensions(){
        System.out.println("Height:" + myHeight);
        System.out.println("Width:" + myWidth);
        System.out.println("Speed:" + mySpeed);
    }

    private void createGrid() throws NoSuchMethodException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, InstantiationException {
        myGrid = new Grid();
        int row = 0;

        NodeList rowNodeList = doc.getElementsByTagName(rowNodeName);

        for(int i = 0; i<rowNodeList.getLength(); i++){
            int col = 0;
            Node singleRowNode = rowNodeList.item(i);
            Element singleRowElement = (Element) singleRowNode;
            NodeList cellsNodeList = singleRowElement.getElementsByTagName(cellNodeName);

            for(int k = 0; k<cellsNodeList.getLength(); k++) {
                if (k < myWidth){
                    Node singleCellNode = cellsNodeList.item(k);
                    Integer cellState = Integer.valueOf(singleCellNode.getTextContent());

                    Cell myCell = makeCell(cellState);
                    myGrid.placeCell(col, row, myCell);

                    col++;
                }
            }
            while(col < myWidth){
                Cell myCell = makeCell(defaultState);
                myGrid.placeCell(col, row, myCell);
                col++;
            }
            row++;
        }
        //myGrid.setRandomGrid(myTitle, myParameters, new double[]{.2,.7,0}, myWidth, myHeight); //Random Grid, for testing purposes.
    }

    private Cell makeCell(int state) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException {
        Class cellClass = Class.forName(packagePrefixName +myTitle);
        Cell cell = (Cell)(cellClass.getConstructor().newInstance());

        for (Map.Entry<String, Double> parameterEntry : myParameters.entrySet()) {
            cell.setParam(parameterEntry.getKey(), parameterEntry.getValue());
        }

        for(Map.Entry<Integer, Color> stateEntry: myStates.entrySet()){
            cell.setStateColor(stateEntry.getKey(), stateEntry.getValue());
        }

        cell.setState(state);
        return cell;
    }
}
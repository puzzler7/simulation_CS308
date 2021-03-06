package cellsociety.config;

import cellsociety.exceptions.XMLWriteException;
import cellsociety.simulation.grid.Grid;
import javafx.scene.paint.Color;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.Map;

/**
 * Responsible for Saving a user-created Simulation into an XML Config file
 * Assumes that the user inputs a valid file name/path to save to, and has enough disk space there, otherwise exceptions are thrown.
 * Example: The Visualizer calls the XMLWriter class when the user wants to save a File.
 * @Author Alex Xu, aqx
 */
public class XMLWriter {
    private static final String ROOT_NODE_NAME = "Simulation";
    private static final String ROW_NUMBER_NAME = "numbr";

    private Grid myGrid;
    private Config myConfig;
    private Document myDocument;
    private String custom = "true";

    /**
     * XMLWriter constructor. Takes a Grid object and a Config object as parameters.
     * @param config Config object that was originally responsible for creating the grid
     * @param grid A grid that represents the states that the user wants to save
     */
    public XMLWriter(Config config, Grid grid) throws XMLWriteException{
        myGrid = grid;
        myConfig = config;
        try {
            setupDocument();
        } catch (ParserConfigurationException e) {
            throw new XMLWriteException(e);
        }
    }

    /**
     * Saves an XML file at the given filepath
     * @param filepath where the user wants to save the XML created
     */
    public void saveXML(String filepath) throws XMLWriteException{
        addNodes();
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(myDocument);
            StreamResult streamResult = new StreamResult(new File(filepath));
            transformer.transform(domSource, streamResult);
        }catch(TransformerException e){
            throw new XMLWriteException(e);
        }
    }

    private void setupDocument() throws ParserConfigurationException {
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        myDocument = documentBuilder.newDocument();
    }

    private void addNodes(){
        Element root = myDocument.createElement(ROOT_NODE_NAME);
        myDocument.appendChild(root);
        root.appendChild(getConfigInfo());
        root.appendChild(getCellsInfo());
    }

    private Node getConfigInfo(){
        Element configInfo = myDocument.createElement(Config.CONFIG_NODE_NAME);
        configInfo.appendChild(createEndNode(Config.TITLE_NODE_NAME, myConfig.getTitle()));
        configInfo.appendChild(createEndNode(Config.AUTHOR_NODE_NAME, myConfig.getAuthor()));
        configInfo.appendChild(createEndNode(Config.SHAPE_NODE_NAME, myConfig.getShape()));
        configInfo.appendChild(createEndNode(Config.BORDER_TYPE_NODE, myConfig.getBorderType()));
        configInfo.appendChild(createEndNode(Config.MASK_NODE_NAME, formatMask(myConfig.getMask())));
        configInfo.appendChild(getDimensionsInfo());
        configInfo.appendChild(getSpecialParametersInfo());
        configInfo.appendChild(getStatesInfo());
        configInfo.appendChild(createEndNode(Config.CUSTOM_NODE_NAME, custom));
        return configInfo;
    }

    private Node getCellsInfo(){
        Element cellsInfo = myDocument.createElement(Config.CELLS_NODE_NAME);
        for(int r = 0; r<myGrid.getHeight(); r++){
            cellsInfo.appendChild(getRowNodes(r));
        }
        return cellsInfo;
    }

    private Node getRowNodes(int row){
        Element rowNode = myDocument.createElement(Config.ROW_NODE_NAME);
        rowNode.setAttribute(ROW_NUMBER_NAME, ""+row);
        for(int c = 0; c<myGrid.getWidth(); c++){
            rowNode.appendChild(createEndNode(Config.CELL_NODE_NAME, ""+myGrid.getState(row, c)));
        }
        return rowNode;
    }

    private Node getDimensionsInfo(){
        Element dimensionsInfo = myDocument.createElement(Config.DIMENSIONS_NODE_NAME);
        dimensionsInfo.appendChild(createEndNode(Config.WIDTH_NODE_NAME, ""+myGrid.getWidth()));
        dimensionsInfo.appendChild(createEndNode(Config.HEIGHT_NODE_NAME, ""+myGrid.getHeight()));
        dimensionsInfo.appendChild(createEndNode(Config.SPEED_NODE_NAME, ""+myConfig.getSpeed()));
        return dimensionsInfo;
    }

    private Node getSpecialParametersInfo(){
        Element specialParametersNode = myDocument.createElement(Config.PARAMETERS_NODE_NAME);
        String[] parameters = myGrid.getParams();
        for(String p : parameters){
            specialParametersNode.appendChild(createEndNode(Config.SINGLE_PARAMETER_NODE_NAME, ""+myGrid.getParam(p), Config.PARAMETER_NAME_ATTRIBUTE_NAME, p));
        }
        return specialParametersNode;
    }

    private Node getStatesInfo(){
        Element statesInfoNode = myDocument.createElement(Config.STATES_NODE_NAME);
        statesInfoNode.appendChild(createEndNode(Config.DEFAULT_STATE_NODE_NAME, myConfig.getDefaultState()));

        Map<Integer, Color> statesMap = myConfig.getStates();
        for(Map.Entry<Integer, Color> entry : statesMap.entrySet()){
            statesInfoNode.appendChild(createStateNode(entry.getKey(), entry.getValue()));
        }
        return statesInfoNode;
    }

    private Node createStateNode(Integer id, Color color){
        Element stateNode = myDocument.createElement(Config.SINGLE_STATE_NODE_NAME);
        stateNode.appendChild(createEndNode(Config.STATE_ID_NODE_NAME, ""+id));
        stateNode.appendChild(createEndNode(Config.COLOR_NODE_NAME, ""+color));
        return stateNode;
    }

    private String formatMask(int[] inputArray){
        String returnString = "";
        for(int i = 0; i < inputArray.length; i++){
            returnString = returnString + inputArray[i] + " ";
        }
        return returnString;
    }

    private Node createEndNode(String name, String value){
        Element node = myDocument.createElement(name);
        node.appendChild(myDocument.createTextNode(value));
        return node;
    }

    private Node createEndNode(String name, String value, String attributeName, String attributeValue){
        Element node = (Element) createEndNode(name, value);
        node.setAttribute(attributeName, attributeValue);
        return node;
    }
}
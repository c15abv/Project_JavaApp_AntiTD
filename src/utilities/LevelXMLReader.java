package utilities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import start.AreaPosition;
import start.GameLevel;
import start.Position;
import tiles.PathTile;
import tiles.PathTile.ValidPath;
import tiles.Tile;
import tiles.VoidTile;
import tiles.WallTile;

/**
 * @author Jan Nylén, Alexander Ekström
 *
 */
public class LevelXMLReader{
    //elements 
    public static final String LEVEL = "level";
    public static final String ROW = "row";
    public static final String TILE = "tile";

    //attributes
    public static final String LANDON = "landOn";
    public static final String DIRECTION = "direction";
    public static final String LVLNAME = "name";

    private String XMLFile;

    public LevelXMLReader(/*some xml file*/){
    }

    public LevelXMLReader(String XMLFile){
        this.XMLFile = XMLFile;
    }

    public static void main(String[] args){
        
        LevelXMLReader reader = new LevelXMLReader();
        if(reader.validateXmlAgainstXsds("XML/Levels.xml",
                "XML/LevelsXMLSchema.xsd")){
            reader.toLevel("XML/Levels.xml");
        }
        
        
    }

    /**
     * Author: Jan Nylén
     * @param xmlMapFile
     * @return
     */    
    public Document createDocument(String xmlMapFile){

        File xmlInputFile = new File(xmlMapFile);
        DocumentBuilderFactory dbFactory
        = DocumentBuilderFactory.newInstance();

        DocumentBuilder dBuilder = null;

        try {
            dBuilder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            return dBuilder.parse(xmlInputFile);
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /** The toLevel method takes in a a string referring
     *  to an xmlMapFile and 
     * @param xmlMapFile - A string representing an XML
     *  in the XML directory
     * @return gameLevel - A representation of a game
     *  level with its rules, landons and map
     */
    public GameLevel toLevel(String xmlMapFile){

        Position position = null;
        int nrOfLevels = 0;
        int nrOfRows = 0;
        int nrOfTiles = 0;
        String tileType = null;

        Document levelsDoc = null;
        Element eElementRow = null;
        Element eElementLevel = null;
        NodeList eElementsTiles = null;
        Element aTileElement = null;
        Node nNodeLevel = null;

        int attackerCredit = 0;
        int defenderCredit = 0;
        int scoreGoal = 0;
        int timeToFinish = 0;
        int nrOfTemplates = 0;
        String levelName = new String("default");

        HashMap<AreaPosition, Tile> levelMap = 
                new HashMap<AreaPosition, Tile>();

        LandOnAreaCreator landOnAreaCreator = new LandOnAreaCreator();

        System.out.println("----------------------------");
        levelsDoc = createDocument(xmlMapFile);
        NodeList levelList = levelsDoc.getElementsByTagName(LEVEL);
        nrOfLevels = levelsDoc.getElementsByTagName(LEVEL).getLength();

        //For every level
        for (int levelIndex = 0; levelIndex < nrOfLevels; levelIndex++) {

            nNodeLevel = levelList.item(levelIndex); 
            System.out.println("\nCurrent Element :"
                    + nNodeLevel.getNodeName());

            if (nNodeLevel.getNodeType() == Node.ELEMENT_NODE){

                eElementLevel = (Element) nNodeLevel; 

                nrOfRows = eElementLevel.getElementsByTagName(ROW).getLength();                
                NodeList rowNodeList = eElementLevel.getElementsByTagName(ROW);

                //get the name of the gameLevel
                levelName = eElementLevel.getAttribute(LVLNAME);
                //get rules for the gameLevel					
                attackerCredit = Integer.parseInt
                        (eElementLevel.getElementsByTagName("attackerCredit").
                                item(0).getTextContent());
                defenderCredit = Integer.parseInt
                        (eElementLevel.getElementsByTagName("defenderCredit").
                                item(0).getTextContent());
                scoreGoal = (Integer.parseInt
                        (eElementLevel.getElementsByTagName
                                ("attackingPlayerScoreGoal").item(0).
                                getTextContent()));
                timeToFinish = Integer.parseInt(eElementLevel.
                        getElementsByTagName("timeToFinish").item(0).
                        getTextContent());
                nrOfTemplates = Integer.parseInt(eElementLevel.
                        getElementsByTagName("nrOfTemplates").item(0).
                        getTextContent());

                //For every row
                for (int rowIndex = 0; rowIndex < nrOfRows; rowIndex++) {                      

                    if(rowNodeList.item(rowIndex).getNodeType()
                            == Node.ELEMENT_NODE)
                    {
                        //Get the current row to be read and get its tiles +
                        //nr of tiles
                        eElementRow = (Element) rowNodeList.item(rowIndex);
                        eElementsTiles = eElementRow.getElementsByTagName(TILE);
                        nrOfTiles = eElementsTiles.getLength();                        							

                        //For every tile
                        for (int tileIndex = 0; tileIndex <nrOfTiles;
                                tileIndex++) {

                            //take out the target tile and cast to an element
                            aTileElement = (Element) eElementsTiles.
                                    item(tileIndex);	

                            //create a position out of the rowIndex and
                            //tileIndex.position = new Position(rowIndex * 50, 
                            //tileIndex * 50);
                            position = new Position(rowIndex, tileIndex);
                            AreaPosition areaPosition = new AreaPosition
                                    (position, 50, 50);
                            tileType = eElementsTiles.item(tileIndex).
                                    getTextContent();                    

                            ValidPath validPath = null;

                            validPath = ValidPath.getEnumByString
                                    (aTileElement.getAttribute(DIRECTION));

                            //check what kind of tile it is and put a
                            // tile of that type into the gameLevel map
                            //at that position

                            //TELEPOROTOTOT
                            if(tileType.equals("V") || tileType.equals("W")){

                                if(aTileElement.hasAttribute(LANDON)){                                    
                                    levelMap.put(areaPosition,
                                            (Tile)landOnAreaCreator.
                                            CreateTileDynamically(aTileElement.
                                                    getAttribute(LANDON), position));                                    
                                }
                                else if(tileType.equals("W")){

                                    levelMap.put(areaPosition,
                                            new WallTile(position, tileType));
                                }
                                else if(tileType.equals("V")){

                                    levelMap.put(areaPosition,
                                            new WallTile(position, tileType));
                                }
                            }
                            else if(tileType.equals("P")||
                                    tileType.equals("S")||
                                    tileType.equals("G")){
                                if(aTileElement.hasAttribute(LANDON)){                        

                                    PathTile pathTile = (PathTile) 
                                            landOnAreaCreator.
                                            CreatePathTileDynamically(
                                                    aTileElement.
                                                    getAttribute(LANDON),
                                                    position, validPath);

                                    levelMap.put(areaPosition,
                                            pathTile);             
                                }
                                else{
                                    levelMap.put(areaPosition,
                                            new PathTile(position, validPath));
                                }
                            }
                            //not sure on this else, it makes sure that
                            //the hashMap is always created though..
                            /*else{levelMap.put(areaPosition,
                                    new VoidTile(position));
                            }*/
                        }
                    }
                }
            }
        }
        //atm only the last level in the xml file is returned..
        return new GameLevel(scoreGoal, levelMap, levelName,
                attackerCredit, defenderCredit, timeToFinish, nrOfTemplates);
    }

    /**
     * Author: Jan Nylén
     * @param xmlFile
     * @param validationFile
     * @return
     */
    public boolean validateXmlAgainstXsds(String xmlFile,
            String validationFile)  
    {
        SchemaFactory schemaFactory = SchemaFactory.
                newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        File fXMLFIle = new File(xmlFile);
        File fValidationFile = new File(validationFile);

        Schema schema = null;

        try {
            schema = schemaFactory.newSchema(fValidationFile);
        } catch (SAXException e) {
            // Couldnt open the validation file
            return false;
        }

        Validator validator = schema.newValidator();

        try {
            validator.validate(new StreamSource(fXMLFIle));
        } catch (SAXException e) {
            //something 
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            //couldnt open the xml file
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * @return
     */
    public ArrayList<String> getLvlNames(){  

        Document levelsDoc = null;
        NodeList nNodeLevelList = null;

        ArrayList<String> gameLevelNames = new ArrayList<String>();
        levelsDoc = createDocument(XMLFile);

        nNodeLevelList = levelsDoc.getElementsByTagName(LEVEL); 

        for(int i=0; i<nNodeLevelList.getLength(); i++ ){
            //GETS THE NAME WRONG
            //gameLevelNames.add(nNodeLevelList.item(i).getAttribute("name");
        }        
        return gameLevelNames;        
    }

    /**
     * @param lvlName The name attribute of a level as
     *  specified in the levelmap
     * @return a gameLevel specified in the xmlFile sent in as a
     *  parameter in the constructor
     *  
     *  funkar inte med string, use attribute - ID instead
     */
    public GameLevel getLevelByName(String lvlName){

        // temp holders
        Document levelsDoc = null;
        Element eElementRow = null;
        Element eElementLevel = null;
        NodeList eElementsTiles = null;
        Element aTileElement = null;
        
        Position position = null;
        int nrOfRows = 0;
        int nrOfTiles = 0;
        String tileType = null;

        int attackerCredit = 0;
        int defenderCredit = 0;
        int scoreGoal = 0;
        int timeToFinish = 0;
        int nrOfTemplates = 0;
        String levelName = new String("default");

        HashMap<AreaPosition, Tile> levelMap = 
                new HashMap<AreaPosition, Tile>();

        LandOnAreaCreator landOnAreaCreator = new LandOnAreaCreator();

        levelsDoc = createDocument(XMLFile);
        
        
        //levelsDoc.getElementsByTagName(lvlName);
        
        //System.out.println("test byName");

        eElementLevel = levelsDoc.getElementById(lvlName);
        System.out.println("test ID");
        nrOfRows = eElementLevel.getElementsByTagName(ROW).getLength(); 
        System.out.println("test ID2");
        NodeList rowNodeList = eElementLevel.getElementsByTagName(ROW);
        System.out.println("test ID3");

        //get the name of the gameLevel
        
        System.out.println("test beforeLvlName");
        levelName = eElementLevel.getAttribute(LVLNAME);
        
        System.out.println("test levelName:" + levelName);
        //get rules for the gameLevel                   
        attackerCredit = Integer.parseInt(eElementLevel.
                getElementsByTagName("attackerCredit").item(0)
                .getTextContent());
        defenderCredit = Integer.parseInt(eElementLevel.
                getElementsByTagName("defenderCredit").item(0)
                .getTextContent());
        scoreGoal = (Integer.parseInt(eElementLevel.
                getElementsByTagName("attackingPlayerScoreGoal").item(0)
                .getTextContent()));
        timeToFinish = Integer.parseInt(eElementLevel.
                getElementsByTagName("timeToFinish").item(0)
                .getTextContent());
        nrOfTemplates = Integer.parseInt(eElementLevel.
                getElementsByTagName("nrOfTemplates").item(0)
                .getTextContent());

        //For every row in a level
        for (int rowIndex = 0; rowIndex < nrOfRows; rowIndex++) {                      

            if(rowNodeList.item(rowIndex).getNodeType()
                    == Node.ELEMENT_NODE)
            {
                // return null;
            }

            //Get the current row to be read and get its tiles +
            //nr of tiles
            eElementRow = (Element) rowNodeList.item(rowIndex);
            eElementsTiles = eElementRow.getElementsByTagName(TILE);
            nrOfTiles = eElementsTiles.getLength();                                                 

            //For every tile
            for (int tileIndex = 0; tileIndex <nrOfTiles; tileIndex++){

                //take out the target tile and cast to an element
                aTileElement = (Element) eElementsTiles.
                        item(tileIndex);    

                position = new Position(rowIndex, tileIndex);
                AreaPosition areaPosition = new AreaPosition(position, 50, 50);
                tileType = eElementsTiles.item(tileIndex).getTextContent();                    

                ValidPath validPath = null;

                validPath = ValidPath.getEnumByString
                        (aTileElement.getAttribute(DIRECTION));

                //check what kind of tile it is and put a
                // tile of that type into the gameLevel map
                //at that position

                //TELEPOROTOTOT

                //If it is a void or wall tile
                if(tileType.equals("V") || tileType.equals("W")){
                    if(aTileElement.hasAttribute(LANDON)){                                    
                        levelMap.put(areaPosition,
                                (Tile)landOnAreaCreator
                                .CreateTileDynamically(aTileElement
                                        .getAttribute(LANDON), position));                                    
                    }
                    else if(tileType.equals("W")){

                        levelMap.put(areaPosition,
                                new WallTile(position, tileType));
                    }
                    else if(tileType.equals("V")){

                        levelMap.put(areaPosition,
                                new WallTile(position, tileType));
                    }
                }
                // If it is a kind of pathTile
                else if(tileType.equals("P")||
                        tileType.equals("S")||
                        tileType.equals("G")){
                    if(aTileElement.hasAttribute(LANDON)){                        

                        PathTile pathTile = (PathTile)landOnAreaCreator
                                .CreatePathTileDynamically(aTileElement
                                        .getAttribute(LANDON), position,
                                        validPath);

                        levelMap.put(areaPosition,
                                pathTile);             
                    }
                    else{
                        levelMap.put(areaPosition,
                                new PathTile(position, validPath));
                    }
                }
                //not sure on this else, it makes sure that
                //the hashMap is always created though..
                else{
                    //create any tile or say that the tileType is undefined...
                    //levelMap.put(areaPosition, new VoidTile(position));
                    //return null
                }
            }
            //}
            // else return null;??
        }
        return new GameLevel(scoreGoal, levelMap, levelName,
                attackerCredit, defenderCredit, timeToFinish, nrOfTemplates);
    }
}


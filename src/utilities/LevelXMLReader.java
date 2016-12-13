package utilities;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;

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
import tiles.EnterTileEffect;
import tiles.GoalTile;
import tiles.PathTile;
import tiles.PathTile.ValidPath;
import tiles.StartTile;
import tiles.TeleportTile;
import tiles.Tile;
import tiles.TowerTile;
import tiles.VoidTile;
import tiles.WallTile;


public class LevelXMLReader{

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
    /** The toLevel method takes in a a string referring to an xmlMapFile and 
     * @param xmlMapFile - A string representing an XML in the XML directory
     * @return gameLevel - A representation of a game level with its rules, landons and map
     */
    public GameLevel toLevel(String xmlMapFile){

        Position position = null;
        int nrOfLevels = 0;
        int nrOfRows = 0;
        int nrOfTiles = 0;
        String tileType = null;

        Element eElementRow = null;
        Element eElementLevel = null;
        NodeList eElementsTiles = null;
        Element aTileElement = null;
        Node nNodeLevel = null;

        LandOnAreaCreator landOnAreaCreator = new LandOnAreaCreator(); 

        ArrayList gameLevelArrayList = new ArrayList<GameLevel>();
        GameLevel gameLevel = new GameLevel();

        File inputFile = new File(xmlMapFile);
        DocumentBuilderFactory dbFactory
        = DocumentBuilderFactory.newInstance();

        DocumentBuilder dBuilder = null;

        try {
            dBuilder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Document doc = null;
        try {
            doc = dBuilder.parse(inputFile);
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        doc.getDocumentElement().normalize();

        System.out.println("Root element :"
                + doc.getDocumentElement().getNodeName());

        NodeList levelList = doc.getElementsByTagName("level");
        System.out.println("----------------------------");

        nrOfLevels = doc.getElementsByTagName("level").getLength();

        //For every level
        for (int levelIndex = 0; levelIndex < nrOfLevels; levelIndex++) {

            nNodeLevel = levelList.item(levelIndex);      

            System.out.println("\nCurrent Element :"
                    + nNodeLevel.getNodeName());				

            if (nNodeLevel.getNodeType() == Node.ELEMENT_NODE) {

                eElementLevel = (Element) nNodeLevel;      

                //set rules for the gameLevel					
                gameLevel.setAttackerCredit(Integer.parseInt
                        (eElementLevel.getElementsByTagName("attackerCredit").
                                item(0).getTextContent()));
                gameLevel.setDefenderCredit(Integer.parseInt
                        (eElementLevel.getElementsByTagName("defenderCredit").
                                item(0).getTextContent()));
                gameLevel.setAttackingPlayerScoreGoal(Integer.parseInt
                        (eElementLevel.getElementsByTagName
                                ("attackingPlayerScoreGoal").item(0).
                                getTextContent()));
                gameLevel.setTimeToFinish(Integer.parseInt(eElementLevel.
                        getElementsByTagName("timeToFinish").item(0).
                        getTextContent()));
                gameLevel.setNrOfTemplates(Integer.parseInt(eElementLevel.
                        getElementsByTagName("nrOfTemplates").item(0).
                        getTextContent()));

                nrOfRows = eElementLevel.getElementsByTagName("row").getLength();

                System.out.println("NrOfRows : " + nrOfRows);
                NodeList rowNodeList = eElementLevel.getElementsByTagName("row");

                System.out.println("Level name : " + eElementLevel.getAttribute("name"));                   

                //For every row
                for (int rowIndex = 0; rowIndex < nrOfRows; rowIndex++) {                      

                    if(rowNodeList.item(rowIndex).getNodeType() == Node.ELEMENT_NODE)
                    {
                        //Get the element to be read and get the tiles + nr of tiles
                        eElementRow = (Element) rowNodeList.item(rowIndex);
                        eElementsTiles = eElementRow.getElementsByTagName("tile");
                        nrOfTiles = eElementsTiles.getLength();                        							

                        //For every tile
                        for (int tileIndex = 0; tileIndex <nrOfTiles; tileIndex++) {

                            //take out the target tile and cast to an element
                            aTileElement = (Element) eElementsTiles.item(tileIndex);	

                            //create a position out of the rowIndex and tileIndex
                            //position = new Position(rowIndex * 50, tileIndex * 50);                            
                            AreaPosition areaPosition = new AreaPosition(rowIndex, tileIndex, 50, 50);
                            tileType = eElementsTiles.item(tileIndex).getTextContent();                                                     

                            //check what kind of tile it is and put a
                            // tile of that type into the gameLevel map
                            //at that position

                            if(tileType.equals("W")){
                                gameLevel.getLevelMap().put(areaPosition, new WallTile(position, tileType));
                            }else if(tileType.equals("P")){
                                
                                if(aTileElement.hasAttribute("landOn")){                                

                                    try {       
                                        //casta till den pathTile. 
                                        // aClassName.cast
                                        
                                        aTileElement.getAttribute("direction");
                                        
                                        ValidPath validPath = PathTile.ValidPath.CROSSROAD;
                                        
                                        Class<?> landOnClass = Class.forName("tiles." + 
                                                aTileElement.getAttribute("landOn"));

                                        PathTile pathTile = (PathTile) landOnAreaCreator.CreateTileDynamically(
                                                landOnClass, areaPosition);  

                                        System.out.println("created a landOn dynamically");

                                        gameLevel.getLevelMap().put(areaPosition,
                                                new TeleportTile(areaPosition, null));

                                    } catch (ClassNotFoundException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }              

                                }else{
                                    gameLevel.getLevelMap().put(position, new PathTile(areaPosition, tileType, validPath));
                                }	
                            }
                            else if(tileType.equals("S")){gameLevel.getLevelMap().put(position, new StartTile(position, tileType ,validPath));
                            }
                            else if(tileType.equals("G")){gameLevel.getLevelMap().put(position, new GoalTile(position, tileType ,validPath));
                            }
                            else{gameLevel.getLevelMap().put(position, new VoidTile(position));
                            }
                        }
                    }
                }

                //input each gamelevel into the arraylist of gameLevels to be returned
                //gameLevelArrayList[level] = gameLevel;
            }
        }
        //atm only the last level in the xml file is returned..

        return gameLevel;
    }

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



}


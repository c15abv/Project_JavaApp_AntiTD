package utilities;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import start.GameLevel;
import start.Position;
import tiles.EnterTileEffect;
import tiles.GoalTile;
import tiles.PathTile;
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
        if(reader.validateXmlAgainstXsds("XML/Levels.xml", "XML/LevelsXMLSchema.xsd")){
            reader.toLevel("XML/Levels.xml");
        }
    }
    /**
     * @param xmlMapFile - A string representing an XML in the XML directory
     * @return gameLevel - A representation of a game level with its rules, landons and map
     */
    public GameLevel toLevel(String xmlMapFile){

        Position position = null;
        int nrOfLevels = 0;
        int nrOfRows = 0;
        int nrOfTiles = 0;
        //int nrOfLandOns = 0;
        String tileType = null;

        Element eElementRow = null;
        Element eElementLevel = null;
        NodeList eElementsTile = null;
        Element eElementsTile2 = null;

        GameLevel gameLevel = new GameLevel();

        try {  
            File inputFile = new File(xmlMapFile);
            DocumentBuilderFactory dbFactory
            = DocumentBuilderFactory.newInstance();

            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            System.out.println("Root element :"
                    + doc.getDocumentElement().getNodeName());

            NodeList levelList = doc.getElementsByTagName("level");
            System.out.println("----------------------------");

            nrOfLevels = doc.getElementsByTagName("level").getLength();

            //For every level
            for (int level = 0; level < nrOfLevels; level++) {

                Node nNodeLevel = levelList.item(level);      

                System.out.println("\nCurrent Element :"
                        + nNodeLevel.getNodeName());				

                if (nNodeLevel.getNodeType() == Node.ELEMENT_NODE) {

                    eElementLevel = (Element) nNodeLevel;            

                    //set rules for the gameLevel					
                    gameLevel.setAttackerCredit(Integer.parseInt(eElementLevel.getElementsByTagName("attackerCredit").item(0).getTextContent()));
                    gameLevel.setDefenderCredit(Integer.parseInt(eElementLevel.getElementsByTagName("defenderCredit").item(0).getTextContent()));
                    gameLevel.setAttackingPlayerScoreGoal(Integer.parseInt(eElementLevel.getElementsByTagName("attackingPlayerScoreGoal").item(0).getTextContent()));
                    gameLevel.setTimeToFinish(Integer.parseInt(eElementLevel.getElementsByTagName("timeToFinish").item(0).getTextContent()));
                    gameLevel.setNrOfTemplates(Integer.parseInt(eElementLevel.getElementsByTagName("nrOfTemplates").item(0).getTextContent()));

                    //set landOns
                    /*nrOfLandOns = eElementLevel.getElementsByTagName("landOn").getLength();
					for(int landOnIndex = 0; landOnIndex < nrOfLandOns; landOnIndex++)
					{
						gameLevel.getLandOnFiles().add(eElementLevel.getElementsByTagName("landOn").item(landOnIndex).getTextContent().trim());
					}*/
                    
                    nrOfRows = eElementLevel.getElementsByTagName("row").getLength();

                    System.out.println("NrOfRows : " + nrOfRows);
                    NodeList rowNodeList = eElementLevel.getElementsByTagName("row");

                    System.out.println("Level name : " + eElementLevel.getAttribute("name"));                   
                    

                    //For every row
                    for (int rowIndex = 0; rowIndex < nrOfRows; rowIndex++) {                      

                        if(rowNodeList.item(rowIndex).getNodeType() == Node.ELEMENT_NODE)
                        {
                            eElementRow = (Element) rowNodeList.item(rowIndex);
                            nrOfTiles = eElementRow.getElementsByTagName("tile").getLength();
                            eElementsTile = eElementRow.getElementsByTagName("tile");							

                            //For every cell
                            for (int tileIndex = 0; tileIndex <nrOfTiles; tileIndex++) {

                                eElementsTile2 = (Element) eElementsTile.item(tileIndex);		

                                position = new Position(rowIndex * 50, tileIndex * 50);							
                                tileType = eElementsTile.item(tileIndex).getTextContent();

                                if(tileType.equals("W")){
                                    gameLevel.getLevelMap().put(position, new WallTile(position, tileType));
                                }else if(tileType.equals("P")){	

                                    if(eElementsTile2.getAttribute("landOn")== "teleport"){
                                        gameLevel.getLevelMap().put(position, new TeleportTile(position));
                                    }else{
                                        gameLevel.getLevelMap().put(position, new PathTile(position, tileType, eElementsTile2.getAttribute("landOn")));
                                    }	

                                }else if(tileType.equals("T")){
                                    gameLevel.getLevelMap().put(position, new TowerTile(position, tileType));
                                }else if(tileType.equals("S")){
                                    gameLevel.getLevelMap().put(position, new StartTile(position, tileType));
                                }else if(tileType.equals("G")){
                                    gameLevel.getLevelMap().put(position, new GoalTile(position, tileType));
                                }else{
                                    gameLevel.getLevelMap().put(position, new VoidTile(position));
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            //hantera fler fel
            // have to change the whole method to handle exceptions better
        }
        //atm only the last level in the xml file is returned..
        
        return gameLevel;
    }

    public boolean validateXmlAgainstXsds(String xmlFile, String validationFile)  
    {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

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


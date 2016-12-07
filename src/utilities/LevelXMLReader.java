package utilities;

import java.io.File;
import java.io.IOException;
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
import tiles.GoalTile;
import tiles.PathTile;
import tiles.StartTile;

import tiles.TowerTile;

import tiles.WallTile;


public class LevelXMLReader{

	public LevelXMLReader(/*some xml file*/){

	}
	
	public static void main(String[] args){
		LevelXMLReader reader = new LevelXMLReader();
		if(reader.validateXmlAgainstXsds("XML/Levels.xml", "XML/LevelsXMLSchema.xsd")){
		reader.toLevel("XML/Levels.xml");
		}

	}

	/*private void load(){

	}*/

	//
	public GameLevel toLevel(String xmlMapFile){

		Position position = null;
		int nrOfLevels = 0;
		int nrOfRows = 0;
		int nrOfCells = 0;
		int nrOfLandOns = 0;
		String tileType = null;

		Element eElementRow = null;
		Element eElementLevel = null;
		NodeList eElementsCell = null;
		
		Element eElementsCell2 = null;
		
		GameLevel gameLevel = new GameLevel();

		WallTile wallTile = null;
		PathTile pathTile = null;
		TowerTile towerTile = null;
		StartTile startTile = null;
		GoalTile goalTile = null;		
		
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

			for (int level = 0; level < nrOfLevels; level++) {

				Node nNodeLevel = levelList.item(level);      

				System.out.println("\nCurrent Element :"
						+ nNodeLevel.getNodeName());				

				if (nNodeLevel.getNodeType() == Node.ELEMENT_NODE) {

					eElementLevel = (Element) nNodeLevel;                

					System.out.println("Level name : " + eElementLevel.getAttribute("name"));					
					nrOfRows = eElementLevel.getElementsByTagName("row").getLength();

					//set rules for the gameLevel
					gameLevel.setAttackerCredit(Integer.parseInt(eElementLevel.getElementsByTagName("attackerCredit").item(0).getTextContent()));
					gameLevel.setDefenderCredit(Integer.parseInt(eElementLevel.getElementsByTagName("defenderCredit").item(0).getTextContent()));
					gameLevel.setAttackingPlayerScoreGoal(Integer.parseInt(eElementLevel.getElementsByTagName("attackingPlayerScoreGoal").item(0).getTextContent()));
					gameLevel.setTimeToFinish(Integer.parseInt(eElementLevel.getElementsByTagName("timeToFinish").item(0).getTextContent()));					
					
					//set landOns
					nrOfLandOns = eElementLevel.getElementsByTagName("landOn").getLength();
					
					for(int landOnIndex = 0; landOnIndex < nrOfLandOns; landOnIndex++)
					{
						gameLevel.getLandOnFiles().add(eElementLevel.getElementsByTagName("landOn").item(landOnIndex).getTextContent().trim());
						
							
					}
					
					System.out.println(gameLevel.getLandOnFiles().get(0).length());
				
					
					System.out.println("NrOfRows : " + nrOfRows);
					NodeList rowNodeList = eElementLevel.getElementsByTagName("row");

					for (int rowIndex = 0; rowIndex < nrOfRows; rowIndex++) {                      

						if(rowNodeList.item(rowIndex).getNodeType() == Node.ELEMENT_NODE)
						{
							eElementRow = (Element) rowNodeList.item(rowIndex);
							nrOfCells = eElementRow.getElementsByTagName("cell").getLength();
							
							eElementsCell = eElementRow.getElementsByTagName("cell");							

							for (int cellIndex = 0; cellIndex <nrOfCells; cellIndex++) {

								eElementsCell2 = (Element) eElementRow.getElementsByTagName("cell").item(cellIndex);		
										
								position = new Position(rowIndex, cellIndex);							
								tileType = eElementsCell.item(cellIndex).getTextContent();								
							    
								if(tileType.equals("W")){
									wallTile = new WallTile(position, tileType);
									gameLevel.getLevelMap().put(position, wallTile);
								}else if(tileType.equals("P")){									
									pathTile = new PathTile(position, tileType, eElementsCell2.getAttribute("landOn"));
									
									System.out.println("land on: " + eElementsCell2.getAttribute("landOn"));								
	
									gameLevel.getLevelMap().put(position, pathTile);									
								}else if(tileType.equals("T")){
									towerTile = new TowerTile(position, tileType);
									gameLevel.getLevelMap().put(position, towerTile);
								}else if(tileType.equals("S")){
									startTile = new StartTile(position, tileType);
									gameLevel.getLevelMap().put(position, startTile);
								}else if(tileType.equals("G")){
									goalTile = new GoalTile(position, tileType);
									gameLevel.getLevelMap().put(position, goalTile);
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			//hantera fler fel
		}
		return gameLevel;
	}
	public boolean validateXmlAgainstXsds(String xmlFile, String validationFile)  
    {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
 
        Schema schema = null;
        try {
            schema = schemaFactory.newSchema( new File(validationFile));
        } catch (SAXException e) {
           return false;
        }
        Validator validator = schema.newValidator();
 
        try {
            validator.validate(new StreamSource(new File(xmlFile)));
        } catch (SAXException e) {
        	System.out.println("Fel i xmlfilen");
        	return false;
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}


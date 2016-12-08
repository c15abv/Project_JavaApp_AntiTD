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
import tiles.EnterTileEffect;
import tiles.GoalTile;
import tiles.PathTile;
import tiles.StartTile;
import tiles.TeleportTile;
import tiles.TowerTile;
import tiles.VoidTile;
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

	/**
	 * @param className takes in a String representing a (landOn) class and then loads it.
	 */ // create tile from Class
	private void load(String className){
		//ta in namnet på klassSträngen
		//instantiera klassen
		//Kolla så att den implementerar EnterTileEffect
		
		//kontrollera att den har en metod som heter LandOn		
		//hämt konstruktorn (kolla namnet på konstruktorn?? --> att den slutar med Tile)
		//skapa ett objekt av klassen (som kommer vara t.ex hasteTile) och casta till en (tile)
		//returnera tile
		
		
		//skicka in tile i gamelevel i toLevel
		
		
		
		try {
			Class<?> classFromInput = Class.forName(className);
			
			Object myClassObject = classFromInput.newInstance();
			
			if(myClassObject instanceof EnterTileEffect){
				
			}
	
			
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 	

	}
	
	private void checkIfclassImplementsInterface(String className){
		try {
			Class<?> classFromInput = Class.forName(className);
			
			Object myClassObject = classFromInput.newInstance();
	
			
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 	

	}
	
	

	//
	/**
	 * @param xmlMapFile
	 * @return gameLevel - A representation of a game level with its rules, landons and map
	 */
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

					System.out.println("NrOfRows : " + nrOfRows);
					NodeList rowNodeList = eElementLevel.getElementsByTagName("row");

					for (int rowIndex = 0; rowIndex < nrOfRows; rowIndex++) {                      

						if(rowNodeList.item(rowIndex).getNodeType() == Node.ELEMENT_NODE)
						{
							eElementRow = (Element) rowNodeList.item(rowIndex);
							nrOfCells = eElementRow.getElementsByTagName("cell").getLength();

							eElementsCell = eElementRow.getElementsByTagName("cell");							

							for (int cellIndex = 0; cellIndex <nrOfCells; cellIndex++) {

								eElementsCell2 = (Element) eElementsCell.item(cellIndex);		

								position = new Position(rowIndex * 50, cellIndex * 50);							
								tileType = eElementsCell.item(cellIndex).getTextContent();

								if(tileType.equals("W")){
									gameLevel.getLevelMap().put(position, new WallTile(position, tileType));
								}else if(tileType.equals("P")){	

									if(eElementsCell2.getAttribute("landOn")== "teleport"){
										gameLevel.getLevelMap().put(position, new TeleportTile(position));
									}else{
										gameLevel.getLevelMap().put(position, new PathTile(position, tileType, eElementsCell2.getAttribute("landOn")));
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


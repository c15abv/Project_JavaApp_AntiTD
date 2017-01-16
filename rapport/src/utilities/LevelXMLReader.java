/*
 * LevelXMlReader
 *
 * Version 1.1
 *
 * Copyright grupp 9 antiTD apjava ht 16
 */

package utilities;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
 * LevelXMLReader is used to parse an XML file following the LevelsXMLSchema.xsd
 * into a GameLevel.
 *
 * @author Jan Nylen, Alexander Ekstrom
 */
public class LevelXMLReader {
	// elements
	public static final String LEVEL = "level";
	public static final String ROW = "row";
	public static final String TILE = "tile";
	// attributes
	public static final String LANDON = "landOn";
	public static final String DIRECTION = "direction";
	public static final String LVLNAME = "nameID";
	public static final String PATH_LEVELS = "Levels.xml";
	public static final String PATH_XML_SCHEMA = "LevelsXMLSchema.xsd";

	private String XMLFile;
	private String XMLSchema = PATH_XML_SCHEMA;
	private ArrayList<GameLevel> GameLevels;
	private boolean customXMLFile = false;

	/**
	 * Constructor that initiates the LevelXMLReader object with default levels.
	 */
	public LevelXMLReader() {
		GameLevels = new ArrayList<GameLevel>();
		this.XMLFile = PATH_LEVELS;
	}

	/**
	 * Constructor that initiates the LevelXMLReader object with custom levels.
	 *
	 * @param XMLFile
	 *            Path to the XML file that should contain the game levels.
	 */
	public LevelXMLReader(String XMLFile) {
		this.XMLFile = XMLFile;
		this.customXMLFile = true;
	}

	/**
	 * A method used to extract an element as specified by its name from the
	 * input NodeList
	 *
	 * @param levelList
	 *            A nodelist consisting levelNodes
	 * @param lvlName
	 *            A string specifying a node in the nodelist
	 *
	 * @return Element, returns the node specified by the input casted to an
	 *         element String lvlName from the NodeList. Returns null if the
	 *         levelList is empty or if it doesnt have a node as specified by
	 *         the string argument
	 */
	public Element getLevelElementFromNodeList(NodeList levelList,
			String lvlName) {

		Element eElementLevel = null;
		for (int levelIndex = 0; levelIndex < levelList
				.getLength(); levelIndex++) {

			Node nNodeLevel = levelList.item(levelIndex);

			if (nNodeLevel.getNodeType() == Node.ELEMENT_NODE) {

				eElementLevel = (Element) nNodeLevel;

				if (eElementLevel.getAttribute("nameID").equals(lvlName)) {

					return eElementLevel;
				}
			}
		}
		return null;
	}

	/**
	 * The toLevel method takes in a a string referring to an xmlMapFile and
	 * returns an arrayList of all levels in it. The level elements are listed
	 * in the order that they are given in the XML file.
	 *
	 * @param xmlMapFile
	 *            A string representing an XML in the XML directory
	 * @return ArrayList An arraylist of gameLevels
	 * @throws IOException
	 *             IOException
	 * @throws SAXException
	 *             SAXException
	 * @throws ParserConfigurationException
	 *             ParserConfigurationException
	 */
	public ArrayList<GameLevel> toLevel(String xmlMapFile)
			throws ParserConfigurationException, SAXException, IOException {

		Position position = null;
		ValidPath validPath = null;
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

		HashMap<AreaPosition, Tile> levelMap = new HashMap<AreaPosition, Tile>();

		LandOnAreaCreator landOnAreaCreator = new LandOnAreaCreator();

		// System.out.println("----------------------------");
		levelsDoc = createDocument();
		NodeList levelList = levelsDoc.getElementsByTagName(LEVEL);

		// For every level
		for (int levelIndex = 0; levelIndex < levelList
				.getLength(); levelIndex++) {

			nNodeLevel = levelList.item(levelIndex);

			if (nNodeLevel.getNodeType() == Node.ELEMENT_NODE) {

				eElementLevel = (Element) nNodeLevel;
				NodeList rowNodeList = eElementLevel.getElementsByTagName(ROW);

				levelName = eElementLevel.getAttribute(LVLNAME);

				// get the name of the gameLevel
				attackerCredit = Integer.parseInt(
						eElementLevel.getElementsByTagName("attackerCredit")
								.item(0).getTextContent());
				defenderCredit = Integer.parseInt(
						eElementLevel.getElementsByTagName("defenderCredit")
								.item(0).getTextContent());
				scoreGoal = (Integer.parseInt(eElementLevel
						.getElementsByTagName("attackingPlayerScoreGoal")
						.item(0).getTextContent()));
				timeToFinish = Integer.parseInt(
						eElementLevel.getElementsByTagName("timeToFinish")
								.item(0).getTextContent());
				nrOfTemplates = Integer.parseInt(
						eElementLevel.getElementsByTagName("nrOfTemplates")
								.item(0).getTextContent());

				// For every row
				for (int rowIndex = 0; rowIndex < rowNodeList
						.getLength(); rowIndex++) {

					if (rowNodeList.item(rowIndex)
							.getNodeType() == Node.ELEMENT_NODE) {
						// Get the current row to be read and get its tiles +
						// nr of tiles
						eElementRow = (Element) rowNodeList.item(rowIndex);
						eElementsTiles = eElementRow.getElementsByTagName(TILE);

						// For every tile
						for (int tileIndex = 0; tileIndex < eElementsTiles
								.getLength(); tileIndex++) {

							// take out the target tile and cast to an element
							aTileElement = (Element) eElementsTiles
									.item(tileIndex);

							// create a position out of the rowIndex and
							// tileIndex.position = new Position(rowIndex * 50,
							// tileIndex * 50);
							position = new Position(tileIndex * Tile.size,
									rowIndex * Tile.size);
							AreaPosition areaPosition = new AreaPosition(
									position, Tile.size, Tile.size);
							tileType = eElementsTiles.item(tileIndex)
									.getTextContent();

							validPath = ValidPath.getEnumByString(
									aTileElement.getAttribute(DIRECTION));

							// check what kind of tile it is and put a
							// tile of that type into the gameLevel map
							// at that position

							// TELEPOROTOTOT
							if (tileType.equals("V") || tileType.equals("W")) {

								/*
								 * if(aTileElement.hasAttribute(LANDON)){
								 * levelMap.put(areaPosition,
								 * (Tile)landOnAreaCreator
								 * .CreateTileDynamically(aTileElement
								 * .getAttribute(LANDON), position)); }
								 */
								if (tileType.equals("W")) {

									levelMap.put(areaPosition,
											new WallTile(position, tileType));
								} else if (tileType.equals("V")) {

									levelMap.put(areaPosition,
											new VoidTile(position));
								}
							} else if (tileType.equals("P")
									|| tileType.equals("S")
									|| tileType.equals("G")) {
								if (aTileElement.hasAttribute(LANDON)) {

									PathTile pathTile = (PathTile) landOnAreaCreator
											.CreatePathTileDynamically(
													aTileElement.getAttribute(
															LANDON),
													position, validPath);

									levelMap.put(areaPosition, pathTile);
								} else {
									levelMap.put(areaPosition,
											new PathTile(position, validPath));
								}
							}
							// not sure on this else, it makes sure that
							// the hashMap is always created though..
							/*
							 * else{levelMap.put(areaPosition, new
							 * VoidTile(position)); }
							 */
						}
					}
				}
			}
			GameLevels.add(new GameLevel(scoreGoal, levelMap, levelName,
					attackerCredit, defenderCredit, timeToFinish, nrOfTemplates,
					0, 0));
		}

		// atm only the last level in the xml file is returned..
		return GameLevels;
	}

	/**
	 * Creates a Document out of an input xmlMapFile
	 *
	 * @author: Jan Nylen
	 *
	 * @return The document created.
	 *
	 * @throws ParserConfigurationException
	 *             ParserConfigurationException
	 * @throws IOException
	 *             IOException
	 * @throws SAXException
	 *             SAXException
	 */
	public Document createDocument()
			throws ParserConfigurationException, SAXException, IOException {

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

		DocumentBuilder dBuilder;

		dBuilder = dbFactory.newDocumentBuilder();

		Document doc;

		if (!customXMLFile) {

			InputStream is;

			is = ClassLoader.getSystemClassLoader()
					.getResourceAsStream(XMLFile);

			doc = dBuilder.parse(is);

			is.close();

		} else {
			File xmlInputFile = new File(XMLFile);

			doc = dBuilder.parse(xmlInputFile);
		}
		return doc;

	}

	/**
	 * Validates an xmlfile against a schema.
	 *
	 * @author: Jan Nylen
	 *
	 * @return true if the file was validated correctly, else false.
	 * @throws SAXException
	 *             SAXException
	 * @throws IOException
	 *             IOException
	 */
	public boolean validateXmlAgainstXsds() throws SAXException, IOException {
		SchemaFactory schemaFactory = SchemaFactory
				.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

		InputStream is = ClassLoader.getSystemClassLoader()
				.getResourceAsStream(XMLSchema);

		Schema schema = schemaFactory.newSchema(new StreamSource(is));

		is = ClassLoader.getSystemClassLoader().getResourceAsStream(XMLFile);

		Validator validator = schema.newValidator();

		validator.validate(new StreamSource(is));

		is.close();

		return true;
	}

	/**
	 * GetLvlNames creates a docuement out of the XMLFile given as input to the
	 * xmlreader constructor.
	 *
	 * @return an arraylist of names
	 * @throws IOException
	 *             IOException
	 * @throws SAXException
	 *             SAXException
	 * @throws ParserConfigurationException
	 *             ParserConfigurationException
	 */
	public ArrayList<String> getLvlNames()
			throws ParserConfigurationException, SAXException, IOException {

		Document levelsDoc = null;
		NodeList nNodeLevelList = null;
		Element nodeElement = null;

		ArrayList<String> gameLevelNames = new ArrayList<String>();

		levelsDoc = createDocument();

		nNodeLevelList = levelsDoc.getElementsByTagName(LEVEL);

		for (int i = 0; i < nNodeLevelList.getLength(); i++) {

			nodeElement = (Element) nNodeLevelList.item(i);

			gameLevelNames.add(nodeElement.getAttribute(LVLNAME));
		}
		return gameLevelNames;
	}

	/**
	 * Method used to read a level element as specified by the string lvlName.
	 *
	 * @param lvlName
	 *            The name attribute of a level as specified in the levelmap
	 * @return a gameLevel with the parameters read from an XMLfile
	 * @throws IOException
	 *             IOException
	 * @throws SAXException
	 *             SAXException
	 * @throws ParserConfigurationException
	 *             ParserConfigurationException
	 *
	 */
	public GameLevel getLevelByName(String lvlName)
			throws ParserConfigurationException, SAXException, IOException {

		// temp holders
		Document levelsDoc = null;
		Element eElementRow = null;
		Element eElementLevel = null;
		NodeList eElementsTiles = null;
		Element aTileElement = null;
		@SuppressWarnings("unused")
		int nrOfGoalTiles = 0;
		@SuppressWarnings("unused")
		int nrOfStartTiles = 0;

		Position position = null;
		String tileType = null;
		ValidPath validPath = null;

		int attackerCredit = 0;
		int defenderCredit = 0;
		int scoreGoal = 0;
		int timeToFinish = 0;
		int nrOfTemplates = 0;
		String levelName = new String();

		HashMap<AreaPosition, Tile> levelMap = new HashMap<AreaPosition, Tile>();

		LandOnAreaCreator landOnAreaCreator = new LandOnAreaCreator();
		levelsDoc = createDocument();

		eElementLevel = getLevelElementFromNodeList(
				levelsDoc.getElementsByTagName(LEVEL), lvlName);

		NodeList rowNodeList = eElementLevel.getElementsByTagName(ROW);

		// get the name of the gameLevel
		levelName = eElementLevel.getAttribute(LVLNAME);

		// get rules for the gameLevel
		attackerCredit = Integer
				.parseInt(eElementLevel.getElementsByTagName("attackerCredit")
						.item(0).getTextContent());
		defenderCredit = Integer
				.parseInt(eElementLevel.getElementsByTagName("defenderCredit")
						.item(0).getTextContent());
		scoreGoal = (Integer.parseInt(
				eElementLevel.getElementsByTagName("attackingPlayerScoreGoal")
						.item(0).getTextContent()));
		timeToFinish = Integer.parseInt(eElementLevel
				.getElementsByTagName("timeToFinish").item(0).getTextContent());
		nrOfTemplates = Integer
				.parseInt(eElementLevel.getElementsByTagName("nrOfTemplates")
						.item(0).getTextContent());

		// For every row in a level
		for (int rowIndex = 0; rowIndex < rowNodeList.getLength(); rowIndex++) {

			if (rowNodeList.item(rowIndex).getNodeType() == Node.ELEMENT_NODE) {
				// Get the current row to be read and get its tiles +
				// nr of tiles
				eElementRow = (Element) rowNodeList.item(rowIndex);
				eElementsTiles = eElementRow.getElementsByTagName(TILE);

				// For every tile
				for (int tileIndex = 0; tileIndex < eElementsTiles
						.getLength(); tileIndex++) {

					// take out the target tile and cast to an element
					aTileElement = (Element) eElementsTiles.item(tileIndex);

					position = new Position(tileIndex * Tile.size,
							rowIndex * Tile.size);
					AreaPosition areaPosition = new AreaPosition(position,
							Tile.size, Tile.size);
					tileType = eElementsTiles.item(tileIndex).getTextContent();
					validPath = ValidPath.getEnumByString(
							aTileElement.getAttribute(DIRECTION));

					// check what kind of tile it is and put a
					// tile of that type into the gameLevel map
					// at that position

					// If it is a void or wall tile
					if (tileType.equals("V") || tileType.equals("W")) {
						/*
						 * if(aTileElement.hasAttribute(LANDON)){
						 * levelMap.put(areaPosition, (Tile)landOnAreaCreator
						 * .CreateTileDynamically(aTileElement
						 * .getAttribute(LANDON), position)); }
						 */
						if (tileType.equals("W")) {

							levelMap.put(areaPosition, new WallTile(position));
						} else if (tileType.equals("V")) {

							levelMap.put(areaPosition, new VoidTile(position));
						}
					}
					// If it is a kind of pathTile
					else if (tileType.equals("P") || tileType.equals("S")
							|| tileType.equals("G")) {
						if (aTileElement.hasAttribute(LANDON)) {

							PathTile pathTile = (PathTile) landOnAreaCreator
									.CreatePathTileDynamically(
											aTileElement.getAttribute(LANDON),
											position, validPath);

							levelMap.put(areaPosition, pathTile);

							if (tileType.equals("S")) {
								nrOfGoalTiles++;
							} else if (tileType.equals("G")) {
								nrOfGoalTiles++;
							}

						} else {
							levelMap.put(areaPosition,
									new PathTile(position, validPath));
						}
					}
				}
			} else {
				return null;
			}
		}
		return new GameLevel(scoreGoal, levelMap, levelName, attackerCredit,
				defenderCredit, timeToFinish, nrOfTemplates,
				eElementsTiles.getLength(), rowNodeList.getLength());

	}
}

/*
 * JUnitXmlReader
 *
 * Version 0.1
 *
 * Copyright grupp 9 antiTD apjava ht 16
 */
package utilities;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import start.GameLevel;

/**
 * @author Jan
 *
 */
public class JUnitXMLReader {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * 
     */
    @Test
    public void testToGameLevel() {        

        LevelXMLReader levelXMLReader= new LevelXMLReader();
        ArrayList<GameLevel> gameLevels = null;

        gameLevels = levelXMLReader.toLevel("XML/Levels.xml");
                
        GameLevel gameLevel = gameLevels.get(0);

        System.out.println("attacker credit: " + gameLevel.getAttackerCredit());
        System.out.println("defender credit: " + gameLevel.getDefenderCredit());
        System.out.println("timetoFinish: " + gameLevel.getTimeToFinish());
        System.out.println("scoregoal attacker: " + gameLevel.getAttackingPlayerScoreGoal());
        System.out.println("nrOfTemplates: " + gameLevel.getNrOfTemplates());
        System.out.println("test ToLevel name: " + gameLevel.getLevelName());

        assertTrue(gameLevel.getAttackerCredit() == 101);
        assertTrue(gameLevel.getDefenderCredit() == 101);
        assertTrue(gameLevel.getTimeToFinish() == 11);
        assertTrue(gameLevel.getAttackingPlayerScoreGoal() == 8);
        assertTrue(gameLevel.getNrOfTemplates() == 7);
        assertTrue(gameLevel.getLevelName().trim().equals("testlevel"));
        
        gameLevel = gameLevels.get(1);

        System.out.println("attacker credit: " + gameLevel.getAttackerCredit());
        System.out.println("defender credit: " + gameLevel.getDefenderCredit());
        System.out.println("timetoFinish: " + gameLevel.getTimeToFinish());
        System.out.println("scoregoal attacker: " + gameLevel.getAttackingPlayerScoreGoal());
        System.out.println("nrOfTemplates: " + gameLevel.getNrOfTemplates());
        System.out.println("test ToLevel name: " + gameLevel.getLevelName());

        assertTrue(gameLevel.getAttackerCredit() == 102);
        assertTrue(gameLevel.getDefenderCredit() == 102);
        assertTrue(gameLevel.getTimeToFinish() == 12);
        assertTrue(gameLevel.getAttackingPlayerScoreGoal() == 5);
        assertTrue(gameLevel.getNrOfTemplates() == 10);
        assertTrue(gameLevel.getLevelName().trim().equals("testlevel2"));
    }

    /**
     * 
     */
    @Test
    public void testMapSize() {
        LevelXMLReader levelXMLReader= new LevelXMLReader();
        ArrayList<GameLevel> gameLevels = null;

        gameLevels = levelXMLReader.toLevel("XML/Levels.xml");

        System.out.print("gameLevel.getLevelMap().size()" + 
        gameLevels.get(0).getLevelMap().size());

        assertTrue(gameLevels.get(0).getLevelMap().size() == 64);
    }

    @Test
    public void TestGetLvlNames() {

        LevelXMLReader levelXMLReader= new LevelXMLReader("XML/Levels.xml");   
        
        ArrayList<String> lvlNames = levelXMLReader.getLvlNames();
        
        System.out.print("JUnit lvlName:" + lvlNames.get(1));
        
        assertTrue(lvlNames.get(1).equals("testlevel2"));

    }
    
    @Test
    public void testLevelByName() {

        LevelXMLReader levelXMLReader= new LevelXMLReader("XML/Levels.xml");
        
        GameLevel gameLevel = new GameLevel();

        gameLevel = levelXMLReader.getLevelByName("testlevel");
        
        System.out.print("Level name junit:" + gameLevel.getLevelName());

        System.out.print("Junit: map size: gameLevel.getLevelMap().size()"
                + gameLevel.getLevelMap().size());
        assertTrue(gameLevel.getLevelMap().size() == 64);

    }
    
    @Test
    public void testValidateXMLagainstXDS() {
        
        LevelXMLReader levelXMLReader= new LevelXMLReader();
        
        assertTrue(levelXMLReader.validateXmlAgainstXsds("XML/Levels.xml",
                "XML/LevelsXMLSchema.xsd"));
    }
    
    @Test
    public void testCreateDocument() {
        
        LevelXMLReader levelXMLReader= new LevelXMLReader("XML/Levels.xml");
        
        assertTrue(levelXMLReader.createDocument("XML/Levels.xml")!= null);
        
        
    }




}

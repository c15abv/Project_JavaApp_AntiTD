package utilities;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import start.GameLevel;

public class JUnitXMLReader {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testloadClass() {
        
        LevelXMLReader levelXMLReader= new LevelXMLReader();
        GameLevel gameLevel = new GameLevel();
        
        gameLevel = levelXMLReader.toLevel("XML/Levels2.xml"); 
                
        System.out.println("attacker credit: " + gameLevel.getAttackerCredit());
        System.out.println("defender credit: " + gameLevel.getDefenderCredit());
        System.out.println("timetoFinish: " + gameLevel.getTimeToFinish());
        System.out.println("scoregoal attacker: " + gameLevel.getAttackingPlayerScoreGoal());
        System.out.println("nrOfTemplates: " + gameLevel.getNrOfTemplates());
        
        
        assertTrue(gameLevel.getAttackerCredit() == 101);
        assertTrue(gameLevel.getDefenderCredit() == 101);
        assertTrue(gameLevel.getTimeToFinish() == 11);
        assertTrue(gameLevel.getAttackingPlayerScoreGoal() == 7);
        assertTrue(gameLevel.getNrOfTemplates() == 7);
        
        
    }
    
    @Test
    public void testMapSize() {
        
        LevelXMLReader levelXMLReader= new LevelXMLReader();
        GameLevel gameLevel = new GameLevel();
        
        gameLevel = levelXMLReader.toLevel("XML/Levels2.xml");
        
        System.out.println("gameLevel.getLevelMap().size()" + gameLevel.getLevelMap().size());
        
        assertTrue(gameLevel.getLevelMap().size() == 64);
        
    }
    
   

}

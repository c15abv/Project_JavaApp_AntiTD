/*
 * JUnitLandOnAreaCreator
 *
 * Version 0.1
 *
 * Copyright grupp 9 antiTD apjava ht 16
 */
package utilities;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import start.AreaPosition;
import start.Position;
import tiles.GoalTile;
import tiles.PathTile;
import tiles.PathTile.ValidPath;
import tiles.TeleportTile;
import tiles.Tile;
import tiles.VoidTile;

public class JUnitLandOnAreaCreator {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * 
     */
    //@Test
    public void testCreatingTileDynamically() {        
        
        LandOnAreaCreator landOnCreator = new LandOnAreaCreator();
        
        Position position = new Position(0, 0);
        
        Object voidTile = landOnCreator.CreateTileDynamically("VoidTile", position);
        
        assertTrue((voidTile instanceof VoidTile ));
    }
    
    /**
     * 
     */
    @Test
    public void testCreatingPathTileDynamically() {        
        
        LandOnAreaCreator landOnCreator = new LandOnAreaCreator();
        
        Position position = new Position(0, 0);
        
        Object goalTile = landOnCreator.CreatePathTileDynamically
                ("GoalTile", position, ValidPath.getEnumByString("HORIZONTAL"));
        
        assertTrue((goalTile instanceof GoalTile ));
    }
    
    /**
     * 
     */
    @Test
    public void testImplements() {        
        
       
       Position position = new Position(0, 0);
       ValidPath validPath = ValidPath.HORIZONTAL;
       
       PathTile pathTile = new PathTile(position, validPath);
       
       LandOnAreaCreator landOnCreator = new LandOnAreaCreator();
              
       //voidTiles kan inte ta landON
        
        assertTrue(landOnCreator.checkIfclassImplEnterTileEffectandTile(pathTile));
    }
    
    
}

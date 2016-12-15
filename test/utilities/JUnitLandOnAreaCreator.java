package utilities;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import start.Position;
import tiles.TeleportTile;

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
    @Test
    public void testCreatingTileDynamically() {        
        
        LandOnAreaCreator landOnCreator = new LandOnAreaCreator();
        
        Position position = new Position(0, 0);
        
        Object teleportTile = landOnCreator.CreateTileDynamically("TeleportTile", position);
        
        assertTrue((teleportTile instanceof TeleportTile ));

                
    }
}

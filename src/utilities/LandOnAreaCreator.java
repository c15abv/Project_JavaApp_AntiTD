package utilities;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import creatures.CreatureFigure;
import start.AreaPosition;
import start.Position;
import tiles.EnterTileEffect;
import tiles.PathTile;
import tiles.TeleportTile;
import tiles.Tile;

/**
 * @author Jan
 * LandOnAreaCreator is used to dynamically load area classes into
 * the AntiTD game.
 */
public class LandOnAreaCreator {    

    /**
     * constructor for the LandOnAreaCreator.
     */
    public LandOnAreaCreator(){

    }

    // main is used for testing
    public static void main(String[] args){

        LandOnAreaCreator landOnCreator = new LandOnAreaCreator();
        AreaPosition areaPosition = new AreaPosition(0, 0, 50, 50);


        Class<?> classFromInput;
        try {
            classFromInput = Class.forName("tiles.TeleportTile");

            TeleportTile teleportTile = (TeleportTile) landOnCreator.
                    CreateTileDynamically(classFromInput, areaPosition);

            if(teleportTile instanceof TeleportTile ){
                System.out.println("it is an instance!");
            }

        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * @param aClassName
     * @param position
     * @return null if the tile couldnt be created, else it returns an object
     * of the tile that was specified in the parameter aClassName
     */
    public Object CreateTileDynamically(Class<?> aClassName, AreaPosition areaPosition){
        //take in the name of the class
        //Instantiate the class 
        try {
            // Get the constructor, create an instance of it.                       
            Constructor<?> constructor = aClassName.getConstructor
                    (Position.class);

            //check so that it implements interface
            if(checkIfclassImplementsEnterTileEffectandTile
                    (constructor.newInstance(areaPosition))){
                //return a tile casted to the class that
                //was specified in the aClassName param
                //return as an object and do the cast in xml reader instead.
                Object object = new Object();
                object = constructor.newInstance(areaPosition);
                
                return  object;
            }                
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            //If constructor cant be found
            e.printStackTrace();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Should be changed to instanceOf pathTile
     * @param myClassObject
     * @return
     */
    public boolean checkIfclassImplementsEnterTileEffectandTile(Object myClassObject){

        if(myClassObject instanceof EnterTileEffect && myClassObject instanceof Tile){
            return true;
        }   

        return false;
    }
}

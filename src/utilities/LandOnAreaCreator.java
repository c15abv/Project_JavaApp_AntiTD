/*
 * LandOnAreaCreator
 *
 * Version 0.1
 *
 * Copyright grupp 9 antiTD apjava ht 16
 */
package utilities;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import start.Position;
import tiles.EnterTileEffect;
import tiles.PathTile;
import tiles.Tile;
import tiles.PathTile.ValidPath;

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

   /* // main is used for testing
    public static void main(String[] args){

        LandOnAreaCreator landOnCreator = new LandOnAreaCreator();
        Position position = new Position(0, 0);
        
        Class<?> classFromInput;
        PathTile pathTile = (PathTile) landOnCreator.
                CreatePathTileDynamically("PathTile", position,
                        ValidPath.getEnumByString("HORIZONTAL"));

        if(pathTile instanceof PathTile ){
        }
    }*/
    
    public Class loadClassDynamically(String className){
        ClassLoader classLoader = LandOnAreaCreator.class.getClassLoader();

        try {
            Class aClass = classLoader.loadClass(className);
            System.out.println("aClass.getName() = " + aClass.getName());
            
            //return theClass;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Used to create a tile of type PathTile dynamiccaly
     * @param aClassName
     * @param position
     * @return null if the tile couldnt be created, else it returns an object
     * of the tile that was specified in the parameter aClassName
  
     */
    public Object CreatePathTileDynamically(String aClassName,
            Position position, ValidPath validPath){
        //take in the name of the class
        //Instantiate the class
        try {
            //the tiles. is where the class has to be
            //change to taking in full filepath instead??
            Class<?> aClass = Class.forName("tiles." + aClassName);
            // Get the constructor, create an instance of it.                       
            Constructor<?> constructor = aClass.getConstructor
                    (Position.class, ValidPath.class);
            
            //check so that it implements interface
            if(checkIfclassImplEnterTileEffectandTile
                    (constructor.newInstance(position, validPath))){
                //return a tile casted to the class that
                //was specified in the aClassName param
                //return as an object and do the cast in xml reader instead.
                Object object = new Object();
                object = constructor.newInstance(position, validPath);
                
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
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // if null cancel read
        //and msg that something is wrong with the levels file
        return null;
    }
    
    /**
     * Used to create a tile of type Tile dynamiccaly
     * @param aClassName
     * @param position
     * @return
     */
    public Object CreateTileDynamically(String aClassName,
            Position position){
        //take in the name of the class
        //Instantiate the class
        try {
            Class<?> aClass = Class.forName("tiles." + aClassName);
            
            // Get the constructor, create an instance of it.                       
            Constructor<?> constructor = aClass.getConstructor
                    (Position.class);

            //check so that it implements interface
            if(checkIfclassImplEnterTileEffectandTile(constructor
                    .newInstance(position))){
                
                //return a tile casted to the class that
                //was specified in the aClassName param
                //return as an object and do the cast in xml reader instead.
                Object object = new Object();
                object = constructor.newInstance(position);
                
                System.out.println("created object");                
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
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // if null cancel read
        //and msg that something is wrong with the levels file
        return null;
    }

    /**
     * Used to test if a class implements the EnterTileEffect LandOn and that it
     * extends the Tile class
     * @param myClassObject
     * @return
     */
    public boolean checkIfclassImplEnterTileEffectandTile(Object myClassObject){

        if(myClassObject instanceof EnterTileEffect && myClassObject instanceof Tile){
            return true;
        }   

        return false;
    }
    
    
}

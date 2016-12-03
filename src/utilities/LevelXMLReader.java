package utilities;

import start.GameLevel;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;

public class LevelXMLReader{
    
    public static void main(String[] args){
        
    }

    public LevelXMLReader(/*some xml file*/){
    }

    public void readXML(){

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        
        Document doc = builder.parse("");
    }

    /*private void load(){

	}*/



    //
    public GameLevel toLevel(){
        return new GameLevel();
    }
}

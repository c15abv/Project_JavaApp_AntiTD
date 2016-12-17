package towers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import towers.AIMemory.AIMemoryLoader.Errors;
import towers.PlanDetails.TowerBuildPlan;

public class AIMemory{

	public static class AIMemoryLoader{
		
		public enum Errors{
			NO_ERROR, INVALID_MEMORY_FORMAT;
		}
		
		public static final String PATH = "res/memory";
		
		private int mapValueSought, loadedSuccessValue, mapValueUsed;
		private String path, loadedMemory, loadedSpecificMemory;
		private ArrayList<TowerBuildPlan> towerPlan;
		private Errors error;
		
		public AIMemoryLoader(int mapValue){
			this.mapValueSought = mapValue;
			this.path = PATH;
			towerPlan = new ArrayList<TowerBuildPlan>();
			error = Errors.NO_ERROR;
			loadedSuccessValue = mapValueUsed = 0;
			loadedMemory = null;
			loadedSpecificMemory = null;
		}
		
		public AIMemoryLoader setPath(String path){
			this.path = path;
			return this;
		}
		
		public AIMemory load() throws IOException{
			loadMemory();
			return new AIMemory(this);
		}
		
		private void loadMemory() throws IOException{
			BufferedReader bufferedReader = 
					new BufferedReader(new FileReader(path));
			StringBuilder stringBuilder;
			String line;
			
			try{
				stringBuilder = new StringBuilder();
			    line = bufferedReader.readLine();

			    while(line != null){
			    	stringBuilder.append(line);
			        line = bufferedReader.readLine();
			    }
			    
			    loadedMemory = stringBuilder.toString();
			    loadStringMemoriesToList(loadedMemory);
			}finally{
				bufferedReader.close();
			}
		}
		
		private void loadStringMemoriesToList(String memories){
			int pos = findBestMatchingMemory(memories);
			
			if(error == Errors.NO_ERROR && pos != -1){
				memoryToTowerPlanStructure();
			}
		}
		
		private int findBestMatchingMemory(String memories){
			int tempMapValue, oldTempMapValue, diff, diffOld;
			String[] mapMemory;
			String[] memoryLineTemp;
			String[] memoryLineHeadTemp;
			int pos = -1;
			oldTempMapValue = 0;
			
			if(memories != null){
				mapMemory = memories.split("\\;");
				
				for(int i = 0; i < mapMemory.length; i++){
					memoryLineTemp = mapMemory[i].split("\\+");
					
					if(memoryLineTemp.length > 0){
						memoryLineHeadTemp = memoryLineTemp[0].split("\\:");
					
						if(memoryLineHeadTemp.length == 2){
							try{
								tempMapValue = Integer.parseInt(memoryLineHeadTemp[0]);
								diffOld = Math.abs(mapValueSought - oldTempMapValue);
								diff = Math.abs(mapValueSought - tempMapValue);
								if(diff <= diffOld){
									mapValueUsed = oldTempMapValue = tempMapValue;
									loadedSpecificMemory = mapMemory[i];
									pos = i;
								}
							}catch(NumberFormatException e){
								error = Errors.INVALID_MEMORY_FORMAT;
							}
						}
					}
				}
			}
			
			return pos;
		}
		
		private void memoryToTowerPlanStructure(){
			PlanDetails details;
			String[] mapMemorySingleTower;
			String[] mapMemoryTemp;
			
			if(loadedSpecificMemory != null){
				mapMemoryTemp = loadedSpecificMemory.split("\\+");
			
				try{
					String[] asd = mapMemoryTemp[0]
							.split("\\:");
					this.loadedSuccessValue = Integer.parseInt(asd[1]);
				}catch(NumberFormatException e){
					error = Errors.INVALID_MEMORY_FORMAT;
				}
				
				for(int i = 1; i < mapMemoryTemp.length; i++){
					mapMemorySingleTower = mapMemoryTemp[i].split("\\:");
					try{
						details = new PlanDetails(
								Integer.parseInt(mapMemorySingleTower[1]),
								Double.parseDouble(mapMemorySingleTower[2]));
						towerPlan.add(details.new TowerBuildPlan(
								Integer.parseInt(mapMemorySingleTower[0]),
								details));
					}catch(NumberFormatException e){
						error = Errors.INVALID_MEMORY_FORMAT;
					}
				}
			}
		}
	}
	
	private int mapValueSought, loadedSuccessValue, mapValueUsed;
	private String path, loadedMemory, loadedSpecificMemory;
	private ArrayList<TowerBuildPlan> towerPlan;
	private Errors error;
	
	private AIMemory(AIMemoryLoader loader){
		this.mapValueSought = loader.mapValueSought;
		this.path = loader.path;
		this.towerPlan = loader.towerPlan;
		this.error = loader.error;
		this.loadedSuccessValue = loader.loadedSuccessValue;
		this.mapValueUsed = loader.mapValueUsed;
		this.loadedMemory = loader.loadedMemory;
		this.loadedSpecificMemory = loader.loadedSpecificMemory;
		
		loadedSpecificMemory += ";";
	}
	
	public ArrayList<TowerBuildPlan> getTowerBuildPlan(){
		return new ArrayList<TowerBuildPlan>(towerPlan);
	}

	public int getSoughtMapValue(){
		return mapValueSought;
	}
	
	public int getUsedMapValue(){
		return mapValueUsed;
	}

	public String getPath(){
		return path;
	}

	public Errors getError(){
		return error;
	}
	
	public void saveMemory(int success, ArrayList<TowerBuildPlan> plans){
		String newSubString = "";
		StringBuilder stringBuilder;
		String newString = "";
		char[] array;
		
		System.out.println(success + " > " + loadedSuccessValue);
		
		if(success > loadedSuccessValue){
			newSubString += "" + mapValueSought + ":" + success;
			
			for(TowerBuildPlan plan : plans){
				newSubString += "+" + plan.getTime() + ":" + 
						plan.getDetails().getNumFigures() + ":" +
						plan.getDetails().getPositionValue();
			}
			
			newSubString += ";";
			
			System.out.println("new memory: " + newSubString);
			System.out.println(mapValueSought + " == " + mapValueUsed);
			
			if(loadedMemory != null && loadedSpecificMemory != null &&
					mapValueSought == mapValueUsed){
				System.out.println("replace");
				newString = loadedSpecificMemory
						.replaceAll("(?=[]\\[+&|!(){}^\"~*?:\\\\-])", "\\\\");
				loadedMemory = loadedMemory.replaceAll(newString, newSubString);
			}else if(loadedMemory != null){
				System.out.println("append");
				stringBuilder = new StringBuilder(loadedMemory);
				stringBuilder.append(newSubString);
				loadedMemory = stringBuilder.toString();
			}else{
				System.out.println("new");
				loadedMemory = newSubString;
			}
			
			try(PrintWriter out = new PrintWriter(path)){
				System.out.println(loadedMemory);
			    out.print(loadedMemory);
			    out.close();
			}catch(FileNotFoundException e){
				System.out.println("not found");
			}
		}
	}
}

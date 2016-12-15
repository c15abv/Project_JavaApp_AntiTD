package towers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import towers.AIMemory.AIMemoryLoader.Errors;
import towers.PlanDetails.TowerBuildPlan;

public class AIMemory{

	public static class AIMemoryLoader{
		
		public enum Errors{
			NO_ERROR, INVALID_MEMORY_FORMAT;
		}
		
		public static final String PATH = "memory.txt";
		
		private int mapValue, loadedSuccessValue;
		private String path;
		private ArrayList<TowerBuildPlan> towerPlan;
		private Errors error;
		
		public AIMemoryLoader(int mapValue){
			this.mapValue = mapValue;
			this.path = PATH;
			towerPlan = new ArrayList<TowerBuildPlan>();
			error = Errors.NO_ERROR;
			loadedSuccessValue = 0;
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
			    	stringBuilder.append(System.lineSeparator());
			        line = bufferedReader.readLine();
			    }
			    
			    loadStringMemoriesToList(stringBuilder.toString());
			}finally{
				bufferedReader.close();
			}
		}
		
		private void loadStringMemoriesToList(String memories){
			String[] mapMemoryTemp = null;
			int pos = findBestMatchingMemory(memories, mapMemoryTemp);
			
			if(error == Errors.NO_ERROR && pos != -1){
				memoryToTowerPlanStructure(mapMemoryTemp);
			}
		}
		
		private int findBestMatchingMemory(String memories, String[] mapMemoryTemp){
			int tempMapValue, oldTempMapValue, diff, diffOld;
			String[] mapMemory = memories.split("\\;");
			int pos = -1;
			oldTempMapValue = 0;
			
			for(int i = 0; i < mapMemory.length; i++){
				mapMemoryTemp = mapMemory[i].split("\\+");
				
				if(mapMemoryTemp.length == 2){
					try{
						tempMapValue = Integer.parseInt(mapMemoryTemp[i]);
						diffOld = Math.abs(mapValue - oldTempMapValue);
						diff = Math.abs(mapValue - tempMapValue);
						if(diff <= diffOld){
							oldTempMapValue = tempMapValue;
							pos = i;
						}
					}catch(NumberFormatException e){
						error = Errors.INVALID_MEMORY_FORMAT;
					}
				}
			}
			return pos;
		}
		
		private void memoryToTowerPlanStructure(String[] mapMemoryTemp){
			PlanDetails details;
			String[] mapMemorySingleTower;
			
			try{
				this.loadedSuccessValue = Integer.parseInt(mapMemoryTemp[0]
						.split("\\:")[1]);
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
	
	private int mapValue, loadedSuccessValue;
	private String path;
	private ArrayList<TowerBuildPlan> towerPlan;
	private Errors error;
	
	private AIMemory(AIMemoryLoader loader){
		this.mapValue = loader.mapValue;
		this.path = loader.path;
		this.towerPlan = loader.towerPlan;
		this.error = loader.error;
		this.loadedSuccessValue = loader.loadedSuccessValue;
	}
	
	public ArrayList<TowerBuildPlan> getTowerBuildPlan(){
		return new ArrayList<TowerBuildPlan>(towerPlan);
	}

	public int getMapValue(){
		return mapValue;
	}

	public String getPath(){
		return path;
	}

	public Errors getError(){
		return error;
	}
	
	public void saveMemory(){
		//save
	}
}

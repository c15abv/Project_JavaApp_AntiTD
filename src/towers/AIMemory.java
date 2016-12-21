package towers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import towers.AIMemory.AIMemoryLoader.Errors;
import towers.PlanDetails.TowerBuildPlan;

/**
 * AIMemory handles everything to do with the memory
 * processing of the AIDefendingPlayer.
 * 
 * @author Alexander Beliaev
 *
 */
public class AIMemory{

	/**
	 * AIMemoryLoader load the most adequate memory.
	 * 
	 * @author Alexander Beliaev
	 *
	 */
	public static class AIMemoryLoader{
		
		public enum Errors{
			NO_ERROR, INVALID_MEMORY_FORMAT;
		}
		
		public static final String PATH = "memory";
		
		private int mapValueSought, loadedSuccessValue, mapValueUsed;
		private String path, loadedMemory, loadedSpecificMemory;
		private ArrayList<TowerBuildPlan> towerPlan;
		private Errors error;
		
		/**
		 * Creates a loading class for the AIMemory class,
		 * loads memories and instantiates an AIMemory object
		 * with the most adequate memory. When searching for 
		 * a memory the most similar (level) is chosen.
		 * 
		 * @param mapValue the value of the level.
		 */
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
			InputStream is = getClass().getResourceAsStream(path);
			InputStreamReader fis = new InputStreamReader(is);
			BufferedReader bufferedReader = 
					new BufferedReader(fis);
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
	
	/**
	 * Loads the memory used by the AIDefendingPlayer.
	 * 
	 * @param loader
	 */
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
	
	/**
	 * Attempts to save the memory. The memory is only saved if the
	 * most recent session resulting in a higher success rate than the
	 * success rate which was loaded from the memory.
	 * 
	 * @param success the success of the most recent session (tower build plan)
	 * @param plans the tower build plan
	 */
	public void saveMemory(int success, ArrayList<TowerBuildPlan> plans){
		String newSubString = "";
		StringBuilder stringBuilder;
		String newString = "";
		
		if(success > loadedSuccessValue){
			newSubString += "" + mapValueSought + ":" + success;
			
			for(TowerBuildPlan plan : plans){
				newSubString += "+" + plan.getTime() + ":" + 
						plan.getDetails().getNumFigures() + ":" +
						plan.getDetails().getPositionValue();
			}
			
			newSubString += ";";
			
			if(loadedMemory != null && loadedSpecificMemory != null &&
					mapValueSought == mapValueUsed){
				newString = loadedSpecificMemory
						.replaceAll("(?=[]\\[+&|!(){}^\"~*?:\\\\-])", "\\\\");
				loadedMemory = loadedMemory.replaceAll(newString, newSubString);
			}else if(loadedMemory != null){
				stringBuilder = new StringBuilder(loadedMemory);
				stringBuilder.append(newSubString);
				loadedMemory = stringBuilder.toString();
			}else{
				loadedMemory = newSubString;
			}
			
			try(PrintWriter out = new PrintWriter(path)){
			    out.print(loadedMemory);
			    out.close();
			}catch(FileNotFoundException e){
			}
		}
	}
}

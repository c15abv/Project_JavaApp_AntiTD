package towers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import creatures.AttackingPlayer;
import creatures.CreatureFigure;
import creatures.CreatureFigureTemplate;
import start.AreaPosition;
import start.Figures;
import start.Game;
import start.GameLevel;
import start.Position;
import tiles.Tile;
import tiles.VoidTile;
import towers.PlanDetails.TowerBuildPlan;
import utilities.ActionTimer;
import utilities.ColorCreator;
import utilities.Lock;
import utilities.TimerListener;

/**
 * AIDefendingPlayer.
 * 
 * @author Alexander Beliaev
 * @version 1.0
 */
public class AIDefendingPlayer implements TimerListener, Runnable{
	
	public static class Builder{
		
		public static final int DEFAULT_NUMBER_OF_TOWERS = 3;
		public static final int DEFAULT_TOWER_MUTATION_RANGE = 1000;
		public static final int DEFAULT_TOWER_MUTATION_CHANCE_PERCENT = 2;
		public static final int DEFAULT_TOWER_BUILD_CHANCE_PERMILLION = 500;
		
		private DefendingPlayer defender;
		private AttackingPlayer attacker;
		private GameLevel level;
		private AIMemory memory;
		private int numberOfTowers, towerMutationChance,
			towerMutationRange, towerBuildChance;
		private ArrayList<CreatureFigureTemplate> knownTargets;
		private Lock gameLock;
		private ActionTimer timer;
		private long timerId;
		private boolean enableLearnFromExperiences;
		
		public Builder(DefendingPlayer player, AttackingPlayer attacker,
				GameLevel level, Game game, AIMemory memory){
			this.defender = player;
			this.attacker = attacker;
			this.level = level;
			this.memory = memory;
			this.knownTargets = new ArrayList<CreatureFigureTemplate>();
			this.numberOfTowers = DEFAULT_NUMBER_OF_TOWERS;
			this.timer = game.getTimer();
			this.timerId = game.getGameTimeTimerId();
			this.towerMutationRange = DEFAULT_TOWER_MUTATION_RANGE;
			this.towerMutationChance = DEFAULT_TOWER_MUTATION_CHANCE_PERCENT;
			this.towerBuildChance = DEFAULT_TOWER_BUILD_CHANCE_PERMILLION;
			this.enableLearnFromExperiences = false;
		}
		
		public Builder setNumberOfTowers(int numberOfTowers){
			if(numberOfTowers > 0)
				this.numberOfTowers = numberOfTowers;
			return this;
		}
		
		public Builder setKnownTowerTargets(CreatureFigureTemplate ...
				templates){
			for(CreatureFigureTemplate template : templates){
				knownTargets.add(template);
			}
			return this;
		}
		
		public Builder setGameLock(Lock gameLock){
			this.gameLock = gameLock;
			return this;
		}
		
		public Builder setGameTimer(ActionTimer timer, long timerId){
			this.timer = timer;
			this.timerId = timerId;
			return this;
		}
		
		public Builder setTowerMutationTimeRange(int towerMutationRange){
			this.towerMutationRange = towerMutationRange;
			return this;
		}
		
		//percent
		public Builder setTowerMutationTimeChance(int towerMutationChance){
			this.towerMutationChance = towerMutationChance;
			return this;
		}
		
		//per million
		public Builder setBuildTowerChance(int towerBuildChance){
			this.towerBuildChance = towerBuildChance;
			return this;
		}
		
		public Builder enableLearnFromExperience(){
			enableLearnFromExperiences = true;
			return this;
		}
		
		public AIDefendingPlayer build(){
			return new AIDefendingPlayer(this);
		}
	}
	
	public static final int MILLION = (int) 10e6;
	
	private volatile boolean isRunning = false;
	
	private DefendingPlayer defender;
	private AttackingPlayer attacker;
	private GameLevel level;
	private AIMemory memory;
	private Random random;
	private int numberOfTowers, towerMutationTimeChance,
		towerMutationTimeRange;
	private ArrayList<CreatureFigureTemplate> knownTargets;
	private ArrayList<TowerFigureTemplate> towerTemplates;
	private ArrayList<TowerBuildPlan> towerBuildPlanList;
	private HashMap<Long, TowerBuildPlan> towerBuildPlanMap;
	private ArrayList<Long> notifiedList;
	private ArrayList<Long> plannedTowersLeft;
	private ArrayList<TowerBuildPlan> builtTowersList;
	private Lock gameLock, lock;
	private int towerBuildChance;
	private volatile int successThisSession;
	private ActionTimer timer;
	private long timerId;
	private boolean enableLearnFromExperiences, canBuildTowers;
	
	private final double middleX, middleY;
	
	private AIDefendingPlayer(Builder builder){
		this.defender = builder.defender;
		this.attacker = builder.attacker;
		this.level = builder.level;
		this.memory = builder.memory;
		this.numberOfTowers = builder.numberOfTowers;
		this.knownTargets = builder.knownTargets;
		this.gameLock = builder.gameLock;
		this.timer = builder.timer;
		this.timerId = builder.timerId;
		this.towerMutationTimeRange = builder.towerMutationRange;
		this.towerMutationTimeChance = builder.towerMutationChance;
		this.towerBuildChance = builder.towerBuildChance;
		this.enableLearnFromExperiences = builder.enableLearnFromExperiences;
		
		random = new Random();
		
		middleX = (double)level.getTilesX() / 2;
		middleY = (double)level.getTilesY() / 2;
		
		canBuildTowers = true;
		successThisSession = 0;
		
		towerTemplates = new ArrayList<TowerFigureTemplate>();
		towerBuildPlanMap = new HashMap<Long, TowerBuildPlan>();
		notifiedList = new ArrayList<Long>();
		builtTowersList = new ArrayList<TowerBuildPlan>();
		plannedTowersLeft = new ArrayList<Long>();
		lock = new Lock();
		init();
	}
	

	@Override
	public void run(){
		Random random = new Random();
		ArrayList<Long> tempNotified = null;
		ArrayList<Long> notifiedBuilt;
		PlanDetails detailsTemp;
		TowerBuildPlan towerPlan;
		isRunning = true;
		
		setTimerForPlannedTowers();
		
		while(isRunning && !Thread.interrupted()){
			notifiedBuilt = null;
			
			if(canBuildTowers){
				if((random.nextInt(MILLION) + 1) <= towerBuildChance){
					buildTower(null);
				}
				if(!towerBuildPlanMap.isEmpty()){
					tempNotified = getTempNotifiedList();
					for(Long id : tempNotified){
						if((towerPlan = towerBuildPlanMap.get(id)) != null  &&
								(detailsTemp = towerPlan.getDetails()) != null){
							if(notifiedBuilt == null){
								notifiedBuilt = new ArrayList<Long>();
							}
							buildTower(detailsTemp);
							notifiedBuilt.add(id);
							plannedTowersLeft.remove(id);
						}
					}
					
					if(notifiedBuilt != null){
						removeNotifiedBuilt(notifiedBuilt);
					}
				}
			}
			try{
				Thread.currentThread();
				Thread.sleep(1);
			}catch(InterruptedException e){
			}
		}
		
		successThisSession = finalizeAI();
	}
	
	private int finalizeAI(){
		int success = 0;
		if(!isRunning){
			success = calculateSuccess();
			if(enableLearnFromExperiences){
				memory.saveMemory(success, builtTowersList);
			}
		}
		return success;
	}
	
	public synchronized int getSuccess(){
		return successThisSession;
	}
	
	public synchronized void terminate(){
		isRunning = false;
	}

	@Override
	public void receiveNotification(Long id){
		try{
			lock.lock();
			notifiedList.add(id);
		}catch(InterruptedException e){
		}finally{
			lock.unlock();
		}
	}
	
	private int calculateSuccess(){
		ArrayList<TowerFigure> towers = getTowers();
		int killedCreatures = 0;
		int creaturesSpawned = getCreaturesSpawned();
		int success = 0;
		double goalToRequired = 0;
		double killedToSpawnedRatio = 0;
		long timeLeft = timer.timeLeft(timerId);
		
		if(towers != null){
			for(TowerFigure tower : towers){
				killedCreatures += tower.getKilled();
			}
			
			if(creaturesSpawned > 0){
				killedToSpawnedRatio = (double)killedCreatures /
						creaturesSpawned;
			}else{
				killedToSpawnedRatio = 1;
			}
			
			if(killedToSpawnedRatio == 1){
				success = 200;
			}else if(timeLeft <= 0){
				success = 100;
				
				success += Math.abs(killedToSpawnedRatio -
						goalToRequired) * 100;
			}else{
				success += Math.abs(killedToSpawnedRatio -
						goalToRequired) * 100;
			}
		}
		
		return success;
	}
	
	private int getCreaturesSpawned(){
		int creatures = 0;
		
		try{
			gameLock.lock();
			creatures = attacker.getCreaturesBought();
		}catch(InterruptedException e){
		}finally{
			gameLock.unlock();
		}
		
		return creatures;
	}
	
	private HashMap<AreaPosition, Tile> getLevelMap(){
		HashMap<AreaPosition, Tile> levelMap = null;
		
		try{
			gameLock.lock();
			levelMap = level.getLevelMap();
		}catch(InterruptedException e){
		}finally{
			gameLock.unlock();
		}
		
		return levelMap;
	}
	
	private ArrayList<TowerFigure> getTowers(){
		ArrayList<TowerFigure> towers = null;
		
		try{
			gameLock.lock();
			towers = defender.getDefence();
		}catch(InterruptedException e){
		}finally{
			gameLock.unlock();
		}
		
		return towers;
	}
	
	private boolean addTower(TowerFigure tower, int cost){
		boolean success = false;
		
		tower.setActionTimer(timer);
		try{
			gameLock.lock();
			defender.addTowerFigure(tower);
			defender.setCredits(defender.getCredits() - cost);
			success = true;
		}catch(InterruptedException e){
		}finally{
			gameLock.unlock();
		}
		
		return success;
	}
	
	private ArrayList<CreatureFigure> getHorde(){
		ArrayList<CreatureFigure> horde = null;
		
		try{
			gameLock.lock();
			horde = attacker.getHorde();
		}catch(InterruptedException e){
		}finally{
			gameLock.unlock();
		}
		
		return horde;
	}
	
	private int getAverageHueAmongHorde(){
		ArrayList<CreatureFigure> horde = null;
		int hueTotal = 0;
		
		horde = getHorde();
		
		if(horde != null){
			for(CreatureFigure figure : horde){
				hueTotal += figure.getHue();
			}
		}
		
		return horde.size() != 0 ? hueTotal / horde.size() : 0;
	}
	
	private Figures getMostCommonShapeAmongHorde(){
		ArrayList<CreatureFigure> horde = null;
		Figures shape;
		int circle, triangle, square;
		circle = triangle = square = 0;
		
		horde = getHorde();
		
		if(horde != null){
			for(CreatureFigure figure : horde){
				switch(figure.getShape()){
				case CIRCLE:
					circle++;
					break;
				case SQUARE:
					square++;
					break;
				case TRIANGLE:
					triangle++;
					break;
				default:
					break;
				}
			}
		}
		
		if(circle > square && circle > triangle){
			shape = Figures.CIRCLE;
		}else if(square > circle && square > triangle){
			shape = Figures.SQUARE;
		}else if(triangle > circle && triangle > square){
			shape = Figures.TRIANGLE; 
		}else{
			shape = Figures.STAR;
		}
		
		return shape;
	}
	
	private ArrayList<Long> getTempNotifiedList(){
		ArrayList<Long> tempNotified = null;
		
		try{
			lock.lock();
			tempNotified = new ArrayList<Long>(notifiedList);
		}catch(InterruptedException e){
		}finally{
			lock.unlock();
		}
		
		return tempNotified;
	}
	
	private void removeNotifiedBuilt(ArrayList<Long> ids){
		try{
			lock.lock();
			for(Long id : ids){
				notifiedList.remove(id);
			}
		}catch(InterruptedException e){
		}finally{
			lock.unlock();
		}
	}
	
	private void buildTower(PlanDetails details){
		if(details != null){
			buildTowerFromPlan(details);
			towerBuildPlanMap.remove(plannedTowersLeft.get(0));
		}else{
			buildTowerFromScratch();
		}
		
	}
	
	private HashMap<AreaPosition, VoidTile> getAvailableVoidTiles(
			HashMap<AreaPosition, Tile> levelMap){
		HashMap<AreaPosition, VoidTile> voidMap = 
				new HashMap<AreaPosition, VoidTile>();
		Tile tile = null;
		for(Map.Entry<AreaPosition, Tile> entry : levelMap.entrySet()){
			tile = entry.getValue();
			if(tile.buildable() && !((VoidTile)tile).occupied()){
				voidMap.put(entry.getKey(), (VoidTile)tile);
			}
		}
		
		return new HashMap<AreaPosition, VoidTile> (voidMap);
	}
	
	private void buildTowerFromScratch(){
		TowerFigureTemplate template = 
			towerTemplates.get(random.nextInt(numberOfTowers));
		HashMap<AreaPosition, Tile> levelMap = getLevelMap();
		TowerBuildPlan plan = null;
		HashMap<AreaPosition, VoidTile> voidMap = null;
		VoidTile tile = null;
		int randomTile;
		int temp = 0;
		
		if(levelMap != null){
			voidMap = getAvailableVoidTiles(levelMap);
			if(voidMap.size() > 0){
				randomTile = random.nextInt(voidMap.size());
				for(Map.Entry<AreaPosition, VoidTile> entry : voidMap.entrySet()){
					if(randomTile == temp){
						tile = entry.getValue();
						if(defender.getCredits() >= template.getCost()){
							if(addTower(template.createNewTower(
									new Position(tile.getPosition().getX(),
											tile.getPosition().getY(), Tile.size)),
									template.getCost())){
								tile.setIsOccupied(true);
								plan = generateBuildPlan(tile);
								builtTowersList.add(plan);
								return;
							}
						}else{
							return;
						}
					}
					temp++;
				}
			}else{
				canBuildTowers = false;
			}
		}
	}
	
	private TowerBuildPlan generateBuildPlan(VoidTile tile){
		PlanDetails details;
		int tileX, tileY;
		double relPositionVal;
		long time = getCurrentGameTimeElapsed();
		int hordeSize = attacker.getHordeSize();
		
		tileX = tile.getPosition().getX() / Tile.size;
		tileY = tile.getPosition().getY() / Tile.size;
		tileX = (int) Math.abs(tileX - middleX);
		tileY = (int) Math.abs(tileY - middleY);
		relPositionVal = Math.sqrt(Math.pow((double)tileX / middleX, 2) +
					Math.pow((double)tileY / middleY, 2));
		
		details = new PlanDetails(hordeSize, relPositionVal);
		
		return details.new TowerBuildPlan(time, details);
	}
	
	private boolean attemptCreation(TowerFigureTemplate template, VoidTile tile,
			HashMap<AreaPosition, VoidTile> voidMap, int x, int y){
		AreaPosition areaPosition = 
				new AreaPosition((int)(x + middleX) * Tile.size,
				(int)(y + middleY) * Tile.size, Tile.size, Tile.size);
		if(defender.getCredits() >= template.getCost()){
			if((tile = voidMap.get(areaPosition)) != null &&
					addTower(template.createNewTower(
							new Position(tile.getPosition().getX(),
									tile.getPosition().getY(),
									Tile.size)),
							template.getCost())){
				tile.setIsOccupied(true);
				builtTowersList.add(generateBuildPlan(tile));
				return true;
			}
		}else{
			return true;
		}
		
		return false;
	}
	
	private void buildTowerFromPlan(PlanDetails details){
		HashMap<AreaPosition, Tile> levelMap = getLevelMap();
		HashMap<AreaPosition, VoidTile> voidMap = null;
		VoidTile tile = null;
		int averageHue = getAverageHueAmongHorde();
		Figures mostCommonShape = getMostCommonShapeAmongHorde();
		TowerFigureTemplate template = 
				chooseTemplateToUse(averageHue, mostCommonShape);
		double posValue = details.getPositionValue();
		int tileX, tileY;
		int randomTile;
		int temp = 0;
		
		tileX = (int)(middleX * posValue);
		tileY = (int)(middleY * posValue);
		
		if(levelMap != null){
			voidMap = getAvailableVoidTiles(levelMap);
			if(voidMap.size() > 0){
				if(random.nextInt(2) == 0){
					for(int x = -tileX / 2; x < tileX / 2; x++){
						for(int y = -tileY / 2; y < tileY / 2; y++){
							if(attemptCreation(template, tile, voidMap, x, y)){
								return;
							}
						}
					}
				}else{
					for(int x = tileX / 2; x > -tileX / 2; x--){
						for(int y = tileY / 2; y > -tileY / 2; y--){
							if(attemptCreation(template, tile, voidMap, x, y)){
								return;
							}
						}
					}
				}
				
				
				randomTile = random.nextInt(voidMap.size());
				for(Map.Entry<AreaPosition, VoidTile> entry : voidMap.entrySet()){
					if(randomTile == temp){
						tile = entry.getValue();
						if(defender.getCredits() >= template.getCost()){
							if(addTower(template.createNewTower(
									new Position(tile.getPosition().getX(),
											tile.getPosition().getY(), Tile.size))
									, template.getCost())){
								tile.setIsOccupied(true);
								builtTowersList.add(generateBuildPlan(tile));
								return;
							}
						}else{
							return;
						}
					}
					temp++;
				}
			}
		}else{
			canBuildTowers = false;
		}
	}
	
	private TowerFigureTemplate chooseTemplateToUse(int hue,
			Figures shape){
		TowerFigureTemplate temp = null;
		
		for(TowerFigureTemplate template : towerTemplates){
			if(template.getTowerType() == shape){
				if(temp == null || temp.getTowerType() != shape){
					temp = template;
				}else if(temp.getTowerType() == shape &&
						Math.abs(hue - template.getHue()) <
						Math.abs(hue - temp.getHue())){
					temp = template;
				}
			}else{
				if(temp == null){
					temp = template;
				}else if(temp.getTowerType() != shape &&
						Math.abs(hue - template.getHue()) <
						Math.abs(hue - temp.getHue())){
					temp = template;
				}
			}
		}
		
		return temp;
	}
	
	private long getCurrentGameTimeElapsed(){
		return (long) (timer.timeElapsed() / 10e5);
	}
	
	private void init(){
		chooseTowers();
		loadMemory();
	}

	//not very elegant;
	//requires a lot of testing/balancing to
	//find the most appropriate values.
	private void chooseTowers(){
		CreatureFigureTemplate creatureTemplate;
		for(int i = 0; i < numberOfTowers; i++){
			if(i < knownTargets.size()){
				creatureTemplate = knownTargets.get(i);
				towerTemplates.add(new TowerFigureTemplate(
						creatureTemplate.getCreatureType(),
						creatureTemplate.getHue(), 
						500, 200, 10, 10));
			}else{
				towerTemplates.add(new TowerFigureTemplate(
						Figures.getRandom(), ColorCreator.getRandomHue(), 
						500, 200, 10, 10));
			}
		}
	}
	
	private void loadMemory() {
		long id, time, timeTemp;
		Random random = new Random();
		towerBuildPlanList = memory.getTowerBuildPlan();
		
		for(TowerBuildPlan plan : towerBuildPlanList){
			id = timer.getNewUniqueId();
			time = plan.getTime();
			if(random.nextInt(100) + 1 <= towerMutationTimeChance){
				time = (timeTemp = random.nextInt(2 * towerMutationTimeRange) + 
						1) <= towerMutationTimeRange ? time + timeTemp : 
							Math.abs(time + (towerMutationTimeRange - timeTemp));
				plan.setNewTime(time);
			}
			
			plannedTowersLeft.add(id);
			towerBuildPlanMap.put(id, plan);
		}
	}
	
	private void setTimerForPlannedTowers(){
		for(Map.Entry<Long, TowerBuildPlan> entry : towerBuildPlanMap.entrySet()){
			timer.setTimer(entry.getKey(), this, entry.getValue().getTime());
		}
	}
}

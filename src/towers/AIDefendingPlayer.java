package towers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import creatures.CreatureFigureTemplate;
import start.Figures;
import start.Game;
import start.GameLevel;
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
public class AIDefendingPlayer extends Thread implements TimerListener{
	
	public static class Builder{
		
		public static final int DEFAULT_NUMBER_OF_TOWERS = 3;
		public static final int DEFAULT_TOWER_MUTATION_RANGE = 1000;
		public static final int DEFAULT_TOWER_MUTATION_CHANCE_PERCENT = 2;
		
		private DefendingPlayer player;
		private GameLevel level;
		private AIMemory memory;
		private int numberOfTowers, towerMutationChance,
			towerMutationRange;
		private ArrayList<CreatureFigureTemplate> knownTargets;
		private Lock gameLock;
		private ActionTimer timer;
		private long timerId;
		
		public Builder(DefendingPlayer player, GameLevel level,
				Game game, AIMemory memory){
			this.player = player;
			this.level = level;
			this.memory = memory;
			this.knownTargets = new ArrayList<CreatureFigureTemplate>();
			this.numberOfTowers = DEFAULT_NUMBER_OF_TOWERS;
			this.timer = game.getTimer();
			this.timerId = game.getGameTimeTimerId();
			this.towerMutationRange = DEFAULT_TOWER_MUTATION_RANGE;
			this.towerMutationChance = DEFAULT_TOWER_MUTATION_CHANCE_PERCENT;
		}
		
		public Builder setNumberOfTowers(int numberOfTowers){
			this.numberOfTowers = numberOfTowers;
			return this;
		}
		
		public Builder setKnownTowerTargets(
				CreatureFigureTemplate ... templates){
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
		
		public Builder setTowerMutationTimeChance(int towerMutationChance){
			this.towerMutationChance = towerMutationChance;
			return this;
		}
		
		public AIDefendingPlayer build(){
			return new AIDefendingPlayer(this);
		}
	}
	
	private volatile boolean isRunning = false;
	
	private DefendingPlayer player;
	private GameLevel level;
	private AIMemory memory;
	private int numberOfTowers, towerMutationChance,
		towerMutationRange;
	private ArrayList<CreatureFigureTemplate> knownTargets;
	private ArrayList<TowerFigureTemplate> towerTemplates;
	private ArrayList<TowerBuildPlan> towerBuildPlanList;
	private HashMap<Long, PlanDetails> towerBuildPlanMap;
	private ArrayList<Long> notifiedList;
	private Lock gameLock, lock;
	private int currentTower;
	private ActionTimer timer;
	private long timerId;
	
	private AIDefendingPlayer(Builder builder){
		this.player = builder.player;
		this.level = builder.level;
		this.memory = builder.memory;
		this.numberOfTowers = builder.numberOfTowers;
		this.knownTargets = builder.knownTargets;
		this.gameLock = builder.gameLock;
		this.timer = builder.timer;
		this.timerId = builder.timerId;
		this.towerMutationRange = builder.towerMutationRange;
		this.towerMutationChance = builder.towerMutationChance;
		
		towerTemplates = new ArrayList<TowerFigureTemplate>();
		towerBuildPlanMap = new HashMap<Long, PlanDetails>();
		notifiedList = new ArrayList<Long>();
		currentTower = 0;
		lock = new Lock();
		init();
	}
	

	@Override
	public void run(){
		while(isRunning){
			//to be implemented
		}
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
	
	private void init(){
		chooseTowers();
		loadMemory();
	}

	//not very elegant
	private void chooseTowers(){
		CreatureFigureTemplate creatureTemplate;
		for(int i = 0; i < numberOfTowers; i++){
			if(i < knownTargets.size()){
				creatureTemplate = knownTargets.get(i);
				towerTemplates.add(new TowerFigureTemplate(
						creatureTemplate.getCreatureType(),
						creatureTemplate.getHue(), 
						300, 200, 5, 10));
			}else{
				towerTemplates.add(new TowerFigureTemplate(
						Figures.getRandom(), ColorCreator.getRandomHue(), 
						300, 200, 5, 10));
			}
		}
	}
	
	private void loadMemory(){
		long id, time, timeTemp;
		Random random = new Random();
		towerBuildPlanList = memory.getTowerBuildPlan();
		
		for(TowerBuildPlan plan : towerBuildPlanList){
			id = timer.getNewUniqueId();
			time = plan.getTime();
			if(random.nextInt(100) + 1 <= towerMutationChance){
				time = (timeTemp = random.nextInt(2 * towerMutationRange) + 
						1) <= towerMutationRange ? time + timeTemp : 
							Math.abs(time + (towerMutationRange - timeTemp));
			}
			
			towerBuildPlanMap.put(id, plan.getDetails());
			timer.setTimer(id, this, time);
		}
	}
}

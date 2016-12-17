package start;

import java.awt.Dimension;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;

import creatures.AttackingPlayer;
import creatures.CreatureFigure;
import creatures.CreatureFigureTemplate;
import creatures.CreatureFigure.Orientation;
import tiles.PathTile.Direction;
import tiles.StartTile;
import towers.AIDefendingPlayer;
import towers.AIMemory;
import towers.DefendingPlayer;
import utilities.ColorCreator;
import utilities.LevelXMLReader;
import utilities.Lock;

public class AITrainer{
	
	public enum AttackerAIType{
		RANDOM, OSCILLATING, BUNDLE_BUYER;
	}
	
	class VerySimpleAttackerAI implements Runnable{
		
		private AttackerAIType type;
		private volatile boolean isRunning = false;
		private ArrayList<CreatureFigureTemplate> templates;
		private AttackingPlayer attacker;
		private Random random;
		private Lock lock;
		private ArrayList<StartTile> start;
		
		VerySimpleAttackerAI(AttackerAIType type, AttackingPlayer attacker,
				ArrayList<CreatureFigureTemplate> templates, Lock lock,
				ArrayList<StartTile> start){
			this.type = type;
			this.templates = templates;
			this.attacker = attacker;
			this.lock = lock;
			this.start = start;
			
			random = new Random();
		}

		@Override
		public void run(){
			isRunning = true;
			long timedelta = 0;
			while(isRunning && !Thread.interrupted()){
				switch(type){
				case BUNDLE_BUYER:
					try{
						buyBundle();
					}catch(InterruptedException e){
						terminate();
					}
					break;
				case OSCILLATING:
					break;
				case RANDOM:
					break;
				}
				timedelta = System.currentTimeMillis();
				while(System.currentTimeMillis() - timedelta < 5000
						&& isRunning);
			}
		}
		
		private void buyBundle() throws InterruptedException{
			int index = 0;
			int numberToBuy = random.nextInt(7) + 3;
			CreatureFigureTemplate temp;
			long timedelta = 0;
			
			System.out.println(numberToBuy);
			
				for(int i = 0; i < numberToBuy; i++){
					timedelta = System.currentTimeMillis();
					while(System.currentTimeMillis() - timedelta < 250 &&
							isRunning);
					if(!isRunning)
						break;
					index = random.nextInt(templates.size());
					temp = templates.get(index);
					if(attacker.getCredits() >= temp.getCost()){
						try{
							lock.lock();
							attacker.addCreatureFigure(prepareCreature(getRandomStart(), temp));
							attacker.setCredits(attacker.getCredits() - temp.getCost());
						}finally{
							lock.unlock();
						}
						
					}
				}
		}
		
		private CreatureFigure prepareCreature(StartTile tile, CreatureFigureTemplate temp){
			Position position = tile.getPosition();
			Direction direction = tile.getStartingDirection();
			return temp.createNewCreature(position, direction);
		}

		private StartTile getRandomStart(){
			int startIndex = random.nextInt(start.size());
			return start.get(startIndex);
		}
		
		public synchronized void terminate(){
			isRunning = false;
		}
	}
	
	class GameHandler{
		
		@SuppressWarnings("unused")
		private static final long serialVersionUID = 1L;
		
		private Game game;
		private Lock lock;
		private GameRunner runner;
		private Thread runnerThread, aiDefThread, aiAtkThread;
		private AttackingPlayer attacker;
		private DefendingPlayer defender;
		private ArrayList<CreatureFigureTemplate> templatesList;
		private AIMemory memory = null;
		private AIDefendingPlayer aiDef;
		private GameLevel level = null;
		private LevelXMLReader xmlReader;
		private ArrayList<String> levelNames;
		private ArrayList<StartTile> start;
		private VerySimpleAttackerAI aiAtk;
		private CreatureFigureTemplate temp;
		private Random random = new Random();
		private int successLimit;
		private AttackerAIType type;
		private boolean isFinished = false;
		
		GameHandler(AttackerAIType type, int successLimit){
			this.successLimit = successLimit;
			this.type = AttackerAIType.BUNDLE_BUYER;
			
		    xmlReader = new LevelXMLReader("XML/levels.xml");
		    levelNames = xmlReader.getLvlNames();
		    
			setup();
		}
		
		public void start(){
			runnerThread.start();
			game.startGame();
			aiDefThread.start();
			aiAtkThread.start();
		}
		
		private void setup(){
			level = xmlReader.getLevelByName(levelNames.get(0));
			start = level.getStartTiles();
			
			try{
				memory = new AIMemory
						.AIMemoryLoader(level.getInitialLevelMapHash()).load();
			}catch(IOException e1){
				e1.printStackTrace();
			}
			
			game = new Game(level, 1200, 800);
			attacker = game.getAttacker();
			defender = game.getDefender();
			runner = new GameRunner(game);
			lock = game.getLock();
			
			templatesList = new ArrayList<CreatureFigureTemplate>();
			for(int i = 0; i < 3; i++){
				temp = new CreatureFigureTemplate(Figures.STAR,
						ColorCreator.getRandomHue(),
						random.nextDouble() + 0.5, 10, Orientation.RANDOM,
						level);
				templatesList.add(temp);
			}
			
			attacker.setLowestCost(10);
			
			aiDef = new AIDefendingPlayer.Builder(defender,
					attacker, level, game, memory)
					.enableLearnFromExperience()
					.setGameTimer(game.getTimer(), game.getGameTimeTimerId())
					.setGameLock(game.getLock())
					.setTowerMutationTimeChance(5)
					.setTowerMutationTimeRange(2000)
					.setBuildTowerChance(250)
					.build();
			
			aiAtk = new AITrainer().new VerySimpleAttackerAI(type,
					attacker, templatesList,
					lock, start);
			
			runnerThread = new Thread(runner);
			aiDefThread = new Thread(aiDef);
			aiAtkThread = new Thread(aiAtk);
		}
		
		public void run(){
			start();
			boolean running = true;
			while(!Thread.interrupted() && running){
				try{
					lock.lock();
					if(game.getGameState() == Game.GameState.ENDED){
						aiAtk.terminate();
						aiDef.terminate();
						runner.terminate();
						Thread.sleep(100);
						
						if(aiDef.getSuccess() >= successLimit){
							isFinished = true;
						}
						running = false;
						break;
					}
				}catch(InterruptedException e){
					running = false;
					break;
				}finally{
					lock.unlock();
				}
				
				try{
					Thread.sleep(10);
				}catch(InterruptedException e){
				}
			}
		}
		
		public boolean isFinished(){
			return isFinished;
		}
		
		public Game getGame(){
			return game;
		}
		
		public void join(){
			try{
				aiAtkThread.interrupt();
				aiAtkThread.join();
				aiDefThread.interrupt();
				aiDefThread.join();
				runnerThread.interrupt();
				runnerThread.join();
			}catch(InterruptedException e){
			}
		}
	}

	public static void main(String[] args){
		GameHandler handler = null;
		boolean done = false;
		JFrame frame;
		
		handler = new AITrainer()
				.new GameHandler(AttackerAIType.BUNDLE_BUYER, 200);
		
		frame = new JFrame();
		frame.setSize(new Dimension(Game.SIZE_X, Game.SIZE_Y));
		frame.setMinimumSize(new Dimension(Game.SIZE_X, Game.SIZE_Y));
		frame.setMaximumSize(new Dimension(Game.SIZE_X, Game.SIZE_Y));
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		frame.add(handler.getGame());
		frame.pack();
		
		while(!done){
			handler.run();
			done = handler.isFinished();
			if(!done){
				frame.remove(handler.getGame());
				handler.join();
				handler = new AITrainer()
						.new GameHandler(AttackerAIType.BUNDLE_BUYER, 200);
				frame.add(handler.getGame());
				frame.pack();
			}
		}
	}
}

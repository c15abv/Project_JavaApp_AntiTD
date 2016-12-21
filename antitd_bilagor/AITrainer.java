package trainer;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;

import creatures.AttackingPlayer;
import creatures.CreatureFigure;
import creatures.CreatureFigureTemplate;
import creatures.CreatureFigure.Orientation;
import start.Figures;
import start.Game;
import start.GameLevel;
import start.GameRunner;
import start.Position;
import tiles.PathTile.Direction;
import tiles.StartTile;
import towers.AIDefendingPlayer;
import towers.AIMemory;
import towers.DefendingPlayer;
import utilities.ColorCreator;
import utilities.LevelXMLReader;
import utilities.Lock;

/**
 * A training program used to train the AIDefendingPlayer
 * in the AntiTD game.
 * 
 * @author Alexander Beliaev
 *
 */
public class AITrainer{
	
	public enum AttackerAIType{
		RANDOM, OSCILLATING, BUNDLE_BUYER;
	}
	
	/**
	 * Very simple AI for the attacking player.
	 * At the moment only implemented with the BUNDLE_BUYER affix.
	 * When the AI is run with BUNDLE_BUYER it attempts to buy 3-9 creatures
	 * each within .25 seconds of each other, 
	 * then waits 5 seconds until doing so again.
	 * 
	 * It chooses starting position at random.
	 * 
	 * @author Alexander Beliaev
	 */
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
		private AttackerAIType type;
		private String memoryPath;
		private int levelIndex;
		
		GameHandler(AttackerAIType type, String path, int levelIndex,
				String memoryPath){
			this.type = AttackerAIType.BUNDLE_BUYER;
			this.levelIndex = levelIndex;
			this.memoryPath = memoryPath;
			
		    xmlReader = new LevelXMLReader(path);
		    levelNames = xmlReader.getLvlNames();
		}
		
		private void start(){
			runnerThread.start();
			game.startGame();
			aiDefThread.start();
			aiAtkThread.start();
		}
		
		public void setup(){
			int cost, rand;
			
			level = xmlReader.getLevelByName(levelNames.get(levelIndex));
			start = level.getStartTiles();
			
			try{
				memory = new AIMemory
						.AIMemoryLoader(level.getInitialLevelMapHash())
						.setPath(memoryPath)
						.load();
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
				cost = 10;
				if((rand = random.nextInt(10)) == 0){
					cost = 20;
				}
				temp = new CreatureFigureTemplate(Figures.STAR,
						ColorCreator.getRandomHue(),
						random.nextDouble() + 0.5, cost, Orientation.RANDOM,
						level);
				if(rand == 0){
					temp.setActionTimer(game.getTimer());
					temp.enableTeleporter((long)random.nextInt(4001)+1000);
				}
				templatesList.add(temp);
			}
			
			attacker.setLowestCost(10);
			
			aiDef = new AIDefendingPlayer.Builder(defender,
					attacker, level, game, memory)
					.enableLearnFromExperience()
					.setGameTimer(game.getTimer(), game.getGameTimeTimerId())
					.setGameLock(game.getLock())
					.setTowerMutationTimeChance(25)
					.setTowerMutationTimeRange(2000)
					.setBuildTowerChance(500)
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
		
		public Game getGame(){
			return game;
		}
		
		public int getLevelWidth(){
			return level.getWidth();
		}
		
		public int getLevelHeight(){
			return level.getHeight();
		}
		
		public void join(){
			try{
				aiAtk.terminate();
				aiDef.terminate();
				runner.terminate();
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

	class TrainerFrame extends JFrame{
		
		private static final long serialVersionUID = 1225918472026339511L;
		
		Game game;
		
		TrainerFrame(Dimension dimen, GameHandler handler){
			game = null;
			
			/*setSize(dimen);
			setMaximumSize(dimen);*/
			setResizable(true);
			setLocationRelativeTo(null);
			setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			pack();
			setVisible(true);
			
			WindowListener exitListener = new WindowAdapter() {

			    @Override
			    public void windowClosing(WindowEvent e) {
			    	if(game != null){
			    		handler.join();
			    	}
			        System.exit(0);
			    }
			};
			
			addWindowListener(exitListener);
		}
		
		void addGame(Game game){
			if(this.game != null){
				remove(this.game);
			}
			this.game = game;
			add(this.game);
			pack();
		}
	}
	
	public static void main(String[] args){
		GameHandler handler = null;
		Game game;
		TrainerFrame frame;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		Dimension dimen = new Dimension((int)(width - width / 10),
				(int)(height - height / 10));
		int numberOfIterations = 0;
		String path = null;
		String memoryPath = null;
		int levelIndex = 0;
		
		if(args.length == 4){
			path = new String(args[0]);
			
			try{
				levelIndex = Integer.parseInt(args[1]);
			}catch(NumberFormatException e){
				System.err.println("Argument 2 must be an integer.");
				return;
			}
			
			memoryPath = new String(args[2]);
			
			try{
				numberOfIterations = Integer.parseInt(args[3]);
			}catch(NumberFormatException e){
				System.err.println("Argument 4 must be an integer.");
				return;
			}
			
		}else{
			System.err.println("Wrong number of arguments.");
		}
		
		handler = new AITrainer()
				.new GameHandler(AttackerAIType.BUNDLE_BUYER, path, levelIndex,
						memoryPath);
		handler.setup();
		if(handler.getLevelWidth() < dimen.getWidth())
			dimen = new Dimension(handler.getLevelWidth(), (int)dimen.getHeight());
		
		if(handler.getLevelHeight() < dimen.getHeight())
			dimen = new Dimension((int)dimen.getHeight(), handler.getLevelHeight());
			
		frame = new AITrainer().new TrainerFrame(dimen, handler);
		
		for(int i = 0; i < numberOfIterations; i++){
			handler.setup();
			game = handler.getGame();
			game.changeSize((int)dimen.getWidth(), (int)dimen.getHeight());
			frame.addGame(game);
			handler.run();
			handler.join();
		}
	}
}

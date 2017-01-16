package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import start.Game.GameResult;
import utilities.HighScoreInfo;

/**
 * Class that builds the gui.
 *
 * @author Karolina Jonz�n and Alexander Ekstrom
 * @version 1.0
 */
public class GameView implements View {
	private JFrame frame;
	private Color upperPanelColor = Color.DARK_GRAY;
	private Color lowerPanelColor = Color.DARK_GRAY;
	private JPanel centerPanel;
	private JPanel lowerPanel;
	private JTable highScoreTable = new JTable();
	private ViewModel viewModel;
	private RightPanel rightPanel;
	private JTextField timeTextField;
	private JTextField pointsTextField;
	private JMenuItem newRestartGameItem;

	/**
	 * Constructor that sets the view model to the given parameter and initiates
	 * the gui.
	 *
	 * @param viewModel
	 *            A ViewModel.
	 */
	public GameView(ViewModel viewModel) {
		this.viewModel = viewModel;

		initUI();
	}

	/**
	 * Initiates the gui by building and adding the different panels to the
	 * frame.
	 */
	private void initUI() {
		frame = new JFrame("Anit-TD Game");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		centerFrame();

		JPanel upperPanel = buildUpperPanel();
		centerPanel = buildCenterPanel();
		rightPanel = new RightPanel(viewModel);
		JPanel lowerPanel = buildLowerPanel();

		frame.add(upperPanel, BorderLayout.NORTH);
		frame.add(rightPanel, BorderLayout.EAST);
		frame.add(centerPanel, BorderLayout.CENTER);
		frame.add(lowerPanel, BorderLayout.SOUTH);

		initHighScoreTable();

		frame.pack();

	}

	/**
	 * Centers the frame on the screen.
	 */
	private void centerFrame() {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dim.width / 2 - frame.getSize().width / 2,
				dim.height / 2 - frame.getSize().height / 2);

	}

	/**
	 * Creates and shows a message dialog based on the given parameters.
	 *
	 * @param infoMessage
	 * @param titleBar
	 */
	private void showInfoBox(String infoMessage, String titleBar) {
		JOptionPane.showMessageDialog(null, infoMessage, titleBar,
				JOptionPane.INFORMATION_MESSAGE);

	}

	/**
	 * Builds and returns the lower panel of the gui.
	 *
	 * @return
	 */
	private JPanel buildLowerPanel() {
		lowerPanel = new JPanel();

		lowerPanel.setBackground(lowerPanelColor);
		JPanel timeContainer = new JPanel(new FlowLayout());
		timeContainer.setBackground(lowerPanelColor);

		JLabel timeLabel = new JLabel("Time: ");
		setStyle(timeLabel);
		timeTextField = new JTextField();
		setStyle(timeTextField);

		timeLabel.setLabelFor(timeTextField);

		timeContainer.add(timeLabel);
		timeContainer.add(timeTextField);

		JPanel pointsContainer = new JPanel(new FlowLayout());
		pointsContainer.setBackground(lowerPanelColor);

		JLabel pointsLabel = new JLabel("Points: ");
		setStyle(pointsLabel);
		pointsTextField = new JTextField("0");
		setStyle(pointsTextField);

		pointsLabel.setLabelFor(pointsTextField);

		pointsContainer.add(pointsLabel);
		pointsContainer.add(pointsTextField);

		GridBagLayout gridBag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(10, 10, 0, 0);
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 1;
		c.weighty = 1;
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.NONE;

		gridBag.setConstraints(timeContainer, c);

		c.gridy = 1;

		gridBag.setConstraints(pointsContainer, c);

		lowerPanel.setLayout(gridBag);
		lowerPanel.add(timeContainer);
		lowerPanel.add(pointsContainer);

		return lowerPanel;
	}

	/**
	 * Sets the style of a text field.
	 *
	 * @param comp
	 */
	private void setStyle(JTextField comp) {
		Font f = new Font("Arial", Font.BOLD, 20);
		Border b = BorderFactory.createEmptyBorder();

		comp.setBackground(lowerPanelColor);
		comp.setFont(f);
		comp.setBorder(b);
		comp.setForeground(Color.WHITE);
		comp.setEditable(false);

	}

	/**
	 * Sets the style of a JLabel.
	 *
	 * @param comp
	 */
	private void setStyle(JLabel comp) {
		Font f = new Font("Arial", Font.BOLD, 20);
		Border b = BorderFactory.createEmptyBorder();

		comp.setBackground(lowerPanelColor);
		comp.setFont(f);
		comp.setBorder(b);
		comp.setForeground(Color.WHITE);
	}

	/**
	 * Builds and returns the center panel of the gui.
	 *
	 * @return
	 */
	private JPanel buildCenterPanel() {
		JPanel centerPanel = new JPanel();

		centerPanel.setBackground(Color.WHITE);
		return centerPanel;
	}

	/**
	 * Builds and returns the upper panel of the gui.
	 *
	 * @return
	 */
	private JPanel buildUpperPanel() {
		JPanel upperPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		upperPanel.setBackground(upperPanelColor);

		upperPanel.add(buildGameMenu());
		upperPanel.add(buildInfoMenu());

		return upperPanel;
	}

	/**
	 * Build the information menu of the upper panel.
	 *
	 * @return
	 */
	private JMenuBar buildInfoMenu() {
		JMenuBar menuBar;
		JMenu menu;
		JMenuItem menuItem;

		menuBar = new JMenuBar();

		menu = new JMenu("Info");

		menuBar.add(menu);

		menuItem = new JMenuItem("About");

		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showInfoBox(
						"Game created by Alexander Beliaev, Jan Nylen, Alexander Ekstrom and Karolina Jonzen.",
						"About");

			}
		});

		menu.add(menuItem);

		menuItem = new JMenuItem("Help");

		JTextArea textArea = new JTextArea();
		textArea.setText(
				"This is an anti-tower defence game where you have a limited "
						+ "amount of time and credit to create troops and try to reach goal "
						+ "while the computer builds towers. You get credits each time a creature reaches"
						+ " a goal position, depending on its health. The game is won when a certain number of"
						+ " troops has reached goal, which varies over the levels. The game is lost when the time"
						+ " is up. Your resulting score is based on the time it took to finish the level and can be"
						+ "saved."
						+ "\n\nGame options\nFrom the Game Menu, you can start/restart, pause/resume and quit a game"
						+ " and reach the high score table.\n\n"
						+ "Create troops\nCreate your troops in the side panel before you start each level. "
						+ "Following are explanations of the parameters that can be chosen:\n"
						+ "Shape: The creatures geometrical shape. A creature takes more damage from "
						+ "towers with the same shape.\n"
						+ "Teleporter: Teleporter troops are more expensive than normal troops "
						+ "and can be used to drop start and goal teleports for any creature to use. "
						+ "The drop time for the start teleport can be chosen in play mode.\n"
						+ "Color: The creature's color as a hue between 0-359. The more similar a "
						+ "creature's color is to an attacking tower's, the more damage it takes.\n"
						+ "Size: The creature's size, which controls its hitpoints and speed. A larger "
						+ "creature can take more damage, in exchange for less speed and vice versa.\n"
						+ "Orientation: Controls what direction the creature prioritizes in crossroads.\n\n"
						+ "Buy troops\nWhen the game has started, choose your troops in the Your troop-panel, "
						+ "and see information in the Properties box. Before sending out the troop, choose "
						+ "a start position on the map buy clicking near it. You can buy troops as long as you "
						+ "have enough credits."
						+ "\n\nThe level map\nFollowing are explanations of components in the map:\n"
						+ "Start positions: red pulsating circles.\n"
						+ "Chosen start position: red pulsating circles surrounded by a yellow frame.\n"
						+ "Goal positions: yellow pulsating circles.\n"
						+ "Teleporter positions: gray pulsating circles for unconnected start teleporters,"
						+ " otherwise blue pulsating circles.\n"
						+ "Paths: white dotted lines.\n"
						+ "Walls: diffuse light brown circles.\n"
						+ "Tower areas: light brown crosses.\n");

		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(textArea);

		scrollPane.setPreferredSize(new Dimension(500, 500));

		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, scrollPane, "Help",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});

		menu.add(menuItem);

		return menuBar;
	}

	/**
	 * Builds and returns the game menu of the upper panel.
	 *
	 * @return
	 */
	private JMenuBar buildGameMenu() {
		JMenuBar menuBar;
		JMenu menu;
		JMenuItem menuItem;

		menuBar = new JMenuBar();

		menu = new JMenu("Game Menu");

		menuBar.add(menu);

		newRestartGameItem = new JMenuItem("New Game");

		GameView view = this;
		newRestartGameItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				newRestartGameItem.setText("Restart game");

				if (viewModel.gameIsRunning()) {
					String infoMessage = "Are you sure?";
					String title = "New game";
					Object[] options = { "New game", "Cancel" };

					int n = JOptionPane.showOptionDialog(frame, infoMessage,
							title, JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, options,
							options[0]);

					if (n == JOptionPane.YES_OPTION) {

						viewModel.quitGame();
						resetGame(1);
					}
				} else {

					if (viewModel.gameIsInitiated()) {
						viewModel.quitBeforeStart();
						resetGame(1);
					} else {
						try {
							viewModel.initGame(view, 0);
							rightPanel.initUI();
							frame.pack();
							viewModel.changeSizeOfGameCanvas(
									rightPanel.getWidth());
							frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
						} catch (NullPointerException e) {
							showDialogOnLevelError();
						}

					}

				}
			}

		});

		menu.add(newRestartGameItem);

		JToggleButton pauseAndResumeBtn = createPauseAndResumeButton();

		menu.add(pauseAndResumeBtn);

		pauseAndResumeBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				AbstractButton abstractButton = (AbstractButton) actionEvent
						.getSource();
				String btnText = abstractButton.getText();

				if (btnText.equals("  Resume")) {
					viewModel.resumeGame();

				} else {
					viewModel.pauseGame();
				}
			}

		});

		menuItem = new JMenuItem("Highscore");

		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showHighScoreTable();
			}
		});

		menu.add(menuItem);

		menuItem = new JMenuItem("Quit");

		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showQuitGameDialog(
						"Are you sure you want to quit the current game?",
						"Quit game");
			}
		});

		menu.add(menuItem);

		return menuBar;
	}

	/**
	 * Resets the UI and initiates a new game with the level index given as
	 * parameter.
	 *
	 * @param levelIndex
	 */
	private void resetGame(int levelIndex) {
		rightPanel.resetGui();

		viewModel.initGame(this, levelIndex);

		rightPanel.initUI();

		pointsTextField.setText("0");

		frame.pack();

		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}

	/**
	 * Shows the high score table.
	 */
	private void showHighScoreTable() {
		try {
			ArrayList<HighScoreInfo> h = viewModel.getFromDataBase();

			DefaultTableModel model = (DefaultTableModel) highScoreTable
					.getModel();

			model.setRowCount(0);

			highScoreTable.setCellSelectionEnabled(false);

			HighScoreInfo hi;

			for (int i = 0; i < h.size(); i++) {
				hi = new HighScoreInfo();
				hi = h.get(i);
				model.addRow(new Object[] { hi.getName(), hi.getScore(),
						hi.getLevel(), hi.getTime() });
			}

			highScoreTable.setModel(model);

			JOptionPane.showMessageDialog(null, new JScrollPane(highScoreTable),
					"HIGHSCORE - Top 10", JOptionPane.PLAIN_MESSAGE);
		} catch (SQLException e1) {
			JOptionPane.showMessageDialog(null,
					"Could not get data from database.");
		}
	}

	/**
	 * Builds and shows a confirm dialog based on the given parameters.
	 *
	 * @param infoMessage
	 * @param title
	 */
	private void showQuitGameDialog(String infoMessage, String title) {
		Object[] options = { "Quit game", "Cancel" };
		int n = JOptionPane.showOptionDialog(frame, infoMessage, title,
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				options, // the titles of buttons
				options[0]); // default button title

		if (n == JOptionPane.YES_OPTION) {
			if (viewModel.gameIsRunning()) {
				viewModel.quitGame();
			} else {
				viewModel.quitBeforeStart();
			}
			frame.dispose();
		}
	}

	@Override
	public void showResult(GameResult result, int score, Time time) {
		String infoMessage = "";
		String title = "";

		switch (result) {
		case ATTACKER_WINNER:

			if (viewModel.isLastLevel()) {
				Object[] options = { "Quit and save game" };
				infoMessage = "Congratulations! You beat the final level!\n Your total score is "
						+ (int) viewModel.getTotalScore() + ".";
				title = "Game ended";
				int n;

				n = JOptionPane.showOptionDialog(frame, infoMessage, title,
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
						null, options, options[0]);

				if (n == JOptionPane.YES_OPTION) {

					showOnQuitDialog(score, time);

				} else {
					frame.dispose();
				}
			} else {
				Object[] options = { "Next level", "Quit game" };
				infoMessage = "YOU WON!\n Your current score is "
						+ (int) viewModel.getTotalScore() + ".";
				title = "Game ended";
				int n;

				n = JOptionPane.showOptionDialog(frame, infoMessage, title,
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
						null, options, options[0]);

				if (n == JOptionPane.YES_OPTION) {

					resetGame(2);

				} else {
					showOnQuitDialog(score, time);
				}
			}
			break;

		case DEFENDER_WINNER:
			Object[] options4 = { "Try again", "Quit game" };
			infoMessage = "YOU LOST!\nYour resulting score is "
					+ (int) viewModel.getTotalScore() + ".";
			title = "Game ended";

			int n = JOptionPane.showOptionDialog(frame, infoMessage, title,
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
					null, options4, options4[0]);

			if (n == JOptionPane.YES_NO_OPTION) {
				resetGame(viewModel.getCurrentLevel());
			} else {
				showOnQuitDialog(score, time);
			}
			break;
		case NA:
			break;

		}

	}

	private void showOnQuitDialog(int score, Time time) {
		Object[] options = { "Save score", "Don't save score" };
		String infoMessage = "Do you want to save your score?";
		String title = "Game ended";

		int n = JOptionPane.showOptionDialog(frame, infoMessage, title,
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				options, options[0]);

		if (n == JOptionPane.YES_OPTION) {
			String name = JOptionPane.showInputDialog(frame,
					"Enter your name: ");
			try {
				viewModel.insertIntoDataBase(name, score, time,
						viewModel.getLevelInfo().getLevelName());

				Object[] options2 = { "View high score table", "Quit game" };
				infoMessage = "Your score was saved.";
				title = "Result saved";

				n = JOptionPane.showOptionDialog(frame, infoMessage, title,
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
						null, options2, options2[0]);

				if (n == JOptionPane.YES_OPTION) {
					showHighScoreTable();

				}

			} catch (SQLException e) {
				JOptionPane.showMessageDialog(frame,
						"Could not insert data into database. Your result will not be saved.");
				e.printStackTrace();
			}
		} else

		{
			frame.dispose();
		}
	}

	/**
	 * Initiates the high score table by setting its header and style.
	 */
	private void initHighScoreTable() {

		String header[] = { "Name", "Score", "Level", "Time" };

		// instance table model
		@SuppressWarnings("serial")
		DefaultTableModel model = new DefaultTableModel() {

			@Override
			public boolean isCellEditable(int row, int column) {
				// all cells false
				return false;
			}
		};
		model.setColumnCount(4);
		highScoreTable.setModel(model);

		TableColumn column1 = new TableColumn();
		for (int i = 0; i < highScoreTable.getColumnCount(); i++) {
			column1 = highScoreTable.getTableHeader().getColumnModel()
					.getColumn(i);
			column1.setHeaderValue(header[i]);
		}

		highScoreTable.setCellSelectionEnabled(false);
		highScoreTable.getTableHeader().setReorderingAllowed(false);

		@SuppressWarnings("serial")
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable arg0,
					Object arg1, boolean arg2, boolean arg3, int arg4,
					int arg5) {
				Component tableCellRendererComponent = super.getTableCellRendererComponent(
						arg0, arg1, arg2, arg3, arg4, arg5);
				int align = DefaultTableCellRenderer.CENTER;

				((DefaultTableCellRenderer) tableCellRendererComponent)
						.setHorizontalAlignment(align);
				return tableCellRendererComponent;
			}
		};

		highScoreTable.setDefaultRenderer(Object.class, renderer);
	}

	/**
	 * Creates and returns the pause and resume button in the game menu.
	 *
	 * @return
	 */
	private JToggleButton createPauseAndResumeButton() {
		@SuppressWarnings("serial")
		Action toggleAction = new AbstractAction("  Pause") {

			@Override
			public void actionPerformed(ActionEvent e) {
				AbstractButton button = (AbstractButton) e.getSource();

				if (button.isSelected()) {
					button.setText("  Resume");
				} else {
					button.setText("  Pause");
				}
			}
		};

		Border emptyBorder = BorderFactory.createEmptyBorder();
		JToggleButton toggleButton = new JToggleButton(toggleAction);
		toggleButton.setBorder(emptyBorder);
		toggleButton.setContentAreaFilled(false);

		toggleButton.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				toggleButton.setContentAreaFilled(true);
				Color lightBlue = new Color(163, 184, 204);
				toggleButton.setBackground(lightBlue);
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				toggleButton.setContentAreaFilled(false);
				toggleButton.setBackground(UIManager.getColor("control"));
			}
		});

		return toggleButton;
	}

	/**
	 * Makes the gui visible.
	 */
	public void show() {
		this.frame.setVisible(true);
	}

	@Override
	public void setPoints(int points) {
		pointsTextField.setText(
				String.valueOf(points) + "/" + viewModel.getScoreGoal());
		lowerPanel.revalidate();
		lowerPanel.repaint();
	}

	@Override
	public JPanel getLevelMapPanel() {
		return centerPanel;
	}

	@Override
	public void setCredits(int credit) {
		rightPanel.setCreditTextField(credit);
	}

	@Override
	public JPanel getSidePanel() {
		return rightPanel;
	}

	@Override
	public void showDialogOnLevelError() {
		JOptionPane.showMessageDialog(null, "Error when reading XML-file.");
	}

}

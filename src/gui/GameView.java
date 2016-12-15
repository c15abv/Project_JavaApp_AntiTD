package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import utilities.DatabaseHandler;
import utilities.HighScoreInfo;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

/**
 * Class that builds the gui.
 * 
 * @author karro
 *
 */
@SuppressWarnings("serial")
public class GameView {
	private JFrame frame;
	private Color upperPanelColor = Color.DARK_GRAY;
	private Color lowerPanelColor = Color.DARK_GRAY;
	private JPanel centerPanel;
	private JTable highScoreTable = new JTable();
	private GameViewModel viewModel;
	private RightPanel rightPanel;

	/**
	 * Constructor that sets the view model to the given parameter and initiates
	 * the gui.
	 * 
	 * @param viewModel
	 */
	public GameView(GameViewModel viewModel) {
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
		JPanel lowerPanel = new JPanel();

		lowerPanel.setBackground(lowerPanelColor);
		Border b = BorderFactory.createEmptyBorder();

		GridBagLayout gridBag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		c.ipadx = 10;
		c.ipady = 10;
		c.insets = new Insets(10, 10, 0, 0);
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 1;
		c.weighty = 1;
		c.anchor = GridBagConstraints.WEST;

		JTextField time = new JTextField("Time:");
		time.setBackground(lowerPanelColor);
		Font f = new Font("Arial", Font.BOLD, 20);
		time.setForeground(Color.WHITE);
		time.setFont(f);
		time.setEditable(false);
		time.setBorder(b);

		gridBag.setConstraints(time, c);

		c.insets = new Insets(0, 10, 10, 0);
		c.gridy = 1;

		JTextField reachedGoal = new JTextField("Reached goal:");
		reachedGoal.setBackground(lowerPanelColor);
		reachedGoal.setFont(f);
		reachedGoal.setBorder(b);
		reachedGoal.setForeground(Color.WHITE);
		reachedGoal.setEditable(false);

		gridBag.setConstraints(reachedGoal, c);

		lowerPanel.setLayout(gridBag);

		lowerPanel.add(time);
		lowerPanel.add(reachedGoal);

		return lowerPanel;
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
			public void actionPerformed(ActionEvent e) {
				showInfoBox("About text", "About");

			}
		});

		menu.add(menuItem);

		menuItem = new JMenuItem("Help");

		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showInfoBox("Help text", "Help");
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

		menuItem = new JMenuItem("New Game");

		menuItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				viewModel.initGame(centerPanel);

				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						rightPanel.resetGui();
						rightPanel.initUI();
					}
				});
				// rightPanel.setStartCreditTextField(
				// viewModel.getLevelInfo().getStartCredit());
			}

		});

		menu.add(menuItem);

		JToggleButton pauseAndResumeBtn = createPauseAndResumeButton();

		menu.add(pauseAndResumeBtn);

		pauseAndResumeBtn.addActionListener(new ActionListener() {

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
			public void actionPerformed(ActionEvent e) {
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

					JOptionPane.showMessageDialog(null,
							new JScrollPane(highScoreTable),
							"HIGHSCORE - Top 10", JOptionPane.PLAIN_MESSAGE);

				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(null,
							"Could not get data from database.");
				}

			}
		});

		menu.add(menuItem);

		menuItem = new JMenuItem("Quit");

		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				confirmDialog("Are you sure you want to quit the current game?",
						"Are you sure?");
			}
		});

		menu.add(menuItem);

		return menuBar;
	}

	/**
	 * Build and shows a confirm dialog based on the given parameters.
	 * 
	 * @param infoMessage
	 * @param title
	 */
	private void confirmDialog(String infoMessage, String title) {
		Object[] options = { "Quit game", "Cancel" };
		int n = JOptionPane.showOptionDialog(frame, infoMessage, title,
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				options, // the titles of buttons
				options[0]); // default button title

		if (n == JOptionPane.YES_OPTION) {
			frame.dispose();
		}
	}

	/**
	 * Initiates the highscore table by setting its header and style.
	 */
	private void initHighScoreTable() {

		String header[] = { "Name", "Score", "Level", "Time" };

		// instance table model
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
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				toggleButton.setContentAreaFilled(true);
				Color lightBlue = new Color(163, 184, 204);
				toggleButton.setBackground(lightBlue);
			}

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

}

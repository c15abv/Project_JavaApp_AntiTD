package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

public class GameView extends JFrame {
	private JFrame frame;
	private Color rightPanelColor = UIManager.getColor("control");
	private Color upperPanelColor = Color.DARK_GRAY;
	private Color lowerPanelColor = Color.DARK_GRAY;

	public static void main(String[] args) {
		GameView gv = new GameView();
		gv.show();
	}

	public GameView() {
		initUI();

	}

	private void initUI() {
		frame = new JFrame("Anit-TD Game");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 400);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		centerFrame();

		JPanel upperPanel = buildUpperPanel();
		JPanel rightPanel = buildRightPanel();
		JPanel leftPanel = buildLeftPanel();
		JPanel lowerPanel = buildLowerPanel();

		frame.add(upperPanel, BorderLayout.NORTH);
		frame.add(rightPanel, BorderLayout.EAST);
		frame.add(leftPanel, BorderLayout.CENTER);
		frame.add(lowerPanel, BorderLayout.SOUTH);

		frame.pack();

	}

	private void centerFrame() {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dim.width / 2 - frame.getSize().width / 2,
				dim.height / 2 - frame.getSize().height / 2);

	}

	private void infoBox(String infoMessage, String titleBar) {
		JOptionPane.showMessageDialog(null, infoMessage, titleBar,
				JOptionPane.INFORMATION_MESSAGE);

	}

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

		gridBag.setConstraints(lowerPanel, c);

		lowerPanel.setLayout(gridBag);

		lowerPanel.add(time);
		lowerPanel.add(reachedGoal);

		return lowerPanel;
	}

	private JPanel buildLeftPanel() {
		JPanel leftPanel = new JPanel();
		leftPanel.setBackground(Color.GREEN);
		return leftPanel;
	}

	private JPanel buildRightPanel() {
		/* Create layout for the panel */
		GridBagLayout gridBag1 = new GridBagLayout();

		JPanel rightPanel = new JPanel();

		rightPanel.setBorder(BorderFactory.createEtchedBorder());
		rightPanel.setBackground(rightPanelColor);

		/* Create radio buttons for the different figures */
		JRadioButton triangleBtn = new TriangleRadioButton();
		JRadioButton circleBtn = new CircleRadioButton();
		JRadioButton squareBtn = new SquareRadioButton();

		/* Add figures to group */
		ButtonGroup creaturesGroup = new ButtonGroup();

		creaturesGroup.add(triangleBtn);
		creaturesGroup.add(circleBtn);
		creaturesGroup.add(squareBtn);

		/* Place buttons in a panel */
		JPanel radioPanel = new JPanel();

		radioPanel.add(triangleBtn);
		radioPanel.add(circleBtn);
		radioPanel.add(squareBtn);
		radioPanel.setBackground(rightPanelColor);

		/* Create layout for the panel */
		GridBagLayout gridBag2 = new GridBagLayout();

		GridBagConstraints c = new GridBagConstraints();
		c.ipadx = 10;
		c.ipady = 10;
		c.gridx = 0;
		c.gridy = 0;

		gridBag2.setConstraints(triangleBtn, c);

		c.gridx = 1;

		gridBag2.setConstraints(circleBtn, c);

		c.gridx = 2;
		gridBag2.setConstraints(squareBtn, c);

		radioPanel.setLayout(gridBag2);
		Border raisedetched = BorderFactory
				.createEtchedBorder(EtchedBorder.RAISED);

		TitledBorder titledBorder = BorderFactory
				.createTitledBorder(raisedetched, "CHOOSE TROOP");
		titledBorder.setTitleJustification(TitledBorder.CENTER);
		radioPanel.setBorder(titledBorder);

		JButton buyBtn = new JButton("Buy");
		buyBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String message;

				try {
					String creature = creaturesGroup.getSelection()
							.getActionCommand();
					message = "Creature chosen: " + creature;

				} catch (NullPointerException e) {
					message = "Select troop first.";
				}

				JOptionPane.showMessageDialog(frame, message);
			}

		});

		JTextField creditTextField = new JTextField("100");
		creditTextField.setColumns(4);
		creditTextField.setBackground(rightPanelColor);
		creditTextField.setHorizontalAlignment(JTextField.CENTER);
		Font f = new Font("Arial", Font.BOLD, 30);
		creditTextField.setFont(f);
		creditTextField.setEditable(false);

		titledBorder = BorderFactory.createTitledBorder(raisedetched,
				"CURRENT CREDIT");
		titledBorder.setTitleJustification(TitledBorder.CENTER);
		creditTextField.setBorder(titledBorder);

		JTextField costTextField = new JTextField("0");
		costTextField.setColumns(4);
		costTextField.setBackground(rightPanelColor);
		costTextField.setHorizontalAlignment(JTextField.CENTER);
		costTextField.setFont(f);
		costTextField.setEditable(false);

		titledBorder = BorderFactory.createTitledBorder(raisedetched,
				"TOTAL COST");
		titledBorder.setTitleJustification(TitledBorder.CENTER);
		costTextField.setBorder(titledBorder);

		JCheckBox teleporterCheckBox = new JCheckBox("Teleporter");
		JRadioButton leftRadioBtn = new JRadioButton("Left");
		JRadioButton rightRadioBtn = new JRadioButton("Right");
		ButtonGroup leftRightBtnGroup = new ButtonGroup();
		leftRightBtnGroup.add(leftRadioBtn);
		leftRightBtnGroup.add(rightRadioBtn);

		JPanel radioPanel2 = new JPanel();
		radioPanel2.add(leftRadioBtn);
		radioPanel2.add(rightRadioBtn);

		titledBorder = BorderFactory.createTitledBorder(raisedetched,
				"CHOOSE DIRECTION");
		titledBorder.setTitleJustification(TitledBorder.CENTER);
		radioPanel2.setBorder(titledBorder);

		JPanel costAndBuy = new JPanel();
		costAndBuy.add(costTextField);
		costAndBuy.add(buyBtn);

		c.ipadx = 10;
		c.ipady = 10;
		c.insets = new Insets(10, 10, 10, 10);
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;

		gridBag1.setConstraints(creditTextField, c);

		c.gridy = 1;

		gridBag1.setConstraints(radioPanel, c);

		c.gridy = 2;

		gridBag1.setConstraints(teleporterCheckBox, c);

		c.gridy = 3;

		gridBag1.setConstraints(radioPanel2, c);

		c.gridy = 4;

		gridBag1.setConstraints(costAndBuy, c);

		rightPanel.setLayout(gridBag1);

		rightPanel.add(creditTextField);
		rightPanel.add(radioPanel);
		rightPanel.add(teleporterCheckBox);
		rightPanel.add(radioPanel2);
		rightPanel.add(costAndBuy);

		return rightPanel;
	}

	private JPanel buildUpperPanel() {
		JPanel upperPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		upperPanel.setBackground(upperPanelColor);

		upperPanel.add(buildGameMenu());
		upperPanel.add(buildInfoMenu());

		return upperPanel;
	}

	private JMenuBar buildInfoMenu() {
		JMenuBar menuBar;
		JMenu menu;
		JMenuItem menuItem;

		// Create the menu bar.
		menuBar = new JMenuBar();

		// Build the menu
		menu = new JMenu("Info");
		menu.setMnemonic(KeyEvent.VK_A);
		menu.getAccessibleContext().setAccessibleDescription(
				"The only menu in this program that has menu items");
		menuBar.add(menu);

		// a group of JMenuItems
		menuItem = new JMenuItem("About", KeyEvent.VK_T);
		menuItem.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
		menuItem.getAccessibleContext()
				.setAccessibleDescription("This doesn't really do anything");

		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				infoBox("About text", "About");
			}
		});

		menu.add(menuItem);

		// a group of JMenuItems
		menuItem = new JMenuItem("Help", KeyEvent.VK_T);
		menuItem.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
		menuItem.getAccessibleContext()
				.setAccessibleDescription("This doesn't really do anything");

		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				infoBox("Help text", "Help");
			}
		});

		menu.add(menuItem);

		return menuBar;

	}

	private void confirmDialog(String infoMessage, String title) {
		Object[] options = { "Quit game", "Cancel" };
		int n = JOptionPane.showOptionDialog(frame, infoMessage, title,
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, // do
																				// not
																				// use
																				// a
																				// custom
																				// Icon
				options, // the titles of buttons
				options[0]); // default button title

		if (n == JOptionPane.YES_OPTION) {
			frame.dispose();
		}
	}

	private JMenuBar buildGameMenu() {
		JMenuBar menuBar;
		JMenu menu;
		JMenuItem menuItem;

		// Create the menu bar.
		menuBar = new JMenuBar();

		// Build the first menu.
		menu = new JMenu("Game Menu");
		menu.setMnemonic(KeyEvent.VK_A);
		menu.getAccessibleContext().setAccessibleDescription(
				"The only menu in this program that has menu items");
		menuBar.add(menu);

		// a group of JMenuItems
		menuItem = new JMenuItem("New Game", KeyEvent.VK_T);
		menuItem.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
		menuItem.getAccessibleContext()
				.setAccessibleDescription("This doesn't really do anything");
		menu.add(menuItem);

		JToggleButton pauseAndResumeBtn = createPauseAndResumeButton();
		// pauseAndResumeBtn.setSize(menuBar.getSize());
		menu.add(pauseAndResumeBtn);

		// a group of JMenuItems
		menuItem = new JMenuItem("Quit", KeyEvent.VK_T);
		menuItem.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
		menuItem.getAccessibleContext()
				.setAccessibleDescription("This doesn't really do anything");

		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				confirmDialog("Are you sure you want to quit the current game?",
						"Are you sure?");
			}
		});

		menu.add(menuItem);
		/*
		 * menuItem = new JMenuItem("Both text and icon", new
		 * ImageIcon("images/middle.gif")); menuItem.setMnemonic(KeyEvent.VK_B);
		 * menu.add(menuItem);
		 */

		return menuBar;
	}

	public JToggleButton createPauseAndResumeButton() {
		Action toggleAction = new AbstractAction("  Pause") {

			@Override
			public void actionPerformed(ActionEvent e) {
				AbstractButton button = (AbstractButton) e.getSource();

				if (button.isSelected()) {
					button.setText("  Resume");
					// Start the action here
				} else {
					button.setText("  Pause");
					// Stop the action here
				}
			}
		};

		Border emptyBorder = BorderFactory.createEmptyBorder();
		JToggleButton toggleButton = new JToggleButton(toggleAction);
		toggleButton.setBorder(emptyBorder);
		// toggleButton.setBorderPainted(false);
		// toggleButton.setFocusPainted(false);
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

	public void show() {
		this.frame.setVisible(true);
	}

}

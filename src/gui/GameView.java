package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
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
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import creatures.CircleCreatureFigure;
import creatures.CreatureFigure;
import creatures.CreatureFigureTemplate;
import start.Figures;
import start.Position;

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
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		centerFrame();

		JPanel upperPanel = buildUpperPanel();
		JPanel rightPanel = new RightPanel(3, 100);
		JPanel leftPanel = buildCenterPanel();
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

	private void showInfoBox(String infoMessage, String titleBar) {
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

		lowerPanel.setLayout(gridBag);

		lowerPanel.add(time);
		lowerPanel.add(reachedGoal);

		return lowerPanel;
	}

	private JPanel buildCenterPanel() {
		JPanel leftPanel = new JPanel();
		leftPanel.setBackground(Color.GREEN);
		return leftPanel;
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

		menuBar = new JMenuBar();

		menu = new JMenu("Info");
		menu.setMnemonic(KeyEvent.VK_A);
		menu.getAccessibleContext().setAccessibleDescription(
				"The only menu in this program that has menu items");
		menuBar.add(menu);

		menuItem = new JMenuItem("About", KeyEvent.VK_T);
		menuItem.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
		menuItem.getAccessibleContext()
				.setAccessibleDescription("This doesn't really do anything");

		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showInfoBox("About text", "About");
			}
		});

		menu.add(menuItem);

		menuItem = new JMenuItem("Help", KeyEvent.VK_T);
		menuItem.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
		menuItem.getAccessibleContext()
				.setAccessibleDescription("This doesn't really do anything");

		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showInfoBox("Help text", "Help");
			}
		});

		menu.add(menuItem);

		return menuBar;
	}

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

	private JMenuBar buildGameMenu() {
		JMenuBar menuBar;
		JMenu menu;
		JMenuItem menuItem;

		menuBar = new JMenuBar();

		menu = new JMenu("Game Menu");
		menu.setMnemonic(KeyEvent.VK_A);
		menu.getAccessibleContext().setAccessibleDescription(
				"The only menu in this program that has menu items");
		menuBar.add(menu);

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
	
	private JToggleButton createPauseAndResumeButton() {
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

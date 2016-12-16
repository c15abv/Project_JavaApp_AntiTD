package gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import creatures.CreatureFigure.Orientation;
import start.Figures;

/**
 * Class that builds the right panel of the gui where the user can create and
 * buy troops.
 * 
 * @author Karolina Jonzén
 * @version 1.0
 */
@SuppressWarnings("serial")
public class RightPanel extends JPanel {
	private List<FigureRepresentation> troop = new ArrayList<>();
	private Color backgroundColor = UIManager.getColor("control");
	private Font textFont = new Font("Arial", Font.BOLD, 30);
	private int currentCredit = 0;
	private JPanel modePanel;
	private boolean isPlayMode = false;
	private ButtonGroup shapeBtnGroup = new ButtonGroup();
	private ButtonGroup troopsBtnGroup = new ButtonGroup();
	private JPanel creaturePreview;
	private JButton createBtn;
	private JButton startGameBtn;
	private JButton buyBtn;
	private FigureRepresentation figure;
	private JSlider colorSlider;
	private JSlider sizeSlider;
	private JSlider teleporterTimeSlider;
	private int currentCreatureCost = 0;
	private JPanel troopPanel;
	private JPanel creatorModePanel;
	private JPanel playModePanel;
	private JPanel settingsPanel;
	private JTextField costTextField;
	private boolean creatureSelected = false;
	private JPanel creatureInfoPanel;
	private JTextField creditTextField;
	private JTextField hitPointsTextField;
	private JTextField speedTextField;
	private JTextField teleporterTextField;
	private JTextField directionTextField;
	private GameViewModel gameViewModel;

	/**
	 * Constructor that sets the game view model to the given parameter and
	 * initiates the gui.
	 * 
	 * @param gameViewModel
	 */
	public RightPanel(GameViewModel gameViewModel) {
		super();
		this.gameViewModel = gameViewModel;
	}

	/**
	 * Initiates the gui by building the different components and setting the
	 * layout.
	 */
	public void initUI() {

		GridBagLayout gb = new GridBagLayout();

		GridBagConstraints c = new GridBagConstraints();

		initCreatorModePanel();

		troopPanel = new JPanel();

		initTroopPanel();

		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(15, 15, 15, 15);
		c.anchor = GridBagConstraints.NORTH;

		gb.setConstraints(troopPanel, c);
		this.setLayout(gb);
		this.add(troopPanel);

		settingsPanel = new JPanel();

		c = new GridBagConstraints();
		c.insets = new Insets(15, 15, 15, 15);
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.HORIZONTAL;

		createCreditTextField();
		gb.setConstraints(creditTextField, c);
		settingsPanel.add(creditTextField);

		c.gridy = 1;

		createCreaturePreview();
		gb.setConstraints(creaturePreview, c);
		settingsPanel.add(creaturePreview);

		c.gridy = 2;

		modePanel = creatorModePanel;

		gb.setConstraints(modePanel, c);
		creaturePreview.setPreferredSize(new Dimension(
				(FigureRepresentation.TEMP_SIZE * sizeSlider.getMaximum() / 100)
						+ 30,
				(FigureRepresentation.TEMP_SIZE * sizeSlider.getMaximum() / 100)
						+ 30));

		settingsPanel.add(modePanel);

		settingsPanel.setLayout(gb);

		c = new GridBagConstraints();

		c.gridx = 1;
		c.gridy = 0;
		c.fill = GridBagConstraints.VERTICAL;

		gb.setConstraints(settingsPanel, c);

		this.add(settingsPanel);

		initCreditTextFieldValue();

		this.revalidate();
		this.repaint();

	}

	/**
	 * Initiates the troop panel.
	 */
	private void initTroopPanel() {
		troopPanel.setLayout(new BoxLayout(troopPanel, BoxLayout.PAGE_AXIS));

		setStyle(troopPanel, "YOUR TROOP");

		for (int i = 0; i < gameViewModel.getLevelInfo().getNrOfTroops(); i++) {

			JPanel emptyCreature = new EmptyFigureRepresentation();
			Border border = BorderFactory.createDashedBorder(Color.DARK_GRAY);
			JPanel container = new JPanel();
			container.setBorder(border);

			container.add(emptyCreature);

			troopPanel.add(container);

		}

		troopPanel.addContainerListener(new ContainerListener() {

			@Override
			public void componentAdded(ContainerEvent arg0) {
				if (troop.size() == gameViewModel.getLevelInfo()
						.getNrOfTroops()) {
					createBtn.setEnabled(false);
					startGameBtn.setEnabled(true);
				}

			}

			@Override
			public void componentRemoved(ContainerEvent arg0) {

			}

		});

		troopPanel.revalidate();
		troopPanel.repaint();
	}

	/**
	 * Fills the troop panel by making containers for the number of troops
	 * according to the current level.
	 */
	protected void fillTroopPanel() {
		for (int i = 0; i < gameViewModel.getLevelInfo().getNrOfTroops(); i++) {

			JPanel emptyCreature = new EmptyFigureRepresentation();
			Border border = BorderFactory.createDashedBorder(Color.DARK_GRAY);
			JPanel container = new JPanel();
			container.setBorder(border);

			container.add(emptyCreature);

			troopPanel.add(container);

		}
	}

	/**
	 * Initiates the creator mode panel.
	 */
	private void initCreatorModePanel() {

		creatorModePanel = new JPanel();

		GridBagLayout gb = new GridBagLayout();

		GridBagConstraints c = new GridBagConstraints();

		c.insets = new Insets(10, 0, 10, 0);
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.HORIZONTAL;

		JPanel shapesPanel = createShapesPanel();
		gb.setConstraints(shapesPanel, c);
		creatorModePanel.add(shapesPanel);

		c.gridy = 1;

		JCheckBox teleporterCheckBox = createTeleporterCheckBox();
		gb.setConstraints(teleporterCheckBox, c);
		creatorModePanel.add(teleporterCheckBox, c);

		c.gridy = 2;

		createColorSlider();
		gb.setConstraints(colorSlider, c);
		creatorModePanel.add(colorSlider);

		c.gridy = 3;

		createSizeSlider();
		gb.setConstraints(sizeSlider, c);
		creatorModePanel.add(sizeSlider);

		c.gridy = 4;

		JPanel orientationPanel = createOrientationPanel();
		gb.setConstraints(orientationPanel, c);
		creatorModePanel.add(orientationPanel);

		c.gridy = 5;

		createCostTextField();
		gb.setConstraints(costTextField, c);
		creatorModePanel.add(costTextField);

		c.gridy = 6;
		c.fill = GridBagConstraints.NONE;

		createCreateButton();
		gb.setConstraints(createBtn, c);
		creatorModePanel.add(createBtn);

		c.gridy = 7;

		createStartGameButton();
		gb.setConstraints(startGameBtn, c);
		creatorModePanel.add(startGameBtn);

		creatorModePanel.setLayout(gb);

	}

	/**
	 * Creates and returns the orientation panel of the creator mode panel.
	 * 
	 * @return
	 */
	private JPanel createOrientationPanel() {
		JPanel directionPanel = new JPanel();

		JRadioButton leftRadioBtn = new JRadioButton("Left");
		leftRadioBtn.setActionCommand(Orientation.LEFT.name());
		JRadioButton rightRadioBtn = new JRadioButton("Right");
		rightRadioBtn.setActionCommand(Orientation.RIGHT.name());

		ItemListener itemListener = new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				AbstractButton aButton = (AbstractButton) e.getSource();

				ButtonModel aModel = aButton.getModel();
				if (aModel.isSelected()) {
					String orientation = aButton.getActionCommand();

					if (orientation.equals(Orientation.LEFT.name())) {
						figure.setOrientation(Orientation.LEFT);

					} else {
						figure.setOrientation(Orientation.RIGHT);
					}

				}
			}
		};

		leftRadioBtn.addItemListener(itemListener);
		rightRadioBtn.addItemListener(itemListener);

		ButtonGroup leftRightBtnGroup = new ButtonGroup();
		leftRightBtnGroup.add(leftRadioBtn);
		leftRightBtnGroup.add(rightRadioBtn);

		directionPanel.add(leftRadioBtn);
		directionPanel.add(rightRadioBtn);
		setStyle(directionPanel, "CHOOSE DIRECTION");

		return directionPanel;
	}

	/**
	 * Updates the cost text field to the current creature cost.
	 */
	private void updateCostTextField() {
		costTextField.setText(String.valueOf(currentCreatureCost));
		figure.setCost(currentCreatureCost);
	}

	/**
	 * Creates and return the teleporter checkbox of the creator mode panel.
	 * 
	 * @return
	 */
	private JCheckBox createTeleporterCheckBox() {
		JCheckBox teleporter = new JCheckBox("Teleporter");

		teleporter.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				int telePorterCost = gameViewModel.getLevelInfo()
						.getTeleporterCost();
				if (teleporter.isSelected()) {
					updateCurrentCost(telePorterCost);
					figure.setIsTeleportCreature(true);
					currentCreatureCost = telePorterCost;
				} else {
					updateCurrentCost(-telePorterCost);
					figure.setIsTeleportCreature(false);
					currentCreatureCost = gameViewModel.getLevelInfo()
							.getCreatureCost();
				}
				updateCostTextField();
				figure.setCost(currentCreatureCost);

			}
		});

		return teleporter;
	}

	/**
	 * Creates the create button of the creator mode panel.
	 */
	private void createCreateButton() {
		createBtn = new JButton("Create");
		Container container = this.getTopLevelAncestor();

		createBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ae) {
				String message;

				try {
					int index = 0;
					int troopSize = troop.size();
					if (troopSize != 0) {
						index = troopSize;
					}

					
					JRadioButton troopCreature = new TroopRadioButton(figure,
							index);
					troopsBtnGroup.add(troopCreature);

					ItemListener itemListener = new ItemListener() {
						public void itemStateChanged(ItemEvent e) {
							AbstractButton aButton = (AbstractButton) e
									.getSource();

							ButtonModel aModel = aButton.getModel();
							if (aModel.isSelected()) {

								if (isPlayMode) {
									int index = Integer.parseInt(
											aButton.getActionCommand());
									FigureRepresentation figure = troop
											.get(index);
									updateCreaturePreview(figure);
									hitPointsTextField.setText("HP: "
											+ String.valueOf(gameViewModel
													.getHitpoints(index)));
									speedTextField.setText("Speed: " + "?");
									directionTextField.setText("Orientation: "
											+ figure.orientation.name());

									if (figure.isTeleportCreature) {
										teleporterTextField.setText(
												"Teleporter: " + "Yes");

										teleporterTimeSlider.setEnabled(true);

									} else {
										teleporterTextField
												.setText("Teleporter: " + "No");
										teleporterTimeSlider.setEnabled(false);

									}
									currentCreatureCost = figure.cost;
									updateCostTextField();

								}

							}
						}
					};

					troopCreature.addItemListener(itemListener);

					JPanel container = new JPanel();
					container.add(troopCreature);
					container.setBorder(
							BorderFactory.createDashedBorder(Color.DARK_GRAY));

					if (figure.getOrientation() != null) {
						troop.add(figure);

						createNewCreature(figure.getCreatureType().name());

						troopPanel.remove(index);
						troopPanel.add(container, index);
						troopPanel.revalidate();
						troopPanel.repaint();

					} else {
						message = "Select orientation of creature.";
						JOptionPane.showMessageDialog(container, message);

					}

				} catch (NullPointerException e) {
					message = "Select troop first.";
					JOptionPane.showMessageDialog(container, message);
				}

			}

		});
	}

	/**
	 * Disables components.
	 */
	public void disableComponents() {
		buyBtn.setEnabled(false);
		teleporterTimeSlider.setEnabled(false);

	}

	/**
	 * Sets the current figure shown in the creature preview to the creature
	 * type given as parameter.
	 * 
	 * @param creatureType
	 */
	private void createNewCreature(String creatureType) {
		switch (Figures.valueOf(creatureType)) {
		case TRIANGLE:
			figure = new TriangleRepresentation(colorSlider.getValue(),
					(float) sizeSlider.getValue() / 100,
					figure.isTeleportCreature, figure.getOrientation(),
					figure.cost);
			//System.out.println("Figure size = "+figure.getScale());

			break;

		case CIRCLE:
			figure = new CircleRepresentation(colorSlider.getValue(),
					(float) sizeSlider.getValue() / 100,
					figure.isTeleportCreature, figure.getOrientation(),
					figure.cost);
			break;

		case SQUARE:
			figure = new SquareRepresentation(colorSlider.getValue(),
					(float) sizeSlider.getValue() / 100,
					figure.isTeleportCreature, figure.getOrientation(),
					figure.cost);
			break;
		default:
			break;
		}
	}

	/**
	 * Creates and returns the buy button of the play mode panel.
	 * 
	 * @return
	 */
	private void initBuyButton() {
		buyBtn = new JButton("Buy");
		Container container = this.getTopLevelAncestor();

		buyBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ae) {
				String message;

				try {
					ButtonModel btn = troopsBtnGroup.getSelection();
					int index = Integer.parseInt(btn.getActionCommand());

					if (currentCreatureCost <= currentCredit) {
						if (troop.get(index).isTeleportCreature) {
							gameViewModel.buyCreature(index,
									(long) teleporterTimeSlider.getValue());
						} else {
							gameViewModel.buyCreature(index);
						}
						updateCreditTextField();
					} else {
						JOptionPane.showMessageDialog(null,
								"You don't have enough credit to buy this creature.");
					}

				} catch (NullPointerException e) {
					message = "Select troop first.";
					e.printStackTrace();
					JOptionPane.showMessageDialog(container, message);
				}

			}

		});
	}

	/**
	 * Updates the credit text field based on the chosen creature's cost.
	 */
	public void updateCreditTextField() {
		int oldCredit = Integer.parseInt(creditTextField.getText());
		int newCredit = oldCredit - currentCreatureCost;
		currentCredit = newCredit;

		creditTextField.setText(String.valueOf(currentCredit));
	}

	/**
	 * Sets the credit text field to the given parameter.
	 */
	public synchronized void setCreditTextField(int credit) {
		creditTextField.setText(String.valueOf(credit));
	}

	/**
	 * Creates the start game button of the creator mode panel.
	 */
	private void createStartGameButton() {
		startGameBtn = new JButton("Start game");
		startGameBtn.setEnabled(false);
		isPlayMode = true;

		startGameBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ae) {
				initPlayModePanel();
				GridBagLayout layout = (GridBagLayout) settingsPanel
						.getLayout();
				GridBagConstraints c = new GridBagConstraints();
				settingsPanel.remove(modePanel);
				modePanel = playModePanel;

				troopsBtnGroup.setSelected(((AbstractButton) troopsBtnGroup
						.getElements().nextElement()).getModel(), true);
				
				for(FigureRepresentation figure:troop)	{
					gameViewModel.addTroop(figure);
				}

				c.gridx = 0;
				c.gridy = 0;
				c.fill = GridBagConstraints.HORIZONTAL;
				c.weightx = 1;
				c.weighty = 1;
				c.insets = new Insets(15, 15, 15, 15);

				layout.setConstraints(creditTextField, c);

				c.gridy = 1;

				layout.setConstraints(creaturePreview, c);

				c.gridy = 2;

				layout.setConstraints(modePanel, c);
				settingsPanel.setLayout(layout);

				settingsPanel.add(modePanel);
				settingsPanel.revalidate();
				settingsPanel.repaint();

				gameViewModel.startGame();
			}

		});

	}

	/**
	 * Initiates the play mode panel.
	 */
	private void initPlayModePanel() {
		playModePanel = new JPanel() {
			@Override
			public Dimension getPreferredSize() {
				return creatorModePanel.getSize();
			}
		};

		GridBagLayout gb = new GridBagLayout();

		GridBagConstraints c = new GridBagConstraints();

		c.insets = new Insets(10, 0, 10, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 1;
		c.gridx = 0;
		c.gridy = 0;

		createCreatureInfoPanel();
		gb.setConstraints(creatureInfoPanel, c);
		playModePanel.add(creatureInfoPanel);

		c.gridy = 2;

		gb.setConstraints(costTextField, c);
		playModePanel.add(costTextField);

		c.gridy = 3;
		c.fill = GridBagConstraints.NONE;

		initBuyButton();
		gb.setConstraints(buyBtn, c);
		playModePanel.add(buyBtn);

		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;

		createTeleporterTimeSlider();
		gb.setConstraints(teleporterTimeSlider, c);
		playModePanel.add(teleporterTimeSlider);

		playModePanel.setLayout(gb);

	}

	/**
	 * Creates the panel showing creature property information in the play mode
	 * panel.
	 */
	private void createCreatureInfoPanel() {
		GridBagLayout gb = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		creatureInfoPanel = new JPanel();
		setStyle(creatureInfoPanel, "PROPERTIES");
		hitPointsTextField = createNewPropertyTextField("HP");
		speedTextField = createNewPropertyTextField("Speed");
		directionTextField = createNewPropertyTextField("Direction");
		teleporterTextField = createNewPropertyTextField("Teleporter");

		c.insets = new Insets(10, 10, 10, 10);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 1;
		c.gridx = 0;
		c.gridy = 0;

		gb.setConstraints(hitPointsTextField, c);
		creatureInfoPanel.add(hitPointsTextField);

		c.gridy = 1;

		gb.setConstraints(speedTextField, c);
		creatureInfoPanel.add(speedTextField);

		c.gridy = 2;

		gb.setConstraints(directionTextField, c);
		creatureInfoPanel.add(directionTextField);

		c.gridy = 3;

		gb.setConstraints(teleporterTextField, c);
		creatureInfoPanel.add(teleporterTextField);

		creatureInfoPanel.setLayout(gb);

	}

	/**
	 * Creates and returns a new property text field based on the given
	 * parameter.
	 * 
	 * @param propertyName
	 * @return
	 */
	private JTextField createNewPropertyTextField(String propertyName) {
		JTextField propertyTextField = new JTextField() {

			@Override
			public void setBorder(Border border) {
				// No!
			}
		};

		propertyTextField.setText(propertyName + ": ");
		propertyTextField.setBackground(UIManager.getColor("control"));
		propertyTextField.setEditable(false);

		return propertyTextField;
	}

	/**
	 * Creates the cost text field.
	 */
	private void createCostTextField() {
		costTextField = new JTextField(String.valueOf(currentCreatureCost));
		costTextField.setFont(textFont);
		costTextField.setHorizontalAlignment(JTextField.HORIZONTAL);
		setStyle(costTextField, "CREATURE COST");
	}

	/**
	 * Creates the color slider where the user chooses the creature's color,
	 * which is being updated in the creature preview.
	 */
	private void createColorSlider() {
		colorSlider = new JSlider(JSlider.HORIZONTAL, 0, 369, 0);
		colorSlider.setMinorTickSpacing(10);
		colorSlider.setMajorTickSpacing(100);
		colorSlider.setPaintTicks(true);
		colorSlider.setPaintLabels(true);
		// slider.setSnapToTicks(true);

		setStyle(colorSlider, "CHOOSE COLOR");

		colorSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				int value = colorSlider.getValue();

				figure.setHue(value);
				updateCreaturePreview(figure);

			}
		});

	}

	/**
	 * Creates the size slider panel where the user chooses the creature's size,
	 * which is being updated in the creature preview.
	 */
	private void createSizeSlider() {
		sizeSlider = new JSlider(JSlider.HORIZONTAL, 50, 150, 100);
		sizeSlider.setMinorTickSpacing(50);
		sizeSlider.setMajorTickSpacing(100);
		sizeSlider.setPaintTicks(true);
		sizeSlider.setPaintLabels(true);

		java.util.Hashtable<Integer, JLabel> labelTable = new java.util.Hashtable<Integer, JLabel>();

		labelTable.put(new Integer(150), new JLabel("1.50"));
		labelTable.put(new Integer(100), new JLabel("1.0"));
		labelTable.put(new Integer(50), new JLabel("0.50"));

		sizeSlider.setLabelTable(labelTable);

		sizeSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				float value = sizeSlider.getValue();

				figure.setScale(value / 100);
				updateCreaturePreview(figure);

			}
		});

		setStyle(sizeSlider, "CHOOSE HP/SPEED");
	}

	/**
	 * Creates the creature preview where current creature figure is shown.
	 */
	private void createCreaturePreview() {

		creaturePreview = new JPanel();

		figure = new TriangleRepresentation();

		creaturePreview.add(figure);

		setStyle(creaturePreview, "CREATURE");

	}

	/**
	 * Creates the credit text field.
	 */
	private void createCreditTextField() {
		creditTextField = new JTextField(String.valueOf(currentCredit));
		creditTextField.setHorizontalAlignment(JTextField.CENTER);

		creditTextField.setFont(textFont);
		creditTextField.setEditable(false);

		setStyle(creditTextField, "CURRENT CREDIT");
	}

	/**
	 * Creates the shape panel where the user chooses the creatue's shape, which
	 * is being updated in the creature preview.
	 * 
	 * @return
	 */
	private JPanel createShapesPanel() {

		JPanel shapesPanel = new JPanel();

		JRadioButton triangleBtn = new TriangleRadioButton();
		JRadioButton circleBtn = new CircleRadioButton();
		JRadioButton squareBtn = new SquareRadioButton();

		ItemListener itemListener = new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				AbstractButton aButton = (AbstractButton) e.getSource();

				ButtonModel aModel = aButton.getModel();
				if (aModel.isSelected()) {
					String btnShape = aButton.getText();

					createNewCreature(btnShape);

					updateCreaturePreview(figure);
					if (!creatureSelected) {
						updateCurrentCost(
								gameViewModel.getLevelInfo().getCreatureCost());
						updateCostTextField();
						creatureSelected = true;

					}
				}

			}

		};

		triangleBtn.addItemListener(itemListener);
		circleBtn.addItemListener(itemListener);
		squareBtn.addItemListener(itemListener);

		shapeBtnGroup.add(triangleBtn);
		shapeBtnGroup.add(circleBtn);
		shapeBtnGroup.add(squareBtn);

		shapesPanel.add(triangleBtn);
		shapesPanel.add(circleBtn);
		shapesPanel.add(squareBtn);

		setStyle(shapesPanel, "CHOOSE SHAPE");

		GridBagLayout gb = new GridBagLayout();

		GridBagConstraints c = new GridBagConstraints();
		c.ipadx = 10;
		c.ipady = 10;
		c.gridx = 0;
		c.gridy = 0;

		gb.setConstraints(triangleBtn, c);

		c.gridx = 1;

		gb.setConstraints(circleBtn, c);

		c.gridx = 2;

		gb.setConstraints(squareBtn, c);

		shapesPanel.setLayout(gb);

		return shapesPanel;
	}

	/**
	 * Updates the current cost based on the given parameter.
	 * 
	 * @param costChange
	 */
	private void updateCurrentCost(int costChange) {
		currentCreatureCost += costChange;
		figure.setCost(currentCreatureCost);
	}

	/**
	 * Updates the figure shown in the creature preview to the figure given as
	 * parameter.
	 * 
	 * @param figure
	 */
	private void updateCreaturePreview(FigureRepresentation figure) {
		creaturePreview.removeAll();
		creaturePreview.add(figure);
		creaturePreview.validate();
		creaturePreview.repaint();
	}

	/**
	 * Sets the style of a component based on the given parameters.
	 * 
	 * @param comp
	 * @param borderTitle
	 */
	private void setStyle(JComponent comp, String borderTitle) {
		comp.setBackground(backgroundColor);

		Border raisedetched = BorderFactory
				.createEtchedBorder(EtchedBorder.RAISED);

		TitledBorder titledBorder = BorderFactory
				.createTitledBorder(raisedetched, borderTitle);
		titledBorder.setTitleJustification(TitledBorder.CENTER);
		comp.setBorder(titledBorder);

	}
	
	/**
	 * Sets the credit text field to its start value.
	 * 
	 * @param credit
	 */
	public void initCreditTextFieldValue() {
		currentCredit = gameViewModel.getCredits();
		creditTextField.setText(String.valueOf(currentCredit));
	}

	/**
	 * Resets the gui for a new game.
	 */
	public void resetGui() {
		this.removeAll();
		currentCreatureCost = 0;
	}

	/**
	 * Creates the time slider where user chooses the drop time for teleporters.
	 */
	public void createTeleporterTimeSlider() {
		teleporterTimeSlider = new JSlider(JSlider.HORIZONTAL, 1000, 5000,
				1000);
		teleporterTimeSlider.setMinorTickSpacing(1000);
		// teleporterTimeSlider.setMajorTickSpacing(100);
		teleporterTimeSlider.setPaintTicks(true);
		teleporterTimeSlider.setPaintLabels(true);

		java.util.Hashtable<Integer, JLabel> labelTable = new java.util.Hashtable<Integer, JLabel>();

		labelTable.put(new Integer(5000), new JLabel("5 s"));
		labelTable.put(new Integer(3000), new JLabel("3 s"));
		labelTable.put(new Integer(1000), new JLabel("1 s"));

		teleporterTimeSlider.setLabelTable(labelTable);

		setStyle(teleporterTimeSlider, "CHOOSE DROP TIME");
	}

}

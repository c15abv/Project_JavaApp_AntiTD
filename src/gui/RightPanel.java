package gui;

import java.awt.Color;
import java.awt.Component;
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
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import creatures.CreatureFigureTemplate;
import start.Direction;
import start.Figures;

@SuppressWarnings("serial")
public class RightPanel extends JPanel {
	private int nrOfCreatureTemplates;
	private List<FigureRepresentation> troop = new ArrayList<>();
	private Color backgroundColor = UIManager.getColor("control");
	private Font titleFont = new Font("Arial", Font.PLAIN, 15);
	private Font textFont = new Font("Arial", Font.BOLD, 30);
	private int currentCredit;
	private JPanel modePanel;
	private boolean isPlayMode = false;
	private ButtonGroup shapeBtnGroup = new ButtonGroup();
	private ButtonGroup troopsBtnGroup = new ButtonGroup();
	private JPanel creaturePreview;
	private JButton createBtn;
	private JButton startGameBtn;
	private FigureRepresentation figure;
	private JSlider colorSlider;
	private JSlider sizeSlider;
	private int creatureCost = 50;
	private int currentCreatureCost = 0;
	private JPanel troopPanel;
	private JPanel creatorModePanel;
	private JPanel playModePanel;
	private JPanel settingsPanel;
	private JTextField costTextField;
	private int costTextFieldIndex;
	private int teleporterCost = 80;
	private boolean creatureSelected = false;
	private JPanel creatureInfoPanel;
	private JTextField creditTextField;
	private JTextField hitPointsTextField;
	private JTextField speedTextField;
	

	public RightPanel(int nrOfCreatureTemplates, int levelCredit) {
		super();
		this.nrOfCreatureTemplates = nrOfCreatureTemplates;
		this.currentCredit = levelCredit;

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				initUI();
			}
		});
	}

	private void initUI() {

		GridBagLayout gb = new GridBagLayout();

		GridBagConstraints c = new GridBagConstraints();

		troopPanel = new JPanel();
		initTroopPanel();
		// stroopPanel.setAlignmentY(Component.TOP_ALIGNMENT);

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

		initCreatorModePanel();

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

	}

	private void initTroopPanel() {
		troopPanel.setLayout(new BoxLayout(troopPanel, BoxLayout.PAGE_AXIS));
		// troopPanel.setLayout(new GridLayout(nrOfCreatureTemplates+2, 0, 15,
		// 0));
		setStyle(troopPanel, "YOUR TROOP");

		for (int i = 0; i < nrOfCreatureTemplates; i++) {

			JPanel emptyCreature = new EmptyFigureRepresentation();
			Border border = BorderFactory.createDashedBorder(Color.DARK_GRAY);
			// Border empty = new EmptyBorder(30,30,30,30);
			JPanel container = new JPanel();
			container.setBorder(border);

			container.add(emptyCreature);

			// emptyCreature.setAlignmentX(Component.CENTER_ALIGNMENT);
			// emptyCreature.setAlignmentY(Component.CENTER_ALIGNMENT);
			troopPanel.add(container);

		}

		troopPanel.addContainerListener(new ContainerListener() {

			@Override
			public void componentAdded(ContainerEvent arg0) {
				if (troop.size() == nrOfCreatureTemplates) {
					createBtn.setEnabled(false);
					startGameBtn.setEnabled(true);
				}

			}

			@Override
			public void componentRemoved(ContainerEvent arg0) {
				// TODO Auto-generated method stub

			}

		});

		troopPanel.revalidate();
		troopPanel.repaint();

	}

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
		// c.insets = new Insets(0, 15, 0, 15);

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

		JPanel directionPanel = createDirectionPanel();
		gb.setConstraints(directionPanel, c);
		creatorModePanel.add(directionPanel);

		c.gridy = 5;

		createCostTextField();
		costTextFieldIndex = c.gridy;
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

	private void updateCostTextField() {
		costTextField.setText(String.valueOf(currentCreatureCost));
	}

	private JCheckBox createTeleporterCheckBox() {
		JCheckBox teleporter = new JCheckBox("Teleporter");

		teleporter.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (teleporter.isSelected()) {
					updateCurrentCost(teleporterCost);
					figure.setIsTeleportCreature(true);
				} else {
					updateCurrentCost(-teleporterCost);
					figure.setIsTeleportCreature(false);
				}
				updateCostTextField();

			}
		});

		return teleporter;
	}

	private void createCreateButton() {
		createBtn = new JButton("Create");
		Container container = this.getTopLevelAncestor();
		


		createBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ae) {
				String message;

				try {
					String creature = shapeBtnGroup.getSelection()
							.getActionCommand();
					
					
					
					int index = 0;
					int troopSize = troop.size();
					if (troopSize != 0) {
						index = troopSize;
					}
					

					JRadioButton troopCreature = new TroopRadioButton(figure, index);
					//troopCreature.setEnabled(false);
					troopsBtnGroup.add(troopCreature);
					
					ItemListener itemListener = new ItemListener() {
						public void itemStateChanged(ItemEvent e) {
							AbstractButton aButton = (AbstractButton) e
									.getSource();

							ButtonModel aModel = aButton.getModel();
							if (aModel.isSelected()) {
								// ButtonModel m =
								// creatureSelector.getSelection();
								String btnShape = aButton.getText();
								if (isPlayMode) {
									//FigureRepresentation figure = troop.get(index);
									int index = Integer.parseInt(aButton.getActionCommand());
									System.out.println("Index = "+index);
									FigureRepresentation figure = troop.get(index);
									updateCreaturePreview(figure);
								//	troopPanel.getComponent(troopsBtnGroup.getSelection())
									//updateCreaturePreview()
								}
							}
						}
					};

					troopCreature.addItemListener(itemListener);

					JPanel container = new JPanel();
					container.add(troopCreature);
					container.setBorder(
							BorderFactory.createDashedBorder(Color.DARK_GRAY));

					troop.add(figure);
					
					
					
					switch (Figures.valueOf(figure.getCreatureType().toString())) {
					case TRIANGLE:
						figure = new TriangleRepresentation(
								colorSlider.getValue(),
								(float) sizeSlider.getValue() / 100);
						break;

					case CIRCLE:
						figure = new CircleRepresentation(
								colorSlider.getValue(),
								(float) sizeSlider.getValue() / 100);
						break;

					case SQUARE:
						figure = new SquareRepresentation(
								colorSlider.getValue(),
								(float) sizeSlider.getValue() / 100);
						break;
					default:
						break;
					}
					
					// System.out.println(index);
					troopPanel.remove(index);
					troopPanel.add(container, index);
					troopPanel.revalidate();
					troopPanel.repaint();
					
					

					message = "Type: " + figure.creatureType + "\nDirection: "
							+ figure.direction + "\nIsTeleport: "
							+ figure.isTeleportCreature;

					calculateCreatureCost();

				} catch (NullPointerException e) {
					message = "Select troop first.";
					e.printStackTrace();
				}

				JOptionPane.showMessageDialog(container, message);
			}

		});
	}
	
	private void updateProperties(FigureRepresentation selectedFigure)	{
		//selectedFigure.cost
	}
	

	private JButton createBuyButton() {
		JButton buyBtn = new JButton("Buy");
		Container container = this.getTopLevelAncestor();

		buyBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ae) {
				String message;

				try {
					String creature = shapeBtnGroup.getSelection()
							.getActionCommand();

					message = "Creature chosen: " + creature;

					calculateCreatureCost();

				} catch (NullPointerException e) {
					message = "Select troop first.";
				}

				JOptionPane.showMessageDialog(container, message);
			}

		});

		return buyBtn;
	}

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
			}

		});

	}

	private void initPlayModePanel() {
		playModePanel = new JPanel();/*
										 * {
										 * 
										 * @Override public Dimension
										 * getPreferredSize() { return
										 * creatorModePanel.getSize(); } };
										 */

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

		c.gridy = 1;

		//JButton placeTeleportBtn = new JButton("Place teleport");
		
		gb.setConstraints(costTextField, c);
		playModePanel.add(costTextField);

		c.gridy = 2;
		c.fill = GridBagConstraints.NONE;

		JButton buyBtn = createBuyButton();
		gb.setConstraints(buyBtn, c);
		playModePanel.add(buyBtn);

		playModePanel.setLayout(gb);

	}

	private void createCreatureInfoPanel() {
		GridBagLayout gb = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		creatureInfoPanel = new JPanel();
		setStyle(creatureInfoPanel, "PROPERTIES");
		JTextField hitPointsTextField = createNewPropertyTextField("HP");
		JTextField speedTextField = createNewPropertyTextField("Speed");
		JTextField directionTextField = createNewPropertyTextField("Direction");
		JTextField teleporterTextField = createNewPropertyTextField(
				"Teleporter");

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

	private void createCostTextField() {
		costTextField = new JTextField(String.valueOf(currentCreatureCost));
		costTextField.setFont(textFont);
		costTextField.setHorizontalAlignment(JTextField.HORIZONTAL);
		setStyle(costTextField, "CREATURE COST");
	}

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

	private void createSizeSlider() {
		sizeSlider = new JSlider(JSlider.HORIZONTAL, 50, 150, 100);
		sizeSlider.setMinorTickSpacing(50);
		sizeSlider.setMajorTickSpacing(100);
		sizeSlider.setPaintTicks(true);
		sizeSlider.setPaintLabels(true);
		// sizeSlider.setSnapToTicks(true);

		java.util.Hashtable<Integer, JLabel> labelTable = new java.util.Hashtable<Integer, JLabel>();
		// labelTable.put(new Integer(200), new JLabel("2.0"));
		// labelTable.put(new Integer(175), new JLabel("1.75"));
		labelTable.put(new Integer(150), new JLabel("1.50"));
		// labelTable.put(new Integer(125), new JLabel("1.25"));
		labelTable.put(new Integer(100), new JLabel("1.0"));
		// labelTable.put(new Integer(75), new JLabel("0.75"));
		labelTable.put(new Integer(50), new JLabel("0.50"));
		// labelTable.put(new Integer(25), new JLabel("0.25"));
		// labelTable.put(new Integer(0), new JLabel("0.0"));
		sizeSlider.setLabelTable(labelTable);

		sizeSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				float value = sizeSlider.getValue();

				figure.setScale(value / 100);
				updateCreaturePreview(figure);

			}
		});

		setStyle(sizeSlider, "CHOOSE SIZE");
	}

	private void createCreaturePreview() {
		// creaturePreview = new TriangleRepresentation();

		creaturePreview = new JPanel();

		figure = new TriangleRepresentation();

		creaturePreview.add(figure);

		setStyle(creaturePreview, "CREATURE");

	}

	private void createCreditTextField() {
		creditTextField = new JTextField(String.valueOf(currentCredit));
		creditTextField.setHorizontalAlignment(JTextField.CENTER);

		creditTextField.setFont(textFont);
		creditTextField.setEditable(false);

		setStyle(creditTextField, "CURRENT CREDIT");
	}

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
					// ButtonModel m = creatureSelector.getSelection();
					String btnShape = aButton.getText();

					switch (Figures.valueOf(btnShape)) {
					case TRIANGLE:
						figure = new TriangleRepresentation(
								colorSlider.getValue(),
								(float) sizeSlider.getValue() / 100);
						break;

					case CIRCLE:
						figure = new CircleRepresentation(
								colorSlider.getValue(),
								(float) sizeSlider.getValue() / 100);
						break;

					case SQUARE:
						figure = new SquareRepresentation(
								colorSlider.getValue(),
								(float) sizeSlider.getValue() / 100);
						break;
					default:
						break;
					}
					updateCreaturePreview(figure);
					if (!creatureSelected) {
						updateCurrentCost(creatureCost);
						updateCostTextField();
						creatureSelected = true;

					}
					// updateCreatureCost();

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

	private void updateCurrentCost(int costChange) {
		currentCreatureCost += costChange;
		figure.setCost(currentCreatureCost);
	}

	private void updateCreatureCost() {

		Component toRemove = creatorModePanel.getComponent(costTextFieldIndex);
		GridBagLayout layout = (GridBagLayout) creatorModePanel.getLayout();
		GridBagConstraints gbc = layout.getConstraints(toRemove);
		creatorModePanel.remove(toRemove);
		creatorModePanel.add(costTextField, gbc, costTextFieldIndex);
		creatorModePanel.revalidate();
		creatorModePanel.repaint();
	}

	private JPanel createDirectionPanel() {
		JPanel directionPanel = new JPanel();

		JRadioButton leftRadioBtn = new JRadioButton("Left");
		leftRadioBtn.setActionCommand(Direction.LEFT.name());
		JRadioButton rightRadioBtn = new JRadioButton("Right");
		rightRadioBtn.setActionCommand(Direction.RIGHT.name());

		ItemListener itemListener = new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				AbstractButton aButton = (AbstractButton) e.getSource();

				ButtonModel aModel = aButton.getModel();
				if (aModel.isSelected()) {
					String direction = aButton.getActionCommand();

					if (direction.equals(Direction.LEFT.name())) {
						figure.setDirection(Direction.LEFT);
					} else {
						figure.setDirection(Direction.RIGHT);
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

	private void updateCreaturePreview(FigureRepresentation figure) {
		creaturePreview.removeAll();
		creaturePreview.add(figure);
		creaturePreview.validate();
		creaturePreview.repaint();
	}

	private void updateCreaturePreview() {
		creaturePreview.validate();
		creaturePreview.repaint();
	}

	private void setStyle(JComponent comp, String borderTitle) {
		comp.setBackground(backgroundColor);

		Border raisedetched = BorderFactory
				.createEtchedBorder(EtchedBorder.RAISED);

		TitledBorder titledBorder = BorderFactory
				.createTitledBorder(raisedetched, borderTitle);
		titledBorder.setTitleJustification(TitledBorder.CENTER);
		comp.setBorder(titledBorder);

	}

	private void calculateCreatureCost() {

	}

}

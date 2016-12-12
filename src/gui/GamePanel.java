package gui;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial")
public abstract class GamePanel extends JPanel {
	protected Color backgroundColor = UIManager.getColor("control");
	protected JPanel creaturePreview;
	protected FigureRepresentation figure;
	protected String panelTitle;

	public GamePanel() {
		initPanel();
	}

	public void setStyle() {
		this.setBackground(backgroundColor);

		Border raisedetched = BorderFactory
				.createEtchedBorder(EtchedBorder.RAISED);

		TitledBorder titledBorder = BorderFactory
				.createTitledBorder(raisedetched, panelTitle);
		titledBorder.setTitleJustification(TitledBorder.CENTER);
		this.setBorder(titledBorder);
	}
	
	public FigureRepresentation getFigure()	{
		return this.figure;
	}

	public abstract void initPanel();
}

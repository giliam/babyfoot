package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public abstract class BPanel extends JPanel {
	MainFrame window;
	public BPanel(MainFrame f){
		window = f;
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);
		add(new Header(),BorderLayout.NORTH);
	}
}

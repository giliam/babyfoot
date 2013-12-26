package gui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;

public abstract class BPanel extends JPanel {
	MainFrame window;
	public BPanel(MainFrame f){
		window = f;
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);
		add(new Header(),BorderLayout.NORTH);
	}
}

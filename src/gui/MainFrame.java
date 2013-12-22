package gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class MainFrame extends JFrame implements ActionListener {
	JPanel pan;
	
	public MainFrame(String title) {
		setTitle(title);
	    setSize(800, 800);
	    setLocationRelativeTo(null);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setContentPane(new MenuPanel(this));
	    setResizable(false);
	    setVisible(true);
	}
	
	public MainFrame(){
		this("Babyfoot en réseau");
	}
	
	
	
	static public void main(String[] args) {
	    new MainFrame();
	}

	public void actionPerformed(ActionEvent arg0) {
		
	}
}

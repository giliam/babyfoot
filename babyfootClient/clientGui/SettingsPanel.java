package clientGui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import clientCore.Utils;

public class SettingsPanel extends BPanel implements ActionListener {
	JButton bQuit = new JButton("Quitter le jeu");
	JButton bSave = new JButton("Sauvegarder et retour");
	JPanel centralMenu;
	JLabel label = new JLabel("Sensibilité (%) : " + Utils.getSensibility());
	JSlider slide;
	
	public SettingsPanel(MainFrame f) {
		super(f, false, 200);
		f.setSize(800,800);
	    
		Image image = null;
		try {
			image = ImageIO.read(new File("pictures/background.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	    JLabel viewer = new JLabel(new ImageIcon(image));
	    viewer.setSize(800,800);
	    add(viewer);
		
		//Gauche-droite
	    JPanel left = new JPanel();
	    left.setBackground(Color.WHITE);
	    left.setPreferredSize(new Dimension(250,200));
	    add(left,BorderLayout.WEST);
	    JPanel bottom = new JPanel();
	    bottom.setBackground(Color.WHITE);
	    bottom.setPreferredSize(new Dimension(800,300));
	    add(bottom,BorderLayout.SOUTH);
	    JPanel right = new JPanel();
	    right.setBackground(Color.WHITE);
	    right.setPreferredSize(new Dimension(250,200));
	    add(right,BorderLayout.EAST);
	    //Gestion du menu central
	    centralMenu = new JPanel();
	    centralMenu.setBackground(Color.WHITE);
	    centralMenu.setPreferredSize(new Dimension(400,100));
	    centralMenu.setMaximumSize(new Dimension(400,100));
	    add(centralMenu, BorderLayout.CENTER);
	    
	    slide = new JSlider();
	    slide.setBackground(Color.white);
	    slide.setMaximum(100);
	    slide.setMinimum(0);
	    slide.setValue(Utils.getSensibility());
	    slide.setPaintTicks(false);
	    slide.setPaintLabels(false);
	    slide.setMinorTickSpacing(10);
	    slide.setMajorTickSpacing(20);
	    slide.addChangeListener(new ChangeListener(){
	      public void stateChanged(ChangeEvent event){
	        label.setText("Sensibilité (%) : " + ((JSlider)event.getSource()).getValue());
	      }
	    });
	    label.setPreferredSize(new Dimension(200,100));
	    label.setMinimumSize(new Dimension(200,100));
	    centralMenu.add(slide, BorderLayout.CENTER);
	    centralMenu.add(label, BorderLayout.SOUTH); 
	    
	    //Gestion des boutons
	    bSave.setPreferredSize(new Dimension(200,50));
	    bQuit.setPreferredSize(new Dimension(200,50));
		
	    centralMenu.add(bSave,new Dimension(0,0));
	    centralMenu.add(bQuit,new Dimension(3,0));
		
	    bSave.addActionListener(this);
		bQuit.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		if( e.getSource() == bQuit ){
			getWindow().getMain().closeWindow();
		}else if( e.getSource() == bSave ){
			Utils.setSensibility(slide.getValue());
			getWindow().setContentPane(new MenuPanel(getWindow()));
	    	getWindow().setVisible(true);
		}
	}
}

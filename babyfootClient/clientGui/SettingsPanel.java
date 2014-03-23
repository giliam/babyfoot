package clientGui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
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
	private JButton bQuit = new JButton("Quitter le jeu");
	private JButton bSave = new JButton("Sauver");
	private JButton bSaveAndQuit = new JButton("<html>Sauver<br />Retour</html>");
	private DemonstratorPanel centralMenu;
	private JLabel label = new JLabel("Sensibilité (%) : " + Utils.getSensibility());
	private JSlider slide;
	
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
	    
	    slide = new JSlider();
	    
	    
	    //Gestion du menu central
	    centralMenu = new DemonstratorPanel(slide);
	    centralMenu.setBackground(Color.WHITE);
	    centralMenu.setPreferredSize(new Dimension(800,100));
	    centralMenu.setMaximumSize(new Dimension(800,100));
	    add(centralMenu, BorderLayout.CENTER);
	    
	    
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
	    bSave.setPreferredSize(new Dimension(100,50));
	    bSaveAndQuit.setPreferredSize(new Dimension(100,50));
	    bQuit.setPreferredSize(new Dimension(206,50));
	    
	    bSave.setFocusable(false);
	    bSaveAndQuit.setFocusable(false);
	    bQuit.setFocusable(false);
		slide.setFocusable(false);

		centralMenu.add(bSave,new Dimension(0,0));
	    centralMenu.add(bSaveAndQuit,new Dimension(0,1));
	    centralMenu.add(bQuit,new Dimension(3,0));
		
	    bSave.addActionListener(this);
	    bSaveAndQuit.addActionListener(this);
		bQuit.addActionListener(this);
		
		addKeyListener(centralMenu);
	}

	public void actionPerformed(ActionEvent e) {
		if( e.getSource() == bQuit ){
			getWindow().getMain().closeWindow();
		}else if( e.getSource() == bSave ){
			Utils.setSensibility(slide.getValue());
		}else if( e.getSource() == bSaveAndQuit ){
			Utils.setSensibility(slide.getValue());
			getWindow().setContentPane(new MenuPanel(getWindow()));
	    	getWindow().setVisible(true);
		}
	}
}


class DemonstratorPanel extends JPanel implements MouseMotionListener, KeyListener {
	private int lastkey = 0;
	private int demonstratorY = 0;
	private JSlider slide;
	
	public DemonstratorPanel(JSlider slide){
		this.slide = slide;
		addMouseMotionListener(this);
	}
	
	public void paintComponent(Graphics g){
		g.setColor(Color.black);
		g.drawOval(0, demonstratorY, 20, 20);
	}
	
	
	
	@Override
	public void mouseDragged(MouseEvent arg0) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		int y = e.getY();
		int mov = (int)Math.ceil( ( Math.abs( ( lastkey - y ) * slide.getValue() / ( 1 + slide.getValue() ) ) ) );
		if( lastkey < y )
			demonstratorY += mov;
		else
			demonstratorY -= mov;
		lastkey = y;
		updateUI();
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		if( arg0.getKeyCode() == 32 ){
			demonstratorY = lastkey;
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}
}

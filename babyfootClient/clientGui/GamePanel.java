package clientGui;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements ActionListener {
	
	MainFrame window;
	GameZone gameZone;
	/**
	 * GamePanel Constructor : 
	 * @param f
	 */
	public GamePanel(MainFrame f, boolean testMode) {
		window = f;
		//Nécessaire de mettre 729 car décalage inhérent à l'affichage
		Toolkit tk = Toolkit.getDefaultToolkit();  
		int height = 1200;
		f.setSize(1300,height);
		setLayout(new BorderLayout());
		JPanel chat = null;
		if( !testMode ){
			chat = new ChatPanel(window);
		}else{
			chat = new JPanel();
		}
		/*
		int widthPanels = ((int)tk.getScreenSize().getWidth()-900)/2;
		int widthCenter = 900;*/
		int widthPanel = 100;
		int widthChat = 300;
		int widthCenter = 900;
		
		chat.setBackground(Color.BLACK);
		chat.setPreferredSize(new Dimension(widthChat,height));
		chat.setMinimumSize(new Dimension(widthChat,height));
		add(chat,BorderLayout.EAST);
		
		gameZone = new GameZone(this, testMode, widthCenter);
		gameZone.getInfoPanel().setPreferredSize(new Dimension(widthPanel,height));
		gameZone.getInfoPanel().setMinimumSize(new Dimension(widthPanel,height));
		add(gameZone.getInfoPanel(),BorderLayout.WEST);
		
		PublicPanel topOfGameZone = new PublicPanel("top");
		topOfGameZone.setPreferredSize(new Dimension(widthCenter, (height-gameZone.getHeight())/2-50));
		topOfGameZone.setMinimumSize(new Dimension(widthCenter, (height-gameZone.getHeight())/2-50));
		PublicPanel botOfGameZone = new PublicPanel("bot");
		botOfGameZone.setPreferredSize(new Dimension(widthCenter, (height-gameZone.getHeight())/2));
		botOfGameZone.setMinimumSize(new Dimension(widthCenter, (height-gameZone.getHeight())/2));
		
		JPanel centerZone = new JPanel();
		centerZone.add(topOfGameZone);
		centerZone.add(gameZone);
		centerZone.add(botOfGameZone);
		
		add(centerZone,BorderLayout.CENTER);
		//add(gameZone,BorderLayout.CENTER);
	}
	
	public void actionPerformed(ActionEvent e) {
		
	}

	public MainFrame getWindow() {
		return window;
	}
}

class PublicPanel extends JPanel{
	private String side;
	
	public PublicPanel(String side){
		this.side = side;
	}
	
	public void paint(Graphics g){
		try {
	      Image img = ImageIO.read(new File("pictures/public" + side + ".png"));
	      g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
		} catch (IOException e) {
	      e.printStackTrace();
	    }
	}
}
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
		f.setSize((int)tk.getScreenSize().getWidth(),(int)tk.getScreenSize().getHeight());
		setLayout(new BorderLayout());
		JPanel chat = null;
		if( !testMode ){
			chat = new ChatPanel(window);
		}else{
			chat = new JPanel();
		}
		chat.setBackground(Color.BLACK);
		chat.setPreferredSize(new Dimension(((int)tk.getScreenSize().getWidth()-900)/2,(int)tk.getScreenSize().getHeight()));
		chat.setMinimumSize(new Dimension(((int)tk.getScreenSize().getWidth()-900)/2,(int)tk.getScreenSize().getHeight()));
		add(chat,BorderLayout.EAST);
		
		gameZone = new GameZone(this, testMode);
		gameZone.getInfoPanel().setPreferredSize(new Dimension(((int)tk.getScreenSize().getWidth()-900)/2,(int)tk.getScreenSize().getHeight()));
		gameZone.getInfoPanel().setMinimumSize(new Dimension(((int)tk.getScreenSize().getWidth()-900)/2,(int)tk.getScreenSize().getHeight()));
		add(gameZone.getInfoPanel(),BorderLayout.WEST);
		
		PublicPanel topOfGameZone = new PublicPanel("top");
		topOfGameZone.setPreferredSize(new Dimension(900, ((int)tk.getScreenSize().getHeight()-gameZone.getHeight())/2-50));
		topOfGameZone.setMinimumSize(new Dimension(900, ((int)tk.getScreenSize().getHeight()-gameZone.getHeight())/2-50));
		PublicPanel botOfGameZone = new PublicPanel("bot");
		botOfGameZone.setPreferredSize(new Dimension(900, ((int)tk.getScreenSize().getHeight()-gameZone.getHeight())/2));
		botOfGameZone.setMinimumSize(new Dimension(900, ((int)tk.getScreenSize().getHeight()-gameZone.getHeight())/2));
		
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
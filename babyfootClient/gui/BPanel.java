package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JPanel;

@SuppressWarnings("serial")
/** Cette classe abstraite permet simplement de garder quelques données communes à tous les panels qui seront utilisés dans l'IHM
 par la suite : le LayoutManager qui est un BorderLayout, la couleur du fond (blanche) et la présence du header (cf. @Header) dans la zone NORTH
 du BorderLayout. */
public abstract class BPanel extends JPanel {
	MainFrame window;
	public BPanel(MainFrame f){
		window = f;
		window.getMain().actualPanel = this;
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);
		add(new Header(),BorderLayout.NORTH);
	}
}

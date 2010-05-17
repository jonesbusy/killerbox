import java.awt.*;
import javax.swing.*;
import java.util.*;

import killerbox.KillerBoxServer;

/**
 * 
 */
@SuppressWarnings("serial")
public class ServerGui extends JFrame implements Observer
{
	// Le serveur
	private KillerBoxServer server;
	
	private JTextArea label = new JTextArea(20, 100);
	
	private JScrollPane scroll;
	
	/**
	 * Permet de creer un nouvelle interface pour le serveur
	 * @param server Le serveur
	 */
	public ServerGui(KillerBoxServer server)
	{
		this.server = server;
		this.server.addObserver(this);
		this.label.setSize(400, 200);
		this.label.setEditable(false);
		this.scroll = new JScrollPane(this.label);
		this.scroll.setAutoscrolls(true);
		this.getContentPane().add(this.scroll, BorderLayout.NORTH);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}


	/**
	 * Permet de mettre a jour le message
	 */
	public void update(Observable o, Object arg)
	{
		if(String.class.isInstance(arg))
			this.label.setText(this.label.getText() + (String)arg + "\n");
	}
	
	
	
}

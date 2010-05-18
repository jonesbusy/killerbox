import javax.swing.*;
import java.util.*;

import killerbox.KillerBoxServer;

/**
 * Permet de representer une vue graphique du serveur.
 * @author Valentin Delaye
 * @version 1.0
 * @see KillerBoxServer
 */
@SuppressWarnings("serial")
public class ServerGui extends JFrame implements Observer
{
	/**
	 * Le serveur de jeu
	 */
	private KillerBoxServer server;
	
	/**
	 * Zone ou afficher les messages
	 */
	private JTextArea label = new JTextArea(10, 50);
	
	/**
	 * Barre de defilement lorsqu'il y a trop de messages
	 */
	private JScrollPane scroll = new JScrollPane(this.label, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	
	/**
	 * Constructeur. Permet de creer un nouvelle interface pour le serveur-
	 * @param server Le serveur
	 */
	public ServerGui(KillerBoxServer server)
	{
		this.server = server;
		
		// Observation du serveur
		this.server.addObserver(this);
		
		// Configuration des composants
		this.label.setEditable(false);
		this.scroll.setAutoscrolls(true);
		this.setResizable(false);
		
		this.getContentPane().add(this.scroll);
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}


	/**
	 * Permet de mettre a jour les messages sur la vue.
	 */
	@Override
	public void update(Observable o, Object arg)
	{
		this.label.setText(this.label.getText() + arg + "\n");
	}
}

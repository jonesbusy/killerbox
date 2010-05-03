package killerbox.panel;

import javax.swing.*;

import killerbox.*;
import network.*;

/**
 * Permet de representer la base d'un panel de l'interface
 * KillerBox
 * @author Fabrizio Beretta Piccoli
 * @author Valentin Delaye
 */
@SuppressWarnings("serial")
public abstract class KillerBoxPanel extends JPanel
{	
	/**
	 * La fenetre de base du panel
	 */
	protected BaseWindow base;
	
	/**
	 * Le client associes
	 */
	protected final Client client;
	
	/**
	 * L'ecouteur de connexion
	 */
	protected final ClientListener clientListener;
	
	/**
	 * Constructeur
	 * @param fenetreBase
	 */
	public KillerBoxPanel(BaseWindow base, Client client, ClientListener clientListener)
	{
		this.base = base;
		this.client = client;
		this.clientListener = clientListener;
	}

	/**
	 * Permet de retourner le client
	 * @return Le client
	 */
	public Client getClient()
	{
		return client;
	}

	/**
	 * Permet de retourner l'ecouteur de connexion
	 * @return L'ecouteur de connexion
	 */
	public ClientListener getClientListener()
	{
		return clientListener;
	}
	
	/**
	 * Permet de retourner le bouton principal du panel
	 * @return Le bouton princiapl du label
	 */
	public abstract JButton getDefaultButton();
	
	/**
	 * Doit etre redefini pour savoir a quel endroit on affiche le message 
	 * d'erreur
	 * @param message Le message a afficher
	 */
	public abstract void printError(String message);
	
}

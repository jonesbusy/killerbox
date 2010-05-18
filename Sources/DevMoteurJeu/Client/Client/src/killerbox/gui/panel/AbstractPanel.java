package killerbox.gui.panel;

import javax.swing.*;
import killerbox.gui.*;
import killerbox.network.KillerBoxListener;

/**
 * Permet de representer la base d'un panel de l'interface
 * KillerBox
 * @author Fabrizio Beretta Piccoli
 * @author Valentin Delaye
 */
@SuppressWarnings("serial")
public abstract class AbstractPanel extends JPanel
{
	/**
	 * La fenetre de base du panel
	 */
	protected BaseWindow window;
	
	/**
	 * Le controleur
	 */
	protected KillerBoxListener controller;

	/**
	 * Indique s'il y a une erreur de login. Dans ce cas, on doit
	 * fermer le panel.
	 */
	public boolean errorConnection = false;


	/**
	 * Constructeur. Creer le panel .
	 * @param window La fenetre de base
	 */
	public AbstractPanel(BaseWindow window)
	{
		this.window = window;
		this.controller = this.window.getListener();
	}

	/**
	 * Permet d'afficher un message sur le panel. Par defaut le comportement est : S'il y a
	 * une erreur
	 * de connexion, afficher le panel de connection et afficher le message.
	 * Si c'est un autre message, ne fait rien. Mais peut etre redefini pour savoir a quel
	 * endroit on affiche le message sur le panel.
	 * @param message Le message a afficher
	 */
	public void printMessage(String message)
	{
		if (errorConnection)
		{
			window.setPanel(EnumPanel.PANEL_CONNECTION);
			window.printMessage(message);
		}
	}

	/**
	 * Permet de retourner le bouton principal. Null s'il n'y a
	 * aucun bouton principal sur le Panel
	 * @return Le bouton principal du panel
	 */
	public abstract JButton getDefaultButton();

	/**
	 * Permet d'afficher une partie uniquement visible
	 * par un admin. Est utilie pour les panels ayant une interface
	 * differente si c'est un utilisateur ou un administrateur.
	 */
	public void showAdmin()
	{

	}

	/**
	 * Est utilise pour les panel ayant besoin de recevoir des informations en continu.
	 * La fonction est appelee par le decodeur pour indiquer que de nouvelles informations
	 * sont disponibles et qu'il faut remettre a jour le paneaux. Est principalement
	 * utilise pour la tableau des scores et des parties.
	 */
	public void refreshData()
	{
		
	}

}

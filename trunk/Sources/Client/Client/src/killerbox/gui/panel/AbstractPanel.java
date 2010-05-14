package killerbox.gui.panel;

import javax.swing.*;
import killerbox.gui.*;

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
	protected BaseWindow base;

	/**
	 * Indique s'il y a une erreur de login. Dans ce cas, on doit
	 * fermer le panel.
	 */
	public boolean errorConnection = false;

	/**
	 * Constructeur. Creer le panel .
	 * @param base La fenetre de base
	 */
	public AbstractPanel(BaseWindow base)
	{
		this.base = base;
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
			base.setPanel(EnumPanel.PANEL_CONNECTION);
			base.printMessage(message);
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
	 * Est utilise pour les panel ayant besoin de recevoir des informations en continu
	 */
	public void getData()
	{

	}

}

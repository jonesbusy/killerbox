package killerbox.panel;

import javax.swing.*;

import killerbox.*;

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
	 * Indique s'il y a une erreur de login. Dans ce cas, on doit
	 * fermer le panel
	 */
	public boolean errorConnection = false;
	
	/**
	 * Constructeur
	 * @param fenetreBase
	 */
	public KillerBoxPanel(BaseWindow base)
	{
		this.base = base;
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
	public void printError(String message)
	{
		if(errorConnection)
		{
			base.setPanel(EnumPanel.CONNECTION_PANEL);
			base.printError(message);
		}
	}
	
	/**
	 * Permet d'afficher une partie uniquement visible 
	 * par un admin
	 */
	public void showAdmin()
	{
		
	}
	
}

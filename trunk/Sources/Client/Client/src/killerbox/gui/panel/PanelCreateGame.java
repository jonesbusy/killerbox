package killerbox.gui.panel;

import javax.swing.JButton;

import killerbox.gui.BaseWindow;
import killerbox.gui.panel.AbstractPanel;

/**
 * Permet de representer le panel pour creer une partie.
 * 
 * @author Fabrizio Beretta Piccoli
 * @author Valentin Delaye
 * @version 1.0
 * @see AbstractPanel
 */
@SuppressWarnings("serial")
public class PanelCreateGame extends AbstractPanel
{

	/**
	 * Constructeur. Permet de creer le nouveau Panel.
	 * @param base Reference sur la fenetre.
	 */
	public PanelCreateGame(BaseWindow base)
	{
		super(base);
		
	}

	/**
	 * Permet de retourner le bouton principal. Null s'il n'y a
	 * aucun bouton principal sur le Panel
	 */
	@Override
	public JButton getDefaultButton()
	{
		return null;
	}

}

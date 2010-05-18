package killerbox.gui.panel;

import javax.swing.*;

import killerbox.gui.*;

/**
 * Permet de representer le panel des equipes.
 * @author Fabrizio Beretta Piccoli
 * @author Valentin Delaye
 * @version 1.0
 * @see AbstractPanel
 */
@SuppressWarnings("serial")
public class PanelListPlayersGameTeam extends AbstractPanel
{

	/**
	 * Constructeur. Permet de creer le nouveau Panel.
	 * @param window Reference sur la fenetre.
	 */
	public PanelListPlayersGameTeam(final BaseWindow window)
	{
		super(window);
	}

	/**
	 * Permet de retourner le bouton par defaut.
	 */
	@Override
	public JButton getDefaultButton()
	{
		return null;
	}

}

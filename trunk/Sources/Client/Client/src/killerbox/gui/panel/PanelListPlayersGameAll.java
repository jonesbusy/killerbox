package killerbox.gui.panel;
import java.awt.event.*;
import javax.swing.*;

import killerbox.gui.*;
import static killerbox.gui.panel.EnumPanel.*;

/**
 * Permet de representer le panel ou les liste des joueurs
 * inscris pour la partie
 * @author Valentin Delaye
 * @version 1.0
 * @see AbstractPanel
 */
@SuppressWarnings("serial")
public class PanelListPlayersGameAll extends AbstractPanel
{
	
	/**
	 * Pour permettre d'afficher un message sur la Panel
	 */
	private JLabel labMessage = new JLabel();

	/**
	 * Permettre au createur de supprimer la partie
	 */
	private JButton btnEndGame = new JButton("Supprimer partie");
	
	/**
	 * Constructeur. Permet de creer le nouveau Panel
	 * @param base Reference sur la fenetre
	 */
	public PanelListPlayersGameAll(final BaseWindow base)
	{
		super(base);
		
		// Ecouteurs des composants
		this.btnEndGame.addActionListener(new ActionListener()
		{
			/**
			 * Quand l'utilisateur clique pour supprimer la partie
			 */
			@Override
			public void actionPerformed(ActionEvent e)
			{
				base.getListener().requestDeleteGame();
				base.setPanel(PANEL_SET_ACCOUNT);
			}
		});
		
		// Ajouter les composant
		this.add(this.btnEndGame);
	}

	/**
	 * Permet de retourner le bouton principal
	 */
	@Override
	public JButton getDefaultButton()
	{
		return null;
	}

	/**
	 * Permet d'afficher un message d'erreur sur le panel
	 */
	@Override
	public void printMessage(String message)
	{
		super.printMessage(message);
	}
	
	

}

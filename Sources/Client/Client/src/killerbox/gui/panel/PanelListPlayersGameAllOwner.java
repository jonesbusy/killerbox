package killerbox.gui.panel;

import static killerbox.gui.panel.EnumPanel.PANEL_SET_ACCOUNT;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import killerbox.gui.BaseWindow;

/**
 * Represente le panel pour afficher la liste des joueurs inscrit pour une partie
 * Tous vs tous. Ce panel permet au createur de la partie de demarrer ou supprimer la partie.
 * @author Valentin Delaye
 */
@SuppressWarnings("serial")
public class PanelListPlayersGameAllOwner extends PanelListPlayersGameAll
{
	
	/**
	 * Permettre au createur de supprimer la partie
	 */
	private JButton btnEndGame = new JButton("Supprimer partie");

	/**
	 * Permettre au createur de supprimer la partie
	 */
	private JButton btnStartGame = new JButton("Demarrer partie");

	/**
	 * Constructeur. Permet de creer le panel
	 * @param window
	 * @param gameID
	 */
	public PanelListPlayersGameAllOwner(final BaseWindow window, int gameID)
	{
		super(window, gameID);
		
		// Ecouteurs des composants
		this.btnEndGame.addActionListener(new ActionListener()
		{
			/**
			 * Quand l'utilisateur clique pour supprimer la partie
			 */
			@Override
			public void actionPerformed(ActionEvent e)
			{
				controller.requestDeleteGame();
				window.setPanel(PANEL_SET_ACCOUNT);
			}
		});
		
		this.btnStartGame.addActionListener(new ActionListener() {
			
			/**
			 * Quand l'utilisateur clique pour lancer la partie
			 */
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.requestStartGame();
			}
		});
		
		// Ne pas afficher le bouton retour comme c'est un createur de la partie
		this.btnForward.setVisible(false);
		
		// Ajouter les composant
		this.add(this.btnEndGame);
		this.add(this.btnStartGame);
		
	}

}

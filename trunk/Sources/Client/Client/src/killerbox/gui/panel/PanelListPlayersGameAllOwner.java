package killerbox.gui.panel;

import static killerbox.gui.panel.EnumPanel.PANEL_SET_ACCOUNT;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import com.sun.xml.internal.ws.resources.ModelerMessages;

import killerbox.game.ControllerGame;
import killerbox.game.ModelGame;
import killerbox.gui.BaseWindow;

/**
 * Represente le panel pour afficher la liste des joueurs inscrit pour une partie
 * Tous vs tous. Ce panel permet au createur de la partie de demarrer ou supprimer la
 * partie.
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
	 * Modele de la partie
	 */
	private ModelGame modelGame = new ModelGame();
	
	/**
	 * Controller du jeu
	 */
	private ControllerGame controllerGame = new ControllerGame(modelGame);

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
				// Confirmer la suppression de partie
				if (JOptionPane.showConfirmDialog(window,
						"Est-vous sur de vouloir supprimer la partie ?", window.getTitle(),
						JOptionPane.YES_NO_CANCEL_OPTION) == JOptionPane.OK_OPTION)
				{
					controller.requestDeleteGame();
					window.setPanel(PANEL_SET_ACCOUNT);
				}
			}
		});

		this.btnStartGame.addActionListener(new ActionListener()
		{

			/**
			 * Quand l'utilisateur clique pour lancer la partie
			 */
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				// Confirmer le lancement de la partie
				if (JOptionPane.showConfirmDialog(window, "Est-vous sur de vouloir demarrer la partie ?", window.getTitle(),
						JOptionPane.YES_NO_CANCEL_OPTION) == JOptionPane.OK_OPTION)
				{
					// Calculer la position de tous les joueurs
					controllerGame.positionnerJoueurs();
					
					// Envoyer les données du modèle aux clients de la partie
					controller.sendModel(modelGame);
					
					// Démarrer la partie
					controller.requestStartGame();
				}	
			}
		});

		// Ne pas afficher le bouton retour comme c'est un createur de la partie
		this.btnForward.setVisible(false);

		// Ajouter les composant
		this.add(this.btnEndGame);
		this.add(this.btnStartGame);

	}

}

package killerbox.gui.panel;

import static killerbox.gui.panel.EnumPanel.PANEL_SET_ACCOUNT;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import killerbox.game.*;
import killerbox.gui.*;

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
					// Créer le modèle et le controller
					ModelGame modelGame = new ModelGame();
					modelGame.setEtat(EtatModel.Chargement);
					ControllerGame controllerGame = new ControllerGame(modelGame);
					
					// choisir la carte
					modelGame.setCarte(new CarteBase());
					
					// créer et positionner les joueurs
					for (String player : playersInfo.getPlayers()) {
						controllerGame.addPlayerWithRandomPosition(player,100);
					}
					
					// ajouter le modèle et le controller à la fenêtre
					window.setModelGame(modelGame);
					window.setControllerGame(controllerGame);
					
					// Envoi d'un paquet à tous les joueurs pour changer de panel
					controller.requestPanelGame();
					
					// Envoi d'un ordre de création du controller et du modèle
					controller.requestCreateModelAndController();
					
					// Envoi des données du modèle
					controllerGame.setNetworkController(controller);
					controllerGame.sendModele();
					
					// Envoie l'ordre de commencer la partie
					controllerGame.startGame();
				}
			}
		});

		// Ne pas afficher le bouton retour comme c'est un createur de la partie
		this.btnForward.setVisible(false);
		this.btnStartGame.setEnabled(false);

		// Ajouter les composant
		this.add(this.btnEndGame);
		this.add(this.btnStartGame);

	}

	/**
	 * Est appele quand le panel est mis a jour
	 */
	@Override
	public void refreshData()
	{
		super.refreshData();
		
		// On peut demarer la partie uniquement si plus de 1 joueur
		if(this.playersInfo.getRowCount() > 1)
			this.btnStartGame.setEnabled(true);
		else
			this.btnStartGame.setEnabled(false);
	}
	
	

}

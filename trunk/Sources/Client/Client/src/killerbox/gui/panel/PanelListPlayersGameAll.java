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
	 * Classe Thread permettant de mettre a jour tout les tant de temps
	 * les joueur inscris a la partie
	 * @author Valentin Delaye
	 * @version 1.0
	 */
	private class PlayersLoader extends Thread
	{
		
		/**
		 * Les donnes sont pretes. On peut les recuperer et 
		 * mettre a jour le tableau des parties
		 */
		private synchronized void getData()
		{
			this.notify();
			
			repaint();
			validate();
			
		}

		/**
		 * Permet d'executer le thread. Permet de mettre a jour les donnes concernant
		 * les parties.
		 */
		@Override
		public void run()
		{
			while(true)
			{
				controller.requestPlayers(gameId);

				try
				{
					synchronized (this)
					{
						this.wait();
					}

					Thread.sleep(UPDATE_TIME);
				}
				catch (InterruptedException e)
				{

				}
			}
		}
		
	}
	
	/**
	 * ID de la partie
	 */
	private int gameId;
	
	/**
	 * Rafraichir les utilisateur inscris tout les tant de temps
	 */
	public static final int UPDATE_TIME = 1000;
	
	/**
	 * Pour permettre d'afficher un message sur la Panel.
	 */
	private JLabel labMessage = new JLabel();

	/**
	 * Permettre au createur de supprimer la partie
	 */
	private JButton btnEndGame = new JButton("Supprimer partie");

	/**
	 * Permettre au createur de supprimer la partie
	 */
	private JButton btnStartGame = new JButton("Démarrer partie");
	
	/**
	 * Thread pour permettre de recharger la liste
	 * des joueurs
	 */
	private PlayersLoader playersLoader = new PlayersLoader();
	
	/**
	 * Constructeur. Permet de creer le nouveau Panel
	 * @param window Reference sur la fenetre
	 * @param gameID Numerode la partie
	 */
	public PanelListPlayersGameAll(final BaseWindow window, int gameID)
	{
		super(window);
		this.gameId = gameID;
		
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
		
		// Ajouter les composant
		this.add(this.btnEndGame);
		this.add(this.btnStartGame);
		
		// Demarrer le thread
		this.playersLoader.start();
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
	 * Permet d'afficher un message sur le panel.
	 */
	@Override
	public void printMessage(String message)
	{
		super.printMessage(message);
		this.labMessage.setText(message);
	}
	
	

}

package killerbox.gui.panel;

import java.awt.Dimension;
import java.awt.event.*;

import javax.swing.*;

import killerbox.*;
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

			// Recuperer les donnes sur les scores
			Data data = window.getData();
			playersInfo = data.getPlayersInfo();
			
			// Seter le model
			playersTable.setModel(playersInfo);

		}

		/**
		 * Permet d'executer le thread. Permet de mettre a jour les donnes concernant
		 * les parties.
		 */
		@Override
		public void run()
		{
			while (true)
			{
				// Demander la liste des joueurs pour cette partie
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
	public static final int UPDATE_TIME = 4000;

	/**
	 * Pour permettre d'afficher un message sur la Panel.
	 */
	protected JLabel labMessage = new JLabel();
	
	/**
	 * Label de titre
	 */
	private JLabel labTitle = new JLabel("Liste des joueurs inscris");

	/**
	 * Bouton retour
	 */
	protected JButton btnForward = new JButton("Retour");

	/**
	 * Pour permettre de coupe le panel en deux zone. La zone du haut contient
	 * le tableau des scores, et la zone du bas contient les informations
	 * additionnelles.
	 */
	protected JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
	
	/**
	 * Contient les informations des joueurs.
	 */
	protected PlayersInfo playersInfo;
	
	/**
	 * Tableau pour afficher les differentes informations.
	 */
	protected JTable playersTable = new JTable();
	
	/**
	 * Barre de scroll quand le tableau des scores a trop de resultats.
	 */
	protected JScrollPane scrollPane = new JScrollPane(playersTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	
	
	/**
	 * Thread pour permettre de recharger la liste
	 * des joueurs
	 */
	protected PlayersLoader playersLoader = new PlayersLoader();

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
		this.btnForward.addActionListener(new ActionListener()
		{
			
			/**
			 * Quand l'utilisateur clique sur le bouton retour
			 */
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				// Confirmer le retour
				if (JOptionPane.showConfirmDialog(window, "Est-vous sur de vouloir changer de partie ?", window.getTitle(),
						JOptionPane.YES_NO_CANCEL_OPTION) == JOptionPane.OK_OPTION)
				{
					controller.requestQuitGame(gameId);
					window.setPanel(PANEL_JOIN_GAME);
				}
			}
		});
		
		// Taille zone pour le tableau
		this.playersTable.setPreferredScrollableViewportSize(new Dimension(350, 210));
		this.playersTable.setFillsViewportHeight(true);
		this.splitPane.setDividerSize(0);
		
		// Ajouter les composants
		this.add(this.labTitle);
		this.addTable();
		this.add(this.labMessage);
		
		this.add(this.btnForward);
		
		// Demarrer le thread
		this.playersLoader.start();
	}
	
	/**
	 * Permet d'afficher le tableau de la liste des joueurs sur le panel
	 */
	public void addTable()
	{
		this.splitPane.add(this.scrollPane);
		this.add(this.splitPane);
	}

	/**
	 * Permet de retourner le bouton principal
	 */
	@Override
	public JButton getDefaultButton()
	{
		return this.btnForward;
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

	/**
	 * Permet de rafraichir les donnees du panel
	 */
	@Override
	public void refreshData()
	{
		this.playersLoader.getData();
		
		// Mettre a jour le scroll pane
		playersTable.repaint();
		playersTable.validate();
		scrollPane.getViewport().setView(playersTable);
		repaint();
		validate();
		
	}

}

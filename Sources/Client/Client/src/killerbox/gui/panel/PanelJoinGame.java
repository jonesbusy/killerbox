package killerbox.gui.panel;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import killerbox.*;
import killerbox.gui.*;
import static killerbox.gui.panel.EnumPanel.*;

/**
 * Permet de representer le panel de selection des parties.
 * @author Fabrizio Beretta Piccoli
 * @author Valentin Delaye
 * @version 1.0
 * @see AbstractPanel
 */
@SuppressWarnings("serial")
public class PanelJoinGame extends AbstractPanel
{
	
	/**
	 * Classe Thread permettant de mettre a jour tout les tant de temps
	 * les donnees concernants les parties.
	 * @author Valentin Delaye
	 * @version 1.0
	 */
	private class GamesLoader extends Thread
	{
		
		/**
		 * Les donnes sont pretes. On peut les recuperer et 
		 * mettre a jour le tableau des parties
		 */
		private synchronized void getData()
		{
			this.notify();
			
			// Recuperer les donnes sur les parties
			Data data = window.getData();
			gamesInfo = data.getGamesInfo();
			
			// Seter le model
			gamesTable.setModel(gamesInfo);
			
			// Mettre a jour le scroll pane
			gamesTable.repaint();
			gamesTable.validate();
			scrollPane.getViewport().setView(gamesTable);
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
				controller.requestGames();

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
	 * Mettre a jour le panel toutes les 5 secondes
	 */
	public static final int UPDATE_TIME = 5000;
	
	/**
	 * Label de titre
	 */
	private JLabel labTitle = new JLabel("Veuillez choisir votre partie", JLabel.CENTER);
	
	/**
	 * Pour permettre d'afficher un message sur la Panel
	 */
	private JLabel labMessage = new JLabel("", JLabel.CENTER);
	
	/**
	 * Bouton pour revenir au compte
	 */
	private JButton btnForward = new JButton("Retour");
	
	/**
	 * Bouton pour actualiser les parties
	 */
	private JButton btnRefresh = new JButton("Actualiser");
	
	/**
	 * Bouton pour rejoindre la partie.
	 */
	private JButton btnJoin = new JButton("Rejoindre");
	
	/**
	 * Pour permettre de coupe le panel en deux zone. La zone du haut contient
	 * les differentes parties, et la zone du bas contient les informations
	 * additionnelles.
	 */
	private JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
	
	/**
	 * Contient les informations des parties.
	 */
	private GamesInfo gamesInfo;
	
	/**
	 * ID de la partie selectionne
	 */
	private int gameSelected;
	
	/**
	 * Tableau pour afficher les differentes informations.
	 */
	private JTable gamesTable = new JTable();
	
	/**
	 * Barre de scroll quand le tableau des scores a trop de resultats.
	 */
	private JScrollPane scrollPane = new JScrollPane(gamesTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
	/**
	 * Permet de charger les donnees
	 */
	private GamesLoader loader = new GamesLoader();

	/**
	 * Constructeur. Permet de creer le panel
	 * @param window Reference sur la vue
	 */
	public PanelJoinGame(final BaseWindow window)
	{
		super(window);

		// Taille zone pour le tableau
		this.gamesTable.setPreferredScrollableViewportSize(new Dimension(350, 210));
		this.gamesTable.setFillsViewportHeight(true);
		this.splitPane.setDividerSize(0);
		
		// Permettre de trier les colonnes
		this.gamesTable.setAutoCreateRowSorter(true);
		
		// Taille des composants
		this.labTitle.setPreferredSize(new Dimension(350, 20));
		this.labMessage.setPreferredSize(new Dimension(350, 20));
		this.btnJoin.setEnabled(false);
		
		// Ecouteur des composants
		this.btnForward.addActionListener(new ActionListener()
		{
			
			/**
			 * Quand l'utilisateur clique sur le bouton "Retour"
			 */
			@Override
			public void actionPerformed(ActionEvent e)
			{
				window.setPanel(PANEL_SET_ACCOUNT);
			}
		});
		
		this.btnRefresh.addActionListener(new ActionListener()
		{
		
			/**
			 * Quand l'utilisateur clique sur le bouton "Actualiser"
			 */
			@Override
			public void actionPerformed(ActionEvent e)
			{
				controller.requestGames();
				labMessage.setText("");
			}
		});
		
		this.btnJoin.addActionListener(new ActionListener()
		{
			/**
			 * Quand l'utilisateur clique sur le bouton "Rejoindre"
			 */
			@Override
			public void actionPerformed(ActionEvent e)
			{
				window.setID(gameSelected);
				System.out.println("fooo : " + gameSelected);
				controller.requestJoinGame(gameSelected);
			}
		});
		
		this.gamesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener()
		{
			/**
			 * Quand la selection d'une ligne du tableau change. On sauve l'ID de la
			 * partie selectionnee dans gameSelected
			 */
			@Override
			public void valueChanged(ListSelectionEvent e)
			{
				btnJoin.setEnabled(true);
				ListSelectionModel selection = (ListSelectionModel) e.getSource();
				int rowSelected = selection.getMinSelectionIndex();
				gameSelected = (Integer)gamesInfo.getValueAt(rowSelected, 0);
			}
		});
		
		// Ajout des composant
		this.add(this.labTitle);
		
		// Ajout de la table
		this.splitPane.add(this.scrollPane);
		this.add(this.splitPane);
		
		this.add(this.btnForward);
		this.add(this.btnRefresh);
		this.add(this.btnJoin);
		this.add(this.labMessage);
		
		// Demarrer les recuperation des donnnes
		this.loader.start();
	}

	/**
	 * Permet de retourner le bouton principal du Panel
	 */
	@Override
	public JButton getDefaultButton()
	{
		return this.btnJoin;
	}

	/**
	 * Permet de mettre a jour le model.
	 */
	@Override
	public void refreshData()
	{
		this.loader.getData();
	}

	/**
	 * Affiche un message sur le panel. Est utilise pour indiquer si une
	 * partie est pleine.
	 */
	@Override
	public void printMessage(String message)
	{
		super.printMessage(message);
		this.labMessage.setText(message);
	}
	

}

package killerbox.gui.panel;

import java.awt.*;
import javax.swing.*;
import killerbox.*;
import killerbox.gui.BaseWindow;

/**
 * Permet de representer un Panel abstrait pour afficher diverses
 * informations sur les comptes des joueur (user, score, etc)
 * Cette classe sert de base pour PanelAdminScores et PanelScores
 * 
 * @author Fabrizio Beretta Piccoli
 * @author Valentin Delaye
 * @version 1.0
 * @see AbstractPanel
 */
@SuppressWarnings("serial")
public abstract class AbstractTableScoresPanel extends AbstractPanel
{
	
	/**
	 * Classe Thread permettant de mettre a jour tout les tant de temps
	 * les donnes concernants les scores.
	 * @author Valentin Delaye
	 *
	 */
	protected class ScoresLoader extends Thread
	{
		
		/**
		 * Les donnes sont pretes. On peut les recuperer et 
		 * mettre a jour le tableau des parties
		 */
		public synchronized void getData()
		{

			this.notify();	

			// Recuperer les donnes sur les scores
			Data data = window.getData();
			scoresInfo = data.getScoresInfo();
			
			// Seter le model
			scoresTable.setModel(scoresInfo);
			
			// Mettre a jour le scroll pane
			scoresTable.repaint();
			scoresTable.validate();
			scrollPane.getViewport().setView(scoresTable);
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
				
				try
				{
					// Demander les scores et attendre la reponse
					synchronized (this)
					{
						controller.requestScore();
						this.wait();
						Thread.sleep(UPDATE_TIME);
					}
					
				}
				catch (InterruptedException e)
				{

				}
			}
		}
		
	}
	
	/**
	 * Pour les panels ayant besoin d'avoir une mise a jour
	 * repetee de donnees. (Comme par exemple les tableaux des scores ou les tableau
	 * de parties)
	 */
	public static final int UPDATE_TIME = 3000;
	
	/**
	 * Pour permettre de coupe le panel en deux zone. La zone du haut contient
	 * le tableau des scores, et la zone du bas contient les informations
	 * additionnelles.
	 */
	protected JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
	
	/**
	 * Contient les informations des joueurs.
	 */
	protected ScoresInfo scoresInfo;
	
	/**
	 * Tableau pour afficher les differentes informations.
	 */
	protected JTable scoresTable = new JTable();
	
	/**
	 * Barre de scroll quand le tableau des scores a trop de resultats.
	 */
	protected JScrollPane scrollPane = new JScrollPane(scoresTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

	/**
	 * Pour permettre d'effectuer une mise a jour des scores
	 */
	protected ScoresLoader loader = new ScoresLoader();
	
	/**
	 * Constructeur. Permet de creer le nouveau Panel.
	 * @param window Reference sur la vue
	 */
	public AbstractTableScoresPanel(BaseWindow window)
	{
		super(window);
				
		// Creer le chargeur de scores
		
		// Taille zone pour le tableau
		this.scoresTable.setPreferredScrollableViewportSize(new Dimension(350, 210));
		this.scoresTable.setFillsViewportHeight(true);
		this.splitPane.setDividerSize(0);
		
	}
	
	/**
	 * Permet d'afficher le tableau des scores sur le panel
	 */
	public void addTable()
	{
		this.splitPane.add(this.scrollPane);
		this.add(this.splitPane);
	}

	/**
	 * Permet d'effectuer une mise a jours des donnees. Indique au chargeur qu'il faut
	 * recuperer les nouvelles donnees.
	 */
	@Override
	public void refreshData()
	{
		this.loader.getData();
	}
	
}

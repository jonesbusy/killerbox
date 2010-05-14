package killerbox.gui.panel;

import java.awt.*;
import javax.swing.*;
import java.util.*;
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
	 * Constructeur. Permet de creer le nouveau Panel.
	 * @param base Reference sur la vue
	 */
	public AbstractTableScoresPanel(BaseWindow base)
	{
		super(base);
				
		// Faire une requete des scores pour avoir les resultats a jours
		this.base.getListener().requestScore();
		
		// Taille zone pour le tableau
		this.scoresTable.setPreferredScrollableViewportSize(new Dimension(350, 210));
		this.scoresTable.setFillsViewportHeight(true);
		this.splitPane.setDividerSize(0);
		
		// Recuperer les donnes sur les scores
		this.scoresInfo = base.getScoresInfo();
		
		// Seter le model
		this.scoresTable.setModel(this.scoresInfo);
		
	}
	
	/**
	 * Permet de charger les donnees sur le tableau des scores.
	 * @param user Liste des utilisateurs
	 * @param score Liste des scores
	 * @param admin Indication du status d'administrateur.
	 */
	public void loadData(ArrayList<String> users, ArrayList<Integer> scores, ArrayList<Boolean> admin)
	{

		this.base.getScoresInfo().loadData(users, scores, admin);
		
		// Mettre a jour le scroll pane
		scoresTable.repaint();
		scoresTable.validate();
		this.scrollPane.getViewport().setView(scoresTable);
		this.repaint();
		this.validate();
	}
	
	/**
	 * Permet d'afficher le tableau des scores sur le panel
	 */
	public void addTable()
	{
		this.splitPane.add(this.scrollPane);
		this.add(this.splitPane);
	}
	

}

package killerbox.gui.panel;

import java.awt.*;

import javax.swing.*;
import java.util.ArrayList;

import killerbox.gui.BaseWindow;

/**
 * Permet de representer un Panel abstrait pour afficher diverses
 * informations sur les comptes des joueur (user, score, etc)
 * Cette classe sert de base pour PanelAdminScores et PanelScore
 * @author Valentin Delaye
 */
@SuppressWarnings("serial")
public abstract class AbstractTablePanel extends AbstractPanel
{
	
	/**
	 * Split pane
	 */
	protected JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
	
	/**
	 * Les differents scores
	 */
	protected TableScore scores;
	
	/**
	 * Table des scores
	 */
	protected JTable tableScore = new JTable();
	
	/**
	 * Barre de defilement pour le tableau
	 */
	protected JScrollPane scrollPane = new JScrollPane(tableScore, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);


	/**
	 * Constructeur
	 * @param base Reference sur la vue
	 */
	public AbstractTablePanel(BaseWindow base)
	{
		super(base);
		
		this.scores = base.getTableScores();
		
		// Faire une requete des scores pour avoir les tout dernier scores
		this.base.getListener().requestScore();
		
		// Taille zone pour le tableau
		this.tableScore.setPreferredScrollableViewportSize(new Dimension(350, 210));
		this.tableScore.setFillsViewportHeight(true);
		this.splitPane.setDividerSize(0);
		
		// Seter le model
		this.tableScore.setModel(this.base.getTableScores());
	}
	
	/**
	 * Permet de charger les donnes sur le tableau des scores
	 * @param user Liste des utilisateur
	 * @param score Liste des scores
	 * @param admin Indication d'un administrateur
	 */
	public void loadData(ArrayList<String> users, ArrayList<Integer> scores, ArrayList<Boolean> admin)
	{

		this.base.getTableScores().loadData(users, scores, admin);
		
		// Mettre a jour le scroll pane
		tableScore.repaint();
		tableScore.validate();
		this.scrollPane.getViewport().setView(tableScore);
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

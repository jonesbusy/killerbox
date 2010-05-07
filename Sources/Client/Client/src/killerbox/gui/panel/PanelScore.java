package killerbox.gui.panel;

import java.util.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import killerbox.gui.*;
import static killerbox.gui.panel.EnumPanel.*;

/**
 * Represente le panel pour afficher les scores des jouers
 * @author Valentin Delaye
 * @author Fabrizio Beretta Piccoli
 */
@SuppressWarnings("serial")
public class PanelScore extends AbstractPanel
{
	
	/**
	 * Afficher les 10 meilleures score
	 */
	JLabel labTitle = new JLabel("Voici le tableau des scores", JLabel.CENTER);
		
	/**
	 * Split pane
	 */
	JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
	
	/**
	 * Bouton retour
	 */
	JButton btnForward = new JButton("Retour");
	
	/**
	 * Donnees des joueurs
	 */
	TableScore modelScore = new TableScore();
	
	/**
	 * Table des scores
	 */
	JTable tableScore = new JTable();
	
	/**
	 * Barre de defilement pour le tableau
	 */
	JScrollPane scrollPane = new JScrollPane(tableScore);

	/**
	 * Constructeur
	 * @param base Fenetre de base
	 */
	public PanelScore(final BaseWindow base)
	{
		super(base);
		
		this.base.getListener().requestScore();
		
		// Taille des composants
		this.labTitle.setPreferredSize(new Dimension(350, 20));
		this.tableScore.setPreferredScrollableViewportSize(new Dimension(350, 210));
		this.tableScore.setFillsViewportHeight(true);
		this.splitPane.setDividerSize(0);
		
		// Ajout des composant
		this.tableScore.setModel(this.modelScore);
		this.add(this.labTitle);
		this.splitPane.add(this.scrollPane);
		this.add(this.splitPane);
		this.add(this.btnForward);
		
		// Ecouteur des boutons
		this.btnForward.addActionListener(new ActionListener()
		{
		
			/**
			 * Lorsque l'utilisteur clique sur le bouton retour. Charger le panel
			 * pour gerer son compte et participer aux jeux
			 */
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				base.setPanel(PANEL_SET_ACCOUNT);
			}
		});
		
	}
	
	/**
	 * Permet de charger les donnes sur le tableau des scores
	 * @param user
	 * @param score
	 * @param admin
	 */
	public void loadData(ArrayList<String> user, ArrayList<Integer> score, ArrayList<Boolean> admin)
	{
		for(int i = 0 ; i < user.size() ; i++)
			this.modelScore.setScore(user.get(i), score.get(i), admin.get(i));
		
		this.repaint();
	}

	/**
	 * Retourne le bouton principal du panel
	 */
	@Override
	public JButton getDefaultButton()
	{
		return this.btnForward;
	}
	
}
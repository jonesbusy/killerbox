package killerbox.gui.panel;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import killerbox.gui.*;
import static killerbox.gui.panel.EnumPanel.*;

/**
 * Represente le panel pour afficher les scores des joueurs.
 * 
 * @author Valentin Delaye
 * @author Fabrizio Beretta Piccoli
 * @version 1.0
 * @see AbstractTablePanel
 */
@SuppressWarnings("serial")
public class PanelScores extends AbstractTablePanel
{
	
	/**
	 * Titre du panel.
	 */
	private JLabel labTitle = new JLabel("Voici le tableau des scores", JLabel.CENTER);
		
	
	/**
	 * Bouton retour
	 */
	private JButton btnForward = new JButton("Retour");
	
	/**
	 * Le bouton actualiser
	 */
	private JButton btnRefresh = new JButton("Actualiser");
		
	/**
	 * Constructeur. Permet de creer le Panel.
	 * @param base Fenetre de base
	 */
	public PanelScores(final BaseWindow base)
	{
		super(base);
		
		// Permettre de trier les colonnes
		this.scoresTable.setAutoCreateRowSorter(true);
		
		// Taille des composants
		this.labTitle.setPreferredSize(new Dimension(350, 20));
		
		// Ajout des composant
		this.add(this.labTitle);
		this.addTable();
		this.add(this.btnForward);
		this.add(this.btnRefresh);
		
		// Ecouteur des boutons
		this.btnForward.addActionListener(new ActionListener()
		{
		
			/**
			 * Lorsque l'utilisteur clique sur le bouton retour. Charger le panel
			 * pour gerer son compte.
			 */
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				base.setPanel(PANEL_SET_ACCOUNT);
			}
		});
		
		this.btnRefresh.addActionListener(new ActionListener()
		{
			/**
			 * Lorsque l'utilisateur clique sur le bouton actualiser.
			 */
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				// Charger les donnes presente
				base.getListener().requestScore();
				loadData(scoresInfo.getUsers(), scoresInfo.getScores(), scoresInfo.getAdmin());
			}
		});
		
		// Charger les donnes presente
		loadData(scoresInfo.getUsers(), scoresInfo.getScores(), scoresInfo.getAdmin());
		
	}

	/**
	 * Permet de retourner le bouton principal. Null s'il n'y a
	 * aucun bouton principal sur le Panel
	 * @return Le bouton principal du panel
	 */
	@Override
	public JButton getDefaultButton()
	{
		return this.btnForward;
	}
	
}
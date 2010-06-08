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
public class PanelScores extends AbstractTableScoresPanel
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
	 * @param window Fenetre de base
	 */
	public PanelScores(final BaseWindow window)
	{

		super(window);
		
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
			public void actionPerformed(ActionEvent e)
			{
				window.setPanel(PANEL_SET_ACCOUNT);
			}
		});
		
		this.btnRefresh.addActionListener(new ActionListener()
		{
			/**
			 * Lorsque l'utilisateur clique sur le bouton actualiser.
			 */
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// Desactiver temporairement le bouton
				btnRefresh.setEnabled(false);
				
				// Charger les donnes presente
				controller.requestScore();
			}
		});
		
		
		// Demarrer le thread de recuperation des donnees
		this.loader.start();
		
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

	/**
	 * Est appele quand les donnees sont mise a jour
	 */
	@Override
	public void refreshData()
	{
		super.refreshData();
		
		// Reactiver le bouton actualiser
		this.btnRefresh.setEnabled(true);
	}
	
}
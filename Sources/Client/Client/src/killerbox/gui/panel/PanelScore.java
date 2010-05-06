package killerbox.gui.panel;

import java.awt.Dimension;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.JTableHeader;

import killerbox.gui.*;

/**
 * Represente le panel pour afficher les scores des jouers
 * @author Valentin Delaye
 * @author Fabrizio Beretta Piccoli
 */
@SuppressWarnings("serial")
public class PanelScore extends AbstractPanel
{
	/**
	 * Bouton retour
	 */
	JButton btnForward = new JButton("Retour");
	
	JPanel panTable = new JPanel();
	
	/**
	 * Table des scores
	 */
	JTable tableScore = new JTable();

	/**
	 * Constructeur
	 * @param base Fenetre de base
	 */
	public PanelScore(BaseWindow base)
	{
		super(base);
		
		this.panTable.setPreferredSize(new Dimension(350, 30));
		
		TableScore modelScore = new TableScore();
		this.tableScore.setTableHeader(new JTableHeader());
		
		modelScore.setScore("toto", "1000");
		modelScore.setScore("tata", "1000");
		modelScore.setScore("toto", "1000");
		modelScore.setScore("tata", "1000");
		modelScore.setScore("toto", "1000");
		modelScore.setScore("tata", "1000");
		modelScore.setScore("toto", "1000");
		modelScore.setScore("tata", "1000");
		modelScore.setScore("toto", "1000");
		modelScore.setScore("tata", "1000");
		modelScore.setScore("toto", "1000");
		modelScore.setScore("tata", "1000");
		this.tableScore.setModel(modelScore);
		
		this.add(this.panTable);
		this.panTable.add(this.tableScore);
		this.add(this.btnForward);
		
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
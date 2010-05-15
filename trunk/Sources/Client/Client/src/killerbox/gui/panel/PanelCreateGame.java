package killerbox.gui.panel;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import killerbox.gui.*;
import static killerbox.gui.panel.EnumPanel.*;

/**
 * Permet de representer le panel pour creer une partie.
 * 
 * @author Fabrizio Beretta Piccoli
 * @author Valentin Delaye
 * @version 1.0
 * @see AbstractPanel
 */
@SuppressWarnings("serial")
public class PanelCreateGame extends AbstractPanel
{
	
	/**
	 * Label de titre.
	 */
	private JLabel labTitle = new JLabel("Choisir le type de partie", JLabel.CENTER);

	/**
	 * Bouton pour creer la partie.
	 */
	private JButton btnCreate = new JButton("Creer");
	
	/**
	 * Bouton retour.
	 */
	private JButton btnForward = new JButton("Retour");
	
	/**
	 * Groupe de bouton pour la selection des parties
	 */
	private ButtonGroup groupBtn = new ButtonGroup();
	
	/**
	 * Pour disposer les bouton en colonnes
	 */
	private JPanel panBtn = new JPanel(new GridLayout(2, 0));
	
	/**
	 * Bouton radio tous contre tous
	 */
	private JRadioButton btnTous = new JRadioButton("Tous contre tous");
	
	/**
	 * Bouton radio par equipe
	 */
	private JRadioButton btnTeam = new JRadioButton("Par equipes");
	
	/**
	 * Constructeur. Permet de creer le nouveau Panel.
	 * @param base Reference sur la fenetre.
	 */
	public PanelCreateGame(final BaseWindow base)
	{
		super(base);
		
		// Taille des composants
		this.labTitle.setPreferredSize(new Dimension(350, 100));
		this.panBtn.setPreferredSize(new Dimension(380, 100));
		this.panBtn.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Type de partie"));
		
		// Config des composants
		this.btnTous.setSelected(true);
		
		// Les valeurs des boutons
		this.btnTous.setActionCommand("0");
		this.btnTeam.setActionCommand("1");
		
		// Ecouteur des composants
		this.btnCreate.addActionListener(new ActionListener()
		{
		
			/**
			 * Lorsque l'utilisateur clique sur le bouton creer
			 */
			@Override
			public void actionPerformed(ActionEvent e)
			{
				int type = Integer.parseInt(groupBtn.getSelection().getActionCommand());
				base.getListener().requestCreateGame(type);
				base.setPanel(PANEL_LIST_PLAYERS_GAME);
			}
		});
		
		this.btnForward.addActionListener(new ActionListener()
		{
			/**
			 * Quand l'utilisateur clique sur le bouton retour
			 */
			@Override
			public void actionPerformed(ActionEvent e)
			{
				base.setPanel(PANEL_SET_ACCOUNT);
			}
		});
		
		// Grouper les boutons
		this.groupBtn.add(this.btnTous);
		this.groupBtn.add(this.btnTeam);
		
		// Ajout des composants
		this.add(this.labTitle);
		this.add(this.panBtn);
		this.panBtn.add(this.btnTous);
		this.panBtn.add(this.btnTeam);
		this.add(this.btnForward);
		this.add(this.btnCreate);
		
	}

	/**
	 * Permet de retourner le bouton principal. Null s'il n'y a
	 * aucun bouton principal sur le Panel
	 */
	@Override
	public JButton getDefaultButton()
	{
		return this.btnCreate;
	}

}

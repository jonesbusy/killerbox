package killerbox.gui.panel;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import killerbox.gui.*;
import static killerbox.gui.panel.EnumPanel.*;

/**
 * Represente le panel de gestion de compte et des parties. L'utilisateur
 * peut decider de creer une nouvelle partie ou de rejoindre une partie en cours
 * @author Valentin Delaye
 * @author Fabrizio Beretta Piccoli
 */
@SuppressWarnings("serial")
public class PanelSetAccount extends AbstractPanel
{

	/**
	 * Message utilisateur
	 */
	private static final String CONFIRM_DELETE_ACCOUNT = "Etes-vous sur de vouloir supprimer votre compte ?" +
																		  "\nCette operation est irreversible.";
	
	/**
	 * Panel partie
	 */
	private JLabel labGame = new JLabel("Choisir une option", JLabel.CENTER);

	/**
	 * Label ou afficher le message
	 */
	private JLabel labMessage = new JLabel();

	/**
	 * Panel pour contenir les bouton
	 */
	private JPanel panBtn = new JPanel();

	/**
	 * (Admin) Zone admin
	 */
	private JLabel labAdminZone = new JLabel("Zone administration");

	/**
	 * (Admin) Pour la gestion des comptes
	 */
	private JButton btnAdminScore = new JButton("Gestion des comptes");

	/**
	 * Bouton rejoindre partie
	 */
	private JButton btnJoin = new JButton("Rejoindre");

	/**
	 * Bouton creer partie
	 */
	private JButton btnCreate = new JButton("Creer partie");

	/**
	 * Bouton pour consulter les scores
	 */
	private JButton btnConsultScores = new JButton("Consulter les scores");
	
	/**
	 * Bouton pour changer son mot de passe
	 */
	private JButton btnChangePassword = new JButton("Changer son mot de passe");

	/**
	 * Bouton pour supprimer son copmte
	 */
	private JButton btnDeleteAccount = new JButton("Supprimer son compte");
	
	/**
	 * Bouton pour se deconnecter
	 */
	private JButton btnDisconnect = new JButton("Se deconnecter");

	/**
	 * Constructeur
	 * @param window Fenetre de base
	 */
	public PanelSetAccount(final BaseWindow window)
	{
		super(window);

		// Damande si admin
		this.controller.requestAdmin();

		// Taille des composants
		this.labGame.setPreferredSize(new Dimension(350, 50));
		GridLayout layoutBtn = new GridLayout(10, 1);
		layoutBtn.setVgap(10);
		this.panBtn.setLayout(layoutBtn);

		// Ajout des composants
		this.add(this.labGame);
		this.add(this.panBtn);
		this.panBtn.add(this.btnJoin);
		this.panBtn.add(this.btnCreate);
		this.panBtn.add(this.btnConsultScores);
		this.panBtn.add(this.btnChangePassword);
		this.panBtn.add(this.btnDeleteAccount);
		this.panBtn.add(this.btnDisconnect);

		this.panBtn.add(this.labAdminZone);
		this.panBtn.add(this.btnAdminScore);

		this.labAdminZone.setVisible(false);
		this.btnAdminScore.setVisible(false);

		// Ecouteur des composants
		this.btnDisconnect.addActionListener(this.window.getActionDisconnect());
		this.btnDeleteAccount.addActionListener(new ActionListener()
		{
			/**
			 * Lorsque l'utilisateur clique sur le bouton pour supprimer
			 * son compte.
			 */
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// Confirmer la suppression
				if (JOptionPane.showConfirmDialog(window, CONFIRM_DELETE_ACCOUNT, window
						.getTitle(), JOptionPane.ERROR_MESSAGE) == JOptionPane.OK_OPTION)
				{
					controller.requestDeleteAccount();
					controller.setDeconnected();
				}
					
			}
		});
		
		// Modification du mot de passe
		this.btnChangePassword.addActionListener(new ActionListener()
		{
			
			/**
			 * Quand l'utilisateur clique sur le bouton pour
			 * changer son mot de passe
			 */
			@Override
			public void actionPerformed(ActionEvent e)
			{
				window.setPanel(PANEL_CHANGE_PASSWORD);
			}
		});
		
		// Affichage des scores
		this.btnConsultScores.addActionListener(new ActionListener()
		{
			/**
			 * Quand l'utilisateur clique sur le bouton pour
			 * afficher les scores
			 */
			@Override
			public void actionPerformed(ActionEvent e)
			{
				window.setPanel(PANEL_VIEW_SCORES);
			}
		});
		
		this.btnAdminScore.addActionListener(new ActionListener()
		{
			/**
			 * Quand l'administrateur clique sur le bouton pour gerer
			 * les comptes
			 */
			@Override
			public void actionPerformed(ActionEvent e)
			{
				window.setPanel(PANEL_ADMIN_SCORES);
			}
		});
		
		this.btnJoin.addActionListener(new ActionListener()
		{
			/**
			 * Quand l'utilisateur clique pour rejoindre une partie
			 */
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				window.setPanel(PANEL_JOIN_GAME);
			}
		});
		
		this.btnCreate.addActionListener(new ActionListener()
		{
		
			/**
			 * Quand l'utilisateur clique sur le bouton pour creer une partie
			 */
			@Override
			public void actionPerformed(ActionEvent e)
			{
				window.setPanel(PANEL_CREATE_GAME);
			}
		});

	}

	/**
	 * Permet de retourner le bouton principal. Null s'il n'y a
	 * aucun bouton principal sur le Panel
	 * @return Le bouton principal du panel
	 */
	@Override
	public JButton getDefaultButton()
	{
		return this.btnJoin;
	}

	/**
	 * Affiche une erreur sur le panel
	 */
	@Override
	public void printMessage(String message)
	{
		super.printMessage(message);

		if (!errorConnection)
			this.labMessage.setText(message);
	}

	/**
	 * Afficher les parties admin
	 */
	@Override
	public void showAdmin()
	{
		super.showAdmin();
		this.labAdminZone.setVisible(true);
		this.btnAdminScore.setVisible(true);
	}

}

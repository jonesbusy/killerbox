package killerbox.gui.panel;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import killerbox.gui.*;

/**
 * 
 * @author Valentin Delaye
 */
@SuppressWarnings("serial")
public class PanelGame extends AbstractPanel
{

	/**
	 * Message utilisateur
	 */
	private static final String CONFIRM_DELETE_ACCOUNT = "Etes-vous sur de vouloir supprimer votre compte ? Cette operation est irreversible.";

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
	 * Zone admin
	 */
	private JLabel labAdminZone = new JLabel("Zone administration");

	/**
	 * Pour la gestion des comptes
	 */
	private JButton btnSetAccount = new JButton("Gestion des comptes");

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
	 * Bouton pour supprimer son copmte
	 */
	private JButton btnDeleteAccount = new JButton("Supprimer son compte");

	/**
	 * Constructeur
	 * @param base Fenetre de base
	 */
	public PanelGame(final BaseWindow base)
	{
		super(base);

		// Damande si admin
		this.base.getListener().requestAdmin();

		// Taille des composants
		this.labGame.setPreferredSize(new Dimension(350, 50));
		GridLayout layoutBtn = new GridLayout(6, 1);
		layoutBtn.setVgap(10);
		this.panBtn.setLayout(layoutBtn);

		// Ajout des composants
		this.add(this.labGame);
		this.add(this.panBtn);
		this.panBtn.add(this.btnJoin);
		this.panBtn.add(this.btnCreate);
		this.panBtn.add(this.btnConsultScores);
		this.panBtn.add(this.btnDeleteAccount);

		this.panBtn.add(this.labAdminZone);
		this.panBtn.add(this.btnSetAccount);

		this.labAdminZone.setVisible(false);
		this.btnSetAccount.setVisible(false);

		// Ecouteur des composants
		this.btnDeleteAccount.addActionListener(new ActionListener()
		{
			/**
			 * Lors du clique sur le bouton pour supprimer son compte
			 */
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// Confirmer la suppression
				if (JOptionPane.showConfirmDialog(base, CONFIRM_DELETE_ACCOUNT, base
						.getTitle(), JOptionPane.ERROR_MESSAGE) == JOptionPane.OK_OPTION)
					base.getListener().requestDeleteAccount();
			}
		});

	}

	/**
	 * Retourne le bouton principal du panel
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
	public void printError(String message)
	{
		super.printError(message);

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
		this.btnSetAccount.setVisible(true);
	}

}

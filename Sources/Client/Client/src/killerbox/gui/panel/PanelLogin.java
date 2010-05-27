package killerbox.gui.panel;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import killerbox.gui.*;

/**
 * Permet de representer le panel permettant de demander les informations
 * de connexion.
 * 
 * @author Fabrizio Beretta Piccoli
 * @author Valentin Delaye
 * @version 1.0
 * @see AbstractPanel
 */
@SuppressWarnings("serial")
public class PanelLogin extends AbstractPanel
{

	/**
	 * Titre du panel.
	 */
	private JLabel labTitre = new JLabel("Veuillez vous authentifier", JLabel.CENTER);

	/**
	 * Bouton pour creer un compte.
	 */
	private JButton btnCreateAccount = new JButton("Creer un compte");

	/**
	 * Informations pour s'authentifier.
	 */
	private JLabel labLogin = new JLabel("Login");
	private JLabel labPass = new JLabel("Mot de passe");
	private JTextField texLogin = new JTextField();
	private JPasswordField texPass = new JPasswordField();

	/**
	 * Bouton pour se s'authentifier
	 */
	private JButton btnConnect = new JButton("S'authentifier");

	/**
	 * Pour afficher un message sur le panel.
	 */
	private JLabel labMessage = new JLabel();

	/**
	 * Constructeur. Permet de creer le nouveau Panel. Place
	 * les composants et cree les ecouteur des composants.
	 * @param window Fenetre de base
	 */
	public PanelLogin(final BaseWindow window)
	{
		super(window);

		this.btnConnect.setEnabled(false);

		// Associer les label
		this.labLogin.setLabelFor(this.texLogin);
		this.labPass.setLabelFor(this.texPass);

		// Taille des composants
		this.labTitre.setPreferredSize(new Dimension(350, 40));
		this.labLogin.setPreferredSize(new Dimension(100, 40));
		this.labPass.setPreferredSize(new Dimension(100, 40));
		this.labMessage.setPreferredSize(new Dimension(300, 40));
		this.texLogin.setColumns(20);
		this.texPass.setColumns(20);

		// Ajout des composants
		this.add(this.labTitre);
		this.add(this.labLogin);
		this.add(this.texLogin);
		this.add(this.labPass);
		this.add(this.texPass);
		this.add(this.btnConnect);
		this.add(this.btnCreateAccount);
		this.add(this.labMessage);

		// Ecouteur des champs de texte
		DocumentListener changeListener = new DocumentListener()
		{

			/**
			 * Lorsqu'un caractere est enleve de la zone de saisie.
			 * Effectue la meme action que quand un caractere est ajoute dans la zone.
			 */
			@Override
			public void removeUpdate(DocumentEvent event)
			{
				insertUpdate(event);
			}

			/**
			 * Lorsqu'un caractere est ajoute de la zone de saisie
			 */
			@Override
			public void insertUpdate(DocumentEvent event)
			{
				// Enlever le message s'il y en a un
				labMessage.setText("");

				if (!texLogin.getText().isEmpty() && texPass.getPassword().length != 0)
					btnConnect.setEnabled(true);
				else
					btnConnect.setEnabled(false);
			}

			/**
			 * Notification quand un propriete du document ecoute change. Non
			 * utilise dans ce cas la.
			 */
			@Override
			public void changedUpdate(DocumentEvent event)
			{

			}
		};

		// Ecouteur des boutons
		this.texLogin.getDocument().addDocumentListener(changeListener);
		this.texPass.getDocument().addDocumentListener(changeListener);

		this.btnConnect.addActionListener(new ActionListener()
		{
			/**
			 * Lors du clique sur le bouton s'authentifier. Celui-ci
			 * fait appel au controleur pour envoyer les informations d'authentification.
			 * Il garde également directement le nom du joueur
			 */
			@Override
			public void actionPerformed(ActionEvent e)
			{
				window.setNomJoueur(texLogin.getText());
				
				controller.sendCredentias(texLogin.getText(),
						new String(texPass.getPassword()));
			}
		});

		this.btnCreateAccount.addActionListener(new ActionListener()
		{

			/**
			 * Lorsque l'utilisateur clique sur le bouton pour creer un nouveau compte.
			 */
			@Override
			public void actionPerformed(ActionEvent e)
			{
				window.setPanel(EnumPanel.PANEL_CREATE_ACCOUNT);
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
		return this.btnConnect;
	}

	/**
	 * Lorsque le label recoit un message d'erreur.
	 * Un charge le panel de connecion et on affiche le message d'erreur
	 */
	@Override
	public void printMessage(String message)
	{
		super.printMessage(message);

		if (!errorConnection)
			this.labMessage.setText(message);
	}

}

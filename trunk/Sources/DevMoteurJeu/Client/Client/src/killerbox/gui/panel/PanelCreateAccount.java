package killerbox.gui.panel;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.util.*;

import killerbox.gui.*;
import static killerbox.gui.panel.EnumPanel.*;

/**
 * Represente le panel de creation de compte
 * @author Valentin Delaye
 */
@SuppressWarnings("serial")
public class PanelCreateAccount extends AbstractPanel
{

	/**
	 * Label accueil
	 */
	private JLabel labCreateAccount = new JLabel(
			"Veuillez choisir vos informations de connexion", JLabel.CENTER);
	
	/**
	 * Label pour afficher differents message
	 */
	private JLabel labMessage = new JLabel("", JLabel.CENTER);

	/**
	 * Label du login
	 */
	private JLabel labLogin = new JLabel("Nom d'utilisateur");

	/**
	 * Label du mot de passe 1
	 */
	private JLabel labPass1 = new JLabel("Mot de passe");

	/**
	 * Label du mot de passe 2
	 */
	private JLabel labPass2 = new JLabel("Confirmer le mot de passe");

	/**
	 * Bouton pour creer le compte
	 */
	private JButton btnCreate = new JButton("Creer");

	/**
	 * Bouton pour retourner au panel de connexion
	 */
	private JButton btnForward = new JButton("Retour");

	/**
	 * Pour choisir le nom d'utilisateur
	 */
	private JTextField textLogin = new JTextField();

	/**
	 * Premier mot de passe
	 */
	private JPasswordField textPass1 = new JPasswordField();

	/**
	 * Pour le ressaisir
	 */
	private JPasswordField textPass2 = new JPasswordField();

	/**
	 * Constructeur
	 * @param base Fenetre de base
	 */
	public PanelCreateAccount(final BaseWindow base)
	{
		super(base);

		btnCreate.setEnabled(false);

		// Definition des tailles
		this.labCreateAccount.setPreferredSize(new Dimension(350, 50));
		this.labMessage.setPreferredSize(new Dimension(350, 40));
		this.labLogin.setPreferredSize(new Dimension(150, 40));
		this.labPass1.setPreferredSize(new Dimension(150, 40));
		this.labPass2.setPreferredSize(new Dimension(150, 40));
		this.textLogin.setColumns(20);
		this.textPass1.setColumns(20);
		this.textPass2.setColumns(20);

		// Associer les label
		this.labLogin.setLabelFor(this.textLogin);
		this.labPass1.setLabelFor(this.labPass1);
		this.labPass2.setLabelFor(this.labPass2);

		// Ajouter les composants
		this.add(this.labCreateAccount);
		this.add(this.labLogin);
		this.add(this.textLogin);
		this.add(this.labPass1);
		this.add(this.textPass1);
		this.add(this.labPass2);
		this.add(this.textPass2);
		this.add(this.btnForward);
		this.add(this.btnCreate);
		this.add(this.labMessage);

		// Lors de la modification des champs de texte
		DocumentListener changeText = new DocumentListener()
		{

			@Override
			public void removeUpdate(DocumentEvent e)
			{
				insertUpdate(e);
			}

			@Override
			public void insertUpdate(DocumentEvent e)
			{
				// S'il y a quelque chose dans les champs
				if (!textLogin.getText().isEmpty() && textPass1.getPassword().length > 0
						&& textPass2.getPassword().length > 0)
				{
					btnCreate.setEnabled(true);
				}
				else
					btnCreate.setEnabled(false);

			}

			@Override
			public void changedUpdate(DocumentEvent e)
			{

			}
		};

		// Ajout des ecouteur
		this.textLogin.getDocument().addDocumentListener(changeText);
		this.textPass1.getDocument().addDocumentListener(changeText);
		this.textPass2.getDocument().addDocumentListener(changeText);

		// Ecouteur des bouton
		this.btnCreate.addActionListener(new ActionListener()
		{

			/**
			 * Action lorsqu'on clique sur le bouton creer
			 */
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// Les deux password ok
				if (Arrays.equals(textPass1.getPassword(), textPass2.getPassword()))
				{
					base.getListener().requestAccount(textLogin.getText(), new String(textPass1.getPassword()));
				}

				// Sinon erreur
				else
				{
					JOptionPane.showMessageDialog(base, "Les mots de passe sont differents", base
							.getTitle(), JOptionPane.ERROR_MESSAGE);
					textPass1.setText("");
					textPass2.setText("");
					textPass1.requestFocus();
				}
			}
		});

		this.btnForward.addActionListener(new ActionListener()
		{
			/**
			 * Lors du clique sur le bouton retour
			 */
			@Override
			public void actionPerformed(ActionEvent e)
			{
				base.setPanel(PANEL_LOGIN);
			}
		});

	}

	/**
	 * Retourne le bouton principal du panel
	 */
	@Override
	public JButton getDefaultButton()
	{
		return this.btnCreate;
	}

	/**
	 * Affiche l'erreur
	 */
	@Override
	public void printMessage(String message)
	{
		super.printMessage(message);

		// Ce n'est pas une erreur de connection
		if (!errorConnection)
			this.labMessage.setText(message);
	}

}

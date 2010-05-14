package killerbox.gui.panel;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

import killerbox.gui.*;
import static killerbox.gui.panel.EnumPanel.*;

/**
 * Represente le panel pour changer son mot de passe
 * @author Valentin Delaye
 * @author Fabrizio Beretta Piccoli
 * @version 1.0
 * @see AbstractPanel
 */
@SuppressWarnings("serial")
public class PanelChangePassWord extends AbstractPanel
{

	/**
	 * Label change mot de passe
	 */
	private JLabel labChangePass = new JLabel(
			"Veuillez choisir votre nouveau mot de passe", JLabel.CENTER);
	
	/**
	 * Label pour afficher differents messages.
	 */
	private JLabel labMessage = new JLabel("", JLabel.CENTER);
	
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
	private JButton btnModify = new JButton("Modifier");

	/**
	 * Bouton pour retourner au panel de connexion
	 */
	private JButton btnForward = new JButton("Retour");
	
	/**
	 * Premier mot de passe
	 */
	private JPasswordField textPass1 = new JPasswordField();

	/**
	 * Pour le ressaisir
	 */
	private JPasswordField textPass2 = new JPasswordField();
	
	/**
	 * Constructeur du panel
	 * @param base Fenetre de base
	 */
	public PanelChangePassWord(final BaseWindow base)
	{
		super(base);
		
		// Definition des tailles
		this.labChangePass.setPreferredSize(new Dimension(350, 50));
		this.labMessage.setPreferredSize(new Dimension(350, 40));
		this.labPass1.setPreferredSize(new Dimension(150, 40));
		this.labPass2.setPreferredSize(new Dimension(150, 40));
		this.textPass1.setColumns(20);
		this.textPass2.setColumns(20);

		// Associer les label
		this.labPass1.setLabelFor(this.labPass1);
		this.labPass2.setLabelFor(this.labPass2);

		// Ajouter les composants
		this.add(this.labChangePass);
		this.add(this.labPass1);
		this.add(this.textPass1);
		this.add(this.labPass2);
		this.add(this.textPass2);
		this.add(this.btnForward);
		this.add(this.btnModify);
		this.add(this.labMessage);
		
		this.btnModify.setEnabled(false);
		
		// Lors de la modification des champs de texte
		DocumentListener changePass = new DocumentListener()
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
				if (textPass1.getPassword().length > 0 && textPass2.getPassword().length > 0)
				{
					btnModify.setEnabled(true);
				}
				else
					btnModify.setEnabled(false);

			}

			@Override
			public void changedUpdate(DocumentEvent e)
			{

			}
		};
		
		this.textPass1.getDocument().addDocumentListener(changePass);
		this.textPass2.getDocument().addDocumentListener(changePass);
		
		// Ecouteur des bouton
		this.btnModify.addActionListener(new ActionListener()
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
					base.getListener().requestModifyPass(new String(textPass1.getPassword()));
					base.setPanel(PANEL_SET_ACCOUNT);
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
		
		// Action bouton retour
		this.btnForward.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				base.setPanel(PANEL_SET_ACCOUNT);
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
		return this.btnModify;
	}

}

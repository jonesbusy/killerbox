package killerbox.panel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import killerbox.*;

/**
 * Permet de representer le panel permettant de demander les informations
 * de connection
 * @author Fabrizio Beretta Piccoli
 * @author Valentin Delaye
 */
@SuppressWarnings("serial")
public class LoginPanel extends KillerBoxPanel
{
	
	/**
	 * Titre du panel
	 */
	private JLabel labTitre = new JLabel("Veuillez vous authentifier", JLabel.CENTER);
	
	/**
	 * 
	 */
	private JButton btnCreateAccount = new JButton("Creer un compte");
	
	/**
	 * Informations pour s'authentifier
	 */
	private JLabel labLogin = new JLabel("Login");
	private JLabel labPass = new JLabel("Mot de passe");
	private JTextField texLogin = new JTextField();
	private JTextField texPass = new JTextField();
	
	/**
	 * Bouton pour se s'authentifier
	 */
	private JButton btnConnect = new JButton("S'authentifier");
	
	/**
	 * Pour afficher le message d'erreur
	 */
	private JLabel labErreur = new JLabel();

	/**
	 * Constructeur
	 * @param base Fenetre de base
	 */
	public LoginPanel(final BaseWindow base)
	{
		super(base);
		
		this.btnConnect.setEnabled(false);
		
		// Associer les label
		this.labLogin.setLabelFor(this.texLogin);
		this.labPass.setLabelFor(this.texPass);
		
		// Taille des composants
		this.labTitre.setPreferredSize(new Dimension(350, 40));
		this.labLogin.setPreferredSize(new Dimension(100, 40));
		this.labPass.setPreferredSize(new Dimension(100, 40));
		this.labErreur.setPreferredSize(new Dimension(300, 40));
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
		this.add(this.labErreur);
				
		
		// Ecouteur des champs de texte
		DocumentListener changeListener = new DocumentListener()
		{
			
			@Override
			public void removeUpdate(DocumentEvent arg0)
			{
				insertUpdate(arg0);
			}
			
			@Override
			public void insertUpdate(DocumentEvent arg0)
			{
				// Enlever le message s'il y en a un
				labErreur.setText("");
				
				if(!texLogin.getText().isEmpty() && !texPass.getText().isEmpty())
					btnConnect.setEnabled(true);
				else
					btnConnect.setEnabled(false);
			}
			
			@Override
			public void changedUpdate(DocumentEvent arg0)
			{

			}
		};
		
		// Ecouteur des boutons
		this.texLogin.getDocument().addDocumentListener(changeListener);
		this.texPass.getDocument().addDocumentListener(changeListener);
		
		this.btnConnect.addActionListener(new ActionListener()
		{
			/**
			 * Lors du clique sur le bouton s'authentifier
			 */
			@Override
			public void actionPerformed(ActionEvent e)
			{				
				base.getListener().sendCredentias(texLogin.getText(), texPass.getText());
			}
		});
		
		this.btnCreateAccount.addActionListener(new ActionListener()
		{
		
			/**
			 * Pour aller a la creation du compte
			 */
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				base.setPanel(EnumPanel.CREATE_ACCOUNT_PANEL);
			}
		});
		
	}

	/**
	 * Le bouton principal est le bouton de connecion
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
	public void printError(String message)
	{
		super.printError(message);
		
		if(!errorConnection)
			this.labErreur.setText(message);
	}

}

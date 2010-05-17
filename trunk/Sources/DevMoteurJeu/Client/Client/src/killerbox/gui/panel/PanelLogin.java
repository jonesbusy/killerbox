package killerbox.gui.panel;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import killerbox.gui.*;

/**
 * Permet de representer le panel permettant de demander les informations
 * de connection
 * @author Fabrizio Beretta Piccoli
 * @author Valentin Delaye
 */
@SuppressWarnings("serial")
public class PanelLogin extends AbstractPanel
{
	
	/**
	 * Titre du panel
	 */
	private JLabel labTitre = new JLabel("Veuillez vous authentifier", JLabel.CENTER);
	
	/**
	 * Bouton pour creer un compte
	 */
	private JButton btnCreateAccount = new JButton("Creer un compte");
	
	/**
	 * Informations pour s'authentifier
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
	 * Pour afficher un message sur le panel
	 */
	private JLabel labMessage = new JLabel();

	/**
	 * Constructeur
	 * @param base Fenetre de base
	 */
	public PanelLogin(final BaseWindow base)
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
			
			public void removeUpdate(DocumentEvent arg0)
			{
				insertUpdate(arg0);
			}
			
			public void insertUpdate(DocumentEvent arg0)
			{
				// Enlever le message s'il y en a un
				labMessage.setText("");
				
				if(!texLogin.getText().isEmpty() && texPass.getPassword().length != 0)
					btnConnect.setEnabled(true);
				else
					btnConnect.setEnabled(false);
			}
			
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
			public void actionPerformed(ActionEvent e)
			{	
				base.getListener().sendCredentias(texLogin.getText(), new String(texPass.getPassword()));
			}
		});
		
		this.btnCreateAccount.addActionListener(new ActionListener()
		{
		
			/**
			 * Pour aller a la creation du compte
			 */
			public void actionPerformed(ActionEvent arg0)
			{
				base.setPanel(EnumPanel.PANEL_CREATE_ACCOUNT);
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
	public void printMessage(String message)
	{
		super.printMessage(message);
		
		if(!errorConnection)
			this.labMessage.setText(message);
	}

}

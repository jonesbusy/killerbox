package killerbox.panel;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import java.net.*;
import killerbox.*;
import network.*;

import static killerbox.panel.EnumPanel.*;

/**
 * Represente la panel permettant de se connecter au serveur.
 * (Adresse ip et mot de passe)
 * @author Fabrizio Beretta Piccoli
 * @author Valentin Delaye
 */
@SuppressWarnings("serial")
public class ConnectionPanel extends KillerBoxPanel
{
	
	/**
	 * Bouton pour se connecter
	 */
	private JButton btnConnect = new JButton("Se connecter");
	
	/**
	 * Label ip
	 */
	private JLabel labIpServer = new JLabel("Adresse serveur");
	
	/**
	 * Label port
	 */
	private JLabel labPortServer = new JLabel("Port serveur");
	
	/**
	 * Pour l'affichage d'une erreur eventuelle
	 */
	private JLabel labMessage = new JLabel();
	
	/**
	 * Champs pour rentrer l'adresse ip
	 */

	private JTextField texIpServer = new JTextField(20);
	
	/**
	 * Champs pour rentrer le port
	 */

	private JTextField texPortServer = new JTextField(20);
		
	/**
	 * Layout du panel
	 */
	private FlowLayout layout = new FlowLayout();

	/**
	 * Constructeur
	 * @param base La fenetre de base
	 */
	public ConnectionPanel(final BaseWindow base, Client client, ClientListener clientListener)
	{
				
		super(base, client, clientListener);
		this.setLayout(layout);
		
		
		// Set les label
		this.labIpServer.setLabelFor(this.texIpServer);
		this.labPortServer.setLabelFor(this.texPortServer);
		
		// Taille des text field
		this.labIpServer.setPreferredSize(new Dimension(100, 40));
		this.labPortServer.setPreferredSize(new Dimension(100, 40));
		
		JLabel labWelcome = new JLabel("Veuillez vous connecter a un serveur pour jouer");
		labWelcome.setPreferredSize(new Dimension(300, 40));
		
		this.labMessage.setPreferredSize(new Dimension(300, 30));
		
		// Ajout des composants
		this.add(labWelcome);
		this.add(this.labIpServer);
		this.add(this.texIpServer);
		this.add(this.labPortServer);
		this.add(this.texPortServer);
		this.add(this.btnConnect);
		this.add(this.labMessage);
		this.btnConnect.setEnabled(false);
		
		
		// Ecouteur lors de la modification des champs
		DocumentListener changeListener = new DocumentListener()
		{
			
			@Override
			public void removeUpdate(DocumentEvent e)
			{
				insertUpdate(e);
			}
			
			@Override
			public void insertUpdate(DocumentEvent e)
			{
				
				// Enlever le message s'il y en a un
				labMessage.setText("");
				
				if(!texIpServer.getText().isEmpty() && !texPortServer.getText().isEmpty())
					btnConnect.setEnabled(true);
				else
					btnConnect.setEnabled(false);
			}
			
			@Override
			public void changedUpdate(DocumentEvent e)
			{

			}
		};
		
		// Activer uniquement si les champs contiennent quelque chose
		this.texIpServer.getDocument().addDocumentListener(changeListener);
		this.texPortServer.getDocument().addDocumentListener(changeListener);
		
		// Lors du clique sur le bouton connecte
		this.btnConnect.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// Pour recuperer le port du serveur
				int port = 0;
				
				// Pour recuperer l'adresse du serveur
				InetAddress address = null;
				
				// Pour creer le client
				Client client = null;
				
				// Format de l'adresse
				try
				{
					 address = InetAddress.getByName(texIpServer.getText());
				}
				catch (UnknownHostException e1)
				{
					JOptionPane.showMessageDialog(base, "L'adresse est incorrecte",
							base.getTitle(), JOptionPane.ERROR_MESSAGE);
				}
				
				// Format du port
				try
				{
					port = Integer.parseInt(texPortServer.getText());
					if(port < 1 || port > 65536)
						JOptionPane.showMessageDialog(base, "Le port doit etre compris entre 1 et 65535",
								base.getTitle(), JOptionPane.ERROR_MESSAGE);
				}
				catch(NumberFormatException ex)
				{
					JOptionPane.showMessageDialog(base, "Le port doit etre numerique",
							base.getTitle(), JOptionPane.ERROR_MESSAGE);
				}
				
				// Si correct, essayer de connecter le client
				if(port != 0 && address != null)
				{
					client = new Client(address.getHostAddress(), port);
					
					// Connecter le client
					base.setClient(client);
					
					// Connection reussie
					if(client.connect())
						base.setPanel(LOGIN_PANEL);
					
				}
				
			}
		});
		
	}

	/**
	 * Retourne le bouton principal
	 */
	@Override
	public JButton getDefaultButton()
	{
		return this.btnConnect;
	}

	/**
	 * Affiche un message d'erreur
	 */
	@Override
	public void printError(String message)
	{
		this.labMessage.setText(message);
	}

}

package killerbox.gui.panel;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import java.net.*;
import killerbox.gui.*;
import network.*;

import static killerbox.gui.panel.EnumPanel.*;

/**
 * Represente la panel permettant de se connecter au serveur.
 * (Adresse ip et port)
 * @author Fabrizio Beretta Piccoli
 * @author Valentin Delaye
 */
@SuppressWarnings("serial")
public class PanelServerConnection extends AbstractPanel
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
	private JLabel labMessage = new JLabel("", JLabel.CENTER);
	
	/**
	 * Champs pour rentrer l'adresse ip
	 */

	private JTextField texIpServer = new JTextField(20);
	
	/**
	 * Champs pour rentrer le port
	 */

	private JTextField texPortServer = new JTextField(20);
		
	/**
	 * Constructeur
	 * @param base La fenetre de base
	 */
	public PanelServerConnection(final BaseWindow base)
	{
				
		super(base);
		
		// Port par defaut
		this.texPortServer.setText("7000");
		this.texIpServer.setText("127.0.0.1");
		this.btnConnect.setEnabled(true);
		
		// Set les label
		this.labIpServer.setLabelFor(this.texIpServer);
		this.labPortServer.setLabelFor(this.texPortServer);
		
		// Taille des text field
		this.labIpServer.setPreferredSize(new Dimension(120, 40));
		this.labPortServer.setPreferredSize(new Dimension(120, 40));
		
		JLabel labWelcome = new JLabel("Veuillez vous connecter a un serveur pour jouer", JLabel.CENTER);
		labWelcome.setPreferredSize(new Dimension(350, 40));
		this.labMessage.setPreferredSize(new Dimension(350, 40));
		
		// Ajout des composants
		this.add(labWelcome);
		this.add(this.labIpServer);
		this.add(this.texIpServer);
		this.add(this.labPortServer);
		this.add(this.texPortServer);
		this.add(this.btnConnect);
		this.add(this.labMessage);		
		
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
					JOptionPane.showMessageDialog(base, "L'adresse est incorrecte.",
							base.getTitle(), JOptionPane.ERROR_MESSAGE);
				}
				
				// Format du port
				try
				{
					port = Integer.parseInt(texPortServer.getText());
					if(port < 1 || port > 65536)
					{
						JOptionPane.showMessageDialog(base, "Le port doit etre compris entre 1 et 65535.",
								base.getTitle(), JOptionPane.ERROR_MESSAGE);
						port = 0;
					}
				}
				catch(NumberFormatException ex)
				{
					JOptionPane.showMessageDialog(base, "Le port doit etre numerique.",
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
						base.setPanel(PANEL_LOGIN);
					else
						JOptionPane.showMessageDialog(base, "Impossible de se connecter.",
								base.getTitle(), JOptionPane.ERROR_MESSAGE);
					
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
	public void printMessage(String message)
	{
		this.labMessage.setText(message);
	}

}

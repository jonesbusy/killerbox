package killerbox;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import killerbox.panel.*;
import network.*;
import static killerbox.panel.EnumPanel.*;

/**
 * Represente la classe de base de la fenetre
 * @author Fabrizio Beretta Piccoli
 * @author Valentin Delaye
 */
@SuppressWarnings("serial")
public class BaseWindow extends JFrame implements Observer
{
	
	/**
	 * Le client
	 */
	private Client client;
	
	/**
	 * L'ecouteur de message
	 */
	private ClientListener clientListener;
	
	/**
	 * Le message de confirmation de fermeture
	 */
	private static final String CONFIRM_QUIT_MESSAGE = "Etes-vous sur de vouloir quitter ?";
	
	/**
	 * Hauteur de la fenetre
	 */
	public static final int HEIGHT = 400;
	
	/**
	 * Largeur de la fenetre
	 */
	public static final int WIDTH = 400;
	
	/**
	 * Boite de dialogue "About KillerBox"
	 */
	private AboutDialog aboutDialog = new AboutDialog(this);
	
	/**
	 * Le bouton d'activation principal
	 */
	private JButton defaultButton;

	/**
	 * Menu principal
	 */
	private JMenuBar menu = new JMenuBar();
	private JMenu fileMenu = new JMenu("Fichier");
	private JMenu gameMenu = new JMenu("Partie");
	private JMenu helpMenu = new JMenu("Aide");
	
	/**
	 * Items
	 */
	private JMenuItem quitItem = new JMenuItem("Quitter");
	private JMenuItem aboutItem = new JMenuItem("A propos de KillerBox");
	
	/**
	 * Le pannel courant
	 */
	private KillerBoxPanel panel;
	
	/**
	 * Panel de connexion au serveur
	 */
	private ConnectionPanel connectionPanel;
	
	/**
	 * Panel de demande de login
	 */
	private LoginPanel loginPanel;
	
	/**
	 * Constructeur
	 * @param height La hauteur de la fenetre
	 * @param width La largeur de la fenetre
	 */
	public BaseWindow()
	{
		
		this.setTitle("KillerBox");
		this.setSize(HEIGHT, WIDTH);
		this.setResizable(false);
		
		// Centre de l'ecran
		this.setLocationRelativeTo(null);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		
		// Construire le menu
		this.setJMenuBar(this.menu);
		this.menu.add(this.fileMenu);
		this.menu.add(this.gameMenu);
		this.menu.add(this.helpMenu);
		this.fileMenu.add(this.quitItem);
		this.helpMenu.add(this.aboutItem);
		
		// Action des items menu
		this.quitItem.addActionListener(new ActionListener()
		{
			/**
			 * Lors du clique sur le bouton quitter
			 */
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(confirmQuit(BaseWindow.CONFIRM_QUIT_MESSAGE))
				{
					setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					dispose();
				}
			}
		});
		
		// Bouton about
		this.aboutItem.addActionListener(new ActionListener()
		{
			/**
			 * Lors du clique sur le bouton about
			 */
			@Override
			public void actionPerformed(ActionEvent e)
			{
				aboutDialog.setVisible(true);
			}
		});
		
		// Evenement de la fenetre
		this.addWindowListener(new WindowAdapter()
		{

			/**
			 * Lorsque la fenetre se ferme
			 */
			@Override
			public void windowClosing(WindowEvent e)
			{
				if(confirmQuit(BaseWindow.CONFIRM_QUIT_MESSAGE))
					setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				else
					setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			}
			
		});

		// Panel de connection au demarrage
		this.setPanel(CONNECTION_PANEL);

		// Afficher la fen�tre
		this.setVisible(true);
	}
	
	
	/**
	 * Permet de setter le client principal
	 * @param client Le client
	 */
	public void setClient(Client client)
	{
		this.client = client;
		this.clientListener = new ClientListener(client, this);
		
		// Message d'erreur qui peuvent provenir du client
		client.addObserver(this);
		
		// Le client listener recoit toute les informations recues par le serveur
		client.addObserver(this.clientListener);
		
	}

	/**
	 * Permet de changer le panel de la fenetre
	 * @param panel Le nouveau pannel
	 */
	private void loadPanel(KillerBoxPanel panel)
	{
		// Seter le panel courant
		this.panel = panel;
		
		// Setter le bouton principal du panel
		this.defaultButton = panel.getDefaultButton();
		this.getRootPane().setDefaultButton(this.defaultButton);
		
		Container container = this.getContentPane();
		container.removeAll();
		container.add(panel);
		container.validate();
		container.repaint();
	}

	/**
	 * Permet de changer le panel de la fenetre
	 * @param type
	 */
	public void setPanel(EnumPanel type)
	{
		switch (type)
		{
			case CONNECTION_PANEL:
				loadPanel(new ConnectionPanel(this, this.client, this.clientListener));
				break;
			case LOGIN_PANEL:
				loadPanel(new LoginPanel(this, this.client, this.clientListener));
				break;
		}
	}
	
	/**
	 * Permet de confirmer ou non la fermeture de la fenetre. 
	 * Affiche de ce fait une boite de message
	 * @param message Message a afficher
	 * @return True l'utilisateur desire quitter, false sinon
	 */
	private boolean confirmQuit(String message)
	{
		return JOptionPane.showConfirmDialog(this, message, this.getTitle(),
				  JOptionPane.YES_NO_CANCEL_OPTION) == JOptionPane.OK_OPTION;
	}
	
	/**
	 * Permet d'envoyer un message d'erreur a la vue
	 * @param message Le message d'erreur
	 */
	public void sendError(String message)
	{
		System.out.println(message);
	}


	/**
	 * Lorsque la vue recoit un message. Elle l'affiche sur le panel
	 * en cours.
	 */
	@Override
	public void update(Observable o, Object arg)
	{
		// Si la vue recoit un message (generalement d'erreur)
		if(String.class.isInstance(arg))
			this.panel.printError((String)arg);
	}
	
}
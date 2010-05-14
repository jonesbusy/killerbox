package killerbox.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

import network.*;
import killerbox.network.*;
import killerbox.gui.panel.*;

import static killerbox.gui.panel.EnumPanel.*;

/**
 * Represente la classe principale de la fenetre. Celle-ci contient
 * differentes informations comme le client, le controleur (KillerBoxListener),
 * les differentes scores, etc. La fenetre s'occupe de contenir differents panel.
 * 
 * La fenetre comprends toujours une reference sur le panel en cours (AbstractPanel)
 * ainsi que sur le bouton principal (Actionne avec la touche ENTER).
 * 
 * La vue se charge d'observer les eventuels message du client et du controleur. La
 * plupart des messages recu sont des messages d'erreur.
 *
 * @author Fabrizio Beretta Piccoli
 * @author Valentin Delaye
 * @version 1.0
 * @see Client
 * @see KillerBoxListener
 * @see AbstractPanel
 */
@SuppressWarnings("serial")
public class BaseWindow extends JFrame implements Observer
{

	/**
	 * Le client
	 */
	private Client client;

	/**
	 * Pour stocker temporairement les differents scores recu du serveur.
	 * Les panel sont alors libres de recuperer ces scores a tout moment.
	 */
	private ScoresInfo scoresInfo = new ScoresInfo();

	/**
	 * Controleur. Permet a la fenetre et aux Panels d'envoyer des messages
	 * au serveur.
	 */
	private KillerBoxListener listener;

	/**
	 * Messages a afficher en cas de deconnexion.
	 */
	private static final String MESSAGE_DECO = "Vous etes deconnecte.";

	/**
	 * Le message de confirmation de fermeture de la fenetre.
	 */
	private static final String CONFIRM_QUIT_MESSAGE = "Etes-vous sur de vouloir quitter ?";

	/**
	 * Le message de confirmation de deconnection.
	 */
	private static final String CONFIRM_DISCONNECT_MESSAGE = "Etes-vous sur de vouloir vous deconnecter du serveur ?";

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
	 * Le bouton d'activation principal. Il est obtenu par les
	 * differents panel grace a la methode getDefaultButton
	 */
	private JButton defaultButton;

	/**
	 * Action pour le bouton et menu "Se deconnecter"
	 */
	private ActionListener actionDisconnect;

	/**
	 * Menu principal
	 */
	private JMenuBar menu = new JMenuBar();
	private JMenu fileMenu = new JMenu("Fichier");
	private JMenu gameMenu = new JMenu("Partie");
	private JMenu helpMenu = new JMenu("Aide");

	/**
	 * Items du mene principal
	 */
	private JMenuItem disconnectItem = new JMenuItem("Se deconnecter");
	private JMenuItem quitItem = new JMenuItem("Quitter");
	private JMenuItem aboutItem = new JMenuItem("A propos de KillerBox");

	/**
	 * Reference sur la Panel courant.
	 */
	private AbstractPanel panel;

	/**
	 * Constructeur. Permet de creer la fenetre et places les composants
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
		this.fileMenu.add(this.disconnectItem);
		this.fileMenu.add(this.quitItem);
		this.helpMenu.add(this.aboutItem);

		// Panel de connection au demarrage
		this.setPanel(PANEL_CONNECTION);

		// Afficher la fenetre
		this.setVisible(true);

		// Creation de l'action de deconnection
		this.actionDisconnect = new ActionListener()
		{
			/**
			 * Lors du clique sur le bouton deconnecter. Envoyer un message au serveur, se deconnecter,
			 * charger le panel de connexion au serveur et afficher un message comme quoi l'utilisateur
			 * s'est bien deconnecte.
			 */
			@Override
			public void actionPerformed(ActionEvent arg0)
			{

				if (confirmQuit(BaseWindow.CONFIRM_DISCONNECT_MESSAGE))
				{
					client.send("#logout");
					client.disconnect();
					setPanel(PANEL_CONNECTION);
					panel.printMessage(MESSAGE_DECO);

				}

			}
		};

		// Action des items menu
		this.disconnectItem.addActionListener(this.actionDisconnect);

		this.quitItem.addActionListener(new ActionListener()
		{
			/**
			 * Lorsque l'utilisateur clique sur "Quitter"
			 */
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// Confirmer la fermeture
				if (confirmQuit(BaseWindow.CONFIRM_QUIT_MESSAGE))
				{
					setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					dispose();
					System.exit(EXIT_ON_CLOSE);
				}
			}
		});

		// Bouton about
		this.aboutItem.addActionListener(new ActionListener()
		{
			/**
			 * Lorsque l'utilisateur clique sur le bouton "About"
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
			 * Lorsque l'utilisateur clique sur le bouton de fermeture.
			 */
			@Override
			public void windowClosing(WindowEvent e)
			{
				if (confirmQuit(BaseWindow.CONFIRM_QUIT_MESSAGE))
					setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				else
					setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			}

		});

	}

	/**
	 * Permet de retourner le panel charge sur la fenetre.
	 * @return Le panel en cours
	 */
	public AbstractPanel getPanel()
	{
		return this.panel;
	}

	/**
	 * Permet de retourne l'action pour se deconnecter. Permet aux Panels
	 * qui veulent ajouter un bouton "Deconnecter" d'utiliser le meme evenement.
	 * @return L'action pour se deconnecter
	 */
	public ActionListener getActionDisconnect()
	{
		return actionDisconnect;
	}

	/**
	 * Permet de retourner le controlleur et permettre aux Panels
	 * d'envoyer des messages au serveur.
	 * @return Le controleur
	 */
	public KillerBoxListener getListener()
	{
		return listener;
	}

	/**
	 * Permet de retourner les scores. Permettre ainsi anx Panels d'afficher
	 * a tout moment les differents scores.
	 * @return Les differents scores des utilisateurs
	 */
	public ScoresInfo getScoresInfo()
	{
		return this.scoresInfo;
	}

	/**
	 * Permet de setter le client principal.
	 * @param client Le programme client.
	 */
	public void setClient(Client client)
	{
		this.client = client;

		// Creation de l'ecouteur et du decoder
		this.listener = new KillerBoxListener(client, this, new KillerBoxDecoder(client,
				this));

		// Message d'erreur qui peuvent provenir du client
		client.addObserver(this);

		// Le client listener recoit toute les informations recues par le serveur
		client.addObserver(this.listener);

	}

	/**
	 * Permet de changer le panel de la fenetre
	 * @param panel Le nouveau pannel
	 */
	private void loadPanel(AbstractPanel panel)
	{
		// Seter le panel courant
		this.panel = panel;

		// Setter le bouton principal du panel
		this.defaultButton = panel.getDefaultButton();

		// Le getRootPane crache parfoit une exception depuis AWT
		// Incomprehensible...
		try
		{
			this.getRootPane().setDefaultButton(this.defaultButton);
		}
		catch (Exception e)
		{

		}

		// Mettre a jour la vue
		Container container = this.getContentPane();
		container.removeAll();
		container.add(panel);
		container.validate();
	}

	/**
	 * Permet de changer le panel de la fenetre
	 * @param type Le type enumere du panel
	 */
	public void setPanel(EnumPanel type)
	{
		switch (type)
		{
			case PANEL_CONNECTION:
			{
				this.disconnectItem.setEnabled(false);
				this.panel = new PanelServerConnection(this);
				break;
			}

			case PANEL_LOGIN:
			{
				this.disconnectItem.setEnabled(true);
				this.panel = new PanelLogin(this);
				break;
			}

			case PANEL_CREATE_ACCOUNT:
			{
				this.panel = new PanelCreateAccount(this);
				break;
			}

			case PANEL_SET_ACCOUNT:
			{
				this.panel = new PanelSetAccount(this);
				break;
			}

			case PANEL_CHANGE_PASSWORD:
			{
				this.panel = new PanelChangePassWord(this);
				break;
			}

			case PANEL_VIEW_SCORES:
			{
				this.panel = new PanelScores(this);
				break;
			}

			case PANEL_ADMIN_SCORES:
			{
				this.panel = new PanelAdminScores(this);
				break;
			}

		}

		// Charge le panel
		loadPanel(this.panel);

	}

	/**
	 * Permet de confirmer ou non la fermeture de la fenetre.
	 * Affiche une boite de message.
	 * @param message Message a afficher.
	 * @return True si l'utilisateur desire quitter, false sinon.
	 */
	private boolean confirmQuit(String message)
	{
		return JOptionPane.showConfirmDialog(this, message, this.getTitle(),
				JOptionPane.YES_NO_CANCEL_OPTION) == JOptionPane.OK_OPTION;
	}

	/**
	 * Permet d'envoyer un message d'erreur a la vue. Celle ci
	 * s'occupe de l'afficher sur la panel en cours.
	 * @param message Le message d'erreur.
	 */
	public void printMessage(String message)
	{
		this.panel.printMessage(message);
	}

	/**
	 * Permet de charger les scores recu du serveur
	 * @param user La liste des utilisateurs
	 * @param score La liste des scores
	 * @param admin Liste de boolean pour indiquer le status d'administrateur
	 */
	public void loadScores(ArrayList<String> user, ArrayList<Integer> score,
			ArrayList<Boolean> admin)
	{
		this.scoresInfo.loadData(user, score, admin);
	}

	/**
	 * Lorsque la vue recoit un message du controleur, elle l'affiche sur le panel
	 * en cours. Est utilise par exemple quand le controleur indiquer une action
	 * impossible (Erreur de creation de compte, mauvaises informations de login, etc.)
	 */
	@Override
	public void update(Observable o, Object arg)
	{
		// Si la vue recoit un message (generalement d'erreur)
		if (String.class.isInstance(arg))
			this.panel.printMessage((String) arg);
	}

}

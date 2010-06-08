package killerbox.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import java.util.*;

import network.*;
import killerbox.*;
import killerbox.network.*;
import killerbox.game.ControllerGame;
import killerbox.game.ModelGame;
import killerbox.gui.panel.*;

import static killerbox.gui.panel.EnumPanel.*;

/**
 * Represente la classe principale de la fenetre. Celle-ci contient
 * differentes informations comme le client, le controleur (KillerBoxController),
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
	 * Le client.
	 */
	private Client client;

	/**
	 * Les differentes donnees. Scores, listes joueurs, etc.
	 */
	private Data data = new Data();

	/**
	 * Controleur. Permet a la fenetre et aux Panels d'envoyer des messages
	 * au serveur.
	 */
	private KillerBoxController controller;

	/**
	 * Id de la partie en cours
	 */
	private int id = 0;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

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
	 * Action pour le bouton et menu "rejoindre partie"
	 */
	private ActionListener actionJoin;

	/**
	 * Action pour le bouton et menu "creer partie"
	 */
	private ActionListener actionCreate;

	/**
	 * Action pour le bouton quitter partie
	 */
	private ActionListener actionQuitGame;

	/**
	 * Modèle de jeu
	 */
	private ModelGame modelGame;

	/**
	 * Controller de jeu
	 */
	private ControllerGame controllerGame;

	/**
	 * Nom du joueur
	 */
	private String nomJoueur;

	/**
	 * Menu principal
	 */
	private JMenuBar menu = new JMenuBar();
	private JMenu fileMenu = new JMenu("Fichier");
	private JMenu gameMenu = new JMenu("Partie");
	private JMenu helpMenu = new JMenu("Aide");

	/**
	 * Items du menu principal
	 */
	private JMenuItem disconnectItem = new JMenuItem("Se deconnecter");
	private JMenuItem quitWindowsItem = new JMenuItem("Quitter");
	private JMenuItem aboutItem = new JMenuItem("A propos de KillerBox");

	private JMenuItem quitGame = new JMenuItem("Quitter la partie");

	/**
	 * Items du menu partie
	 */
	private JMenuItem joinGame = new JMenuItem("Rejoindre la partie");
	private JMenuItem createGame = new JMenuItem("Creer une partie");

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
		this.fileMenu.add(this.quitWindowsItem);
		this.helpMenu.add(this.aboutItem);

		// Construire le menu "Partie"
		quitGame.setEnabled(false);
		joinGame.setEnabled(false);
		createGame.setEnabled(false);
		this.gameMenu.add(this.quitGame);
		this.gameMenu.add(this.joinGame);
		this.gameMenu.add(this.createGame);

		// Panel de connection au demarrage
		this.setPanel(PANEL_CONNECTION);

		// Afficher la fenetre
		this.setVisible(true);

		// Observer les donnees
		this.data.addObserver(this);

		// Creation de l'action de deconnection
		this.actionDisconnect = new ActionListener()
		{
			/**
			 * Lors du clique sur le bouton deconnecter. Envoyer un message au serveur, se
			 * deconnecter,
			 * charger le panel de connexion au serveur et afficher un message comme quoi
			 * l'utilisateur
			 * s'est bien deconnecte.
			 */
			@Override
			public void actionPerformed(ActionEvent arg0)
			{

				if (confirmQuit(BaseWindow.CONFIRM_DISCONNECT_MESSAGE))
				{
					client.send("#logout");
					if (arg0.getSource() == quitGame)
					{
						setPanel(PANEL_SET_ACCOUNT);
					}
					else
					{
						client.disconnect();
						setPanel(PANEL_CONNECTION);
					}
					panel.printMessage(MESSAGE_DECO);
				}

			}
		};

		// Action des items menu
		this.disconnectItem.addActionListener(this.actionDisconnect);

		// Action pour quitter la fenetre
		this.quitWindowsItem.addActionListener(new ActionListener()
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

		this.actionQuitGame = new ActionListener()
		{
			/**
			 * Quand l'utilisateur clique sur le bouton quitter partie
			 */
			@Override
			public void actionPerformed(ActionEvent e)
			{
				//controller.requestQuitGame(id);
				setPanel(PANEL_SET_ACCOUNT);
			}
		};

		// action du quitter dans "Partie"
		this.quitGame.addActionListener(this.actionQuitGame);

		this.actionJoin = new ActionListener()
		{
			/**
			 * Quand l'utilisateur clique pour rejoindre une partie
			 */
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				setPanel(PANEL_JOIN_GAME);
			}
		};

		this.joinGame.addActionListener(this.actionJoin);

		this.actionCreate = new ActionListener()
		{

			/**
			 * Quand l'utilisateur clique sur le bouton pour creer une partie
			 */
			@Override
			public void actionPerformed(ActionEvent e)
			{
				setPanel(PANEL_CREATE_GAME);
			}
		};

		this.createGame.addActionListener(this.actionCreate);

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

		// evenement de creer dans "Partie"

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
	 * Permet de setter l'ID de la partie en cours
	 * @param id L'id de la partie en cours
	 */
	public void setID(int id)
	{
		this.id = id;
	}

	/**
	 * Permet de retourne l'action pour se deconnecter. Permet aux Panels
	 * qui veulent ajouter un bouton "Deconnecter" d'utiliser le meme evenement.
	 * @return L'action pour se deconnecter
	 */
	public ActionListener getActionDisconnect()
	{
		return this.actionDisconnect;
	}

	/**
	 * Permet de retourner l'action pour creer une partie
	 * @return L'action pour creer une partie
	 */
	public ActionListener getActionCreerPartie()
	{
		return this.actionCreate;
	}

	/**
	 * Permet de retourner l'action pour rejoindre une partie
	 * @return L'action pour rejoindr la partie
	 */
	public ActionListener getActionRejoindrePartie()
	{
		return this.actionJoin;
	}

	/**
	 * Permet de retourner le controlleur et permettre aux Panels
	 * d'envoyer des messages au serveur.
	 * @return Le controleur
	 */
	public KillerBoxController getController()
	{
		return controller;
	}

	/**
	 * Permet de retourner les donnees
	 * @return Les donnes
	 */
	public Data getData()
	{
		return this.data;
	}

	/**
	 * Permet de setter le client principal.
	 * @param client Le programme client.
	 */
	public void setClient(Client client)
	{
		this.client = client;

		// Creation de l'ecouteur et du decoder
		this.controller = new KillerBoxController(client, this, new KillerBoxDecoder(
				client, this));

		// Message d'erreur qui peuvent provenir du client
		client.addObserver(this);

		// Le client listener recoit toute les informations recues par le serveur
		client.addObserver(this.controller);

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

		this.createGame.setEnabled(false);
		this.joinGame.setEnabled(false);
		this.quitGame.setEnabled(false);

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
				this.createGame.setEnabled(true);
				this.joinGame.setEnabled(true);
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

			case PANEL_JOIN_GAME:
			{
				this.quitGame.setEnabled(true);
				this.panel = new PanelJoinGame(this);
				break;
			}

			case PANEL_CREATE_GAME:
			{
				this.quitGame.setEnabled(true);
				this.panel = new PanelCreateGame(this);
				break;
			}

			case PANEL_LIST_PLAYERS_GAME_ALL:
			{
				this.panel = new PanelListPlayersGameAll(this, id);
				break;
			}

			case PANEL_LIST_PLAYERS_GAME_ALL_OWNER:
			{
				this.panel = new PanelListPlayersGameAllOwner(this, id);
				break;
			}

			case PANEL_GAME:
			{
				this.quitGame.setEnabled(true);
				this.joinGame.setEnabled(false);
				this.createGame.setEnabled(false);
				this.panel = new PanelGame(this);
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
	 * Lorsque la vue recoit un message du controleur, elle l'affiche sur le panel
	 * en cours. Est utilise par exemple quand le controleur indiquer une action
	 * impossible (Erreur de creation de compte, mauvaises informations de login, etc.)
	 */
	@Override
	public void update(Observable o, Object arg)
	{
		// Indiquer au panel de se refraichir
		this.panel.refreshData();

		// Si la vue recoit un message (generalement d'erreur)
		if (String.class.isInstance(arg))
			this.panel.printMessage((String) arg);
	}

	/**
	 * Permet de setter le modele du jeu
	 * @param modelGame Le modele du jeu
	 */
	public void setModelGame(ModelGame modelGame)
	{
		this.modelGame = modelGame;
	}

	/**
	 * Permet de retourner le modele du jeu
	 * @return Le modele du jeu
	 */
	public ModelGame getModelGame()
	{
		return modelGame;
	}

	/**
	 * Permet de setter le controleur du jeu
	 * @param controllerGame Le controleur du jeu
	 */
	public void setControllerGame(ControllerGame controllerGame)
	{
		this.controllerGame = controllerGame;
	}

	/**
	 * Permer de retourner le controleur du jeu
	 * @return Le controleur du jeu
	 */
	public ControllerGame getControllerGame()
	{
		return controllerGame;
	}

	/**
	 * Permet de retourner la hauteur du menu
	 * @return La hauteur du menu
	 */
	public int getHeightMenu()
	{
		return menu.getHeight();
	}

	/**
	 * Permet de setter le nom du joueur
	 * @param nomJoueur Le nom du joueur
	 */
	public void setNomJoueur(String nomJoueur)
	{
		this.nomJoueur = nomJoueur;
	}

	/**
	 * Permet de retourner le nom du joueur
	 * @return Le nom du joueur
	 */
	public String getNomJoueur()
	{
		return nomJoueur;
	}

	/**
	 * Permet de retourner l'item pour quitter
	 * @return
	 */
	public JMenuItem getQuitGame()
	{
		return quitGame;
	}

	/**
	 * Permet de retourner la hauteur de la fenetre
	 * @return La hauteur de la fenetre
	 */
	public static int getDefaultHeight()
	{
		return HEIGHT;
	}

	/**
	 * Permet de retourner la largeur de la fenetre
	 * @return La largeur de la fenetre
	 */
	public static int getDefaultWidth()
	{
		return WIDTH;
	}

}

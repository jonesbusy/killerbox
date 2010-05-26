package killerbox;

import java.io.*;
import java.sql.*;
import java.util.*;

import network.*;

/**
 * Permet de representer un serveur du jeu KillerBox. Le serveur KillerBox est un modele
 * observable qui genere des messages de type AbstractServerStatus. Le serveur utilise
 * les services de classe reutilisable Server afin de gerer les connexion.
 * 
 * KillerBoxServer est egalement un observateur de la classe Serveur afin
 * d'etre averti d'eventuelles erreur ou messages.
 * 
 * @author Valentin Delaye
 * @version 1.0
 * @see Server
 * @see AbstractServerStatus
 * @see DataBase
 * @see GameList
 */
public class KillerBoxServer extends Observable implements Observer
{
	
	/**
	 * Erreur si le port est deja occupe.
	 */
	private static final String PORT_ERROR = "Un serveur existe deja sur ce numero de port.";
	
	/**
	 * Message d'erreur si la connexion a la base de donnee de echoue
	 */
	private static final String ERROR_DATA_BASE = "Erreur de connexion a la base de donnee.";
	
	/**
	 * Pour donner un petit nom du serveur. Utilise pour les messages de log.
	 */
	private static final String SERVER_NAME = "SERVER";
	
	/**
	 * Donner un nom aux utilisateurs invites. Utilise pour les messages de log.
	 */
	private static final String GUEST_NAME = "GUEST";
	
	/**
	 * Message lorsqu'un utilisateur quitte le serveur
	 */
	private static final String QUIT_SERVER = "quitte le serveur.";
	
	/**
	 * Message lorsqu'un utilisateur rejoint le serveur
	 */
	private static final String JOIN_SERVER = "Rejoint le serveur.";

	/**
	 * Modele de serveur associe.
	 */
	private Server server;

	/**
	 * La base de donnee du serveur.
	 */
	private DataBase database;

	/**
	 * Liste des parties du serveur.
	 */
	private GameList gameList = new GameList();

	/**
	 * Permet de mettre en correspondance les numero de connexion
	 * avec les nom d'utilisateurs.
	 */
	private HashMap<String, Integer> users = new HashMap<String, Integer>();

	/**
	 * Liste des ID de connexion non authentifies
	 * (En attente des informations de loggin)
	 */
	private LinkedList<Integer> unauthenticated = new LinkedList<Integer>();

	/**
	 * Permet de creer un nouveau serveur KillerBox
	 * @param numeroPort Le numero du port ou tourne le serveur
	 * @param decoder Le decodeur (protocole utilise)
	 * @param user Le nom d'utilisateur de la base de donnee (killerbox)
	 * @param pass Le mot de passe de la base de donnee
	 */
	public KillerBoxServer(int numeroPort, KillerBoxDecoder decoder, String user,
			String pass) throws Exception
	{

		try
		{
			this.server = new Server(numeroPort, decoder);
			server.addObserver(this);
			this.database = new killerbox.DataBase("localhost", "killerbox", user, pass);
		}
		
		catch (IOException e)
		{
			System.out.println(PORT_ERROR);
			throw e;
		}
		
		catch (SQLException e)
		{
			System.out.println(ERROR_DATA_BASE);
			throw e;
		}

		// Seter les serveurs du decodeur, ainsi que la base de donnee
		decoder.setServer(this.server);
		decoder.setKillerBoxServer(this);
		decoder.setDataBase(this.database);

	}

	/**
	 * Permet de demarrer le serveur
	 */
	public void start()
	{
		this.server.start();
		setChanged();
		notifyObservers(server.new MessageServerStatus(SERVER_NAME + " : KillerBox v 1.0"));
	}
	
	/**
	 * Permet d'effectuer un broadcast a tout les utilisateurs authentifies.
	 * @param message Le message a envoyer
	 */
	public void broadcastAuthentificated(String message)
	{
		// Pour tout les utilisateur authentifies
		for (Map.Entry<String, Integer> entry : this.users.entrySet())
			this.server.send(entry.getValue(), message);

	}

	/**
	 * Permet d'effectuer un broadcast a toute les connections sur le serveur. Authentifees
	 * ou non
	 * @param message Le message a envoyes
	 */
	public void broadcastAll(String message)
	{
		this.broadcastAuthentificated(message);
		for (Integer i : this.unauthenticated)
			this.server.send(i, message);
	}

	/**
	 * Permet d'envoyer un message a tout les utilisateur d'une partie
	 * @param id Id de la partie
	 * @param message Le message a envoyer
	 */
	public void broadcastGame(int id, String message)
	{
		// Liste des utilisateur a qui envoyer le message
		String[] users = this.gameList.getUsers(id);

		// Pour chaqu'un envoyer le message
		for (String user : users)
			this.send(user, message);
	}

	/**
	 * Permet d'envoyer un message a un certain utilisateur
	 * @param user Le nom d'utilisateur
	 * @param message Le message a envoyer
	 */
	public void send(String user, String message)
	{
		if (this.users.get(user) != null)
			this.server.send(this.users.get(user), message);
	}

	/**
	 * Permet de formater la liste des parties pour l'envoi au client. Separe
	 * toutes les informations par des #
	 */
	public String createGamesForSending()
	{
		StringBuilder builder = new StringBuilder("#game#list#");

		// Donner uniquement les parties en attente. Le client n'a effectivement
		// aucun interet de recevoir une liste de partie dont il n'a pas la possibilite de
		// rejoindre
		Game[] games = this.gameList.getWaitingGames();

		for (Game game : games)
		{
			// Ajouter l'ID de la partie, le proprietaire ainsi que le nombre de joueur
			builder.append(game.getID() + "#");
			builder.append(game.getOwner() + "#");
			builder.append(game.getType() + "#");
			builder.append(game.getNbPlayers() + "#");
		}

		return builder.toString();

	}

	/**
	 * Supprime une connexion non authentifiee du serveur
	 * @param id Id de la connexion
	 */
	private void removeUnauthenticated(int id)
	{
		for (int i = 0; i < this.unauthenticated.size(); i++)
			if (this.unauthenticated.get(i) == id)
			{
				this.gameList.deleteGame(this.getUserName(id));
				this.unauthenticated.remove(i);
				break;
			}
	}

	/**
	 * Permet de changer une connexion authentifiee en connexion
	 * non authentifiee. Par exemple si le client reste connecte sur le serveur,
	 * mais decide de se deconnecter de son compte.
	 * @param ID de la connexion
	 */
	public void setUnauthenticated(int id)
	{
		this.removeLogged(id);
		this.unauthenticated.add(id);
	}

	/**
	 * Supprime une connexion autentifiee du serveur.
	 * @param id Id de la connexion
	 */
	private void removeLogged(int id)
	{
		Collection<Integer> valuesId = this.users.values();
		valuesId.remove(id);
	}

	/**
	 * Retourne le nom d'utilisateur suivant le numero d'une connexion.
	 * @param id ID de la connexion
	 * @return Le nom d'utilisateur, null si l'id de connexion n'existe pas
	 */
	public String getUserName(int id)
	{
		for (Map.Entry<String, Integer> entry : this.users.entrySet())
			if (entry.getValue() == id)
				return entry.getKey();

		return null;
	}

	/**
	 * Retourne l'ID d'une connexion suivant le nom d'utilisateur.
	 * @param username Le nom d'utilisateur
	 * @return L'ID de connexion ou 0 si l'utilisateur n'est pas authentifie sur le serveur
	 */
	public int getId(String username)
	{
		return this.users.get(username);
	}

	/**
	 * Permet de retourner la liste des parties disponibles.
	 * @return La liste des parties disponibles.
	 */
	public GameList getGameList()
	{
		return this.gameList;
	}

	/**
	 * Change le status d'une connexion. Celle-ci devient authentifiee et un nom
	 * d'utilisateur lui est associe.
	 * Si plusieurs connexions on le meme login ou mot de passe, on deconnecte
	 * celle deja connectee. Il n'est donc possible que d'etre connecte, avec son compte
	 * que d'un meme client.
	 * @param username Le nom d'utilisateur
	 * @param id L'ID de connexion
	 */
	public synchronized void setConnected(String username, int id)
	{
		this.removeUnauthenticated(id);

		if (this.users.containsKey(username))
			this.server.disconnect(this.users.get(username));

		// Ajout
		this.users.put(username, id);

		// Changement d'etat, car une connexion change de status
		setChanged();
		notifyObservers(this.server.new MessageServerStatus(username.toUpperCase() + " : est maintenant associe a la connexion " + id));

	}

	/**
	 * Lorsque le Server change d'etat. Le serveur peut nous envoyer des chaines de
	 * caractere (qui sont retransmise aux vues) ou alors un objet StatusConnexion.
	 * Si l'objet est un StatusConnexion, celui-ci est analyser et un message approprie
	 * est envoyes aux observateurs.
	 */
	@Override
	public void update(Observable o, Object obj)
	{

		AbstractServerStatus serverStatus = (AbstractServerStatus) obj;
		int id = serverStatus.getId();
		
		// L'utilisateur
		String user = this.getUserName(id);
		
		switch(serverStatus.status)
		{
			
			// Message d'une connexion
			case ALIVE_CONNECTION :
			{
				// C'est un invite
				if(user == null)
					user = GUEST_NAME + " " + id;
				
				setChanged();
				notifyObservers(this.server.new MessageServerStatus(user.toUpperCase() + " : " + serverStatus));
				
				break;
			}
			
			case NEW_CONNECTION :
			{

				unauthenticated.add(id);
				setChanged();
				notifyObservers(this.server.new MessageServerStatus(GUEST_NAME + " " + id + " : " + JOIN_SERVER));
				break;
			}
			
			// Notification de suppression de connexion
			case REMOVED_CONNECTION :
			{
							
				// C'est un invite
				if(user == null)
				{
					user = GUEST_NAME + " " + id;
					this.removeUnauthenticated(serverStatus.getId());
				}
				
				// C'est un client authentifie
				else
				{
					// Supprimer eventuellement les parties de ce proprietaire
					this.gameList.deleteGame(user);
				
					// Supprimer les connexions
					this.removeLogged(serverStatus.getId());
				}
				
				setChanged();
				notifyObservers(this.server.new MessageServerStatus(user.toUpperCase() + " : " + QUIT_SERVER));
			
				}
			
				break;
				
				// Notification serveur
				case SERVER_STATUS :
				{
					
					setChanged();
					notifyObservers(this.server.new MessageServerStatus(SERVER_NAME + " : " + serverStatus));
					break;
				}
			}
			
		}
	
	/**
	 * Permet d'envoyer un message a tout les utilisateur d'une partie sauf
	 * au créateur de celle-ci
	 * @param id Id de la partie
	 * @param message Le message a envoyer
	 */
	public void broadcastGameNotOwner(int idGame, String message) {
		// Renvoyer à tout le monde de la même partie, sauf 
		// au créateur
		String owner = gameList.getOwner(idGame); 
		
		for (String user : gameList.getUsers(idGame)) {
			if (!user.equals(owner))
				send(user, message);
		}
	}
	
}

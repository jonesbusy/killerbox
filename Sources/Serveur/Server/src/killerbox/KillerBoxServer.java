package killerbox;

import java.sql.SQLException;
import java.util.*;

import network.*;
import network.Server.*;

/**
 * Permet de representer un serveur du jeu KillerBox
 * @author Valentin Delaye
 * 
 */
public class KillerBoxServer extends Observable implements Observer
{

	/**
	 * Message d'erreur si la connexion a la base de donnee de echoue
	 */
	private static String ERROR_DATA_BASE = "Erreur de connexion a la base";

	/**
	 * La base de donnee du serveur
	 */
	private DataBase database;

	/**
	 * Serveur associe.
	 */
	private Server serveur;

	/**
	 * Liste des parties.
	 */
	private GameList gameList = new GameList(this);

	/**
	 * Permet de mettre en correspondance les numero de connexion
	 * avec les nom d'utilisateur
	 */
	private HashMap<String, Integer> users = new HashMap<String, Integer>();

	/**
	 * Liste des ID de connexion non autenfies
	 * (En attente des information de loggin)
	 */
	private LinkedList<Integer> unauthenticated = new LinkedList<Integer>();

	/**
	 * Permet de creer un nouveau serveur KillerBox
	 * @param numeroPort
	 */
	public KillerBoxServer(int numeroPort, KillerBoxDecoder decoder, String user,
			String pass)
	{
		this.gameList.createGame("toto", Game.ALL_VS_ALL);
		this.gameList.createGame("tata", Game.TEAM);
		
		this.serveur = new Server(numeroPort, decoder);
		serveur.addObserver(this);
		try
		{
			this.database = new killerbox.DataBase("localhost", "killerbox", user, pass);
		}
		catch (SQLException e)
		{
			setChanged();
			notifyObservers(ERROR_DATA_BASE);
			System.out.println(ERROR_DATA_BASE);
			System.exit(0);
		}

		// Setet les serveur du decodeur
		decoder.setServer(this.serveur);
		decoder.setKillerBoxServer(this);
		decoder.setDataBase(this.database);

	}

	/**
	 * Permet de demarrer le serveur
	 */
	public void start()
	{
		this.serveur.start();
		setChanged();
		notifyObservers("KillerBox v 1.0");
	}

	/**
	 * Permet d'effectuer un broadcast a tout les utilisateurs authentifies.
	 * @param message Le message a envoyer
	 */
	public void broadcastAuthentificated(String message)
	{
		// Pour tout les utilisateur authentifies
		for (Map.Entry<String, Integer> entry : this.users.entrySet())
			this.serveur.send(entry.getValue(), message);

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
			this.serveur.send(i, message);
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
		for(String user : users)
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
			this.serveur.send(this.users.get(user), message);
	}
	
	/**
	 * Permet d'envoyer la liste des parties disponibles a un utilisateur
	 * @param user Le nom d'utilisateur 
	 */
	public void sendGames(String user)
	{
		StringBuilder builder = new StringBuilder("#game#list#");
		Game[] games = this.gameList.getGames();
		for(Game game : games)
		{
			// Ajouter l'ID de la partie, le proprietaire ainsi que le nombre de joueur
			builder.append(game.getID() + "#");
			builder.append(game.getOwner() + "#");
			builder.append(game.getType() + "#");
			builder.append(game.getNbPlayers() + "#");
		}
		
		this.send(user, builder.toString());
		
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
	public void setConnected(String username, int id)
	{
		this.removeUnauthenticated(id);

		if (this.users.containsKey(username))
			this.serveur.disconnect(this.users.get(username));

		// Ajout
		this.users.put(username, id);

		// Changement d'etat, car une connexion change de status
		setChanged();
		notifyObservers(username + " est maintenant associe a la connexion : " + id);

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
		// S'il y a une nouvelle connexion ou un utilisateur deconnecte
		if (Server.StatusConnexion.class.isInstance(obj))
		{
			StatusConnexion status = (StatusConnexion) obj;

			// Recuperer la derniere connexion
			Connexion connexion = this.serveur.getLastConnexion();

			// Nouvelle connexion
			if (status.isNew())
			{
				unauthenticated.add(connexion.getId());

				setChanged();
				notifyObservers("Nouvelle connexion ID : " + connexion.getId());

			}

			// Connexion supprimee
			else
			{
				
				// Supprimer eventuellement les parties de ce proprietaire
				this.gameList.deleteGame(this.getUserName(connexion.getId()));

				// Supprimer les connexions
				this.removeUnauthenticated(status.getId());
				this.removeLogged(status.getId());
				
				setChanged();
				notifyObservers("Connexion supprimee ID : " + status.getId());

			}

		}

		// Sinon c'est un message du serveur, on ne fait que le retransmettre
		else
		{
			setChanged();
			notifyObservers(obj);
		}
	}

}

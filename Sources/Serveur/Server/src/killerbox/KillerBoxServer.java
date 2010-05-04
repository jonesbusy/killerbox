package killerbox;

import java.util.*;
import network.*;
import network.Server.*;


/**
 * Permet de representer un serveur du jeu KillerBox
 * @author Valentin Delaye
 *
 */
public class KillerBoxServer extends Observable implements  Observer
{
	
	/**
	 * La base de donnee du serveur
	 */
	private KillerBoxDataBase database;
	
	/**
	 * Serveur associe
	 */
	private Server serveur;
	
	/**
	 * Permet de mettre en correspondance les numero de connexion
	 * avec les nom d'utilisateur
	 */
	private HashMap<String, Integer> login = new HashMap<String, Integer>();
	
	/**
	 * Liste des ID de connexion non autenfies
	 * (En attente des information de loggin)
	 */
	private LinkedList<Integer> unauthenticated = new LinkedList<Integer>();
	
	/**
	 * Permet de creer un nouveau serveur KillerBox
	 * @param numeroPort
	 */
	public KillerBoxServer(int numeroPort, KillerBoxDecoder decoder, String user, String pass)
	{
		this.serveur = new Server(numeroPort, decoder);
		serveur.addObserver(this);
		try
		{
			this.database = new KillerBoxDataBase("localhost", "killerbox", user, pass);
		}
		catch (Exception e)
		{
			System.out.println("Erreur de connexion a la base");			
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
	 * Permet d'effectuer un broadcast a tout les utilisateur connectes
	 * @param message Le message
	 */
	public void broadcast(String message)
	{
		// Pour tout les utilisateur connectes
		for (Map.Entry<String, Integer> entry : this.login.entrySet())
				this.serveur.send(entry.getValue(), message);
				
	}
	
	/**
	 * Permet d'effectuer un broadcast a toute les connections. Authentifees
	 * ou non
	 * @param message
	 */
	public void broadcastAny(String message)
	{
		this.broadcast(message);
		for(Integer i : this.unauthenticated)
			this.serveur.send(i, message);
	}
	
	/**
	 * Permet d'envoyer un message a un certain utilisateur
	 * @param login
	 * @param message
	 */
	public void send(String login, String message)
	{
		if(this.login.get(login) != null)
			this.serveur.send(this.login.get(login), message);
	}
	
	/**
	 * Supprime une connexion non autentifie du serveur
	 * @param id Id de la connexion
	 */
	private void removeUnauthenticated(int id)
	{
		for(int i = 0 ; i < this.unauthenticated.size() ; i++)
			if(this.unauthenticated.get(i) == id)
			{
				this.unauthenticated.remove(i);
				break;
			}
	}
	
	/**
	 * Supprime une connexion autentifiee du serveur
	 * @param id Id de la connexion
	 */
	private void removeLogged(int id)
	{
		Collection<Integer> valuesId = this.login.values();
		valuesId.remove(id);
	}
	
	/**
	 * Retourne le nom d'utilisateur suivant le numero d'une connexion
	 * @param id Id de la connexion
	 * @return Le nom d'utilisateur, null si l'id de connexion n'existe pas
	 */
	public String getUserName(int id)
	{
		for(Map.Entry<String, Integer> entry : this.login.entrySet())
			if(entry.getValue() == id)
				return entry.getKey();
		
		return null;
	}
	
	/**
	 * Retourne l'ID d'une connexion suivant le nom d'utilisateur
	 * @param username Le nom d'utilisateur
	 * @return L'ID de connexion ou 0 si l'utilisateur n'est pas connecte au serveur
	 */
	public int getId(String username)
	{
		return this.login.get(username);
	}
	
	/**
	 * Ajouter un utilisateur connecte et authentifie
	 * @param username
	 * @param id
	 */
	public void addConnected(String username, int id)
	{
		this.removeUnauthenticated(id);
		
		// Si plusieurs connexions on le meme login ou mot de passe, on deconnecte
		// celle deja connectee
		if(this.login.containsKey(username))
			this.serveur.disconnect(this.login.get(username));
		
		// Ajout
		this.login.put(username, id);
		
		setChanged();
		notifyObservers(username + " est maintenant associe a la connexion : " + id);
		
		// Broadcaster tout le monde
		this.broadcastAny(username + " rejoint la partie");
		
	}
	
	/**
	 * Lorsque le Server change d'etat
	 */
	@Override
	public void update(Observable o, Object obj)
	{
		// S'il y a une nouvelle connexion ou un utilisateur deconnecte
		if(Server.StatusConnexion.class.isInstance(obj))
		{
			StatusConnexion status = (StatusConnexion)obj;
			
			// Recuperer la derniere connexion
			Connexion connexion = this.serveur.getLastConnexion();
			
			// Nouvelle connexion
			if(status.isNew())
			{				
				unauthenticated.add(connexion.getId());
				
				setChanged();
				notifyObservers("Nouvelle connexion ID : " + connexion.getId());
				
			}
			
			// Connexion supprimee
			else
			{
				
				String username = this.getUserName(status.getId());
				
				// Supprimer les connexions
				removeUnauthenticated(status.getId());
				removeLogged(status.getId());
				
				setChanged();
				notifyObservers("connexion supprimee ID : " + status.getId());
				
				if(username != null)
					this.broadcastAny(username + " : s'est deconnecte");
				else
					this.broadcastAny("guest" + status.getId() + " : s'est deconnecte");
				
			}
			
		}
		
		// Sinon c'est un message du serveur, on ne fait que le retransmettre
		else
		{
			setChanged();
			notifyObservers((String)obj);
		}
	}

}

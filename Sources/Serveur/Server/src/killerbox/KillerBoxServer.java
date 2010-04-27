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
	public KillerBoxServer(int numeroPort, Decoder decoder)
	{
		this.serveur = new Server(numeroPort, decoder);
		decoder.setServer(this.serveur);
		serveur.addObserver(this);
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
		java.util.Collection<Integer> valuesId = this.login.values();
		valuesId.remove(id);
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
			
			// On attend son nom d'utilisateur
			Connexion connexion = this.serveur.getLastConnexion();
			
			// Nouvelle connexion
			if(status.isNew())
			{				
				unauthenticated.add(connexion.getId());
				setChanged();
				notifyObservers("Nouvelle connexion ID : " + connexion.getId());
				
			}
			
			else
			{
				
				// Supprimer les connexion
				removeUnauthenticated(status.getId());
				removeLogged(status.getId());
				
				setChanged();
				notifyObservers("connexion supprimee ID : " + connexion.getId());
				
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

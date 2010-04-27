package network;

import java.io.*;
import java.net.*;
import java.util.*;


/**
 * Server
 * @author Valentin Delaye
 * @version 1.0
 */
public class Server extends Observable implements Runnable, Observer
{
	/**
	 * ID courant de connexion
	 */
	private static int ID = 1;
	
	
	/**
	 * Le decoder du serveur
	 */
	private Decoder decoder;
	
	/**
	 * Permettre de nettoyer les connexions innactives
	 * du serveur
	 * @author Valentin Delaye
	 */
	protected class ConnexionRemover implements Runnable
	{
		// Le thread associe au ConnexionRemover
		Thread activity;

		/**
		 * Consructeur
		 */
		public ConnexionRemover()
		{
			this.activity = new Thread(this);
			this.activity.start();
		}

		/**
		 * Action executee par le thread
		 */
		@Override
		public void run()
		{
			while (true)
			{
				try
				{
					// Toute les 1 secondes faire un purge de toute les connexions
					synchronized (this)
					{
						this.wait(1000);
						this.purge();
					}
				}
				catch (InterruptedException e)
				{
					
				}
			}
		}

		/**
		 * Supprime les connexions innactives
		 */
		public void purge()
		{
			for (int i = 0; i < connexions.size(); i++)

				// Le thread n'est plus actif on le detruit donc
				if (!connexions.get(i).isAlive())
				{
					connexions.get(i).kill();
					setChanged();
					notifyObservers("La connexion " + connexions.get(i).getId() + " s'est deconnecte");
					connexions.remove(i);
				}
		}

	}

	/**
	 * Numero de port du serveur
	 */
	int numeroPort;

	/**
	 * Socket du server
	 */
	private ServerSocket serverSocket;

	/**
	 * La liste des connexions
	 */
	private ArrayList<Connexion> connexions = new ArrayList<Connexion>();
	
	//private HashMap<Integer, Connexion> connexions = new HashMap<Integer, Connexion>();

	/**
	 * Le thread associe au serveur. Celui qui recupere
	 * les demandes de connexions.
	 */
	private Thread activite;

	/**
	 * Permet de creer un nouveau serveur
	 * @param numeroPort Le numero de port sur lequel tourne le serveur
	 */
	public Server(int numeroPort, Decoder decoder)
	{
		try
		{
			this.serverSocket = new ServerSocket(numeroPort);
		}
		
		catch (IOException e)
		{
			System.out.println("Un serveur existe deja sur ce numero de port.");
			System.exit(1);
		}

		this.numeroPort = numeroPort;
		
		this.decoder = decoder;
		
		new ConnexionRemover();

		this.activite = new Thread(this);
	}

	/**
	 * Permet de demarrer le serveur
	 */
	public void start()
	{
		setChanged();
		notifyObservers("Serveur demarre");
		activite.start();
	}

	/**
	 * Permet de retourner le nombre de client connectes au serveurs
	 * @return
	 */
	public int getNombreClient()
	{
		return connexions.size();
	}
	
	/**
	 * Permet de retourner la derniere connexion recu par 
	 * le serveur
	 * @return La dernier connexion
	 */
	public Connexion getLastConnexion()
	{
		return this.connexions.get(this.getNombreClient() - 1);
	}
	
	
	/**
	 * Permet transferer un message au serveur
	 * @param message Le message
	 */
	public void sendMessage(String message)
	{
		broadcast(message);
		setChanged();
		notifyObservers(message);
	}
	
	/**
	 * Permet d'envoyer un message a tout les clients
	 * @param message Le message
	 */
	public void broadcast(String message)
	{
		for(Connexion co : connexions)
			co.send(message);
	}
	
	/**
	 * Permet de deconnecter une connexion
	 * @param id L'id de la connexion
	 */
	public void disconnect(int id)
	{
		for(int i = 0 ; i < this.getNombreClient() ; i++)
			if (this.connexions.get(i).getId() == id)
			{
				this.connexions.get(i).close();
				return;
			}
	}
	
	/**
	 * Permet de deconnecter tout les client
	 */
	public void disconnectAll()
	{
		for(int i = 0 ; i < this.getNombreClient() ; i++)
			this.connexions.get(i).close();
	}
	
	

	/**
	 * Permet d'executer le thread et recuperer les connexions
	 */
	@Override
	public void run()
	{
		while (true)
		{
			try
			{
				Server.ID++;
				Connexion connexion = new Connexion(serverSocket.accept(), Server.ID, this, this.decoder);
				connexions.add(connexion);
				setChanged();
				notifyObservers(true);
			}

			catch (IOException e)
			{
				
			}
		}
	}
	

	/**
	 * Lorsque le statut d'une connexion change.
	 * Met a jour les vues correspondantes
	 */
	public void update(Observable o, Object arg)
	{
		setChanged();
		notifyObservers((String)arg);
	}

}

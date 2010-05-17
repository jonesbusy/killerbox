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
	private static int ID = 0;

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
					notifyObservers(new StatusConnexion(connexions.get(i).getId(), false));
					connexions.remove(i);
				}
		}

	}

	/**
	 * Permet de representer le nouveau status d'une connexion
	 * @author Valentin Delaye
	 */
	public class StatusConnexion
	{

		/**
		 * Id de connexion
		 */
		private int id;

		/**
		 * Status de la connexion
		 */
		private boolean status;

		/**
		 * Constructeur
		 * @param id Identificateur de la connexion
		 * @param status Le status. True, c'est une nouvelle connexion sinon False
		 *        si la connexion est annulee
		 */
		public StatusConnexion(int id, boolean status)
		{
			this.id = id;
			this.status = status;
		}

		/**
		 * Indique si c'est une nouvelle connexion ou non
		 * @return Le status de la connexion
		 */
		public boolean isNew()
		{
			return this.status;
		}

		/***
		 * Retourne l'id de la connexion
		 * @return L'id de la connexion
		 */
		public int getId()
		{
			return this.id;
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
	private LinkedList<Connexion> connexions = new LinkedList<Connexion>();

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
	 * @return La derniere connexion
	 */
	public synchronized Connexion getLastConnexion()
	{
		return this.connexions.get(this.getNombreClient() - 1);
	}

	/**
	 * Permet transferer un message non decryptable au serveur.
	 * Celui ci averti ces observers pour un traitement. (Log, affichage, etc...)
	 * @param message Le message
	 */
	public void relay(String message)
	{
		setChanged();
		notifyObservers(message);
	}

	/**
	 * Permet d'envoyer un message a tout les clients
	 * @param message Le message
	 */
	public void broadcast(String message)
	{
		for (Connexion co : connexions)
			co.send(message);
	}
	
	public void send(int id, String message)
	{
		// Envoyer le message a l'ID
		for (Connexion co : connexions)
			if(co.getId() == id)
			{
				co.send(message);
				break;
			}
	}

	/**
	 * Permet de deconnecter une connexion
	 * @param id L'id de la connexion
	 */
	public synchronized void disconnect(int id)
	{
		for (int i = 0; i < this.getNombreClient(); i++)
			if (this.connexions.get(i).getId() == id)
			{
				this.connexions.get(i).close();
				this.connexions.get(i).kill();
				return;
			}
	}

	/**
	 * Permet de deconnecter tout les client
	 */
	public synchronized void disconnectAll()
	{
		for (int i = 0; i < this.getNombreClient(); i++)
			this.connexions.get(i).close();
	}

	/**
	 * Permet d'executer le thread et recuperer les connexions
	 */
	public void run()
	{
		while (true)
		{
			try
			{
				synchronized (Server.class)
				{
					Server.ID++;	
				}
				
				Connexion connexion = new Connexion(serverSocket.accept(), Server.ID,
						this.decoder);
				
				synchronized (this)
				{
					connexions.add(connexion);
				}


				setChanged();
				notifyObservers(new StatusConnexion(Server.ID, true));

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
		notifyObservers((String) arg);
	}

}

package network;

import java.io.*;
import java.net.*;
import java.util.*;

import killerbox.KillerBoxController;

import static network.EnumServerStatus.*;

/**
 * Server. Permet de creer un nouveau serveur. Un serveur
 * est gere en tant d'objet observble par differentes vues.
 * @author Valentin Delaye
 * @version 1.0
 */
public class Server extends Observable implements Runnable
{

	/**
	 * Permettre de nettoyer les connexions innactives
	 * du serveur.
	 * @author Valentin Delaye
	 * @version 1.0
	 * @see Server
	 */
	private class ConnectionRemover implements Runnable
	{
		// Le thread associe au ConnexionRemover
		Thread activity;

		/**
		 * Consructeur
		 */
		public ConnectionRemover()
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
						this.wait(REMOVER_TIME);
						this.purge();
					}
				}
				catch (InterruptedException e)
				{

				}
			}
		}

		/**
		 * Supprime les connexions innactives.
		 */
		public synchronized void purge()
		{
			Collection<Controller> values = connections.values();
			Iterator<Controller> it = values.iterator();
			
			Controller co = null;
			while(it.hasNext())
			{
				co = it.next();
				// Le thread n'est plus actif on tue la connexion et on 
				// la supprime
				if (!co.isAlive())
				{
					co.kill();
					setChanged();
					notifyObservers(new MessageServerStatus(co.getId(), REMOVED_CONNECTION));
					it.remove();
				}
			}
		}

	}
	
	/**
	 * Permet de representer un simple status message serveur. Celui-ci
	 * est simplement representer par un message.
	 * @author Valentin Delaye
	 * @version 1.0
	 * @see AbstractServerStatus
	 */
	public class MessageServerStatus extends AbstractServerStatus
	{

		/**
		 * Permet de creer un nouveau message serveur.
		 * @param message Message du serveur.
		 */
		public MessageServerStatus(String message)
		{
			super(0, SERVER_STATUS, message);
		}
		
		/**
		 * Constructeur. Permet de construire un nouveau status sans message
		 * @param id ID de la connexion
		 * @param status Genre de status
		 */
		public MessageServerStatus(int id, EnumServerStatus status)
		{
			super(id, status);
		}
		
		/**
		 * Permet de creer un nouveau message de serveur
		 * @param id ID de la connexion
		 * @param status Status de la connexion
		 * @param message Message de la connexion
		 */
		public MessageServerStatus(int id, EnumServerStatus status, String message)
		{
			super(id, status, message);
		}
		
	}
		
	/**
	 * ID courant de connexion.
	 */
	private static int ID = 0;

	/**
	 * Le decoder du serveur.
	 */
	private Decoder decoder;

	/**
	 * Message d'affichage lorsque le serveur demarre.
	 */
	private static final String START_MESSAGE = "demarrage en cours...";

	/**
	 * Indique tout le combien de temps le thread notoyeur de connexion
	 * s'execute.
	 */
	private static final int REMOVER_TIME = 2000;

	/**
	 * Numero de port du serveur
	 */
	int portNumber;

	/**
	 * Socket du server
	 */
	private ServerSocket serverSocket;

	/**
	 * La liste des Connexions.
	 */
	private HashMap<Integer, Controller> connections = new HashMap<Integer, Controller>();

	/**
	 * Le thread associe au serveur. Celui qui recupere
	 * les demandes de connexions.
	 */
	private Thread activity;

	/**
	 * Permet de creer un nouveau serveur KillerBox
	 * @param portNumber Le numero du port ou tourne le serveur
	 * @param decoder Le decodeur (protocole utilise)
	 * @throws IOException Si le port est deja occupe
	 */
	public Server(int portNumber, Decoder decoder) throws IOException
	{
		this.serverSocket = new ServerSocket(portNumber);
		this.portNumber = portNumber;
		this.decoder = decoder;
		new ConnectionRemover();
		this.activity = new Thread(this);
	}

	/**
	 * Permet de demarrer le serveur
	 */
	public void start()
	{
		setChanged();
		notifyObservers(new MessageServerStatus(START_MESSAGE));
		activity.start();
	}

	/**
	 * Permet de retourner le nombre de client connectes au serveur.
	 * @return Le nombre de connexions
	 */
	public int getNbConnection()
	{
		return connections.size();
	}

	/**
	 * Permet transferer un message non decryptable au serveur.
	 * Celui ci averti ces observers pour un traitement. (Log, affichage, etc...)
	 * @param id ID de la connexion qui a genere le message
	 * @param message Le message
	 */
	public void relay(int id, String message)
	{
		setChanged();
		notifyObservers(new MessageServerStatus(id, ALIVE_CONNECTION, message));
	}

	/**
	 * Permet d'envoyer un message a tout les clients connectes.
	 * @param message Le message
	 */
	public void broadcast(String message)
	{
		for (Controller c : this.connections.values())
			c.send(message);
	}

	/**
	 * Permet d'envoyer un message a un client donne
	 * @param id ID du client
	 * @param message Le message a lui envoyer
	 */
	public void send(int id, String message)
	{
		Controller co = this.connections.get(id);
		if(co != null)
			co.send(message);
	}

	/**
	 * Permet de deconnecter une connexion
	 * @param id L'id de la connexion
	 */
	public synchronized void disconnect(int id)
	{
		Controller co = this.connections.get(id);
		if(co != null)
			co.close();
	}

	/**
	 * Permet de deconnecter tout les clients.
	 */
	public synchronized void disconnectAll()
	{
		for(Controller co : this.connections.values())
			co.close();
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
				
				synchronized (Server.class)
				{
					Server.ID++;
				}

				KillerBoxController connexion = new KillerBoxController(serverSocket.accept(), Server.ID,
						this.decoder);

				synchronized (this)
				{
					connections.put(Server.ID, connexion);
				}

				// Envoyer le nouveau status (nouvelle connexion)
				setChanged();
				notifyObservers(new MessageServerStatus(Server.ID, NEW_CONNECTION));

			}

			catch (IOException e)
			{

			}
		}
	}

}

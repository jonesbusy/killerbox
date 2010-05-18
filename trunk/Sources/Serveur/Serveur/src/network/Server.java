package network;

import java.io.*;
import java.net.*;
import java.util.*;

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
	 * ID courant de connexion.
	 */
	private static int ID = 0;

	/**
	 * Le decoder du serveur.
	 */
	private Decoder decoder;

	/**
	 * Erreur si le port est deja occupe.
	 */
	private static final String PORT_ERROR = "Un serveur existe deja sur ce numero de port";

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
			for (int i = 0; i < connexions.size(); i++)

				// Le thread n'est plus actif on le detruit donc
				if (!connexions.get(i).isAlive())
				{
					connexions.get(i).kill();
					setChanged();
					notifyObservers(new ConnectionStatus(connexions.get(i).getId(), REMOVED_CONNECTION));
					connexions.remove(i);
				}
		}

	}

	/**
	 * Permet de representer un status d'une connexion. L'objet est
	 * representer par l'ID de connexion ainsi que le nouveau status (nouveau, ancien).
	 * Cela permet d'avertir les vues qu'il y a une nouvelle connexion ou qu'une connexion
	 * a ete supprimee.
	 * @author Valentin Delaye
	 * @version 1.0
	 * @see AbstractServerStatus
	 */
	public class ConnectionStatus extends AbstractServerStatus
	{

		/**
		 * Constructeur.
		 * @param id Identificateur de la connexion
		 * @param alive Le status. True, c'est une nouvelle connexion sinon False si la
		 *        connexion est annulee.
		 */
		public ConnectionStatus(int id, EnumServerStatus alive)
		{
			super(id, alive);
		}

	}
	
	/**
	 * Permet de representer un simple status message.
	 * @author Valentin Delaye
	 * @version 1.0
	 * @see AbstractServerStatus
	 */
	public class MessageStatus extends AbstractServerStatus
	{

		/**
		 * Permet de creer un nouveau message serveur.
		 * @param message Message du serveur.
		 */
		public MessageStatus(String message)
		{
			super(0, SERVER_STATUS, message);
		}
		
	}

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
	private LinkedList<Controller> connexions = new LinkedList<Controller>();

	/**
	 * Le thread associe au serveur. Celui qui recupere
	 * les demandes de connexions.
	 */
	private Thread activity;

	/**
	 * Permet de creer un nouveau serveur KillerBox
	 * @param numeroPort Le numero du port ou tourne le serveur
	 * @param decoder Le decodeur (protocole utilise)
	 */
	public Server(int numeroPort, Decoder decoder)
	{
		try
		{
			this.serverSocket = new ServerSocket(numeroPort);
		}

		catch (IOException e)
		{

			// Afficher les informations d'erreur sur les vus.
			setChanged();
			notifyObservers(new MessageStatus(PORT_ERROR));
			System.out.println(PORT_ERROR);

			System.exit(1);
		}

		this.portNumber = numeroPort;

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
		notifyObservers(new MessageStatus(START_MESSAGE));
		activity.start();
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
	 * Permet d'envoyer un message a tout les clients connectes.
	 * @param message Le message
	 */
	public void broadcast(String message)
	{
		for (Controller co : connexions)
			co.send(message);
	}

	/**
	 * Permet d'envoyer un message a un client donne
	 * @param id ID du client
	 * @param message Le message a lui envoyer
	 */
	public void send(int id, String message)
	{
		// Envoyer le message a l'ID
		for (Controller co : connexions)
			if (co.getId() == id)
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

				Controller connexion = new Controller(serverSocket.accept(), Server.ID,
						this.decoder);

				synchronized (this)
				{
					connexions.add(connexion);
				}

				// Envoyer le nouveau status (nouvelle connexion)
				setChanged();
				notifyObservers(new ConnectionStatus(Server.ID, NEW_CONNECTION));

			}

			catch (IOException e)
			{

			}
		}
	}

}

package network;

import java.io.*;
import java.util.*;

/**
 * Permet de fournir quelques methodes afin d'ecouter les differents message
 * provenant au serveur. Les message sont ensuite decoder par le biais
 * d'un decoder abstrait.
 * 
 * La classe est abstraite et les methodes suivantes doivent etre 
 * redefinies :
 * 
 * 	- setDeconnected() : pour indiquer ce qu'il se passe dans l'ecouteur
 * detecte une deconnexion brutale.
 * 
 * L'ecouteur est un thread qui implemente egalement l'interface Observer
 * afin d'observer les messages provenant du programme client. C'est a dire
 * que l'ecouteur commence a ecouteur les message provenant du serveur
 * uniquement si le client passe en mode "Connecte".
 * 
 * @author Valentin Delaye
 * @version 1.0
 * @see Client
 * @see Decoder
 */
public abstract class Controller implements Observer, Runnable
{
	
	/**
	 * Reference sur le client
	 */
	protected Client client;
	
	/**
	 * Le decodeur de message
	 */
	protected Decoder decoder;
	
	/**
	 * Thread associe a l'ecouteur
	 */
	private Thread activite;
	
	/**
	 * Flux d'entree des messages.
	 */
	protected BufferedReader input;
	
	/**
	 * Constructeur. Permet de creer un nouvel ecouteur.
	 * @param client Le client associe
	 * @param decoder Le decoder
	 */
	public Controller(Client client, Decoder decoder)
	{
		this.client = client;
		this.decoder = decoder;
	}
	
	
	/**
	 * Executer le thread.
	 * S'occupe de lire le message recu et d'appeler le decoder
	 * sur cette ligne.
	 */
	@Override
	public void run()
	{
		String ligne = null;

		// Toujours lire
		while (true)
		{
			try
			{
				ligne = input.readLine();

				if (ligne == null)
					throw new IOException();
				
				// --- Message debug pour afficher les messages recu dans la console ---
				System.out.println("Server : " + ligne);
				
				// Decoder la ligne
				this.decoder.decode(ligne);

			}

			// Deconnection de la part du serveur
			catch (IOException e)
			{
				this.setDeconnected();
				break;
			}

		}
	}
	
	/**
	 * Action lorsque l'ecouteur n'obtient plus d'informations du serveur.
	 * Generalement lors d'une deconnexion
	 */
	public abstract void setDeconnected();
	

	@Override
	public void update(Observable o, Object arg)
	{
		if (Boolean.class.isInstance(arg))
		{
			boolean status = (Boolean) arg;

			// Le client passe en status connecte. On demarre le thread
			// d'ecoute
			if (status)
			{
				this.input = this.client.getBufferedReader();
				this.activite = new Thread(this);
				this.activite.start();
			}

		}
	}

}

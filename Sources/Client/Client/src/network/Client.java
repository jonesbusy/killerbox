package network;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Permet de representer un modele de client. Concretement un client
 * est un socket associe a un socket serveur. Le client permet d'effectuer
 * des operations de bases tel que d'envoyer une chaine de caractere au serveur.
 * 
 * Un client est un objet Observable car il peut changer de status durant le deroulement
 * du programme. En effet, un client peut passer d'un status "Connecte" a un status
 * "Deconnecte". Ce modele de client se veut reutilisable dans d'autre application
 * reseau.
 * 
 * @author Valentin Delaye
 * @version 1.0
 */
public class Client extends Observable
{

	/**
	 * Socket du client.
	 */
	private Socket socket;

	/**
	 * Indique si le client est connecte a un serveur ou non.
	 */
	private boolean connected = false;

	/**
	 * Port ou est connecte le client.
	 */
	private int portNumber;

	/**
	 * Adresse ou est connecte le client
	 */
	private String adresse;

	/**
	 * Canal de sortie (Client vers serveur)
	 */
	private PrintWriter output;

	/**
	 * Canal d'entree (Serveur vers client)
	 */
	private BufferedReader input;

	/**
	 * Constructeur. Permet de creer un nouveau client.
	 * @param adresse Adresse du serveur
	 * @param portNumber Numero de port du serveur
	 */
	public Client(String adresse, int portNumber)
	{
		this.adresse = adresse;
		this.portNumber = portNumber;
	}

	/**
	 * Permet a un client de se connecter sur un serveur.
	 * @return True connexion ok, false connexion impossible
	 */
	public boolean connect()
	{
		InetAddress adresseServer = null;

		try
		{
			adresseServer = InetAddress.getByName(this.adresse);
		}

		catch (UnknownHostException e)
		{
			return false;
		}

		try
		{
			this.socket = new Socket(adresseServer, this.portNumber);

			// Creation des flux
			this.output = new PrintWriter(this.socket.getOutputStream());
			this.input = new BufferedReader(new InputStreamReader(this.socket
					.getInputStream()));

			this.connected = true;
			
			// Le client passe en status connecte
			setChanged();
			notifyObservers(true);

			return true;
		}

		catch (ConnectException e)
		{
			return false;
		}

		catch (IOException e)
		{
			return false;
		}
	}

	/**
	 * Permet d'envoyer un message au serveur.
	 * @param message Le texte a envoyer
	 */
	public void send(String message)
	{
		// -- Pour le debug, afficher sur la console --
		System.out.println("SEND : " + message);
		
		if(this.output != null)
		{
			this.output.println(message);
			this.output.flush();
		}
	}

	/**
	 * Permet de deconnecter le client. Ferme les ressource et passe
	 * en status deconnecte.
	 */
	public void disconnect()
	{
		this.close();
		this.connected = false;
	}

	/**
	 * Indique si le client est connecte
	 * @return True le client est connecte, false sinon.
	 */
	public boolean isConnected()
	{
		return this.connected;
	}
	
	/**
	 * Retourne le flux d'entree. Permet par exemple, pour un controleur
	 * d'analyser les message arrivant au client.
	 * @return Le flux d'entree
	 */
	public BufferedReader getBufferedReader()
	{
		return this.input;
	}

	/**
	 * Permet de fermer le socket et le flux de sortie. 
	 * Averti les observateur de la deconnection.
	 */
	private void close()
	{
		try
		{
			this.socket.close();
			this.input.close();
		}
		catch (IOException e)
		{

		}

		finally
		{
			setChanged();
			notifyObservers(false);
		}
	}

	/**
	 * Ferme proprement les ressources a la destruction.
	 * de l'objet.
	 */
	@Override
	protected void finalize() throws Throwable
	{
		super.finalize();
		this.close();
	}

}

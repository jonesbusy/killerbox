import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Permet de representer un modele de client
 * @author Valentin Delaye
 * @version 1.0
 */
public class Client extends Observable
{

	/**
	 * Socket du client
	 */
	private Socket socket;

	/**
	 * Indique si le client est connecte a un serveur ou non
	 */
	private boolean connected = false;

	/**
	 * Adresse ou est connecte le client
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
	 * Permet de creer un nouveau client
	 * @param adresse Adresse du serveur
	 * @param numeroPort Numero de port du serveur
	 */
	public Client(String adresse, int numeroPort)
	{
		this.adresse = adresse;
		this.portNumber = numeroPort;
	}

	/**
	 * Permet a un client de se connecter sur un serveur
	 * @param adresse
	 * @param portNumber
	 * @return
	 * @throws IOException
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
			setChanged();
			notifyObservers("Impossible de se connecter au server.");
		}

		try
		{
			this.socket = new Socket(adresseServer, this.portNumber);

			this.output = new PrintWriter(this.socket.getOutputStream());
			this.input = new BufferedReader(new InputStreamReader(this.socket
					.getInputStream()));

			this.connected = true;
			setChanged();
			notifyObservers(true);

			return true;
		}

		catch (ConnectException e)
		{
			setChanged();
			notifyObservers("Impossible de se connecter au server.");
			return false;
		}

		catch (IOException e)
		{
			return false;
		}
	}

	/**
	 * Permet d'envoyer un message au serveur
	 * @param message Le texte a envoyer
	 */
	public void send(String message)
	{
		this.output.println(message);
		this.output.flush();
	}

	/**
	 * Envoie une information de deconnection au serveur
	 */
	public void disconnect()
	{
		send("@logout");
		this.close();
		this.connected = false;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isConnected()
	{
		return this.connected;
	}

	/**
	 * Retourne le flux d'entree
	 * @return Le flux d'entree
	 */
	public BufferedReader getBufferedReader()
	{
		return this.input;
	}

	/**
	 * Permet de fermer le socket et le flux de sortie
	 */
	public void close()
	{
		try
		{
			this.socket.close();
			this.input.close();
		}
		catch (IOException e)
		{
			setChanged();
			notifyObservers("Erreur de fermeture du client");
		}

		finally
		{
			setChanged();
			notifyObservers(false);
		}
	}

	/**
	 * 
	 */
	@Override
	protected void finalize() throws Throwable
	{
		super.finalize();
		this.close();
	}

}

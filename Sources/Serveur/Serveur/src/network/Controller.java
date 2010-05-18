package network;

import java.net.*;
import java.io.*;


/**
 * Permet de representer une connexion entre un client
 * et le serveur. Permet egalement de recuperer les differents
 * messages recus du client et de le passer au decodeur.
 * 
 * @author Valentin Delaye
 * @version 1.0
 */
public abstract class Controller implements Runnable
{
	
	/**
	 * Id de la connexion
	 */
	protected int id;
	
	/**
	 * Thread associe a la connexion
	 */
	private Thread thread;
	
	/**
	 * Le socket client associe
	 */
	private Socket socket;
	
	/**
	 * Le decodeur de connexion
	 */
	protected Decoder decoder;
	
	/**
	 * Flux d'entree provenant du client.
	 */
	private BufferedReader input;
	
	/**
	 * Flux des messages du serveur vers le client.
	 */
	private PrintWriter output;
	
	/**
	 * Pour indiquer l'etat de la connexion. Tant que la connexion
	 * est en vie, le thread continue son execution.
	 */
	private boolean running;
	
	/**
	 * Permet de creer une nouvelle connexion
	 * @param socket Le socket de connexion
	 * @param id Id de la connexion
	 * @param Le serveur ou est ratachee la connexion
	 * @throws IOException Si erreur d'entree sortie avec le flux d'entree
	 */
	public Controller(Socket socket, int id, Decoder decoder) throws IOException
	{
		this.socket = socket;
		this.id = id;
		this.decoder = decoder;
		
		this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.output = new PrintWriter(socket.getOutputStream());
		this.thread = new Thread(this);
				
		// Demarrer le thread
		this.running = true;
		this.thread.start();
		
	}
	
	
	/**
	 * Indique si le thread associe a l'activite est en vie
	 * @return True le thread est en vie, false sinon
	 */
	public boolean isAlive()
	{
		return this.thread.isAlive();
	}
	
	/**
	 * Permet d'envoyer un message au client associe
	 * @param message Le message
	 */
	public void send(String message)
	{
		this.output.println(message);
		this.output.flush();
	}
	
	/**
	 * Permet de tuer la connexion. Utilisee par le nettoyeur de connexion
	 * pour fermet proprement les flux et socket
	 */
	public void kill()
	{
		try
		{
			this.socket.close();
			this.input.close();
			this.output.close();
		}
		catch (IOException e)
		{
			
		}
	}
	
	/**
	 * Permet de terminer proprement la connexion. Par exemple
	 * pour repondre a une demande de deconnexion
	 */
	public void close()
	{
		this.running = false;
	}
	
	/**
	 * Permet de retourner l'id de connexion
	 * @return
	 */
	public int getId()
	{
		return id;
	}
	

	/**
	 * Permet de lancer le thread
	 */
	@Override
	public void run()
	{
		String ligne;
		
		try
		{
			while(this.running)
			{
				ligne = input.readLine();
				
				// Le flux est termine
				if (ligne == null)
					break;	
				
				// Demande au decodeur
				decoder.decode(this, ligne);
				
			}
		}
		
		catch (IOException e)
		{

		}
		
		finally
		{
			try
			{
				socket.close();
			}
			catch (IOException e)
			{
				
			}
		}
	}
	
}

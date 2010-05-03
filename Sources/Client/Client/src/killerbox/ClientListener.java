package killerbox;

import network.*;
import java.io.*;
import java.util.*;

/**
 * Classe permettant d'ecouter les differents message du serveur
 * @author	Valentin Delaye
 * @version	1.0
 */
public class ClientListener implements Observer, Runnable
{
	/**
	 * Reference sur le client
	 */
	private Client client;
	
	/**
	 * Reference sur la vue
	 */
	private BaseWindow fenetre;
	
	/**
	 * Flux d'entree
	 */
	private BufferedReader input;
	
	/**
	 * Thread associe a l'ecouteur
	 */
	private Thread activite;
	
	/**
	 * Permet de creer un nouvel ecouteur
	 * @param input Le flux d'entree
	 */
	public ClientListener(Client client, BaseWindow fenetre)
	{
		this.fenetre = fenetre;
		this.client = client;
	}


	/**
	 * Executer le thread.
	 */
	@Override
	public void run()
	{
		String ligne = null;
		
		while(true)
		{
			try
			{	
				ligne = input.readLine();
				
				if(ligne == null)
					throw new IOException();
				
			}
			
			catch (IOException e)
			{
				fenetre.sendError("La connexion avec le serveur a ete interrompue.");
				client.close();
				break;
			}
			
		}
	}


	/**
	 * Indique une modification du client
	 * @param o L'objet client
	 * @param arg Les parametres
	 */
	public void update(Observable o, Object arg)
	{
		if(Boolean.class.isInstance(arg))
		{
			boolean status = (Boolean)arg;
			
			// Le client passe en status connecte
			if(status)
			{
				this.input = this.client.getBufferedReader();
				this.activite = new Thread(this);
				this.activite.start();
			}
			
		}
	}
	
}

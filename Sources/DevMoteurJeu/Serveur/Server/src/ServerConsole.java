import java.util.*;

import killerbox.KillerBoxServer;;

/**
 * Permet d'afficher les messages serveur sur la console
 */
public class ServerConsole implements Observer
{
	/**
	 * Le serveur observe
	 */
	private KillerBoxServer server;
	
	/**
	 * Permet de creer un nouveau serveur sur la console
	 * @param server La serveur
	 */
	public ServerConsole(KillerBoxServer server)
	{
		this.server = server;
		this.server.addObserver(this);
	}

	/**
	 * Afficher les messages recu sur la console
	 */
	public void update(Observable o, Object obj)
	{
		System.out.println((String)obj);
	}
	
	
}

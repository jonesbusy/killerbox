import java.util.*;

import killerbox.KillerBoxServer;


/**
 * Permet de creer une vue de serveur sur la console.
 * @author Valentin Delaye
 * @version 1.0
 * @see KillerBoxServer
 */
public class ServerConsole implements Observer
{
	/**
	 * Le serveur de jeu
	 */
	private KillerBoxServer server;
	
	/**
	 * Constructeur. Permet de creer une nouvelle vue.
	 * @param server Le serveur de jeu
	 */
	public ServerConsole(KillerBoxServer server)
	{
		this.server = server;
		this.server.addObserver(this);
	}

	/**
	 * Afficher les messages recus sur la console.
	 */
	@Override
	public void update(Observable o, Object obj)
	{
		System.out.println(obj);
	}
	
}

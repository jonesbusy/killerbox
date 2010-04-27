package killerbox;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import network.*;


/**
 * Permet de representer un serveur du jeu KillerBox
 * @author Valentin Delaye
 *
 */
public class KillerBoxServer extends Observable implements Runnable, Observer
{
	
	/**
	 * Serveur associe
	 */
	private Server serveur;
	
	/**
	 * Activite associe au thread
	 */
	private Thread activite;
	
	private HashMap<Integer, String> login = new HashMap<Integer, String>();
	
	/**
	 * Permet de creer un nouveau serveur KillerBox
	 * @param numeroPort
	 */
	public KillerBoxServer(int numeroPort, Decoder decoder)
	{
		this.serveur = new Server(numeroPort, decoder);
		decoder.setServer(this.serveur);
		serveur.addObserver(this);
	}
	
	/**
	 * Permet de demarrer le serveur
	 */
	public void start()
	{
		this.serveur.start();
		this.activite = new Thread(this);
		
		setChanged();
		notifyObservers("KillerBox v 1.0");
		
		this.activite.start();
	}
	
	/**
	 * Permet d'executer le thread et associer les nouvelles
	 * connexions aux utilisateurs
	 */
	@Override
	public void run()
	{
		while(true)
		{
			
		}
	}


	/**
	 * 
	 */
	@Override
	public void update(Observable o, Object obj)
	{
		// S'il y a une nouvelle connexion ou un utilisateur deconnecte
		if(Boolean.class.isInstance(obj))
		{
			boolean status = (Boolean)obj;
			
			// Nouvelle connexion
			if(status)
			{
				System.out.println("toto");
			}
		}
		
		// Sinon c'est un message du serveur, on ne fait que le retransmettre
		else
		{
			setChanged();
			notifyObservers((String)obj);
		}
	}

}

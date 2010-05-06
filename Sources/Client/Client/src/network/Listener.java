package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

/**
 * Permet de fournir quelques methodes afin d'ecouter les differents message
 * provenant au serveur. Les message sont ensuite decoder par le biais
 * d'un decoder
 * @author Valentin Delaye
 *
 */
public abstract class Listener implements Observer, Runnable
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
	 * Flux d'entree
	 */
	protected BufferedReader input;
	
	/**
	 * Constructeur
	 * @param client Le client associe
	 * @param decoder Le decoder
	 */
	public Listener(Client client, Decoder decoder)
	{
		this.client = client;
		this.decoder = decoder;
	}
	
	
	/**
	 * Executer le thread.
	 * S'occupe de lire le message recu et d'appeler le decoder
	 * sur cette ligne
	 */
	@Override
	public void run()
	{
		String ligne = null;

		while (true)
		{
			try
			{
				ligne = input.readLine();

				if (ligne == null)
					throw new IOException();

				// Decoder la ligne
				this.decoder.decode(ligne);

			}

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

			// Le client passe en status connecte
			if (status)
			{
				this.input = this.client.getBufferedReader();
				this.activite = new Thread(this);
				this.activite.start();
			}

		}
	}

}

package killerbox;

import network.*;
import java.io.*;
import java.util.*;

/**
 * Classe permettant d'ecouter les differents message du serveur
 * @author Valentin Delaye
 * @version 1.0
 */
public class KillerBoxListener implements Observer, Runnable
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
	 * Le decodeur de message
	 */
	private Decoder decoder;

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
	public KillerBoxListener(Client client, BaseWindow fenetre, Decoder decoder)
	{
		this.fenetre = fenetre;
		this.client = client;
		this.decoder = decoder;
	}

	/**
	 * Permet d'envoyer au serveur les informations de login
	 * @param login
	 * @param pass
	 */
	public void sendCredentias(String login, String pass)
	{
		client.send("login#" + login + '#' + pass);
	}

	/**
	 * Permet d'effectuer une demande de creation de compte sur le serveur
	 * @param login Le login
	 * @param pass Le mot de passe choisi
	 */
	public void requestAccount(String login, String pass)
	{
		client.send("account#create#" + login + '#' + pass);
	}

	/**
	 * Effectuer une demande de suppression de compte de la part du serveur
	 */
	public void requestDeleteAccount()
	{
		client.send("account#delete#");
	}

	/**
	 * Effectuer une demande de creation de compte de la part du serveur
	 * @param login Nom du compte a supprimer
	 */
	public void requestDeleteAccount(String login)
	{
		client.send("account#delete#" + login);
	}
	
	/**
	 * Demande s'il l'utilisateur est admin
	 */
	public void requestAdmin()
	{
		client.send("account#request#admin");
	}
	
	/**
	 * Indique pour un user donne s'il est admin
	 * @param username Le nom d'utilisateur
	 */
	public void requestAdmin(String username)
	{
		client.send("account#request#admin#" + username);
	}

	/**
	 * Effectuer une demande de modification de score
	 * @param login Le compte dont on veut modifier le score
	 */
	public void modifyScore(String login, int score)
	{
		client.send("account#modify#score" + login + '#' + score);
	}

	/**
	 * Effectuer une demande de creation de compte de la part du serveur
	 * @param login Compte a modifier
	 */
	public void modifyPass(String login, String pass)
	{
		client.send("account#modify#pass" + login + '#' + pass);
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
				// Seter la fin de la connecion
				fenetre.getPanel().errorConnection = true;
				fenetre.printError("La connexion avec le serveur a ete interrompue.");
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

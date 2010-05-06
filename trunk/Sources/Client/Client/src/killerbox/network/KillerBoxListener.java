package killerbox.network;

import network.*;
import killerbox.gui.*;

/**
 * Classe permettant d'ecouter les differents message du serveur
 * @author Valentin Delaye
 * @version 1.0
 */
public class KillerBoxListener extends Listener
{

	/**
	 * Reference sur la vue
	 */
	private BaseWindow fenetre;

	/**
	 * Permet de creer un nouvel ecouteur
	 * @param input Le flux d'entree
	 */
	public KillerBoxListener(Client client, BaseWindow fenetre, Decoder decoder)
	{
		super(client, decoder);
		this.fenetre = fenetre;
	}

	/**
	 * Permet d'envoyer au serveur les informations de login
	 * @param login Nom d'utilisateur
	 * @param pass Mot de passe
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
	 * Permet de setter
	 */
	@Override
	public void setDeconnected()
	{
		// Seter la fin de la connecion
		fenetre.getPanel().errorConnection = true;
		fenetre.printError("La connexion avec le serveur a ete interrompue.");
		client.close();
	}
	
	
	
}

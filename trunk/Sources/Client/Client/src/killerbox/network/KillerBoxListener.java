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
	 * Affichage si deconnexion
	 */
	private static final String MESSAGE_DECO = "Vous etes deconnecte.";

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
		client.send("#login#" + login + '#' + pass);
	}

	/**
	 * Permet d'effectuer une demande de creation de compte sur le serveur
	 * @param login Le login
	 * @param pass Le mot de passe choisi
	 */
	public void requestAccount(String login, String pass)
	{
		client.send("#account#create#" + login + '#' + pass);
	}

	/**
	 * Effectuer une demande de suppression de compte
	 */
	public void requestDeleteAccount()
	{
		client.send("#account#delete#");
	}

	/**
	 * Effectuer une demande de creation de compte pour un utilisateur
	 * donne.Uniquement un administrateur est autorise a executer ceci.
	 * @param login Nom du compte a supprimer
	 */
	public void requestDeleteAccount(String login)
	{
		client.send("#account#delete#" + login);
	}

	/**
	 * Demande s'il l'utilisateur est admin
	 */
	public void requestAdmin()
	{
		client.send("#account#request#admin");
	}

	/**
	 * Indique pour un user donne s'il est admin
	 * @param username Le nom d'utilisateur
	 */
	public void requestAdmin(String username)
	{
		client.send("#account#request#admin#" + username);
	}

	/**
	 * Effectuer une demande de modification de score.Uniquement un
	 * administrateur est autorise a executer ceci.
	 * @param login Le compte dont on veut modifier le score
	 */
	public void requestModifyScore(String login, int score)
	{
		client.send("#account#modify#score" + login + '#' + score);
	}

	/**
	 * Effectuer une demande de modification de mot de passe
	 * pour soi meme
	 * @param pass Le nouveau mot de passe
	 */
	public void requestModifyPass(String pass)
	{
		client.send("#account#modify#pass#" + pass);
	}

	/**
	 * Effectuer une demande de modification de mot
	 * de passe pour un utilisateur donne. Uniquement un
	 * administrateur est autorise a executer ceci.
	 * @param login Compte a modifier
	 * @param pass Nouveau password
	 */
	public void requestModifyPass(String login, String pass)
	{
		client.send("#account#modify#pass" + login + '#' + pass);
	}

	/**
	 * Permet de setter
	 */
	@Override
	public void setDeconnected()
	{
		// Seter la fin de la connecion
		fenetre.getPanel().errorConnection = true;
		fenetre.printMessage(MESSAGE_DECO);
		client.close();
	}

}

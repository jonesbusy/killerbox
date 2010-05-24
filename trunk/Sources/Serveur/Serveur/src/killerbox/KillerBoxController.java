package killerbox;

import java.io.*;
import java.net.*;

import network.*;

/**
 * Controleur specialise pour le serveur KillerBox. Permet
 * d'envoyer differents messages predefinis au client. 
 * @author valentin
 *
 */
public class KillerBoxController extends Controller
{

	/**
	 * Permet de construire un nouveau controleur pour une connexion KillerBox.
	 * @param socket Socket client.
	 * @param id ID de la connexion
	 * @param decoder Le decodeur
	 * @throws IOException Si erreur socket
	 */
	public KillerBoxController(Socket socket, int id, Decoder decoder) throws IOException
	{
		super(socket, id, decoder);
	}
	
	/**
	 * Permet d'indiquer au client si son login est correct ou non
	 * @param status True connecion ok, false sinon
	 */
	public void sendLoginStatus(boolean status)
	{
		this.send("#login#" + status);
	}
	
	/**
	 * Indique si la creation de compte a ete effectuee ou non
	 * @param status True la creation du compte est en ordre, false sinon
	 */
	public void sendCreateAccount(boolean status)
	{
		this.send("#account#create#" + status);
	}
	
	/**
	 * Indique si la suppression s'est effectue avec succe
	 * @param status True la suppression du compte est en ordre, false sinon
	 */
	public void sendDeleteAccount(boolean status)
	{
		this.send("#account#delete#" + status);
	}
	
	/**
	 * Indique si un utilisateur est administrateur ou non
	 * @param status True il est administrateur, false sinon
	 */
	public void sendIsAdmin(String user, boolean status)
	{
		this.send("#account#request#admin#" + user + "#" + status);
	}
	
	/**
	 * Permet d'envoyer la liste des parties disponibles au client
	 * @param formatted Les partie formatees pour l'envoi
	 */
	public void sendGames(String formatted)
	{
		this.send(formatted);
	}
	
	/**
	 * Permet d'envoyer la liste des score pour chaque utilisateur.
	 * @param formatted La score formates pour l'envoi
	 */
	public void sendScores(String formatted)
	{
		send("#scores" + formatted);
	}
	
	/**
	 * Permet d'envoyer l'ID de la partie creer
	 * @param id ID de la partie
	 */
	public void sendCreateGame(int id)
	{
		this.send("#game#create#" + id);
	}
	
	/**
	 * Permet d'envoyer si la modification du mot de passe s'est bien 
	 * passee ou non.
	 * @param status True le mot de passe a ete modifie, false sinon
	 */
	public void sendModifyPass(boolean status)
	{
		this.send("#modify#pass#true");
	}
	
	/**
	 * Permet d'envoyer, pour un utilisateur donnee si le mot de passe
	 * a bien ete modifie ou non
	 * @param user L'utilisateur a qui le mot de passe a ete modifie
	 * @param status True le mot de passe a ete modifie, false sinon
	 */
	public void sendModifyPass(String user, boolean status)
	{
		this.send("#modify#passadmin#" + user + "#" + status);
	}
	
	/**
	 * Permet d'envoyer si le score a bien ete modifie
	 * @param status Le nouveau status
	 */
	public void sendModifyScores(boolean status)
	{
		this.send("#modify#scores#true");
	}

}

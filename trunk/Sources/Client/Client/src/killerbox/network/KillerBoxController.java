package killerbox.network;

import java.util.ArrayList;

import network.*;
import killerbox.game.CarteBase;
import killerbox.game.Joueur;
import killerbox.game.ModelGame;
import killerbox.gui.*;

/**
 * Classe permettant d'ecouter les differents message du serveur et de les passer ensuite
 * au decodeur associe.
 * Par le biais de cette classe, les clients peuvent envoyer des messages au serveur.
 * @author Valentin Delaye
 * @version 1.0
 * @see Listener
 * @see KillerBoxDecoder
 */
public class KillerBoxController extends Controller
{
	/**
	 * Affichage si l'ecouteur detecte une deconnexion.
	 */
	private static final String MESSAGE_DECO = "Vous etes deconnecte.";

	/**
	 * Reference sur la vue. Cette vue est ensuite passe au decodeur pour qu'il
	 * puisse effectuer les actions appropriees.
	 */
	private BaseWindow fenetre;

	/**
	 * Permet de creer un nouvel ecouteur.
	 * @param client Reference sur le client
	 * @param fenetre Reference sur la fenetre principale
	 * @param decoder Le decodeur
	 */
	public KillerBoxController(Client client, BaseWindow fenetre, Decoder decoder)
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
	 * Permet d'effectuer une demande de creation de compte sur le serveur.
	 * @param login Le login
	 * @param pass Le mot de passe
	 */
	public void requestAccount(String login, String pass)
	{
		client.send("#account#create#" + login + '#' + pass);
	}

	/**
	 * Effectuer une demande de suppression de compte.
	 */
	public void requestDeleteAccount()
	{
		client.send("#account#delete#");
	}

	/**
	 * Effectuer une demande de creation de compte pour un utilisateur
	 * donne. Uniquement un administrateur est autorise a executer ceci.
	 * @param login Nom du compte a supprimer.
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
	 * Effectuer une demande de modification de score. Uniquement un
	 * administrateur est autorise a executer ceci. La modification d'un score
	 * a la fin d'une partie est automatiquement change par le serveur lui meme. Un client
	 * (non administrateur) n'a pas la possibilite de changer son score.
	 * @param login Le compte dont on veut modifier le score.
	 */
	public void requestModifyScore(String login, int score)
	{
		client.send("#account#modify#scores#" + login + '#' + score);
	}

	/**
	 * Effectuer une demande de modification de mot de passe.
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
	 * @param login Nom d'utilisateur
	 * @param pass Nouveau mot-de-passe
	 */
	public void requestModifyPass(String login, String pass)
	{
		client.send("#account#modify#passadmin#" + login + '#' + pass);
	}
	
	/**
	 * Permet de demander les informations
	 * sur les utilisateurs et leur scores.
	 */
	public void requestScore()
	{
		client.send("#scores#");
	}
	
	/**
	 * Permet de demander les informations sur les differentes parties
	 */
	public void requestGames()
	{
		client.send("#game#list#");
	}
	
	/**
	 * Permet de demander la creation d'une nouvelle partie
	 * @param type Type de la partie. 0 pour Tous vs Tous ou 1 pour par equipe.
	 */
	public void requestCreateGame(int type)
	{
		client.send("#game#create#" + type + "#");
	}
	
	/**
	 * Demande de suppression de partie.
	 */
	public void requestDeleteGame()
	{
		client.send("#game#delete#");
	}
	
	/**
	 * Demande de rejoindre une partie
	 * @param id ID de la partie
	 */
	public void requestJoinGame(int id)
	{
		client.send("#game#join#" + id + "#");
	}
	
	/**
	 * Demande de quitter le jeu
	 * @param id ID de la partie
	 */
	public void requestQuitGame(int id)
	{
		client.send("#game#quit#" + id + "#");
	}
	
	/**
	 * Permet de demander les differents utilisateur inscris pour
	 * une partie donnee
	 * @param id L'ID de la partie
	 */
	public void requestPlayers(int id)
	{
		client.send("#players#" + id);
	}

	/**
	 * Action lorsque l'ecouteur n'obtient plus d'informations du serveur.
	 * Generalement lors d'une deconnexion
	 */
	@Override
	public void setDeconnected()
	{
		// Seter la fin de la connecion
		fenetre.getPanel().errorConnection = true;
		fenetre.printMessage(MESSAGE_DECO);
		
		// Fermer proprement le client
		client.disconnect();
	}

	public void requestStartGame() {
		client.send("#game#start#");
	}
	
	/**
	 * Envoie les données du modèles à tous les clients connectés à la même
	 * partie que le créateur (C'est le créateur uniquement qui utilise cette
	 * fonction).
	 * @param modelGame Le modèle de jeu à envoyer
	 */
	public void sendModel(ModelGame modelGame) {
		
		// On précise !owner pour que les infos ne soient pas renvoyées au
		// créateur de la partie, mais seulement aux autres joueurs.
		String header = "#game#infos#!owner#";
		
		// JOUEURS
		for (Joueur joueur : modelGame.getJoueurs()) {
			// PosX#PosY#nomJoueur
			client.send(header + joueur.getPosX() + "#" + joueur.getPosY() + "#" + joueur.getNom() + "#");
		}
		
		// CARTE
		// Note : on envoie le nom de la classe, comme ça, on a plus qu'a
		// l'instancié chez les autres clients
		client.send(header + (new CarteBase()).getClass().toString()+ "#");
	}
	
	
}

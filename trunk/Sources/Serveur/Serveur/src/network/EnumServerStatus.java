package network;

/**
 * Permet de representer differents information sur le changement de status du serveur.
 * @author Valentin Delaye
 * @version 1.0
 */
public enum EnumServerStatus
{
	// Evenement generer par le serveur lui-meme
	SERVER_STATUS,
	
	// Nouvelle connexion
	NEW_CONNECTION,
	
	// Connexion supprimee
	REMOVED_CONNECTION,
	
	// Connexion en vie
	ALIVE_CONNECTION;
}

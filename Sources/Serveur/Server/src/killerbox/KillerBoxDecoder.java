package killerbox;

import java.util.*;
import network.*;

/**
 * Classe permettant de decoder les informations en provenance
 * des clients. Implemente decode() de la classe abstraire Decoder
 * @author Valentin Delaye
 */
public class KillerBoxDecoder extends Decoder
{

	/**
	 * Serveur KillerBox
	 */
	private KillerBoxServer serverKiller;

	/**
	 * La base de donnee
	 */
	private KillerBoxDataBase dataBaseKiller;

	
	/**
	 * Permet de setter le serveur KillerBox
	 * @param serverKiller Le serveur killerbox
	 */
	public void setKillerBoxServer(KillerBoxServer serverKiller)
	{
		this.serverKiller = serverKiller;
	}

	/**
	 * Permet de setter la base de donnee de KillerBox. Pour notemment
	 * verifier les nom d'utilisateur et mots de passe
	 * @param dataBaseKiller La base de donnee
	 */
	public void setDataBase(KillerBoxDataBase dataBaseKiller)
	{
		this.dataBaseKiller = dataBaseKiller;
	}

	/**
	 * Permet de decoder un message
	 */
	@Override
	public void decode(Connexion connexion, String message)
	{

		StringTokenizer tokens = new StringTokenizer(message, "#");
		String instruction = tokens.nextToken();

		if (instruction.equals("logout"))
			server.disconnect(connexion.getId());

		// Demande de connexion
		else if (instruction.equals("login"))
		{
			// Login/Pass correct
			String login = tokens.nextToken();
			String pass = tokens.nextToken();
			
			if (this.dataBaseKiller.verifierUtilisateur(login, pass))
			{
				server.send(connexion.getId(), "#login#true");
				serverKiller.addConnected(login, connexion.getId());
			}
			
			// Erreur
			else
				server.send(connexion.getId(), "#login#false");

		}
		
		// Instruction destine aux autre joueurs
		else if(instruction.equals("game"))
		{

		}

		// On ne sait pas quoi faire... Simplement relayer le message au serveur
		else
		{
			String username = serverKiller.getUserName(connexion.getId());
			if (username != null)
				server.relay(serverKiller.getUserName(connexion.getId()) + " : " + message);
			else
				server.relay("guest" + connexion.getId() + " : " + message);
		}

	}

}

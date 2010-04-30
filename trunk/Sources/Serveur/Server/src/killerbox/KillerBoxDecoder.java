package killerbox;

import java.util.StringTokenizer;

import network.*;

/**
 * @author valentin
 * 
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
	 * @param server
	 */
	public KillerBoxDecoder()
	{

	}

	/**
	 * Permet de setter le serveur KillerBox
	 * @param serverKiller
	 */
	public void setKillerBoxServer(KillerBoxServer serverKiller)
	{
		this.serverKiller = serverKiller;
	}

	/**
	 * Permet de setter la base de donnee de KillerBox. Pour notemment
	 * verifier les nom d'utilisateur et mots de passe
	 * @param dataBaseKiller
	 */
	public void setDataBase(KillerBoxDataBase dataBaseKiller)
	{
		this.dataBaseKiller = dataBaseKiller;
	}

	/**
	 * 
	 */
	@Override
	public void decode(Connexion connexion, String message)
	{

		StringTokenizer tokens = new StringTokenizer(message, "@");
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
				server.send(connexion.getId(), "Connexion reussie");
				serverKiller.addConnected(login, connexion.getId());
			}
			
			// Erreur
			else
				server.send(connexion.getId(), "Nom d'utilisateur et/ou mot de passe incorrect");

		}

		else
			server.sendMessage(serverKiller.getUserName(connexion.getId()) + " : " + message);

	}

}

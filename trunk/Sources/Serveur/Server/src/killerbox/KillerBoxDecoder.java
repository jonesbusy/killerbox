package killerbox;

import java.sql.SQLException;
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

		// Deconnecter le client qui le demande
		if (instruction.equals("logout"))
			server.disconnect(connexion.getId());

		// Demande de connexion
		else if (instruction.equals("login"))
		{
			// Login/Pass correct
			String login = tokens.nextToken();
			String pass = tokens.nextToken();
			
			// Nom d'utilisateur valide
			if (this.dataBaseKiller.verifierUtilisateur(login, pass))
			{
				server.send(connexion.getId(), "#login#true");
				serverKiller.addConnected(login, connexion.getId());
			}
			
			// Erreur
			else
				server.send(connexion.getId(), "#login#false");

		}
		
		// Operation sur un compte
		else if(instruction.equals("account"))
		{
			instruction = tokens.nextToken();
			
			// Creation d'un compte
			if(instruction.equals("create"))
			{
				String user = tokens.nextToken();
				String pass = tokens.nextToken();
				
				try
				{
					this.dataBaseKiller.ajouterUtilisateur(user, pass, false);
					this.server.send(connexion.getId(), "#account#create#true");
					this.server.relay(user + " : nouvel utilisateur");
				}
				
				// Ajout impossible, probablement qu'il existe deja
				catch (Exception e)
				{
					this.server.send(connexion.getId(), "#account#create#false");
				}
				
			}
			
			// Suppression d'un utilisateur
			else if(instruction.equals("delete"))
			{
				
				String userToDelete = null;
				
				// Un admin indique l'utilisateur a supprimer
				if(tokens.hasMoreTokens())
					userToDelete = tokens.nextToken();
				
				// Supprimer son compte
				else
					userToDelete = serverKiller.getUserName(connexion.getId());
				
				// Verifier les droits
				if(this.serverKiller.getUserName(connexion.getId()).equals(userToDelete)
						|| this.dataBaseKiller.isAdmin(serverKiller.getUserName(connexion.getId())))
				{
					try
					{
						this.dataBaseKiller.supprimerUtilisateur(userToDelete);
						connexion.send("#account#delete#true");
						this.server.relay(userToDelete + " : a supprime sont compte");
						
					}
					
					// Suppression impossible, probablement qu'il n'existe pas
					catch (Exception e)
					{
						connexion.send("#account#delete#false");
					}
				}
				
				// Interdiction de supprimer, pas les droit
				else
				{
					connexion.send("#account#delete#error");
				}
				
			}
			
			// Modification sur le compte
			else if(instruction.equals("modify"))
			{
				
				// Modification de mot de passe pour soi meme
				instruction = tokens.nextToken();
				String user = serverKiller.getUserName(connexion.getId());
				if(instruction.equals("pass"))
				{
					this.dataBaseKiller.modifierPass(user, tokens.nextToken());
					connexion.send("#modify#pass#true");
				}
					
				// Modification pour quelqu'un d'autre (Doit etre admin pour faire ca)
				else if(instruction.equals("passadmin") && dataBaseKiller.isAdmin(serverKiller.getUserName(connexion.getId())))
				{
					user = tokens.nextToken();
					this.dataBaseKiller.modifierPass(user, tokens.nextToken());
					connexion.send("#modify#passadmin#"+ user + "#true");
				}
				
				// Modification du score
				else if(instruction.equals("scores"))
				{
					try
					{
						this.dataBaseKiller.setScore(tokens.nextToken(), Integer.parseInt(tokens.nextToken()));
						connexion.send("#modify#scores#true");
					}
					catch (Exception e)
					{
						connexion.send("#modify#scores#false");
					}
					
				}
				
			}
			
			// Demande d'information sur l'utilisateur
			else if(instruction.equals("request"))
			{
				instruction = tokens.nextToken();
				if(instruction.equals("admin"))
				{
					String user;
					if(tokens.hasMoreTokens())
						user = tokens.nextToken();
					else
						user = serverKiller.getUserName(connexion.getId());
					
					// C'est un admin
					if(this.dataBaseKiller.isAdmin(user))
						connexion.send("#account#request#admin#" + user + "#true");
					
					// Ce n'est pas un admin
					else
						connexion.send("#account#request#admin#" + user + "#false");
				}
			}
		}
		
		// On demande les score
		else if(instruction.equals("scores"))
			try
			{
				connexion.send("#scores" + this.dataBaseKiller.getAll());
			}
			catch (SQLException e)
			{
				connexion.send("#scores#");
			}
			
		else if(instruction.equals("game"))
		{
			
		}

		// Sinon passer au serveur
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

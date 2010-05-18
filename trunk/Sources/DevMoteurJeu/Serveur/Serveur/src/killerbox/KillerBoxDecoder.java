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
	private KillerBoxServer serverKillerBox;

	/**
	 * La base de donnee
	 */
	private DataBase database;

	/**
	 * La liste des parties disponibles
	 */
	private GameList gameList;

	/**
	 * Permet de retourner un nom d'utilisateur pour un ID de connexion.
	 * @param id L'ID de la connexion
	 * @return
	 */
	String getUserName(int id)
	{
		return this.serverKillerBox.getUserName(id);
	}

	/**
	 * Permet de setter le serveur KillerBox
	 * @param serverKiller Le serveur killerbox
	 */
	public void setKillerBoxServer(KillerBoxServer serverKiller)
	{
		this.serverKillerBox = serverKiller;
		this.gameList = this.serverKillerBox.getGameList();
	}

	/**
	 * Permet de setter la base de donnee de KillerBox. Pour notemment
	 * verifier les nom d'utilisateur et mots de passe
	 * @param database La base de donnee
	 */
	public void setDataBase(DataBase database)
	{
		this.database = database;
	}

	/**
	 * Permet de decoder un message.
	 * @param connexion Connexio d'ou provient le message
	 * @param message Message a decoder
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
			if (this.database.isUserValid(login, pass))
			{
				server.send(connexion.getId(), "#login#true");
				serverKillerBox.setConnected(login, connexion.getId());
			}

			// Erreur
			else
				server.send(connexion.getId(), "#login#false");

		}

		// Operation sur un compte
		else if (instruction.equals("account"))
		{
			instruction = tokens.nextToken();

			// Creation d'un compte
			if (instruction.equals("create"))
			{
				String user = tokens.nextToken();
				String pass = tokens.nextToken();

				try
				{
					this.database.addUser(user, pass, false);
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
			else if (instruction.equals("delete"))
			{

				// Nom d'utilisateur a supprimer
				String userToDelete = null;

				// Un admin indique l'utilisateur a supprimer
				if (tokens.hasMoreTokens())
					userToDelete = tokens.nextToken();

				// Supprimer son compte
				else
					userToDelete = this.getUserName(connexion.getId());

				// Verifier les droits. Un utilisateur peut supprimer son propre compte,
				// ou un administrateur peut supprimer n'importe qui
				if (this.getUserName(connexion.getId()).equals(userToDelete)
						|| this.database.isAdmin(this.getUserName(connexion.getId())))
				{
					try
					{
						this.database.deleteUser(userToDelete);
						connexion.send("#account#delete#true");
						this.server.relay(userToDelete + " : a supprime sont compte");

					}

					// Suppression impossible, probablement qu'il n'existe pas
					catch (Exception e)
					{
						connexion.send("#account#delete#false");
					}
				}

				// Interdiction de supprimer, pas les droits
				else
				{
					connexion.send("#account#delete#error");
				}

			}

			// Modification sur le compte
			else if (instruction.equals("modify"))
			{

				// Modification de mot de passe pour soi meme
				instruction = tokens.nextToken();
				String user = this.getUserName(connexion.getId());
				if (instruction.equals("pass"))
				{
					this.database.modifyPass(user, tokens.nextToken());
					connexion.send("#modify#pass#true");
				}

				// Modification pour quelqu'un d'autre (Doit etre admin pour faire ca)
				else if (instruction.equals("passadmin")
						&& database.isAdmin(this.getUserName(connexion.getId())))
				{
					user = tokens.nextToken();
					this.database.modifyPass(user, tokens.nextToken());
					connexion.send("#modify#passadmin#" + user + "#true");
				}

				// Modification du score
				else if (instruction.equals("scores"))
				{
					try
					{
						this.database.setScore(tokens.nextToken(), Integer.parseInt(tokens
								.nextToken()));
						connexion.send("#modify#scores#true");
					}
					catch (Exception e)
					{
						connexion.send("#modify#scores#false");
					}

				}

			}

			// Demande d'informations sur l'utilisateur
			else if (instruction.equals("request"))
			{
				instruction = tokens.nextToken();
				if (instruction.equals("admin"))
				{
					String user;
					if (tokens.hasMoreTokens())
						user = tokens.nextToken();
					else
						user = this.getUserName(connexion.getId());

					// C'est un admin
					if (this.database.isAdmin(user))
						connexion.send("#account#request#admin#" + user + "#true");

					// Ce n'est pas un admin
					else
						connexion.send("#account#request#admin#" + user + "#false");
				}
			}
		}

		/**
		 * Demande d'informations sur les scores
		 */
		else if (instruction.equals("scores"))
			try
			{
				connexion.send("#scores" + this.database.getInfos());
			}
			catch (SQLException e)
			{
				connexion.send("#scores#");
			}

		/**
		 * Demande pour les parties
		 */
		else if (instruction.equals("game"))
		{
			instruction = tokens.nextToken();

			/**
			 * Le client aimerait la liste des parties
			 */
			if (instruction.equals("list"))
				serverKillerBox.sendGames(this.getUserName(connexion.getId()));

			/**
			 * Demande de creation de partie
			 */
			else if (instruction.equals("create"))
			{
				try
				{
					int type = Integer.parseInt(tokens.nextToken());
					String owner = this.getUserName(connexion.getId());
					this.gameList.createGame(owner, type);

					// Envoyer l'ID de la partie
					connexion.send("#game#create#" + this.gameList.getIdOwner((owner)));

					// Message serveur
					this.server.relay(this.getUserName(connexion.getId())
							+ " a cree une nouvelle partie de type "
							+ (type == 0 ? "Tous vs Tous" : "Par Equipe"));
				}
				catch (NumberFormatException e)
				{
					connexion.send("#game#create#false");
				}
			}

			/**
			 * Suppression de la partie
			 */
			else if (instruction.equals("delete"))
			{
				String owner = this.getUserName(connexion.getId());
				int IdGame = this.gameList.getIdOwner(owner);

				// Les differents utilisateurs dans cette partie
				String[] users = this.gameList.getUsers(IdGame);

				// Indiquer la fin de la partie a tout les joueurs
				for (String user : users)
					serverKillerBox.send(user, "#game#end");

				// Supprimer enfin la partie
				this.gameList.deleteGame(owner);

			}

			/**
			 * Demande pour joindre une partie
			 */
			else if (instruction.equals("join"))
			{
				try
				{
					int id = Integer.parseInt(tokens.nextToken());
					String user = this.serverKillerBox.getUserName(connexion.getId());

					// Il reste de la place
					if (this.gameList.getUsers(id) != null && this.gameList.getUsers(id).length < 8)
					{
						this.gameList.joinGame(user, id);
						this.server.relay(user + " rejoint la partie de "
								+ this.gameList.getOwner(id));

						connexion.send("#game#join#true");
					}

					// La partie est pleine
					else
						connexion.send("#game#full");
				}

				catch (NumberFormatException e)
				{
					connexion.send("#game#join#false");
				}
			}
		}

		/**
		 * On ne connait pas, passer au serveur.
		 */
		else
		{
			String username = this.getUserName(connexion.getId());
			
			if (username != null)
				server.relay(this.getUserName(connexion.getId()) + " : " + message);
			
			else
				server.relay("guest" + connexion.getId() + " : " + message);
		}

	}

}

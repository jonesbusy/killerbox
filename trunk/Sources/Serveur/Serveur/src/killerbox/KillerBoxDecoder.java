package killerbox;

import java.sql.*;
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
	 * @param connexion Connexion d'ou provient le message
	 * @param message Message a decoder
	 */
	@Override
	public void decode(Controller controller, String message)
	{

		// On decode des connexion killerbox et pas autre chose
		KillerBoxController connexion = (KillerBoxController) controller;

		StringTokenizer tokens = new StringTokenizer(message, "#");
		String instruction = tokens.nextToken();

		/**
		 * Demande d'authentification
		 */
		if (instruction.equals("logout"))
			server.disconnect(connexion.getId());

		/**
		 * Demande d'authentification
		 */
		else if (instruction.equals("login"))
		{
			// Login/Pass correct
			String login = tokens.nextToken();
			String pass = tokens.nextToken();

			// Nom d'utilisateur valide
			if (this.database.isUserValid(login, pass))
			{
				connexion.sendLoginStatus(true);
				serverKillerBox.setConnected(login, connexion.getId());
			}

			// Erreur
			else
				connexion.sendLoginStatus(false);

		}

		/**
		 * Operation sur un compte
		 */
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
					int id = connexion.getId();
					this.database.addUser(user, pass, false);
					connexion.sendCreateAccount(true);

					// Informer le serveur
					this.server.relay(id, "cree un nouvel utilisateur : " + user);
				}

				// Ajout impossible, probablement qu'il existe deja
				catch (Exception e)
				{
					connexion.sendCreateAccount(false);
				}

			}

			/**
			 * Suppression d'un utilisateur
			 */
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
						connexion.sendDeleteAccount(true);
						this.server.relay(connexion.getId(), "supprime le compte - "
								+ userToDelete);
						this.database.deleteUser(userToDelete);

					}

					// Suppression impossible, probablement qu'il n'existe pas
					catch (Exception e)
					{
						connexion.sendDeleteAccount(false);
						this.server.relay(connexion.getId(), "erreur suppression compte - "
								+ userToDelete);
					}
				}

				// Interdiction de supprimer, pas les droits
				else
					connexion.sendDeleteAccount(false);

			}

			/**
			 * Modification sur le compte
			 */
			else if (instruction.equals("modify"))
			{

				// Modification de mot de passe pour soi meme
				instruction = tokens.nextToken();
				String user = this.getUserName(connexion.getId());
				if (instruction.equals("pass"))
				{
					this.database.modifyPass(user, tokens.nextToken());
					connexion.sendModifyPass(true);
				}

				// Modification pour quelqu'un d'autre (Doit etre admin pour faire ca)
				else if (instruction.equals("passadmin")
						&& database.isAdmin(this.getUserName(connexion.getId())))
				{
					user = tokens.nextToken();
					this.database.modifyPass(user, tokens.nextToken());
					connexion.sendModifyPass(user, true);
				}

				/**
				 * Modification du score
				 */
				else if (instruction.equals("scores"))
				{
					try
					{
						this.database.setScore(tokens.nextToken(), Integer.parseInt(tokens
								.nextToken()));
						connexion.sendModifyScores(true);
					}
					catch (Exception e)
					{
						connexion.sendModifyScores(false);
					}

				}
				else if (instruction.equals("scoresIncrement"))
				{
					try
					{
						String pseudo = tokens.nextToken();
						int score = Integer.parseInt(tokens.nextToken()) +
									this.database.getScore(pseudo);
						this.database.setScore(pseudo,score);
						connexion.sendModifyScores(true);
					}
					catch (Exception e)
					{
						connexion.sendModifyScores(false);
					}
				}

			}

			/**
			 * Demande d'information du serveur
			 */
			else if (instruction.equals("request"))
			{
				instruction = tokens.nextToken();

				/**
				 * Est-ce que je suis un admin ?
				 */
				if (instruction.equals("admin"))
				{
					String user;
					if (tokens.hasMoreTokens())
						user = tokens.nextToken();
					else
						user = this.getUserName(connexion.getId());

					// C'est un admin
					if (this.database.isAdmin(user))
						connexion.sendIsAdmin(user, true);

					// Ce n'est pas un admin
					else
						connexion.sendIsAdmin(user, false);
				}
			}

		}

		/**
		 * Demande d'informations sur les scores
		 */
		else if (instruction.equals("scores"))
		{
			try
			{
				connexion.sendScores(this.database.getInfos());
			}

			// Erreur SQL, impossible d'extraire les scores
			catch (SQLException e)
			{
				connexion.sendScores("");
			}
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
			{
				// Preparer la liste des parties
				String games = this.serverKillerBox.createGamesForSending();
				connexion.sendGames(games);
			}

			/**
			 * Demande de creation de partie
			 */
			else if (instruction.equals("create"))
			{
				try
				{
					// Type de la partie
					int type = Integer.parseInt(tokens.nextToken());

					String owner = this.getUserName(connexion.getId());
					this.gameList.createGame(owner, type);

					// Envoyer l'ID de la partie
					connexion.sendCreateGame(this.gameList.getIdOwner((owner)));

					// Message serveur
					this.server.relay(connexion.getId(), "a cree une nouvelle partie de type "
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
					if (this.gameList.getUsers(id) != null
							&& this.gameList.getUsers(id).length < 8)
					{
						this.gameList.joinGame(user, id);
						this.server.relay(id, " rejoint la partie de "
								+ this.gameList.getOwner(id));

						connexion.send("#game#true");
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

			/**
			 * Demande de quitter une partie
			 */
			else if (instruction.endsWith("quit"))
			{
				// ID de la partie et utilisateur
				int id = Integer.parseInt(tokens.nextToken());
				String user = this.getUserName(connexion.getId());
				String owner = this.gameList.getOwner(id);

				// Si le createur quitte la partie, supprimer la partie
				if(owner != null && owner.equals(user))
					this.gameList.deleteGame(user);
				
				else
					this.gameList.deleteUser(user);

				this.server.relay(connexion.getId(), " quitte la partie de " + owner);
				
			}


			/**
			 * Envoi d'infos sur la partie
			 */
			else if (instruction.equals("infos"))
			{
				String userName = serverKillerBox.getUserName(connexion.getId());
				int idGame = gameList.getId(userName);

				instruction = tokens.nextToken();

				if (instruction.equals("!owner"))
				{
					// Renvoyer à tout le monde de la même partie
					serverKillerBox.broadcastGameNotOwner(idGame, message);
				}
				else if (instruction.equals("others"))
				{
					serverKillerBox.broadcastGameNotUser(idGame, message, userName);
				}
				
				else if(instruction.equals("start"))
				{
					// Recuperer la partie
					String owner = this.getUserName(connexion.getId());
					int gameID = this.gameList.getId(owner);
					
					// Demarrage de la partie reussi (Elle existe et c'est le bon proprietaire)
					if (gameID != -1 && this.gameList.startGame(owner))
					{
						// Prevenir les utilisateur du debut de la partie
						serverKillerBox.broadcastGame(gameID, message);
					}
					else
						connexion.send("#game#start#false");
				}
					
				else
				{
					// Renvoyer à tout le monde de la même partie
					serverKillerBox.broadcastGame(idGame, message);
					System.out.println(message);
					
				}
			}

		} // Fin account

		/**
		 * Demande de joueurs pour une partie donnee
		 */
		else if (instruction.equals("players"))
		{

			instruction = tokens.nextToken();

			try
			{
				int GameID = Integer.parseInt(instruction);

				// La partie existe
				if (this.gameList.getOwner(GameID) != null)
				{
					StringBuilder builder = new StringBuilder("#players#");
					String[] users = this.gameList.getUsers(GameID);
					for (String user : users)
						builder.append(user + "#");

					connexion.send(builder.toString());

				}
				else
					connexion.send("#players#unknown");
			}

			// Mauvais parametre
			catch (NumberFormatException e)
			{
				connexion.send("#players#error");
			}
		}

		/**
		 * Information du moteur de jeu. Relayer au joueurs de la partie
		 */
		else if (instruction.equals("game-engine"))
		{
			instruction = tokens.toString();

			String user = this.getUserName(connexion.getId());

			// Provient de quelle partie
			int id = this.gameList.getId(user);

			this.serverKillerBox.broadcastGame(id, instruction);
		}

		/**
		 * On ne connait pas, passer au serveur.
		 */
		else
			server.relay(connexion.getId(), message);

	}

}

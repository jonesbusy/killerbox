package killerbox;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Permet de representer les differentes parties en cours sur le serveur.
 * Les differentes parties sont representees par un numero unique
 * @author Valentin Delaye
 * @version 1.0
 * @see KillerBoxServer
 */
public class GameList
{

	/**
	 * Afin d'assigner des numeros uniques aux differentes parties.
	 * Represente le numero de la prochaine partie
	 */
	private static int ID = 1;
	
	/**
	 * Nombre de parties possibles
	 */
	//private static final int MAX_GAMES = 10;

	/**
	 * Permet de lister les differentes qui se jouent en ce moment.
	 */
	private ArrayList<Game> played = new ArrayList<Game>();

	/**
	 * Les differentes parties en attente de joueur. Ou alors le proprietaire n'a pas
	 * encore demare la partie.
	 */
	private ArrayList<Game> waiting = new ArrayList<Game>();
	
	/**
	 * Permet de mettre en correspondance les noms d'utilisateur et le numero
	 * de partie dans lequel ils jouent ou sont inscrits. Permet d'eviter de chercher
	 * inutilement dans chaque partie les joueurs. 
	 */
	private HashMap<String, Integer> userGames = new HashMap<String, Integer>();

	
	/**
	 * Creer une nouvelle partie sur le serveur
	 * @param user Le nom d'utilisateur du createur de la partie
	 * @param type Le type de la partie
	 */
	public synchronized void createGame(String user, int type)
	{
		this.waiting.add(new Game(ID, user, type));
		this.userGames.put(user, ID);
		ID++;
	}
	
	/**
	 * Permet de supprimer une partie dont le createur est donne en parametre
	 * @param owner Le createur de la partie
	 */
	public synchronized void deleteGame(String owner)
	{
		// Chercher dans les parties en attente
		for (int i = 0; i < this.waiting.size() ; i++)
		{
			Game game = this.waiting.get(i);
			if (game.getOwner().equals(owner))
			{
			// Supprimer les correspondances Nom d'utilisateur - ID partie
				String[] players = game.getPlayers();
				for(String player : players)
					this.userGames.remove(player);
				
				game.deletePlayers();
				this.waiting.remove(game);
				return;
			}
		}
				
		// Chercher dans les parties en cours de jeu
		for(int i = 0 ; i < this.played.size() ; i++)
		{
			Game game = this.waiting.get(i);
			if (this.played.get(i).getOwner().equals(owner))
			{
				// Supprimer les correspondances Nom d'utilisateur - ID partie
				String[] players = game.getPlayers();
				for(String player : players)
					this.userGames.remove(player);
				
				game.deletePlayers();
				this.played.remove(game);
				return;
			}
		}
	}
	
	/**
	 * Permet de demarrer une partie en attente
	 * @param user Le nom d'utilisateur du createur de la partie
	 */
	public synchronized void startGame(String user)
	{
		for (int i = 0; i < this.waiting.size(); i++)
		{
			Game game = this.waiting.get(i);
			if (game.getOwner().equals(user))
			{
				this.played.add(game);
				this.played.remove(i);
				break;
			}
		}
	}

	/**
	 * Permet de joindre un utilisateur a une partie donnnee
	 * @param username Nom d'utilisateur
	 * @param id ID de la partie a rejoindre
	 */
	public synchronized void joinGame(String user, int id)
	{
		// Ajout dans une partie en attente uniquement
		for (int i = 0; i < this.waiting.size(); i++)
			if (this.waiting.get(i).getID() == id)
			{
				this.userGames.put(user, id);
				this.waiting.get(i).addPlayer(user);
				break;
			}
	}
	
	/**
	 * Permet de retourner le createur de la partie pour un ID donne
	 * @param id Identificateur de la partie
	 * @return Le proprietaire de la partie. Null si la partie n'existe pas
	 */
	public String getOwner(int id)
	{
		// Chercher dans les parties en attente
		for (int i = 0; i < this.waiting.size(); i++)
		{
			Game game = this.waiting.get(i);
			if (game.getID() == id)
				return game.getOwner();
		}
		
		// Chercher dans les parties en cours de jeu
		for (int i = 0; i < this.played.size(); i++)
		{
			Game game = this.played.get(i);
			if (game.getID() == id)
				return game.getOwner();
		}

		return null;

	}
	
	/**
	 * Permet de retourner l'ID de partie pour un proprietaire donne
	 * @param owner Le createur de la partie
	 * @return Le numero de partie. -1 si non trouve
	 */
	public int getIdOwner(String owner)
	{
		// Chercher dans les parties en attente
		for (int i = 0; i < this.waiting.size() ; i++)
			if (this.waiting.get(i).getOwner().equals(owner))
				return this.waiting.get(i).getID();
				
		
		// Chercher dans les parties en cours de jeu
		for(int i = 0 ; i < this.played.size() ; i++)
			if (this.played.get(i).getOwner().equals(owner))
				return this.played.get(i).getID();
				
		return -1;
	}
	
	/**
	 * Permet de retourner l'ID de la partie ou joue l'utilisateur
	 * passe en parametre
	 * @param user L'utilisateur
	 * @return L'ID de la partie. Ou -1 si l'utilisateur ne participe pas a une partie
	 */
	public int getId(String user)
	{
		Integer id = this.userGames.get(user);
		if(id == null)
			return -1;
		else return id;
	}

	/**
	 * Permet de retourner la liste des utilisateurs de la partie demandee. L'ID de la
	 * partie est chercher dans la liste des parties en attente et des parties en cours.
	 * @param id ID de la partie
	 * @return La liste des nom des joueurs. Null si la partie n'existe pas.
	 */
	public String[] getUsers(int id)
	{
		// Chercher dans les parties en attente
		for (int i = 0; i < this.waiting.size() ; i++)
			if (this.waiting.get(i).getID() == id)
				return this.waiting.get(i).getPlayers();
		
		// Chercher dans les parties en cours de jeu
		for(int i = 0 ; i < this.played.size() ; i++)
			if (this.played.get(i).getID() == id)
				return this.played.get(i).getPlayers();

		return null;

	}

	/**
	 * Permet de retourner toutes les parties. Les parties en attentes et les parties en
	 * cours de jeu.
	 * @return Le tableau de toute les parties
	 */
	public Game[] getAllGames()
	{
		ArrayList<Game> liste = new ArrayList<Game>(this.waiting);
		liste.addAll(this.played);
		return (Game[]) liste.toArray(new Game[liste.size()]);
	}
	
	/**
	 * Permet de retourner les parties en attente.
	 * @return Les parties en attente
	 */
	public Game[] getWaitingGames()
	{
		return (Game[]) this.waiting.toArray(new Game[this.waiting.size()]);
	}
	
	/**
	 * Permet de retourner les parties en en cours de jeu.
	 * @return Les parties en attente
	 */
	public Game[] getPlayedGames()
	{
		return (Game[]) this.played.toArray(new Game[this.played.size()]);
	}

}

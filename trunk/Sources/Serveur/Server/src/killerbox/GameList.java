package killerbox;

import java.util.ArrayList;

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
	 * Permet de lister les differentes qui se jouent en ce moment.
	 */
	private ArrayList<Game> played = new ArrayList<Game>();

	/**
	 * Les differentes parties en attente de joueur. Ou alors le proprietaire n'a pas
	 * encore demare
	 * la partie.
	 */
	private ArrayList<Game> waiting = new ArrayList<Game>();

	/**
	 * Reference sur le serveur
	 */
	private KillerBoxServer server;

	/**
	 * Constructeur. Permet de construire un nouvel ensemble de parties
	 * @param server Le serveur de jeu
	 */
	public GameList(KillerBoxServer server)
	{
		this.server = server;
	}

	/**
	 * Creer une nouvelle partie sur le serveur
	 * @param user Le nom d'utilisateur du createur de la partie
	 * @param type Le type de la partie
	 */
	public synchronized void createGame(String user, int type)
	{
		this.waiting.add(new Game(ID++, user, type));
	}
	
	/**
	 * Permet de supprimer une partie dont le createur est donne en parametre
	 * @param owner Le createur de la partie
	 */
	public synchronized void deleteGame(String owner)
	{
		// Chercher dans les parties en attente
		for (int i = 0; i < this.waiting.size() ; i++)
			if (this.waiting.get(i).getOwner().equals(owner))
			{
				this.waiting.get(i).deletePlayers();
				this.waiting.remove(i);
				return;
			}
				
		
		// Chercher dans les parties en cours de jeu
		for(int i = 0 ; i < this.played.size() ; i++)
			if (this.played.get(i).getOwner().equals(owner))
			{
				this.played.get(i).deletePlayers();
				this.played.remove(i);
				return;
			}
	}
	
	/**
	 * Permet de supprimer une partie dont l'ID de la partie est donne en parametre
	 * @param owner Le createur de la partie
	 */
	public synchronized void deleteGame(int id)
	{
		// Chercher dans les parties en attente
		for (int i = 0; i < this.waiting.size() ; i++)
			if (this.waiting.get(i).getID() == id)
			{
				this.waiting.get(i).deletePlayers();
				this.waiting.remove(i);
				return;
			}
				
		
		// Chercher dans les parties en cours de jeu
		for(int i = 0 ; i < this.played.size() ; i++)
			if (this.played.get(i).getID() == id)
			{
				this.played.get(i).deletePlayers();
				this.played.remove(i);
				return;
			}
				
	}

	/**
	 * Permet de demarrer une partie en attente
	 * @param user Le nom d'utilisateur du createur de la partie
	 */
	public synchronized void startGame(String user)
	{
		for (int i = 0; i < this.waiting.size(); i++)
			if (this.waiting.get(i).getOwner().equals(user))
			{
				this.played.add(this.waiting.remove(i));
				break;
			}
	}

	/**
	 * Permet de demarrer une partie en attente
	 * @param id Numero unique de la partie
	 */
	public synchronized void startGame(int id)
	{
		for (int i = 0; i < this.waiting.size(); i++)
			if (this.waiting.get(i).getID() == id)
			{
				this.played.add(this.waiting.remove(i));
				break;
			}
	}

	/**
	 * Permet de joindre un utilisateur a une partie donnnee
	 * @param username Nom d'utilisateur
	 * @param id ID de la partie a rejoindre
	 */
	public synchronized void joinGame(String user, int id)
	{
		// Ajout dans une partie en attente
		for (int i = 0; i < this.waiting.size(); i++)
			if (this.waiting.get(i).getID() == id)
			{
				this.waiting.get(i).addPlayer(user);
				break;
			}
		
		// Ajout dans une partie en cours
		for (int i = 0; i < this.played.size(); i++)
			if (this.played.get(i).getID() == id)
			{
				this.played.get(i).addPlayer(user);
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
		for (int i = 0; i < this.waiting.size(); i++)
			if (this.waiting.get(i).getID() == id)
				return this.waiting.get(i).getOwner();

		return null;

	}
	
	/**
	 * Permet de retourner l'ID de partie pour un proprietaire donne
	 * @param owner Le createur de la partie
	 * @return Le numero de partie. -1 si non trouve
	 */
	public int getId(String owner)
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
	public Game[] getGames()
	{
		ArrayList<Game> liste = new ArrayList<Game>(this.waiting);
		liste.addAll(this.played);
		return (Game[]) liste.toArray(new Game[liste.size()]);
	}

}

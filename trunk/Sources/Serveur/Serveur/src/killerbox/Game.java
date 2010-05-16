package killerbox;

import java.util.ArrayList;

/**
 * Represente concretement une partie (jeu) sur le serveur.
 * @author Valentin Delaye
 * @version 1.0
 */
public class Game
{
	/**
	 * Pour indiquer une partie tous contre tous
	 */
	public static final int ALL_VS_ALL = 0;
	
	/**
	 * Pour indiquer une partie par equipe
	 */
	public static final int TEAM = 1;
	
	/**
	 * Numero unique de la partie
	 */
	private int id;
	
	/**
	 * Type de la partie
	 */
	private int type;
	
	/**
	 * Proprietaire de la partie (Createur de la partie)
	 */
	private String owner;
	
	/**
	 * La liste des joueur inscris pour la partie
	 */
	private ArrayList<String> players = new ArrayList<String>();
	
	/**
	 * Constructeur. Permet de creer une nouvelle partie
	 * @param id Numero de la partie
	 */
	public Game(int id, String owner, int type)
	{
		this.id = id;
		this.owner = owner;
		this.players.add(this.owner);
		this.type = type;
	}
	
	/**
	 * Permet de retourner l'ID de la partie
	 * @return L'ID de la partie
	 */
	public int getID()
	{
		return this.id;
	}
	
	/**
	 * Retourne le type de partie
	 * @return Le type de partie
	 */
	public int getType()
	{
		return this.type;
	}
	
	/**
	 * Permet de retourner le nom du proprietaire de la partie
	 * @return Le nom du proprietaire de la partie
	 */
	public String getOwner()
	{
		return this.owner;
	}
	
	/**
	 * Permet de retournerer le nombre de joueur pour la partie
	 * @return Le nombre de joueur
	 */
	public int getNbPlayers()
	{
		return this.players.size();
	}
	
	/**
	 * Permet de retourner la ligne des joueurs pour cette partie
	 * @return La liste des joueurs
	 */
	public String[] getPlayers()
	{
		return (String[]) this.players.toArray(new String[this.players.size()]);
	}
	
	/**
	 * Ajoute un joueur a la partie en cours
	 * @param user Le nom d'utilisateur
	 */
	public synchronized void addPlayer(String user)
	{
		this.players.add(user);
	}
	
	/**
	 * Supprime un joueur de la partie
	 * @param user Le nom d'utilisateur
	 */
	public synchronized void deletePlayer(String user)
	{
		this.players.remove(user);
	}
	
	/**
	 * Permet d'enlever tout les joueurs de la partie.
	 */
	public synchronized void deletePlayers()
	{
		this.players.clear();
	}

	/**
	 * Creer un code de hachage pour la partie
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	/**
	 * Permet de comparer deux partie. Utilise le numero unique
	 * pour comparer les parties
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Game other = (Game) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
}

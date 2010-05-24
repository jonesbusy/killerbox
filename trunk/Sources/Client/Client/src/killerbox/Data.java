package killerbox;

import java.util.ArrayList;
import java.util.Observable;

/**
 * Permet de representer tres generalement des donnees de jeu KillerBox.
 * Permet de references les scores, les liste des joueurs inscris pour une parties, etc.
 * 
 * @author Valentin Delaye
 * @version 1.0
 */
public class Data extends Observable
{
	/**
	 * Informations sur les parties en cours
	 */
	private GamesInfo gamesInfo = new GamesInfo();
	
	/**
	 * Informations sur les scores
	 */
	private ScoresInfo scoresInfo = new ScoresInfo();
	
	/**
	 * Informations sur les utilisateurs.
	 */
	private PlayersInfo playersInfo = new PlayersInfo();
	
	/**
	 * Permet de charger les scores recu du serveur
	 * @param user La liste des utilisateurs
	 * @param score La liste des scores
	 * @param admin Liste de boolean pour indiquer le status d'administrateur
	 */
	public void loadScores(ArrayList<String> user, ArrayList<Integer> score,
			ArrayList<Boolean> admin)
	{
		this.scoresInfo.loadData(user, score, admin);
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Permet de charger la liste des players inscris pour une partie
	 * @param players La liste des joueurs
	 */
	public void loadPlayers(ArrayList<String> players)
	{
		this.playersInfo.loadData(players);
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Permet de charger les informations concernant les parties
	 * @param id La liste des ID de parties
	 * @param owners La liste des createurs des parties
	 * @param types La liste des types de partie
	 * @param nbPlayers Le nombre de joueur des parties
	 */
	public void loadGames(ArrayList<Integer> id, ArrayList<String> owners,
			ArrayList<Integer> types, ArrayList<Integer> nbPlayers)
	{
		this.gamesInfo.loadData(id, owners, types, nbPlayers);
		setChanged();
		notifyObservers();
	}

	/**
	 * Permet de retourner les informations sur les jeux
	 * @return Les informations sur les jeux
	 */
	public GamesInfo getGamesInfo()
	{
		return this.gamesInfo;
	}

	/**
	 * Permet de retourner les informations sur les scores
	 * @return Les informations sur les scores
	 */
	public ScoresInfo getScoresInfo()
	{
		return this.scoresInfo;
	}
	
	/**
	 * Permet de retourner les informations sur les joueurs d'une partie
	 * @return Les informations sur les joueur d'une partie
	 */
	public PlayersInfo getPlayersInfo()
	{
		return this.playersInfo;
	}
	
}

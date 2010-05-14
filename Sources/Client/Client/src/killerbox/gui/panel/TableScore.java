package killerbox.gui.panel;

import java.util.*;

import javax.swing.table.*;

/**
 * Represente les donnnes contenues dans le tableau d'affichage des scores.
 * Ce model est egalement utilise dans le PanelAdminScore. Celui-ci contient
 * 3 listes (utilisateur, scores, admin). Admin est un boolean qui indique
 * si l'utilisateur est un administrateur.
 * @author Valentin Delaye
 *
 */
@SuppressWarnings("serial")
public class TableScore extends AbstractTableModel
{
	
	/**
	 * Les differents utilisateurs
	 */
	private ArrayList<String> users = new ArrayList<String>();
	
	/**
	 * Les differents scores
	 */
	private ArrayList<Integer> scores = new ArrayList<Integer>();
	
	/**
	 * Indication si administrateur
	 */
	private ArrayList<Boolean> admin = new ArrayList<Boolean>(); 
	
	/**
	 * Nom de colonne
	 */
	private String[] header = {"Nom d'utilisateur", "Score", "Administrateur"};

	/**
	 * Retourne le nombre de colonne de la table
	 */
	@Override
	public int getColumnCount()
	{
		return this.header.length;
	}

	/**
	 * Permet de charger les donnees de score
	 * @param user La liste des utilisateur
	 * @param score La liste des scores
	 * @param admin La liste des information sur les admin
	 */
	public void loadData(ArrayList<String> users, ArrayList<Integer> scores, ArrayList<Boolean> admin)
	{		
		this.users = users;
		this.scores = scores;
		this.admin = admin;
	}
	
	/**
	 * Retourne la liste des utilisateur
	 * @return Les utilisateur
	 */
	public ArrayList<String> getUsers()
	{
		return users;
	}

	/**
	 * Retourne la liste des scores
	 * @return La liste des scores
	 */
	public ArrayList<Integer> getScores()
	{
		return scores;
	}

	/**
	 * Retourne les informations des 
	 * @return Les informations sur les admin
	 */
	public ArrayList<Boolean> getAdmin()
	{
		return admin;
	}

	/**
	 * Retourne le nombre de ligne de la table
	 */
	@Override
	public int getRowCount()
	{
		return users.size();
	}
	
	/**
	 * Permet d'ajouter un nouvel utilisateur dans la table
	 * @param user L'utilisateur
	 * @param score Le score
	 */
	public void addScore(String user, int score, boolean admin)
	{
		this.users.add(user);
		this.scores.add(score);
		this.admin.add(admin);
	}
	
	/**
	 * Retourne le nom de la colonne
	 */
	@Override
	public String getColumnName(int column)
	{
		return this.header[column];
	}

	/**
	 * Retourne la valeur a une ligne est une colone
	 */
	@Override
	public Object getValueAt(int row, int col)
	{
		try
		{
			if(col == 0)
				return users.get(row);
			else if(col == 1)
				return scores.get(row);
			else if(col == 2)
				return admin.get(row) ? "Oui" : "Non";
		}
		catch (IndexOutOfBoundsException e)
		{
			return null;
		}
		
		return null;

	}

}

package killerbox;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

/**
 * Permet de representer des informations sur la liste des joueurs
 * @author Valentin Delaye
 */
@SuppressWarnings("serial")
public class PlayersInfo extends AbstractTableModel
{

	/**
	 * Les differents utilisateurs
	 */
	private ArrayList<String> players = new ArrayList<String>();

	/**
	 * Nom de colonne
	 */
	private String[] header = { "Nom d'utilisateur" };
	
	/**
	 * Permet de charger les donnees des joueurs
	 * @param players La liste des joueurs
	 */
	public void loadData(ArrayList<String> players)
	{
		this.players = players;
	}
	
	/**
	 * Retourne la liste des utilisateur
	 * @return Les utilisateur
	 */
	public ArrayList<String> getPlayers()
	{
		return this.players;
	}

	/**
	 * Une seule colonne, le nom du joueur.
	 */
	@Override
	public int getColumnCount()
	{
		return 1;
	}

	/**
	 * Retourner le nombre de ligne
	 */
	@Override
	public int getRowCount()
	{
		return this.players.size();
	}

	/**
	 * Retourne une valeur a une position donnee. Null si erreur
	 */
	@Override
	public Object getValueAt(int row, int col)
	{
		try
		{
			if (col == 0)
				return players.get(row);
		}
		catch (IndexOutOfBoundsException e)
		{
			return null;
		}

		return null;

	}

	/**
	 * Retourne le nom de la colonne
	 */
	@Override
	public String getColumnName(int col)
	{
		if (col < this.header.length)
			return this.header[col];
		else
			return null;
	}
}

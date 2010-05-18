package killerbox;

import java.util.*;
import javax.swing.table.*;

/**
 * Permet de representer la liste des parties disponibles.
 * Une partie est caracterise par un numero, le createur de la partie, le type
 * de partie ainsi que le nombre de joueur inscrit a la partie.
 */
@SuppressWarnings("serial")
public class GamesInfo extends AbstractTableModel
{

	/**
	 * Les ID des parties.
	 */
	private ArrayList<Integer> id = new ArrayList<Integer>();

	/**
	 * Les proprietaires des parties.
	 */
	private ArrayList<String> owners = new ArrayList<String>();

	/**
	 * Les types de parties.
	 */
	private ArrayList<Integer> types = new ArrayList<Integer>();

	/**
	 * Le nombre de joueurs par parties.
	 */
	private ArrayList<Integer> nbPlayers = new ArrayList<Integer>();

	private String[] header = new String[] { "Id", "Createur", "Type", "Joueurs" };

	/**
	 * Permet de charger les informations concernant les parties
	 * @param id La liste des ID de parties
	 * @param owners La liste des createurs des parties
	 * @param types La liste des types de partie
	 * @param nbPlayers Le nombre de joueur des parties
	 */
	public void loadData(ArrayList<Integer> id, ArrayList<String> owners,
			ArrayList<Integer> types, ArrayList<Integer> nbPlayers)
	{
		this.id = id;
		this.owners = owners;
		this.types = types;
		this.nbPlayers = nbPlayers;
	}

	/**
	 * Retourne le nombre de colonne
	 */
	@Override
	public int getColumnCount()
	{
		return header.length;
	}

	/**
	 * Permet de retourner le nombre de ligne
	 */
	@Override
	public int getRowCount()
	{
		return this.id.size();
	}

	/**
	 * Permet de retourner la valeur
	 * @param row Numero de la ligne
	 * @param col Numero de la colone
	 */
	@Override
	public Object getValueAt(int row, int col)
	{
		try
		{
			switch (col)
			{
				case 0:
					return this.id.get(row);
				case 1:
					return this.owners.get(row);
				case 2:
					return this.types.get(row) == 0 ? "Tous vs Tous" : "Par equipe";
				case 3:
					return this.nbPlayers.get(row) + "/8";
				default:
					return null;
			}
		}
		catch(IndexOutOfBoundsException e)
		{
			return null;
		}
	}

	/**
	 * Permet de retourner le nom d'une colonne
	 */
	@Override
	public String getColumnName(int col)
	{
		if(col < this.header.length)
			return this.header[col];
		else
			return null;
	}

}

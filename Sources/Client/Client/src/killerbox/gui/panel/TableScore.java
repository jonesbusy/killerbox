package killerbox.gui.panel;

import java.util.*;
import javax.swing.table.*;

/**
 * Represente une table de score
 * @author valentin
 *
 */
@SuppressWarnings("serial")
public class TableScore extends AbstractTableModel
{
	
	/**
	 * Les differents utilisateur
	 */
	private ArrayList<String> users = new ArrayList<String>();
	private ArrayList<String> scores = new ArrayList<String>();

	/**
	 * Retourne le nombre de colonne de la table
	 */
	@Override
	public int getColumnCount()
	{
		return 2;
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
	public void setScore(String user, String score)
	{
		this.users.add(user);
		this.scores.add(score);
	}

	/**
	 * Retourne la valeur a une ligne est une colone
	 */
	@Override
	public String getValueAt(int row, int col)
	{
		if(col == 0)
			return users.get(row);
		else if(col == 1)
			return scores.get(row);
		
		return null;
	}

}

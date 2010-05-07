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
	private ArrayList<Integer> scores = new ArrayList<Integer>();
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
	public void setScore(String user, int score, boolean admin)
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
		if(col == 0)
			return users.get(row);
		else if(col == 1)
			return scores.get(row);
		else if(col == 3)
			return admin.get(row);
		
		return null;
	}

}

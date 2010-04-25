import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;



@SuppressWarnings("serial")
public class FenetreComptes extends FenetreBase 
{
	private JTable tab = new JTable(new MonModel());
	private JScrollPane scrollPane = new JScrollPane(tab);
	private Container ContenuGrid = new Container();
	private Container c = getContentPane();
	private JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
	private GridLayout grid = new GridLayout(3,2);
	private JLabel label = new JLabel("Action sur selection: ");
	private JPanel panel = new JPanel(); 
	private JPanel vide = new JPanel();
	private JPanel vide2 = new JPanel();
	private JPanel btValider = new JPanel();
	private JPanel btfermer = new JPanel();
	private JButton valider = new JButton("Valider l'action    ");
	private JButton fermer = new JButton( "Fermer la gestion"); 
	private String[] elemLinste = {"Supprimer compte","Mettre admin","Mettre utilsateur", "reinitialiser m-d-p"};
	private JComboBox liste = new JComboBox(elemLinste);
	
	public FenetreComptes(int hauteur, int largeur) 
	{
		super(hauteur, largeur);
		
		tab.setPreferredScrollableViewportSize(new Dimension(500, 70));
        tab.setFillsViewportHeight(true);
        
        liste.setSelectedIndex(0);
	
		ContenuGrid.setLayout(grid);
		ContenuGrid.add(label);
		ContenuGrid.add(vide);
		ContenuGrid.add(liste);
		btValider.add(valider);
		ContenuGrid.add(btValider);
		ContenuGrid.add(vide2);
		btfermer.add(fermer);
		ContenuGrid.add(btfermer);
		panel.add(ContenuGrid);
		
		splitPane.setDividerLocation(210);
		splitPane.setDividerSize(2);
		splitPane.add(scrollPane);
		splitPane.add(panel);
		
		c.add(splitPane);
		
	}
	
	class MonModel extends AbstractTableModel
	{
		private String[] nomColonnes = {"Pseudo","Score","Administrateur"};
		private Object[][] data = {{"Toto",new Integer(1000),estAdmin(true)},
						   		   {"Killer_STAR", new Integer(980),estAdmin(false)}
								  };

		public int getColumnCount() 
		{
			return nomColonnes.length;
		}

		public int getRowCount() 
		{	
			return data.length;
		}
		
		public String getColumnName(int col) 
		{
			return nomColonnes[col];
		}
	        
		public Object getValueAt(int ligne, int col) 
		{
			return data[ligne][col];
		}
		
		public boolean isCellEditable() 
		{
			return false;
        }		
	}

	private String estAdmin(Boolean bool)
	{
		return bool?"OUI":"NON";
	}
	
}

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JPanel;




@SuppressWarnings("serial")
public class FenetreUser extends FenetreBase
{
	private int x;
	private int y;
	
	JButton nvllePartie = new JButton("Créer une partie");
	JButton rejdrePartie = new JButton("Rejoindre une partie");
	GridLayout bigLayout = new GridLayout(3,0);
	GridLayout layout = new GridLayout(3,0);
	Container bigc = getContentPane();
	Container c = new Container();
	JPanel panel1 = new JPanel();
	JPanel panel2 = new JPanel();
	JPanel vide   = new JPanel();
	
	
	
	public FenetreUser(int hauteur, int largeur) 
	{
		super(hauteur, largeur);
		
		panel1.add(nvllePartie);
		panel2.add(rejdrePartie);	
		c.add(panel1);
		c.add(panel2);
		c.setLayout(layout);
		bigc.add(vide);
		bigc.add(c);		
		bigc.setLayout(bigLayout);
	}

	

}

import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class FenetreAdmin extends FenetreUser 
{

	JButton gestion = new JButton("Gestion des comptes");
	JPanel panel = new JPanel();
	
	public FenetreAdmin(int hauteur, int largeur) 
	{
		super(hauteur, largeur);
		panel.add(gestion);
		super.c.add(panel);
	}

}

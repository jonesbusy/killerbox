import java.awt.Dimension;
import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;


@SuppressWarnings("serial")
public class FenetreBase extends JFrame 
{
	JMenuBar menuBar 			= new JMenuBar();
	JMenu    menu    			= new JMenu("Fichier");
	JMenu    menuPartie 		= new JMenu("Partie");
	JMenu    menuInfo			= new JMenu("?");
	JMenuItem creer   			= new JMenuItem("Creer une partie");
	JMenuItem rejoindre 		= new JMenuItem("Rejoindre une partie");
	JMenuItem quitterPartie  	= new JMenuItem("Quitter la partie");
	JMenuItem aProposDe 		= new JMenuItem("Auteurs");
	JMenuItem quitter   		= new JMenuItem("Quitter");	
	
	public FenetreBase(int hauteur, int largeur)
	{	
		this.setTitle("KillerBox");
		this.setSize(largeur, hauteur);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true); 
        this.setResizable(false);
		
		// le sous menu fichier
		menu.add(quitter);
		
		// le sous menu Partie
		menuPartie.add(creer);
		menuPartie.add(rejoindre);
		menuPartie.add(quitterPartie);
		
		// le sous Menu ?
		menuInfo.add(aProposDe);
		
		// la barre de manu
		setJMenuBar(menuBar);
		menuBar.add(menu);
		menuBar.add(menuPartie);
		menuBar.add(menuInfo);
					
	}
	
}

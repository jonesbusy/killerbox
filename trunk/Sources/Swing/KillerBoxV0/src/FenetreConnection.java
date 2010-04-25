import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class FenetreConnection extends FenetreBase 
{
	
	private GridLayout extern = new GridLayout(3,0);
	private GridLayout intern = new GridLayout(2,2);
	private JPanel vide   = new JPanel();
	private JPanel panel1   = new JPanel();
	private JPanel panel2   = new JPanel();
	private JPanel panel3   = new JPanel();
	private JPanel panel4   = new JPanel();
	private JPanel tout   = new JPanel();
	private JButton annuler = new JButton("annuler");
    private JButton seConnecter = new JButton("Se Connecter");
    private JLabel message = new JLabel("Adresse IP du serveur: ");
    private JTextField textField = new JTextField(10);
    private Container c = getContentPane();
    private Container contenu = new Container();
	
	public FenetreConnection(int hauteur, int largeur) 
	{
		super(hauteur, largeur);
		
		c.setLayout(extern);
		c.add(vide);
		
		panel1.add(message);
		panel2.add(textField);
		panel3.add(annuler);
		panel4.add(seConnecter);
		contenu.add(panel1);
		contenu.add(panel2);
		contenu.add(panel3);
		contenu.add(panel4);
		contenu.setLayout(intern);
		tout.add(contenu);
		
		c.add(tout);
	}

}

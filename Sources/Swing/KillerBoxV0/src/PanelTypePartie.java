import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class PanelTypePartie extends JPanel
{
	
	private JPanel superp = new JPanel();
	private Container contenu = new Container();
	private GridLayout extern = new GridLayout(3,0);
	private GridLayout intern = new GridLayout(1,2);
	private JLabel label = new JLabel("Types de partie: ");
	private JPanel titre = new JPanel();
	private JPanel options = new JPanel();
	private JPanel vide = new JPanel();
	private JPanel btcreer = new JPanel();
	private JButton creer = new JButton("Creer");
	private String[] elemLinste = {"-- Type de partie","Tous vs Tous","Par equipes"};
	private JComboBox liste = new JComboBox(elemLinste);
	
	
	PanelTypePartie()
	{	
		liste.setSelectedIndex(0);
		contenu.setLayout(intern);
		
		titre.add(label);
		contenu.add(titre);
		options.add(liste);
		contenu.add(options);
		btcreer.add(creer);
		
		setLayout(extern);
		add(vide);
		add(contenu);
		add(btcreer);
	}

}

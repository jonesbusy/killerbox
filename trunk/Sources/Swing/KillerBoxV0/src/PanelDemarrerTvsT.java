import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;


@SuppressWarnings("serial")
public class PanelDemarrerTvsT extends PanelSpec
{
	private int nbreJoueur = 7;
	private int nbreJoueurMax = 8;
	
	private JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
	private JLabel joueurs = new JLabel("Joueur (" + nbreJoueur + " / " + nbreJoueurMax +" ):",JLabel.CENTER);
	private JTextArea text = new JTextArea();
	private JButton annuler = new JButton("Annuler");
	private JButton demarrer = new JButton("Demarrer");
	
	public PanelDemarrerTvsT(FenetreBase fenetreBase) 
	{
		super(fenetreBase);
		
		splitPane.setPreferredSize(new Dimension(400,300));
		splitPane.setDividerLocation(35);		
		splitPane.setDividerSize(0);
		
		text.setEditable(false);
		splitPane.add(joueurs);
		splitPane.add(text);
		
		add(splitPane);
		add(annuler);
		add(demarrer);
	}
	
	public String newLine()
	{
		return "\n";
	}
}

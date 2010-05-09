import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;


public class PanelDemarrerEquipe extends PanelSpec
{
	private int nbreJoueurRouge = 3;
	private int nbreJoueurMaxRouge = 4;
	private int nbreJoueurBleu = 2;
	private int nbreJoueurMaxBleu = 4;

	private JSplitPane splitV = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
	private JSplitPane splitH = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
	private JLabel labelGauche = new JLabel("Equipe rouge( " + nbreJoueurRouge + " / " + nbreJoueurMaxRouge +" ):");
	private JLabel labelDroit = new JLabel("Equipe bleu( " + nbreJoueurBleu + " / " + nbreJoueurMaxBleu +" ):");
	private JTextArea textD = new JTextArea(); 
	private JTextArea textG = new JTextArea();
	private JButton annuler = new JButton("Annuler");
	private JButton demarrer = new JButton("Demarrer");
	private JPanel labels = new JPanel();
	private GridLayout grid = new GridLayout(0,2);
	
	
	public PanelDemarrerEquipe(FenetreBase fenetreBase) 
	{
		super(fenetreBase);
		
		splitH.setPreferredSize(new Dimension(400,265));
		splitH.setDividerLocation(195);		
		splitH.setDividerSize(1);
		
		splitV.setPreferredSize(new Dimension(400,300));
		splitV.setDividerLocation(35);		
		splitV.setDividerSize(0);
		
		textD.setEditable(false);
		textG.setEditable(false);
		
		labels.setLayout(grid);
		labels.add(labelGauche);
		labels.add(labelDroit);
		
		splitH.add(textG);
		splitH.add(textD);
	
		splitV.add(labels);		
		splitV.add(splitH);
		
		add(splitV);
		add(annuler);
		add(demarrer);
		
	}

}

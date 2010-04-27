import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

@SuppressWarnings("serial")
public class PaneBienvenue extends PanelSpec implements ActionListener
{
	JLabel bienvenue			= new JLabel("Bienvenue et bla bla bla");
	JPanel btjouer				= new JPanel();
	JPanel label				= new JPanel();
	JPanel vide					= new JPanel();
	JButton jouer				= new JButton("Jouer!!");
	GridLayout grid				= new GridLayout(3,0);

	public PaneBienvenue(FenetreBase fenetreBase)
	{
		super(fenetreBase);
		
		setLayout(grid);
		add(vide);
		label.add(bienvenue);
		add(label);
		btjouer.add(jouer);
		jouer.addActionListener(this);
		add(btjouer);
	}
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == jouer)
		{	
			fenetreBase.setPanelType(PanelType.Principale);
		}
		
	}
}

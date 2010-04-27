import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class PaneBienvenue extends JPanel implements ActionListener
{
	JLabel bienvenue			= new JLabel("Bienvenue et bla bla bla");
	JPanel btjouer				= new JPanel();
	JPanel label				= new JPanel();
	JPanel vide					= new JPanel();
	JButton jouer				= new JButton("Jouer!!");
	GridLayout grid				= new GridLayout(3,0);

	public PaneBienvenue()
	{
		setLayout(grid);
		add(vide);
		label.add(bienvenue);
		add(label);
		btjouer.add(jouer);
		jouer.addActionListener(this);
		add(btjouer);
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == jouer)
		{	
			removeAll();
			add(new PanelPrincipale());
			validate();
			repaint();
		}
		
	}
}

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;




@SuppressWarnings("serial")
public class PanelUserAdmin extends PanelSpec implements ActionListener
{
	JButton nvllePartie = new JButton("Créer une partie");
	JButton rejdrePartie = new JButton("Rejoindre une partie");
	JButton gestion = new JButton("Gestion des comptes");
	GridLayout bigLayout = new GridLayout(3,0);
	GridLayout layout = new GridLayout(3,0);
	Container c = new Container();
	JPanel panel1 = new JPanel();
	JPanel panel2 = new JPanel();
	JPanel vide   = new JPanel();
	JPanel panelgestion = new JPanel();
	JPanel tout = new JPanel();
	
	public PanelUserAdmin(FenetreBase fenetreBase) 
	{
		super(fenetreBase);
		
		tout.setLayout(bigLayout);
		panel1.add(nvllePartie);
		panel2.add(rejdrePartie);
		c.setLayout(layout);
		c.add(panel1);
		c.add(panel2);
		// if (admin){
		panelgestion.add(gestion);
		c.add(panelgestion);
		//}
		tout.add(vide);
		tout.add(c);
		
		add(tout);
		
		nvllePartie.addActionListener(this);
		rejdrePartie.addActionListener(this);
		gestion.addActionListener(this);
	}
		
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == nvllePartie)
		{
			fenetreBase.setPanelType(PanelType.TypePartie);
		}
		
		if (e.getSource() == rejdrePartie)
		{
			fenetreBase.setPanelType(PanelType.Connection);
		}
		
		if (e.getSource() == gestion)
		{
			fenetreBase.setPanelType(PanelType.Comptes);
		}
		
	}
}

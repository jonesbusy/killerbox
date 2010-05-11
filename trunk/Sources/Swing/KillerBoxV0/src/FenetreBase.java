import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class FenetreBase extends JFrame implements ActionListener 
{
	JMenuBar menuBar 			= new JMenuBar();
	JMenu    menu    			= new JMenu("Fichier");
	JMenu    menuPartie 		= new JMenu("Partie");
	JMenu    menuInfo			= new JMenu("?");
	JMenuItem creer   			= new JMenuItem("Creer une partie");
	JMenuItem rejoindre 		= new JMenuItem("Rejoindre une partie");
	JMenuItem quitterPartie  	= new JMenuItem("Quitter la partie");
	JMenuItem aProposDe 		= new JMenuItem("A propos de");
	JMenuItem quitter   		= new JMenuItem("Quitter");	
	JDialog infos				= new JDialog();
	
	// Cr�ation des pannels
	JPanel panelBienvenue 		= new PaneBienvenue(this);
	JPanel panelComptes 		= new PanelComptes(this);
	JPanel panelConnection 		= new PanelConnection(this);
	JPanel panelCreationCompte 	= new PanelCreationCompte(this);
	JPanel panelPrincipal 		= new PanelPrincipale(this);
	JPanel panelTypePartie 		= new PanelTypePartie();
	JPanel panelUserAdmin 		= new PanelUserAdmin(this);
	JPanel panelDemarrer		= new PanelDemarrerTvsT(this);
	JPanel panelDemarrerE		= new PanelDemarrerEquipe(this);
	
	FenetreBase(int hauteur, int largeur)
	{	
		this.setTitle("KillerBox");
		this.setSize(largeur, hauteur);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
		
		// le sous menu fichier
		menu.add(quitter);
		quitter.addActionListener(this);
		
		// le sous menu Partie
		menuPartie.add(creer);
		menuPartie.add(rejoindre);
		menuPartie.add(quitterPartie);
		rejoindre.addActionListener(this);
		
		// le sous Menu ?
		menuInfo.add(aProposDe);
		aProposDe.addActionListener(this);
		
		// la barre de manu
		setJMenuBar(menuBar);
		menuBar.add(menu);
		menuBar.add(menuPartie);
		menuBar.add(menuInfo);
		
		setPanelType(PanelType.DemarrerEquipe);
		
		// Afficher la fen�tre
        this.setVisible(true); 
	}
	
	private void changerPanel(JPanel panel)
	{
		getContentPane().removeAll();
		getContentPane().add(panel);
		getContentPane().validate();
		getContentPane().repaint();
	}
	
	public void setPanelType(PanelType panelType)
	{
		switch (panelType) {
		case Bienvenue:
			changerPanel(panelBienvenue);
			break;
		case Principale:
			changerPanel(panelPrincipal);
			break;
		case Comptes:
			changerPanel(panelComptes);
			break;
		case Connection:
			changerPanel(panelConnection);
			break;
		case CreationCompte:
			changerPanel(panelCreationCompte);
			break;
		case TypePartie:
			changerPanel(panelTypePartie);
			break;
		case UserAdmin:
			changerPanel(panelUserAdmin);
			break;
		case Demarrer:
			changerPanel(panelDemarrer);
			break;
		case DemarrerEquipe:
			changerPanel(panelDemarrerE);
			break;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == aProposDe)
			JOptionPane.showMessageDialog(this,
		            "Projet GEN 09/10 \n\n" +
		            "Auteurs :\n" +
		            "Berdoz Jonas\n" +
		            "Beretta Piccoli Fabrizio\n" +
		            "Delaye Valentin\n" +
		            "Sandoz Michael",
		            "A propos de",1
		            );
		
		if(e.getSource() == rejoindre)
		{	
			setPanelType(PanelType.Connection);
		}
			
		if (e.getSource() == quitter)
			System.exit(0);					
	}
	
}
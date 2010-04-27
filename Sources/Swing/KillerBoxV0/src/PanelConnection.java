import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


@SuppressWarnings("serial")
public class PanelConnection extends JPanel implements ActionListener 
{
	private GridLayout extern = new GridLayout(3,0);
	private GridLayout intern = new GridLayout(2,2);
	private JPanel vide   = new JPanel();
	private JPanel panel1   = new JPanel();
	private JPanel panel2   = new JPanel();
	private JPanel panel3   = new JPanel();
	private JPanel panel4   = new JPanel();
	private JPanel tout   = new JPanel();
	private JButton annuler = new JButton("Annuler");
    private JButton seConnecter = new JButton("Se Connecter");
    private JLabel message = new JLabel("Adresse IP du serveur: ");
    private JTextField textField = new JTextField(10);
    private Container contenu = new Container();
	
	public PanelConnection()
	{	
		setLayout(extern);
		add(vide);
		
		annuler.addActionListener(this);
		seConnecter.addActionListener(this);
		
		panel1.add(message);
		panel2.add(textField);
		panel3.add(annuler);
		panel4.add(seConnecter);
		contenu.setLayout(intern);
		contenu.add(panel1);
		contenu.add(panel2);
		contenu.add(panel3);
		contenu.add(panel4);
		tout.add(contenu);
	
		add(tout);
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == annuler)
		{	
			removeAll();
			add(new PanelPrincipale());
			validate();
			repaint();
		}
		
		if (e.getSource() == seConnecter)
		{	
			removeAll();
			add(new PanelUserAdmin());
			validate();
			repaint();
		}
		
	}

}

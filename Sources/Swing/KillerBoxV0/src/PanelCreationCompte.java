import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


public class PanelCreationCompte extends PanelSpec implements ActionListener 
{
	private final String textFieldString = "nom utilisateur ";
    private final String passwordFieldString = "mot de passe ";
    private final String confirm = "confirmer mot de passe ";
    private JTextField textField = new JTextField(10);
    private JPasswordField passwordField = new JPasswordField(10);
    private JPasswordField confirmation = new JPasswordField(10);
    private JButton creercompte = new JButton("Creer le compte");
    private JButton annuler = new JButton("Annuler");

	public PanelCreationCompte(FenetreBase fenetreBase) 
	{
		super(fenetreBase);
		
		JLabel textFieldLabel = new JLabel(textFieldString + ": ");
	    textFieldLabel.setLabelFor(textField);
	    JLabel passwordFieldLabel = new JLabel(passwordFieldString + ": ");
	    passwordFieldLabel.setLabelFor(passwordField);
	    JLabel confirmLabel = new JLabel(confirm + ": ");
	    confirmLabel.setLabelFor(confirmation);
	    
	    JPanel textControlsPane = new JPanel();
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        textControlsPane.setLayout(gridbag);

        JLabel[] labels = {textFieldLabel, passwordFieldLabel, confirmLabel};
        JTextField[] textFields = {textField, passwordField, confirmation};
        PanelPrincipale.addLabelTextRows(labels, textFields, gridbag, textControlsPane);

        c.gridwidth = GridBagConstraints.REMAINDER; //last
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 1.0;
        textControlsPane.add(annuler, c);
        textControlsPane.add(creercompte, c);
        textControlsPane.setBorder(
                BorderFactory.createCompoundBorder(
                                BorderFactory.createTitledBorder("Creer un compte"),
                                BorderFactory.createEmptyBorder(0,25,0,25)));
       
        
        JPanel leftPane = new JPanel(new BorderLayout());
        leftPane.add(textControlsPane, 
                     BorderLayout.CENTER);
        
        add(leftPane, BorderLayout.CENTER);
        
        creercompte.addActionListener(this);
        annuler.addActionListener(this);
        
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == annuler)
		{	
			fenetreBase.setPanelType(PanelType.UserAdmin);
		}
		
	}
	
}

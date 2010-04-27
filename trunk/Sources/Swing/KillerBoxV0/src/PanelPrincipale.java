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


@SuppressWarnings("serial")
public class PanelPrincipale extends PanelSpec implements ActionListener 
{
	

	private final String textFieldString = "nom utilisateur";
    private final String passwordFieldString = "mot de passe";
    private JButton creercompte = new JButton("Creer un compte");
    private JButton seConnecter = new JButton("Se Connecter");

    public PanelPrincipale(FenetreBase fenetreBase) {
		super(fenetreBase);
		setLayout(new BorderLayout());
		
        JTextField textField = new JTextField(10);
        //textField.setActionCommand(textFieldString);
        //textField.addActionListener(this);

        JPasswordField passwordField = new JPasswordField(10);
        //passwordField.setActionCommand(passwordFieldString);
        //passwordField.addActionListener(this);

        //Create some labels for the fields.
        JLabel textFieldLabel = new JLabel(textFieldString + ": ");
        textFieldLabel.setLabelFor(textField);
        JLabel passwordFieldLabel = new JLabel(passwordFieldString + ": ");
        passwordFieldLabel.setLabelFor(passwordField);

        //Lay out the text controls and the labels.
        JPanel textControlsPane = new JPanel();
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        textControlsPane.setLayout(gridbag);

        JLabel[] labels = {textFieldLabel, passwordFieldLabel};
        JTextField[] textFields = {textField, passwordField};
        addLabelTextRows(labels, textFields, gridbag, textControlsPane);

        c.gridwidth = GridBagConstraints.REMAINDER; //last
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 1.0;
        textControlsPane.add(seConnecter, c);
        textControlsPane.add(creercompte, c);
        textControlsPane.setBorder(
                BorderFactory.createCompoundBorder(
                                BorderFactory.createTitledBorder("Identification"),
                                BorderFactory.createEmptyBorder(0,50,0,50)));
       
        
        JPanel leftPane = new JPanel(new BorderLayout());
        leftPane.add(textControlsPane, 
                     BorderLayout.CENTER);
        add(leftPane, BorderLayout.CENTER);
        
        creercompte.addActionListener(this);
        seConnecter.addActionListener(this);
	}
	
    public static void addLabelTextRows(JLabel[] labels,
    							  JTextField[] textFields,
    							  GridBagLayout gridbag,
    							  Container container) 
    {
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.EAST;
		int numLabels = labels.length;
		
		for (int i = 0; i < numLabels; i++) 
		{
		c.gridwidth = GridBagConstraints.RELATIVE; //next-to-last
		c.fill = GridBagConstraints.NONE;      //reset to default
		c.weightx = 0.0;                       //reset to default
		container.add(labels[i], c);
		
		c.gridwidth = GridBagConstraints.REMAINDER;     //end row
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;
		container.add(textFields[i], c);
		}
    }

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == seConnecter)
		{
			fenetreBase.setPanelType(PanelType.UserAdmin);
		}
		
		if (e.getSource() == creercompte)
		{	
			fenetreBase.setPanelType(PanelType.CreationCompte);
		}
				
		
	}
}

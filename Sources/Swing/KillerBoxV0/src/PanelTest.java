import javax.swing.JButton;
import javax.swing.JPanel;


public class PanelTest extends JPanel 
{
	JButton mmm = new JButton("test");
	
	public PanelTest()
	{
		JPanel panel = new JPanel();
		panel.add(mmm);
		add(panel);
	}
}

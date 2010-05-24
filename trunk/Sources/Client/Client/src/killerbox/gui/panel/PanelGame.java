package killerbox.gui.panel;

import javax.swing.*;

import killerbox.gui.BaseWindow;

public class PanelGame extends AbstractPanel
{

	public PanelGame(BaseWindow window)
	{
		super(window);
		add(new JLabel("Panel de jeu!!"));

	}

	@Override
	public JButton getDefaultButton()
	{
		// TODO Auto-generated method stub
		return null;
	}

}

package killerbox.panel;

import javax.swing.*;

import killerbox.*;
import network.*;

/**
 * Permet de representer le panel permettant de demander les informations
 * de connection
 * @author Fabrizio Beretta Piccoli
 * @author Valentin Delaye
 */
@SuppressWarnings("serial")
public class LoginPanel extends KillerBoxPanel
{

	public LoginPanel(BaseWindow base, Client client, ClientListener clientListener)
	{
		super(base, client, clientListener);
		// TODO Auto-generated constructor stub
	}

	@Override
	public JButton getDefaultButton()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void printError(String message)
	{
		// TODO Auto-generated method stub

	}

}

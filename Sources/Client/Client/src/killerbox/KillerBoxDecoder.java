package killerbox;

import network.*;
import java.util.*;

import killerbox.panel.EnumPanel;

public class KillerBoxDecoder extends Decoder
{
	
	/**
	 * Fenetre associe
	 */
	private BaseWindow fenetre;;

	/**
	 * Constructeur
	 * @param client Le client
	 * @param fenetre La fenetre
	 */
	public KillerBoxDecoder(Client client, BaseWindow fenetre)
	{
		super(client, fenetre);
		this.fenetre = fenetre;
	}

	/**
	 * Permet de decoder le message
	 */
	@Override
	public void decode(String message)
	{
		StringTokenizer tokens = new StringTokenizer(message, "#");
		String instruction = tokens.nextToken();
		
		// On recoit a propo du login
		if(instruction.equals("login"))
		{
			instruction = tokens.nextToken();
			if(instruction.equals("true"))
			{
				this.fenetre.setPanel(EnumPanel.JOIN_GAME_PANEL);
			}
			else if(instruction.equals("false"))
			{
				// Afficher une erreur
				this.fenetre.printError("Le nom d'utilisateur ou mot de passe est incorrect");
			}
				
		}
		
	}

}

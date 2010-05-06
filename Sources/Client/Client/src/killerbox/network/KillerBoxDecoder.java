package killerbox.network;

import network.*;
import java.util.*;

import killerbox.gui.*;
import killerbox.gui.panel.EnumPanel;

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
		
		try
		{
			StringTokenizer tokens = new StringTokenizer(message, "#");
			String instruction = tokens.nextToken();
			
			// Concernant le login
			if(instruction.equals("login"))
			{
				instruction = tokens.nextToken();
				
				// Connection ok
				if(instruction.equals("true"))
				{
					this.fenetre.setPanel(EnumPanel.JOIN_GAME_PANEL);
				}
				
				// Erreur d'authentification
				else if(instruction.equals("false"))
				{
					// Afficher une erreur
					this.fenetre.printError("Le nom d'utilisateur ou mot de passe est incorrect");
				}
					
			}
			
			// Repondre a une demande de modification de compte
			else if(instruction.equals("account"))
			{
				instruction = tokens.nextToken();
				
				// Reponse concernant une creation de compte
				if(instruction.equals("create"))
				{
					instruction = tokens.nextToken();
					if(instruction.equals("true"))
						this.fenetre.setPanel(EnumPanel.LOGIN_PANEL);
					
					else if(instruction.equals("false"))
						this.fenetre.printError("Nom de compte deja utilise");
				}
				
				// On recoit les informations qu'on a demande
				else if(instruction.equals("request"))
				{
					instruction = tokens.nextToken();
					
					if(instruction.equals("admin"))
					{
						// Pas le nom d'utilisateur
						tokens.nextToken();
						
						instruction = tokens.nextToken();
						if(instruction.equals("true"))
							this.fenetre.getPanel().showAdmin();
					}
				}
				
			}
		}
		
		catch(NullPointerException e)
		{
			System.out.println("Impossible de decoder : " + message);
		}
		
	}

}

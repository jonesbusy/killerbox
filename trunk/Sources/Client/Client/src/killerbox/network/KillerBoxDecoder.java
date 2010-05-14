package killerbox.network;

import network.*;

import java.util.*;

import killerbox.gui.*;
import killerbox.gui.panel.*;

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
					this.fenetre.setPanel(EnumPanel.PANEL_SET_ACCOUNT);
				}
				
				// Erreur d'authentification
				else if(instruction.equals("false"))
				{
					// Afficher une erreur
					this.fenetre.printMessage("Le nom d'utilisateur ou mot de passe est incorrect");
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
						this.fenetre.setPanel(EnumPanel.PANEL_LOGIN);
					
					else if(instruction.equals("false"))
						this.fenetre.printMessage("Nom de compte deja utilise");
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
			
			// On recoit des informations sur les scores
			else if(instruction.equals("scores"))
			{
				// Pour recuperer les scores
				ArrayList<String>user = new ArrayList<String>();
				ArrayList<Integer>score = new ArrayList<Integer>();
				ArrayList<Boolean>admin = new ArrayList<Boolean>();
				
				while(tokens.hasMoreElements())
				{
					user.add(tokens.nextToken());
					if(tokens.nextToken().equals("1"))
						admin.add(true);
					else
						admin.add(false);
					
					score.add(Integer.parseInt(tokens.nextToken()));
				}
				
				// C'est la panel de score
				this.fenetre.loadScores(user, score, admin);
			}
		}
		
		catch(NullPointerException e)
		{
			System.out.println("Impossible de decoder : " + message);
		}
		
	}

}

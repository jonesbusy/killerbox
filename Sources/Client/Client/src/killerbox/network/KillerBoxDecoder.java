package killerbox.network;

import network.*;
import java.util.*;
import killerbox.gui.*;
import killerbox.gui.panel.*;

/**
 * Classe permettant de decoder les differents messages recu du serveur. Permet 
 * ensuite d'effectuer les differentes action sur la fenetre. Les messages 
 * recu sont defini selon un protocole entre le serveur et le client.
 * @author Valentin Delaye
 * @version 1.0
 * @see Decoder
 * @see BaseWindow
 */
public class KillerBoxDecoder extends Decoder
{
	
	/**
	 * Reference sur la fenetre
	 */
	private BaseWindow fenetre;;

	/**
	 * Constructeur. Permet de creer un nouveau decodeur de message.
	 * @param client Le programme client
	 * @param fenetre La fenetre graphique
	 */
	public KillerBoxDecoder(Client client, BaseWindow fenetre)
	{
		super(client);
		this.fenetre = fenetre;
	}

	/**
	 * Permet de decoder le message et d'effectuer les differentes actions
	 * sur la fenetre.
	 */
	@Override
	public void decode(String message)
	{
		
		try
		{
			// Pour decouper les messages
			StringTokenizer tokens = new StringTokenizer(message, "#");
			String instruction = tokens.nextToken();
			
			/**
			 * Concernant le login
			 */
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
			
			/**
			 * Concernant le compte
			 */
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
			
			/**
			 * Concernant les scores
			 */
			else if(instruction.equals("scores"))
			{
				// Pour recuperer les scores. On va stocker tout ces informations
				// dans des tableaux dynamique
				ArrayList<String>user = new ArrayList<String>();
				ArrayList<Integer>score = new ArrayList<Integer>();
				ArrayList<Boolean>admin = new ArrayList<Boolean>();
				
				while(tokens.hasMoreElements())
				{
					// Ajout de l'utilisateur
					user.add(tokens.nextToken());
					
					// Information si c'est un admin
					if(tokens.nextToken().equals("1"))
						admin.add(true);
					else
						admin.add(false);
					
					// Ajouter le score (essayer de parser)
					try
					{
						score.add(Integer.parseInt(tokens.nextToken()));
					}
					catch (NumberFormatException e)
					{
						score.add(0);
					}

				}
				
				// Charger les scores dans la fenetre
				this.fenetre.loadScores(user, score, admin);
			}
		}
		
		// Si le serveur nous envoie n'importe quoi
		catch(NullPointerException e)
		{
			System.out.println("Impossible de decoder : " + message);
		}
		
	}

}

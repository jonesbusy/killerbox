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
	private BaseWindow base;

	/**
	 * Constructeur. Permet de creer un nouveau decodeur de message.
	 * @param client Le programme client
	 * @param base La fenetre graphique
	 */
	public KillerBoxDecoder(Client client, BaseWindow base)
	{
		super(client);
		this.base = base;
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
					this.base.setPanel(EnumPanel.PANEL_SET_ACCOUNT);
				}
				
				// Erreur d'authentification
				else if(instruction.equals("false"))
				{
					// Afficher une erreur
					this.base.printMessage("Le nom d'utilisateur ou mot de passe est incorrect");
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
						this.base.setPanel(EnumPanel.PANEL_LOGIN);
					
					else if(instruction.equals("false"))
						this.base.printMessage("Nom de compte deja utilise");
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
							this.base.getPanel().showAdmin();
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
				
				// Charger les scores dans la fenetre et refraichir
				this.base.loadScores(user, score, admin);
				this.base.getPanel().refreshData();
			}
			
			/**
			 * Concernant les parties
			 */
			else if(instruction.equals("game"))
			{
				instruction = tokens.nextToken();
				
				/**
				 * On recoit la liste des parties
				 */
				if(instruction.equals("list"))
				{
					// Pour recuperer les scores. On va stocker tout ces informations
					// dans des tableaux dynamique
					ArrayList<Integer> id = new ArrayList<Integer>();
					ArrayList<String> owners = new ArrayList<String>();
					ArrayList<Integer> types = new ArrayList<Integer>();
					ArrayList<Integer> nbPlayers = new ArrayList<Integer>();
					
					while(tokens.hasMoreElements())
					{
						// Ajouter l'ID de la partie
						try
						{
							id.add(Integer.parseInt(tokens.nextToken()));
						}
						catch (NumberFormatException e)
						{
							id.add(-1);
						}
						
						// Ajouter le proprietaire de la partie
						owners.add(tokens.nextToken());
						
						// Ajouter le type de partie
						try
						{
							types.add(Integer.parseInt(tokens.nextToken()));
						}
						catch (NumberFormatException e)
						{
							types.add(-1);
						}
						
						// Ajouter le nombre d'utilisateur
						try
						{
							nbPlayers.add(Integer.parseInt(tokens.nextToken()));
						}
						catch (NumberFormatException e)
						{
							nbPlayers.add(-1);
						}

					}
					
					// Charger les donnees
					this.base.loadGames(id, owners, types, nbPlayers);
					this.base.getPanel().refreshData();
					
				}
				
				/**
				 * Si on veut rejoindre une partie pleine
				 */
				else if(instruction.equals("full"))
					this.base.getPanel().printMessage("Impossible de rejoindre cette partie. Trop de joueurs");
				
				/**
				 * Fin de la partie en cours
				 */
				else if(instruction.equals("end"))
				{
					this.base.getPanel().printMessage("La partie s'est terminee");
				}
				
				/**
				 * C'est ok pour rejoindre la partie
				 */
				else if(instruction.equals("true"))
				{
					this.base.setPanel(EnumPanel.PANEL_LIST_PLAYERS_GAME_ALL);
				}
				
				/**
				 * On recoit la liste des joueur
				 */
				else if(instruction.equals("players"))
				{
					System.out.println("Liste joueur recu");
				}
			}
						
		}
		
		// Si le serveur nous envoie n'importe quoi
		catch(NullPointerException e)
		{
			System.out.println("Impossible de decoder : " + message);
		}
		
	}

}

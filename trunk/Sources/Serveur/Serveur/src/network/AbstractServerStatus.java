package network;

import static network.EnumServerStatus.*;

/**
 * Permet de representer le changement de status du serveur.
 * Chaque nouveau status est identifie par l'ID de la connexion qui a genere le
 * nouveau status. L'ID est 0 si le status n'est lie a aucune connexion
 * @author Valentin Delaye
 * @version 1.0
 */
public abstract class AbstractServerStatus
{
	
	/**
	 * Id de la connection qui a genere le message. 0 si
	 * c'est un message du serveur.
	 */
	private int id;
	
	/**
	 * Indique le status
	 */
	public EnumServerStatus status;
	
	/**
	 * Representer le message a afficher
	 */
	private String message = "";
	
	/**
	 * Constructeur. Permet de creer un nouveau status.
	 * @param id ID de la connexion
	 */
	public AbstractServerStatus(int id, EnumServerStatus status)
	{
		// Ajouter le bon id de connexion si c'est le serveur
		if(status.equals(SERVER_STATUS))
			this.id = 0;
		else
			this.id = id;
		this.status = status;
	}
	
	/**
	 * Constructeur. Permet de creer un nouveau status avec un message.
	 * @param id ID de la connexion
	 * @param status Indique le status
	 * @param message Message
	 */
	public AbstractServerStatus(int id, EnumServerStatus status, String message)
	{
		this.id = id;
		this.status = status;
		this.message = message;
	}
	
	/**
	 * ID de la connexion qui a genere le nouveau status serveur
	 * @return L'ID de la connexion
	 */
	public int getId()
	{
		return this.id;
	}
	
	/**
	 * Retourner le message de status
	 * @return
	 */
	public String getMessage()
	{
		return this.message;
	}
		
	/**
	 * Permettre d'afficher le message
	 */
	public String toString()
	{
		return this.message;
	}
}

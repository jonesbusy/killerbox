package network;

import network.Server;

/**
 * Permet de representer une classe abstraite de base, permettant de 
 * decoder une instruction (String) et d'effectuer les instructions
 * correspondantes
 * @author Valentin Delaye
 */
public abstract class Decoder
{
	/**
	 * Serveur associes
	 */
	protected Server server;
	
	/**
	 * Permet d'associer au serveur
	 * @param server
	 */
	public void setServer(Server server)
	{
		this.server = server;
	}

	/**
	 * Permet de decoder le message en provenance d'une certaine connexion
	 * @param connexion La connexion
	 * @param message Le message
	 */
	public abstract void decode(Connexion connexion, String message);
	
}

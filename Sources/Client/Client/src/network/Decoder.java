package network;

import javax.swing.*;

/**
 * Classe abstraire qui permet de decoder une instruction
 * en provenance du serveur
 * @author Valentin Delaye
 */
public abstract class Decoder
{
	/**
	 * Client associe
	 */
	protected Client client;
	
	/**
	 * Vue
	 */
	protected JFrame fenetre;
	
	/**
	 * Construit un nouveau decoder
	 * @param client Le client
	 * @param fenetre La vue
	 */
	public Decoder(Client client, JFrame fenetre)
	{
		this.client = client;
		this.fenetre = fenetre;
	}
	
	/**
	 * Permet de decoder une instruction
	 * @param message Le message a decoder
	 */
	public abstract void decode(String message);
	
}

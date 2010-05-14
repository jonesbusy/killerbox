package network;


/**
 * Classe abstraire qui permet de decoder une instruction
 * en provenance du serveur.
 * @author Valentin Delaye
 */
public abstract class Decoder
{
	/**
	 * Client associe
	 */
	protected Client client;
	
	/**
	 * Construit un nouveau decoder.
	 * @param client Programme client.
	 */
	public Decoder(Client client)
	{
		this.client = client;
	}
	
	/**
	 * Permet de decoder une instruction. Les classes derivant Decoder devront definir
	 * le protocole afin de decoder le message et d'effectuer les actions correspondantes.
	 * @param message Le message a decoder
	 */
	public abstract void decode(String message);
	
}

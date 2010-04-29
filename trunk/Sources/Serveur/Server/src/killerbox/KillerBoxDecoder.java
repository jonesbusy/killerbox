/**
 * 
 */
package killerbox;

import network.*;


/**
 * @author valentin
 *
 */
public class KillerBoxDecoder extends Decoder
{
	
	/**
	 * Serveur KillerBox
	 */
	private KillerBoxServer serverKiller;

	/**
	 * @param server
	 */
	public KillerBoxDecoder()
	{
		
	}
	
	/**
	 * Permet de setter le serveur KillerBox
	 * @param serverKiller
	 */
	public void setKillerBoxServer(KillerBoxServer serverKiller)
	{
		this.serverKiller = serverKiller;
	}

	/**
	 * 
	 */
	@Override
	public void decode(Connexion connexion, String message)
	{
				
		if(message.equals("@logout"))
			server.disconnect(connexion.getId());
		
		else
			server.sendMessage(serverKiller.getUserName(connexion.getId()) + " : " + message);
		
	}

}

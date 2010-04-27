/**
 * 
 */
package killerbox;

import network.Connexion;
import network.Decoder;
import network.Server;

/**
 * @author valentin
 *
 */
public class KillerBoxDecoder extends Decoder
{

	/**
	 * @param server
	 */
	public KillerBoxDecoder()
	{
		
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
			server.sendMessage("Client " + connexion.getId() + " : " + message);
	}

}


package network;

import network.Server;

/**
 * @author valentin
 *
 */
public abstract class Decoder
{
	
	
	protected Server server;
	
	public Decoder()
	{
		
	}
	
	public void setServer(Server server)
	{
		this.server = server;
	}

	public abstract void decode(Connexion connexion, String message);
	
	
}

package network;

import java.io.IOException;
import java.net.Socket;

/**
 * Controleur specialise pour le serveur KillerBox. Permet
 * d'envoyer differents messages predefinis au client. 
 * @author valentin
 *
 */
public class KillerBoxController extends Controller
{

	/**
	 * Permet de construire un nouveau controleur pour une connexion.
	 * @param socket
	 * @param id
	 * @param decoder
	 * @throws IOException
	 */
	public KillerBoxController(Socket socket, int id, Decoder decoder) throws IOException
	{
		super(socket, id, decoder);
	}

}

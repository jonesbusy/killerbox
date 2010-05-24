import killerbox.gui.*;

/**
 * Programme principal. Permet de lancer le programme client
 * @author Valentin Delaye
 * @version 1.0
 */
public class LanceurClient
{
	/**
	 * Programme principal
	 * 
	 * @param args Les arguments du programme
	 */
	public static void main(String[] args)
	{
		try
		{
			new BaseWindow();
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
	}
}

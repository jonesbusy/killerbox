package killerbox.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Chat extends Rectangle{
	private ModelGame modelGame;

	public Chat(ModelGame modelGame) {
		this.modelGame = modelGame;
	}
	
	public void dessiner(Graphics g)
	{
		// Récupérer les messages
		ArrayList<Message> messages = modelGame.getMessages();
		
		// Dessiner le fond
		g.setColor(Color.BLACK);
		g.fillRect(x, y, width, height);
		
		// dessiner messages
		int hauteurTexte = g.getFont().getSize();
		int nbMessagesAAfficher = height / hauteurTexte;
		int premierMessage = 0;
		int posY = y + hauteurTexte + 4;
		int posX = 10;
		Message message;
		
		if (messages.size() - nbMessagesAAfficher > 0)
			premierMessage = messages.size() - nbMessagesAAfficher;
		
		for (int i = messages.size() - 1; i >= premierMessage; i--) {
			message = messages.get(i);
			g.setColor(message.getColor());
			g.drawString(message.getMessage(), posX, posY);
			posY = posY + hauteurTexte;
		}
	}

}

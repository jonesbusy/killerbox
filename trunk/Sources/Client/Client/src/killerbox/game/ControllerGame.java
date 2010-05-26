package killerbox.game;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import killerbox.network.Joueur;

public class ControllerGame {
	
	/**
	 * Modele de la partie
	 */
	private ModelGame modelGame;

	public ControllerGame(ModelGame modelGame) {
		this.modelGame = modelGame;
	}

	public void gestionDeplacement(CarteBase carte, Joueur joueur, KeyEvent e) {
		ArrayList<Rectangle> murs = carte.getMurs();
		
		if (e.getKeyCode() == KeyEvent.VK_W)
		{
			joueur.move(0, -joueur.getVitesse());
			
			for(Rectangle mur : murs)
				if(mur.intersects(joueur.getColision()))
					joueur.move(0, joueur.getVitesse());
		}
		
		if (e.getKeyCode() == KeyEvent.VK_S)
		{
			joueur.move(0, joueur.getVitesse());
			
			for(Rectangle mur : murs)
				if(mur.intersects(joueur.getColision()))
					joueur.move(0, -joueur.getVitesse());
		}
		
		if (e.getKeyCode() == KeyEvent.VK_A)
		{
			joueur.move(-joueur.getVitesse(), 0);
			
			for(Rectangle mur : murs)
				if(mur.intersects(joueur.getColision()))
					joueur.move(joueur.getVitesse(), 0);
		}
		
		if (e.getKeyCode() == KeyEvent.VK_D)
		{
			joueur.move(joueur.getVitesse(), 0);
			
			for(Rectangle mur : murs)
				if(mur.intersects(joueur.getColision()))
					joueur.move(-joueur.getVitesse(), 0);
		}
		
	}
	
	/**
	 * M�thode permettant de cr�er et de positionner les joueurs dans le mod�le
	 * par rapport � la carte
	 */
	public void positionnerJoueurs() {
		// G�n�rer une position al�atoire
		
		// V�rifier que la position ne soit pas en conflit avec les joueurs
		// ou murs de la carte.
		
	}

}

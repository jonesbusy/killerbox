package jeu;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import killerbox.network.KillerBoxDecoder;
import killerbox.network.KillerBoxListener;

public class Controller {
	
	KillerBoxListener decoder;

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

}

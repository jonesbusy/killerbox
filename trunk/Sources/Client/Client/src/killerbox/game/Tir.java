package killerbox.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;

import killerbox.network.KillerBoxController;

public class Tir {
	
	private static final int DEGATS = 3;
	private Point source, position;
	private Double angle;
	private TypeTir tir;
	private boolean checkKill;
	private int FPS = 25;
	private ModelGame modelGame;
	private char delim = '#';
	private Tir thisTir = this; // pour les thread anonyme
	private KillerBoxController controllerReseau;
	private boolean fin = false;
	
	private Thread gestionTir = new Thread(new Runnable() {
		
		public void run() {
			
			int waitTime = 1000 / FPS;

			Point posFutur = new Point();
			
			while(!fin)
			{
				try {
					Thread.sleep(waitTime);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				// Récupérer la position futur
				posFutur.x = position.x + (int)(Math.cos(angle) * tir.getVitesse());
				posFutur.y = position.y + (int)(Math.sin(angle) * tir.getVitesse());
				
				// Vérifier que le tir n'entre pas en collision avec une personne
				// ou un objet
				for (Rectangle mur : modelGame.getCarte().getMurs()) {
					if (intersectionLigneRectangle(position,posFutur,mur))
						fin = true;
				}
				
				for (Joueur joueur : modelGame.getJoueurs()) {
					if (intersectionLigneRectangle(position,posFutur,joueur.getRectangle()))
					{
						if (checkKill)
						{
							joueur.setPv(joueur.getPv() - DEGATS);
							
							// Indiquer aux autres joueurs qu'un joueur a été touché
							controllerReseau.sendInfosGameOtherPlayers("positionJoueur#"+joueur.toString());
							
							// Indiquer aux autres joueurs que le joueur est mort
							if (joueur.getPv() <= 0)
							{
								controllerReseau.sendInfosGameOtherPlayers("joueurMort#"+joueur.getNom() + "#" + modelGame.getJoueurActif().getNom());
								modelGame.addMessage(new Message("Vous avez tué " + joueur.getNom() + "!", Color.RED));
								modelGame.removeJoueur(joueur);
							}
							else
							{
								controllerReseau.sendInfosGameMessage(modelGame.getJoueurActif().getNom() + " a touché " + joueur.getNom());
							}
						}
						
						fin = true;
					}
				}
				
				position.setLocation(posFutur);
			}
			
			// TODO peut mettre une animation de fin du tir (explosion)
			
			// Supprimer le tir de la liste
			modelGame.removeTir(thisTir);
		}

		private boolean intersectionLigneRectangle(Point source,
				Point position, Rectangle mur) {
			if (Line2D.linesIntersect(source.x, source.y, position.x, position.y, mur.x, mur.y, mur.x+mur.width, mur.y))
				return true;
			else if (Line2D.linesIntersect(source.x, source.y, position.x, position.y, mur.x+mur.width, mur.y, mur.x, mur.y+mur.height))
				return true;
			else if (Line2D.linesIntersect(source.x, source.y, position.x, position.y, mur.x, mur.y + mur.height, mur.x + mur.width, mur.y+mur.height))
				return true;
			else if (Line2D.linesIntersect(source.x, source.y, position.x, position.y, mur.x, mur.y, mur.x, mur.y+mur.height))
				return true;
				
			return false;
			
		}
	});
	
	public void dessiner(Graphics g)
	{
		g.setColor(Color.ORANGE);
		g.fillOval(position.x, position.y, 6, 6);
	}

	public Tir(Point source, Double angle, TypeTir tir, boolean checkKill, ModelGame modelGame, KillerBoxController controllerReseau) {
		super();
		this.source = source;
		this.angle = angle;
		this.tir = tir;
		this.checkKill = checkKill;
		this.modelGame = modelGame;
		this.position = new Point(source);
		this.controllerReseau = controllerReseau;
		
		gestionTir.start();
	}

	public Point getPosition() {
		return new Point(position);
	}
	
	public String toString()
	{
		return 	Integer.toString(source.x) + delim +
				Integer.toString(source.y) + delim +
				angle.toString() + delim +
				tir.toString();
	}

	public void arreter() {
		fin = true;
	}
	

	
}

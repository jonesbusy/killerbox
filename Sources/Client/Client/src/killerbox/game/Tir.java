package killerbox.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;

import killerbox.network.KillerBoxController;

public class Tir {
	
	private static final int DEGATS = 3;
	private Point position;
	private Double angle;
	private TypeTir tir;
	private int FPS = 25;
	private ModelGame modelGame;
	private char delim = '#';
	private Tir thisTir = this; // pour les thread anonyme
	private KillerBoxController controllerReseau;
	private boolean fin = false;
	private Joueur joueur;
	
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
				
				for (Joueur joueurTouche : modelGame.getJoueurs()) {
					if (joueurTouche != joueur && intersectionLigneRectangle(position,posFutur,joueurTouche.getRectangle()))
					{
						// Si le joueur est le joueur actif, on peut décrémenter la vide du joueur touché
						if (modelGame.getJoueurActif() == joueur)
						{
							joueurTouche.setPv(joueurTouche.getPv() - DEGATS);
							
							// Indiquer aux autres joueurs qu'un joueur a été touché
							controllerReseau.sendInfosGameOtherPlayers("positionJoueur#"+joueurTouche.toString());
							
							// Indiquer aux autres joueurs que le joueur est mort
							if (joueurTouche.getPv() <= 0)
							{
								modelGame.incrementerScore(joueur.getNom(), ScoreJoueur.SCORE_TUE);
								controllerReseau.sendInfosGameOtherPlayers("joueurMort#"+joueurTouche.getNom() + "#" + modelGame.getJoueurActif().getNom());
								modelGame.addMessage(new Message("Vous avez tué " + joueurTouche.getNom() + "!", Color.RED));
								modelGame.removeJoueur(joueurTouche);
							}
							else
							{
								controllerReseau.sendInfosGameMessage(modelGame.getJoueurActif().getNom() + " a touché " + joueurTouche.getNom());
								modelGame.incrementerScore(joueur.getNom(), ScoreJoueur.SCORE_TOUCHE);
							}
							
							controllerReseau.sendInfosGameOtherPlayers("score#"+joueur.getNom() + "#" + modelGame.getScore(joueur.getNom()));
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

	public Tir(Joueur joueur, Double angle, TypeTir tir, ModelGame modelGame, KillerBoxController controllerReseau) {
		super();
		this.joueur = joueur;
		this.angle = angle;
		this.tir = tir;
		this.modelGame = modelGame;
		this.position = new Point(joueur.getPosX(),joueur.getPosY());
		this.controllerReseau = controllerReseau;
		
		gestionTir.start();
	}

	public Point getPosition() {
		return new Point(position);
	}
	
	public String toString()
	{
		return 	joueur.getNom() + delim +
				angle.toString() + delim +
				tir.toString();
	}

	public void arreter() {
		fin = true;
	}
	

	
}

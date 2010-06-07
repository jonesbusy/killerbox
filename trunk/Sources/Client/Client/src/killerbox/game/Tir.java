package killerbox.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import killerbox.network.KillerBoxController;

public class Tir {
	
	private Point source, position;
	private Double angle;
	private TypeTir tir;
	private boolean checkKill;
	private int FPS = 10;
	private ModelGame modelGame;
	private char delim = '#';
	private Tir thisTir = this; // pour les thread anonyme
	private KillerBoxController controllerReseau;
	
	private Thread gestionTir = new Thread(new Runnable() {
		
		public void run() {
			boolean fin = false;
			int waitTime = 1000 / FPS;
			int depX,depY;
			
			while(!fin)
			{
				try {
					Thread.sleep(waitTime);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				depX = (int)(Math.cos(angle) * tir.getVitesse());
				depY = (int)(Math.sin(angle) * tir.getVitesse());

				position.x = position.x + depX;
				position.y = position.y + depY;
				
				// Vérifier que le tir n'entre pas en collision avec une personne
				// ou un objet
				for (Rectangle mur : modelGame.getCarte().getMurs()) {
					if (mur.contains(position))
						fin = true;
				}
				
				for (Joueur joueur : modelGame.getJoueurs()) {
					if (joueur.getRectangle().contains(position))
					{
						if (checkKill)
						{
							joueur.setPv(joueur.getPv() - 10);
							
							// Indiquer aux autres joueurs qu'un joueur a été touché
							controllerReseau.sendInfosGameOtherPlayers("positionJoueur#"+joueur.toString());
							
							// Indiquer aux autres joueurs que le joueur est mort
							if (joueur.getPv() <= 0)
							{
								controllerReseau.sendInfosGameOtherPlayers("joueurMort#"+joueur.getNom());
								modelGame.removeJoueur(joueur);
							}
						}
						
						fin = true;	
					}
				}
			}
			
			// TODO peut mettre une animation de fin du tir (explosion)
			
			// Supprimer le tir de la liste
			modelGame.removeTir(thisTir);
		}
	});
	
	public void dessiner(Graphics g)
	{
		g.setColor(Color.ORANGE);
		g.fillOval(position.x, position.y, 10, 10);
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
	

	
}

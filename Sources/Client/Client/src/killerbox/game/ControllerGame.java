package killerbox.game;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import killerbox.network.KillerBoxController;

public class ControllerGame {
	
	/**
	 * Modele de la partie
	 */
	private ModelGame modelGame;
	
	/**
	 * Controller réseau
	 */
	private KillerBoxController controllerReseau;

	public ControllerGame(ModelGame modelGame) {
		this.modelGame = modelGame;
	}

	public void gestionDeplacement(KeyEvent e) {
		Joueur joueur = modelGame.getJoueurActif();
		CarteBase carte = modelGame.getCarte();
		
		double posFutureX = joueur.getPosX();
		double posFutureY = joueur.getPosY();
		
		ArrayList<Rectangle> murs = carte.getMurs();
		/*
		if (e.getKeyCode() == KeyEvent.VK_W)
		{
			joueur.move(0, -joueur.getVitesse());
			
			for(Rectangle mur : murs)
				if(mur.intersects(joueur.getRectangle()))
					joueur.move(0, joueur.getVitesse());
		}
		
		if (e.getKeyCode() == KeyEvent.VK_S)
		{
			joueur.move(0, joueur.getVitesse());
			
			for(Rectangle mur : murs)
				if(mur.intersects(joueur.getRectangle()))
					joueur.move(0, -joueur.getVitesse());
		}
		
		if (e.getKeyCode() == KeyEvent.VK_A)
		{
			joueur.move(-joueur.getVitesse(), 0);
			
			for(Rectangle mur : murs)
				if(mur.intersects(joueur.getRectangle()))
					joueur.move(joueur.getVitesse(), 0);
		}
		
		if (e.getKeyCode() == KeyEvent.VK_D)
		{
			joueur.move(joueur.getVitesse(), 0);
			
			for(Rectangle mur : murs)
				if(mur.intersects(joueur.getRectangle()))
					joueur.move(-joueur.getVitesse(), 0);
		}
		*/
		
		if (e.getKeyCode() == KeyEvent.VK_W)
		{
			posFutureY = posFutureY - joueur.getVitesse();
		}
		
		if (e.getKeyCode() == KeyEvent.VK_S)
		{
			posFutureY = posFutureY + joueur.getVitesse();
		}
		
		if (e.getKeyCode() == KeyEvent.VK_A)
		{
			posFutureX = posFutureX - joueur.getVitesse();
		}
		
		if (e.getKeyCode() == KeyEvent.VK_D)
		{
			posFutureX = posFutureX + joueur.getVitesse();
		}
		
		// Indiquer la nouvelle position à tous les clients
		controllerReseau.sendInfosGame("positionJoueur#"+joueur.getNom()+"#"+posFutureX+"#"+posFutureY);
		
	}
	
	/**
	 * Méthode permettant de créer et de positionner les joueurs dans le modèle
	 * par rapport à la carte
	 */
	public void positionnerJoueurs() {
		// Générer une position aléatoire
		
		// Vérifier que la position ne soit pas en conflit avec les joueurs
		// ou murs de la carte.
		
	}

	public void addPlayerWithRandomPosition(String player, int lifePoint) {
	
		// Créer un nouveau joueur
		Joueur j = new Joueur(player, 0, 0, lifePoint);
		
		boolean collision = true;
		while(collision)
		{
			collision = false;
			
			// Calculer une nouvelle position aléatoire
			j.setPosX(Math.random()*modelGame.getCarte().getWidth());
			j.setPosY(Math.random()*modelGame.getCarte().getHeight());
			
			// Vérifier s'il y a une collision
			for (Joueur j2 : modelGame.getJoueurs()) {
				if (j.getRectangle().intersects(j2.getRectangle()))
				{
					collision = true;
					break;
				}
			}
			
			if (!collision)
			{
				// Vérifier les collision avec les murs de la carte
				for (Rectangle mur : modelGame.getCarte().getMurs()) {
					if (j.getRectangle().intersects(mur))
					{
						collision = true;
						break;
					}
				}
			}
		}
		
		modelGame.addJoueur(j);
		
		
	}

	public void setNetworkController(KillerBoxController controller) {
		controllerReseau = controller;		
	}
	
	public KillerBoxController getNetworkController() {
		return controllerReseau;		
	}

	public void sendModele() {
		// Envoi du nom de la carte
		controllerReseau.sendInfosGame("!owner#carte#" + modelGame.getCarte().getClass().getName());
		
		// Envoi des joueurs
		for (Joueur joueur : modelGame.getJoueurs()) {
			controllerReseau.sendInfosGame("!owner#joueur#" + joueur.toString());
		}
		
	}
	
	/**
	 * Envoie un paquet à tous les joueurs pour faire passer l'etat de leur
	 * modele à "démarré"
	 */
	public void startGame() {
		controllerReseau.sendInfosGame("start");		
	}

}

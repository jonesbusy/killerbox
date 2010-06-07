package killerbox.game;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
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
	
	public void gestionCommandes(EtatCommandes etat)
	{
		Joueur joueur = modelGame.getJoueurActif();
		CarteBase carte = modelGame.getCarte();
		
		int posInitX = joueur.getPosX();
		int posInitY = joueur.getPosY();
		
		if (etat.isHaut())
		{
			joueur.setPosY(joueur.getPosY() - joueur.getVitesse());
			
			// Gestion des collision
			if (existeCollision())
			{
				joueur.setPosY(posInitY);
			}
		}
		
		if (etat.isBas())
		{
			joueur.setPosY(joueur.getPosY() + joueur.getVitesse());
			
			// Gestion des collision
			if (existeCollision())
			{
				joueur.setPosY(posInitY);
			}
		}
		
		if (etat.isGauche())
		{
			joueur.setPosX(joueur.getPosX() - joueur.getVitesse());
			
			// Gestion des collision
			if (existeCollision())
			{
				joueur.setPosX(posInitX);
			}
		}
		
		if (etat.isDroite())
		{
			joueur.setPosX(joueur.getPosX() + joueur.getVitesse());

			// Gestion des collision
			if (existeCollision())
			{
				joueur.setPosX(posInitX);
			}
		}
		
		// Indiquer la position aux autres joueurs
		controllerReseau.sendInfosGameOtherPlayers("positionJoueur#"+joueur.toString());
		
		// Regarder s'il y a eu un tir
		if (etat.isTir())
		{
			etat.setTir(false);
			Point source = new Point(modelGame.getJoueurActif().getPosX(),modelGame.getJoueurActif().getPosY());
			Double angle = modelGame.getJoueurActif().getAngleSourisJoueur();
			Tir tir = new Tir(source, angle, new TypeTir(30),true,modelGame, controllerReseau);
			modelGame.addTir(tir);
			controllerReseau.sendInfosGameOtherPlayers("tir#"+tir.toString());
		}
	}

	private boolean existeCollision() {
		Joueur joueurActif = modelGame.getJoueurActif();
		
		// Vérification des murs
		for (Rectangle mur : modelGame.getCarte().getMurs()) {
			if (mur.intersects(joueurActif.getRectangle()))
				return true;
		}
		
		// Vérification des joueurs
		for (Joueur joueur : modelGame.getJoueurs()) {
			if (joueur != joueurActif)
				if (joueur.getRectangle().intersects(joueurActif.getRectangle()))
					return true;
		}
		
		return false;
	}

	public void addPlayerWithRandomPosition(String player, int lifePoint) {
	
		// Créer un nouveau joueur
		Joueur j = new Joueur(player, 0, 0, lifePoint);
		
		boolean collision = true;
		while(collision)
		{
			collision = false;
			
			// Calculer une nouvelle position aléatoire
			j.setPosX((int)(Math.random()*modelGame.getCarte().getWidth()));
			j.setPosY((int)(Math.random()*modelGame.getCarte().getHeight()));
			
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

	public void angleJoueurSouris(MouseEvent e) {
		Joueur j = modelGame.getJoueurActif();
		
		double angle = Math.atan((double)(e.getY() - j.getPosY())/ (e.getX() - j.getPosX()));
		
		if (e.getX() < j.getPosX())
		{
			// inverser l'angle
			angle = Math.PI + angle; 
		}
		
		j.setAngleSourisJoueur(angle);
	}

}

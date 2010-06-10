package killerbox.game;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.crypto.spec.PSource;
import javax.swing.JOptionPane;


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
		
		if (joueur != null)
		{
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
			controllerReseau.sendInfosGameOtherPlayers("positionJoueur#"+ joueur.toString());
			
			// Regarder s'il y a eu un tir
			if (etat.isTir())
			{
				etat.setTir(false);
				// Calculer la source du tir (il faut qu'elle soit en dehors du rectangle du joueur,
				// sinon il se tire dessus)
				
				Point source = new Point(joueur.getPosX(),joueur.getPosY());
				Double angle = joueur.getAngleSourisJoueur();
				
				Tir tir = new Tir(joueur, angle, new TypeTir(10),modelGame, controllerReseau);
				modelGame.addTir(tir);
				controllerReseau.sendInfosGameOtherPlayers("tir#"+tir.toString());
			}
		}
	}

	private boolean existeCollision() {
		Joueur joueurActif = modelGame.getJoueurActif();
		CarteBase carte = modelGame.getCarte();
		
		// Vérification des murs
		for (Rectangle mur : carte.getMurs()) {
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
		
		if (j != null)
		{
		
			double angle = Math.atan((double)(e.getY() - j.getPosY())/ (e.getX() - j.getPosX()));

			if (e.getX() < j.getPosX())
			{
				// inverser l'angle
				angle = Math.PI + angle; 
			}
			
			j.setAngleSourisJoueur(angle);
			
		}
	}

	public void afficherMessage(Message message) {
		modelGame.addMessage(message);
	}
	
	/**
	 * Méthode permettant d'arrêter proprement la partie
	 */
	public void arreterPartie() {
		
		// Arrêter toutes les threads de tirs
		for (Tir tir : modelGame.getTirs()) {
			tir.arreter();
		}
		
	}

	public boolean checkFinJeu() {
		if (modelGame.getJoueurs().size() == 1)
		{
			modelGame.setEtat(EtatModel.AFFICHAGE_SCORE);
			
			// si on est le dernier joueur à être en jeu, incrémenter son score
			String nom = modelGame.getJoueurActif().getNom();
			if (modelGame.getJoueurByName(nom) != null) {
				controllerReseau.requestModifyScoreByIncrement(nom, modelGame.getScore(nom));
			}
			
			return true;
		}
		
		return false;
	}

	public void afficherScores(Graphics g, Dimension dimensionPanel) {
		// Variables
		ArrayList<ScoreJoueur> scoresJoueurs = modelGame.getScores();
		Font police = new Font("Arial", Font.PLAIN, 14);
		Font policeTitre = new Font("Arial", Font.BOLD, 16);
		g.setFont(police);
		int sizePolice = g.getFont().getSize();
		Rectangle rectScore = new Rectangle();
		int marge = 10;
		String titre = "SCORES :";
		
		// Récupérer la largeur du plus grand score
		// Récupérer la largeur du plus grand nom
		int largeurScore = 0;
		int largeurNom = 0;
		for (ScoreJoueur sJ : scoresJoueurs) {
			int lengthNom = sJ.getName().length();
			int lenghtScore = String.valueOf(sJ.getScore()).length();
			
			if (lengthNom > largeurNom)
				largeurNom = lengthNom;
			
			if (lenghtScore > largeurScore)
				largeurScore = lenghtScore;
		}
		largeurScore = largeurScore * sizePolice + marge;
		largeurNom = largeurNom * sizePolice + marge;
		int largeurTitre = titre.length() * policeTitre.getSize() + marge;
		
		// Calculer le rectangle
		rectScore.height = (scoresJoueurs.size())*sizePolice + policeTitre.getSize() + 2*marge;
		if (largeurTitre + marge > largeurNom + largeurScore + marge)
			rectScore.width = largeurTitre + marge;
		else
			rectScore.width = largeurNom + largeurScore + marge;
		rectScore.x = (dimensionPanel.width - rectScore.width)/2;
		rectScore.y = (dimensionPanel.height - rectScore.height)/2;
		
		// Dessiner le rectangle
		g.setColor(Color.BLACK);
		g.fillRect(rectScore.x, rectScore.y, rectScore.width, rectScore.height);
		g.setColor(Color.ORANGE);
		g.drawRect(rectScore.x, rectScore.y, rectScore.width, rectScore.height);
		
		// Afficher le score de chaque joueur :
		int posScoreX = rectScore.x + marge;
		int posScoreY = rectScore.y + sizePolice +marge;
		
		g.setColor(Color.WHITE);
		g.setFont(policeTitre);
		g.drawString(titre, posScoreX, posScoreY);
		posScoreY = posScoreY + policeTitre.getSize();
		g.setFont(police);
		for (ScoreJoueur sJ : scoresJoueurs) {
			g.drawString(String.valueOf(sJ.getScore()), posScoreX, posScoreY);
			g.drawString(sJ.getName(), posScoreX+largeurScore, posScoreY);
			posScoreY = posScoreY + sizePolice;
		}
	}
}

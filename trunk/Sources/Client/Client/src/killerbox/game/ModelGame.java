package killerbox.game;

import java.util.ArrayList;

public class ModelGame {

	// Joueur du client
	private Joueur joueurActif;
	
	// Carte
	private CarteBase carte;
	
	// Liste des joueurs
	ArrayList<Joueur> joueurs = new ArrayList<Joueur>();
	
	public Joueur getJoueurActif() {
		return joueurActif;
	}
	
	public void setJoueurActif(Joueur joueurActif) {
		this.joueurActif = joueurActif;
	}

	/**
	 * Récupérer la liste des joueurs
	 * @return
	 */
	public ArrayList<Joueur> getJoueurs() {
		return new ArrayList<Joueur>(joueurs);
	}

}

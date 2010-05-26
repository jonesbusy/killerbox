package killerbox.game;

import java.util.ArrayList;

public class ModelGame {

	// Joueur du client
	private Joueur joueurActif;
	
	// Carte
	private CarteBase carte;
	
	// Etat du modèle
	private EtatModel etat = EtatModel.Chargement;
	
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

	public CarteBase getCarte() {
		return carte;
	}

	public void setCarte(CarteBase carte) {
		this.carte = carte;
	}

	public EtatModel getEtat() {
		return etat;
	}

	public void setEtat(EtatModel etat) {
		this.etat = etat;
	}

}

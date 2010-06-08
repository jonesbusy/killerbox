package killerbox.game;

import java.util.ArrayList;
import java.util.Observable;

public class ModelGame extends Observable{

	// Joueur du client
	private Joueur joueurActif;
	
	// Carte
	private CarteBase carte;
	
	// Etat du modèle
	private EtatModel etat = EtatModel.Chargement;
	
	// Liste des joueurs
	ArrayList<Joueur> joueurs = new ArrayList<Joueur>();
	
	public ArrayList<Message> getMessages() {
		return new ArrayList<Message>(messages);
	}

	private ArrayList<Tir> tirs = new ArrayList<Tir>();
	private ArrayList<Message> messages = new ArrayList<Message>();
	
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
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Permet d'ajouter un joueur à la liste
	 * @param joueur Le joueur à ajouter
	 */
	public void addJoueur(Joueur joueur) {
		joueurs.add(joueur);
	}
	
	/**
	 * Permet de mettre le joueur actif à partir du nom
	 * @param nomJoueur
	 */
	public void setJoueurActif(String nomJoueur) {
		for (Joueur joueur : joueurs) {
			if (joueur.getNom().equals(nomJoueur))
				joueurActif = joueur;
		}
	}
	
	/**
	 * Fonction retournant le joueur ayant le nom passé en paramètre
	 * @param nomJoueur Le joueur a retourner
	 * @return Le joueur correspondant au nom
	 */
	public Joueur getJoueurByName(String nomJoueur) {
		for (Joueur j : joueurs) {
			if (j.getNom().equals(nomJoueur))
				return j;
		}
		return null;
	}

	public void addTir(Tir tir) {
		tirs.add(tir);
	}

	public ArrayList<Tir> getTirs() {
		return new ArrayList<Tir>(tirs);
	}

	public void removeTir(Tir tir) {
		tirs.remove(tir);		
	}

	public void removeJoueur(Joueur joueur) {
		joueurs.remove(joueur);		
	}

	public void addMessage(Message message) {
		messages.add(message);
	}

}

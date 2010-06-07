package killerbox.game;

public class TypeTir {

	private int vitesse;
	
	public int getVitesse() {
		return vitesse;
	}

	public TypeTir(int vitesse) {
		this.vitesse = vitesse;
	}
	
	public String toString()
	{
		return Integer.toString(vitesse);
	}
}

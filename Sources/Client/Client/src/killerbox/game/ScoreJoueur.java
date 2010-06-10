package killerbox.game;

public class ScoreJoueur {

	public static final int SCORE_TOUCHE = 1;
	public static final int SCORE_TUE = 10;
	private String name;
	private int score = 0;

	public ScoreJoueur(String name, int score) {
		super();
		this.name = name;
		this.score = score;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public void incrementerScore(int score) {
		this.score = this.score + score;
	}
}

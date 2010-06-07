package killerbox.game;

import java.awt.Color;

public class Message {
	private String message;

	private Color color;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	public Message(String message, Color color) {
		super();
		this.message = message;
		this.color = color;
	}
}

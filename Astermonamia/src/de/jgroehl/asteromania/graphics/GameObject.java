package de.jgroehl.asteromania.graphics;

import de.jgroehl.asteromania.graphics.interfaces.Drawable;
import de.jgroehl.asteromania.graphics.interfaces.Updatable;

public abstract class GameObject implements Drawable, Updatable {

	public enum Level {
		BOTTOM, TOP
	}

	protected final static float SCREEN_MINIMUM = 0.0f;
	protected final static float SCREEN_MAXIMUM = 1.0f;
	protected float xPosition;
	protected float yPosition;
	private Level level = Level.TOP;

	public GameObject(float xPosition, float yPosition) {
		this.xPosition = xPosition;
		this.yPosition = yPosition;
	}

	public void setPosition(float xPosition, float yPosition) {
		this.xPosition = xPosition;
		this.yPosition = yPosition;
	}

	public float getX() {
		return xPosition;
	}

	public float getY() {
		return yPosition;
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

}

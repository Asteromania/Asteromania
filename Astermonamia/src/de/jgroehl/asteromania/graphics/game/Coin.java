package de.jgroehl.asteromania.graphics.game;

import android.content.Context;
import de.jgroehl.asteromania.control.GameHandler;
import de.jgroehl.asteromania.graphics.GraphicsObject;
import de.jgroehl.asteromania.graphics.animated.AnimatedGraphicsObject;

public class Coin extends AnimatedGraphicsObject {

	private static final int FRAME_COUNT = 13;
	private static final int ANIMATION_PERIOD = 50;
	private static final float V_SPEED = 0.02f;

	public Coin(float xPosition, float yPosition, Context context) {
		super(xPosition, yPosition,
				de.jgroehl.asteromania.R.drawable.rotating_coin, FRAME_COUNT,
				ANIMATION_PERIOD, context);
	}

	@Override
	public void update(GameHandler gameHandler) {

		yPosition += V_SPEED;
		if (yPosition > GraphicsObject.SCREEN_MAXIMUM)
			gameHandler.remove(this);

		if (imagesOverlap(this.xPosition, this.yPosition, this.relativeWidth,
				this.relativeHeight, gameHandler.getPlayer().getX(),
				gameHandler.getPlayer().getY(), gameHandler.getPlayer()
						.getRelativeWidth(), gameHandler.getPlayer()
						.getRelativeHeight())) {
			gameHandler.getSoundManager().playCoinSound();
			gameHandler.getPlayerInfo().addCoins(1);
			gameHandler.remove(this);
		}

		setFrame((getFrame() + 1) % getMaxFrame());

	}

}

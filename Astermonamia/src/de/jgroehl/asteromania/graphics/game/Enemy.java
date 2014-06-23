package de.jgroehl.asteromania.graphics.game;

import android.content.Context;
import android.graphics.Canvas;
import de.jgroehl.asteromania.control.GameHandler;
import de.jgroehl.asteromania.control.GameState;
import de.jgroehl.asteromania.graphics.animated.SimpleAnimatedObject;
import de.jgroehl.asteromania.graphics.game.Shot.Target;
import de.jgroehl.asteromania.graphics.game.statusBars.HpBar;
import de.jgroehl.asteromania.graphics.interfaces.Hitable;

public class Enemy extends SimpleAnimatedObject implements Hitable {

	private static final float UPPER_BOUND = 0.1f;
	private static final float LOWER_BOUND = 0.2f;
	private float speed = 0.01f;

	private boolean moveRight = true;
	private boolean moveTop = true;

	private final HpBar hpBar;

	public Enemy(float xPosition, float yPosition, int graphicsId,
			int frameCount, int animationPeriod, Context context, int lifepoints) {
		super(xPosition, yPosition, graphicsId, frameCount, animationPeriod,
				context);
		hpBar = new HpBar(lifepoints, 0.01f, context);
	}

	@Override
	public void update(GameHandler handler) {
		if (moveRight) {
			xPosition = xPosition + speed;
			if (xPosition > SCREEN_MAXIMUM - getRelativeWidth() / 2)
				moveRight = false;
		} else {
			xPosition = xPosition - speed;
			if (xPosition < SCREEN_MINIMUM - getRelativeWidth() / 2)
				moveRight = true;
		}

		if (moveTop) {
			yPosition = yPosition - speed / 2;
			if (yPosition < UPPER_BOUND)
				moveTop = false;
		} else {
			yPosition = yPosition + speed / 2;
			if (yPosition > LOWER_BOUND)
				moveTop = true;
		}

		hpBar.setPosition(xPosition, yPosition + relativeHeight);
		hpBar.setRelativeWidth(relativeWidth);

		super.update(handler);
	}

	@Override
	public void draw(Canvas c) {
		hpBar.draw(c);
		super.draw(c);
	}

	@Override
	public void getShot(GameHandler gameHandler, Shot shot) {
		if (shot.getTarget() == Target.ENEMY) {
			hpBar.setCurrentValue(hpBar.getCurrentValue() - shot.getDamage());
			if (hpBar.getCurrentValue() <= 0) {
				gameHandler.add(new Coin(xPosition, yPosition, context),
						GameState.MAIN);
				gameHandler.remove(this);
				// FIXME: Delete this line of code!
				gameHandler.add(createNormalEnemy(gameHandler.getContext()),
						GameState.MAIN);
			}
			gameHandler.remove(shot);
		}
	}

	public static Enemy createNormalEnemy(Context context) {

		return new Enemy((float) Math.random(), 0.2f,
				de.jgroehl.asteromania.R.drawable.enemy, 15, 100, context, 20);

	}

}

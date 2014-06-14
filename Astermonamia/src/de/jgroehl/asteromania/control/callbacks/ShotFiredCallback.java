package de.jgroehl.asteromania.control.callbacks;

import android.util.Log;
import de.jgroehl.asteromania.control.GameHandler;
import de.jgroehl.asteromania.control.GameState;
import de.jgroehl.asteromania.control.interfaces.EventCallback;
import de.jgroehl.asteromania.graphics.GameObject.Level;
import de.jgroehl.asteromania.graphics.game.SimpleShot;
import de.jgroehl.asteromania.graphics.game.SimpleShot.Target;

public class ShotFiredCallback implements EventCallback {

	private static final String TAG = ShotFiredCallback.class.getSimpleName();
	private final Target target;

	public ShotFiredCallback(Target target) {
		this.target = target;
	}

	@Override
	public void action(GameHandler gameHandler) {
		Log.d(TAG, "Shot fired");
		gameHandler.getSoundManager().playNormalShotSound();
		SimpleShot shot = new SimpleShot(gameHandler.getPlayer().getX(),
				gameHandler.getPlayer().getY(), target, gameHandler.getPlayer()
						.getShotSpeed(), gameHandler.getResources());
		shot.setLevel(Level.BOTTOM);
		gameHandler.add(shot, GameState.MAIN);

	}

}

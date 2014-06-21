package de.jgroehl.asteromania.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.MotionEvent;
import de.jgroehl.asteromania.control.interfaces.EventHandler;
import de.jgroehl.asteromania.control.interfaces.GraphicsHandler;
import de.jgroehl.asteromania.crypto.CryptoHandler;
import de.jgroehl.asteromania.graphics.GameObject;
import de.jgroehl.asteromania.graphics.GameObject.Level;
import de.jgroehl.asteromania.graphics.game.SpaceShip;
import de.jgroehl.asteromania.graphics.interfaces.Clickable;
import de.jgroehl.asteromania.graphics.interfaces.Drawable;
import de.jgroehl.asteromania.graphics.interfaces.Hitable;
import de.jgroehl.asteromania.graphics.interfaces.Updatable;
import de.jgroehl.asteromania.player.PlayerInfo;
import de.jgroehl.asteromania.player.PlayerInfoDisplay;

public class GameHandler implements GraphicsHandler, EventHandler {

	private Map<GameState, List<GameObject>> gameObjects = new HashMap<GameState, List<GameObject>>();

	private Map<GameState, List<Clickable>> clickableObjects = new HashMap<GameState, List<Clickable>>();

	private Map<GameObject, GameState[]> addedObjects = new HashMap<GameObject, GameState[]>();

	private Map<GameState, List<Hitable>> hitableObjects = new HashMap<GameState, List<Hitable>>();

	private List<GameObject> removedObjects = new ArrayList<GameObject>();

	private final SoundManager soundManager;

	private SpaceShip player;

	private Context context;

	private GameState state = null;

	private PlayerInfo playerInfo;

	private PlayerInfoDisplay playerInfoDisplay;

	public GameHandler(GameState state, SoundManager soundManager,
			Context context, CryptoHandler cryptoHandler) {
		this.state = state;
		for (GameState s : GameState.values()) {
			gameObjects.put(s, new ArrayList<GameObject>());
			clickableObjects.put(s, new ArrayList<Clickable>());
			hitableObjects.put(s, new ArrayList<Hitable>());
		}
		this.soundManager = soundManager;
		this.context = context;
		playerInfo = new PlayerInfo(cryptoHandler, context);

		playerInfoDisplay = new PlayerInfoDisplay(context, playerInfo);
	}

	public void update() {
		if (addedObjects.size() > 0) {
			for (GameObject gameObject : addedObjects.keySet()) {
				for (GameState state : addedObjects.get(gameObject))
					if (gameObject.getLevel().equals(Level.TOP))
						gameObjects.get(state).add(gameObject);
					else
						gameObjects.get(state).add(0, gameObject);

				if (gameObject instanceof Clickable) {
					for (GameState state : addedObjects.get(gameObject))
						clickableObjects.get(state).add((Clickable) gameObject);
				}

				if (gameObject instanceof Hitable) {
					for (GameState state : addedObjects.get(gameObject))
						hitableObjects.get(state).add((Hitable) gameObject);
				}

				if (player == null && gameObject instanceof SpaceShip) {
					player = (SpaceShip) gameObject;
				}
			}
			addedObjects.clear();
		}

		if (removedObjects.size() > 0) {
			for (GameObject gameObject : removedObjects) {
				for (List<GameObject> l : gameObjects.values()) {
					if (l.contains(gameObject))
						l.remove(gameObject);
				}
				for (List<Clickable> l : clickableObjects.values()) {
					if (l.contains(gameObject))
						l.remove(gameObject);
				}
				for (List<Hitable> l : hitableObjects.values()) {
					if (l.contains(gameObject))
						l.remove(gameObject);
				}
			}
			removedObjects.clear();
		}
	}

	@Override
	public void add(GameObject gameObject, GameState... states) {
		addedObjects.put(gameObject, states);
	}

	@Override
	public void remove(GameObject gameObject) {
		removedObjects.add(gameObject);
	}

	@Override
	public List<? extends Drawable> getAllDrawableObjects() {
		return gameObjects.get(state);
	}

	@Override
	public List<? extends Updatable> getAllUpdatableObjects() {
		return gameObjects.get(state);
	}

	@Override
	public void handleEvent(MotionEvent event, Context context,
			int screenWidth, int screenHeight) {
		for (Clickable c : clickableObjects.get(state)) {
			if (c.isClicked((int) event.getX(), (int) event.getY(),
					screenWidth, screenHeight)) {
				c.performAction(this);
			}
		}

	}

	public GameState getGameState() {
		return state;
	}

	public void setState(GameState state) {
		this.state = state;
	}

	public SoundManager getSoundManager() {
		return soundManager;
	}

	public SpaceShip getPlayer() {
		return player;
	}

	public void setPlayer(SpaceShip player) {
		this.player = player;
	}

	public Context getContext() {
		return context;
	}

	public List<Hitable> getHitableObjects() {
		return hitableObjects.get(state);
	}

	public PlayerInfo getPlayerInfo() {
		return playerInfo;
	}

	public PlayerInfoDisplay getPlayerInfoDisplay() {
		return playerInfoDisplay;
	}

}

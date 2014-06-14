package de.jgroehl.asteromania;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import de.jgroehl.asteromania.control.GameHandler;
import de.jgroehl.asteromania.control.GameState;
import de.jgroehl.asteromania.control.SoundManager;
import de.jgroehl.asteromania.control.callbacks.GameStateCallback;
import de.jgroehl.asteromania.control.callbacks.ShotFiredCallback;
import de.jgroehl.asteromania.control.interfaces.EventCallback;
import de.jgroehl.asteromania.graphics.game.SpaceShip;
import de.jgroehl.asteromania.graphics.game.SimpleShot.Target;
import de.jgroehl.asteromania.graphics.interfaces.Drawable;
import de.jgroehl.asteromania.graphics.interfaces.Updatable;
import de.jgroehl.asteromania.graphics.starfield.Starfield;
import de.jgroehl.asteromania.graphics.ui.Button;
import de.jgroehl.asteromania.sensoryInfo.SensorHandler;

public class MainGamePanel extends SurfaceView implements
		SurfaceHolder.Callback {

	private static final String TAG = MainGamePanel.class.getSimpleName();
	private final MainThread thread;

	private final Paint backgroundPaint = new Paint();

	private final GameHandler gameHandler = new GameHandler(GameState.MAIN,
			new SoundManager(getContext()), getResources());
	private final SensorHandler sensorHandler;

	protected MainGamePanel(Context context) {
		this(context, false);
	}

	public MainGamePanel(Context context, boolean debug) {

		super(context);

		sensorHandler = new SensorHandler(context, Context.SENSOR_SERVICE);

		getHolder().addCallback(this);

		thread = new MainThread(this.getHolder(), this, debug);

		setFocusable(true);

		backgroundPaint.setStyle(Paint.Style.FILL);
		backgroundPaint.setColor(Color.BLACK);

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.d(TAG, "Try starting application...");
		if (!thread.isRunning()) {
			initializeGameObjects();
			thread.start();
		} else {
			Log.d(TAG, "Application already running.");
		}
		Log.d(TAG, "Try starting application...[Done]");
	}

	private void initializeGameObjects() {
		gameHandler.add(new Starfield(), GameState.MAIN, GameState.MENU);
		gameHandler.update();

		gameHandler.add(new Button("Start", 0.5f, 0.15f, 0.4f, 0.2f,
				getResources(), new GameStateCallback(GameState.MAIN)),
				GameState.MENU);
		gameHandler.update();
		gameHandler.add(new Button("Score", 0.5f, 0.4f, 0.4f, 0.2f,
				getResources(), new GameStateCallback(GameState.HIGHSCORE)),
				GameState.MENU);
		gameHandler.update();
		gameHandler.add(new Button("Shop", 0.5f, 0.65f, 0.4f, 0.2f,
				getResources(), new GameStateCallback(GameState.SHOP)),
				GameState.MENU);
		gameHandler.update();
		gameHandler.add(new Button("Quit", 0.5f, 0.9f, 0.4f, 0.2f,
				getResources(), new EventCallback() {

					@Override
					public void action(GameHandler gamehandler) {
						System.exit(0);
					}
				}), GameState.MENU);
		gameHandler.update();

		gameHandler.add(
				new SpaceShip(BitmapFactory.decodeResource(getResources(),
						R.drawable.spaceship), sensorHandler, getResources()),
				GameState.MAIN);
		gameHandler.update();

		gameHandler.add(new Button("Shoot", 0.05f, 0.9f, 0.1f, 0.1f,
				getResources(), new ShotFiredCallback(Target.ENEMY)),
				GameState.MAIN);
		gameHandler.add(new Button("Shoot", 0.95f, 0.9f, 0.1f, 0.1f,
				getResources(), new ShotFiredCallback(Target.ENEMY)),
				GameState.MAIN);
		gameHandler.update();

		gameHandler.add(
				new Button(BitmapFactory.decodeResource(getResources(),
						R.drawable.settings), 0.80f, 0.075f, 0.1f, 0.15f,
						getResources(), new GameStateCallback(
								GameState.SETTINGS)), GameState.MAIN);
		gameHandler.update();

		Button button = new Button(BitmapFactory.decodeResource(getResources(),
				R.drawable.home), 0.95f, 0.075f, 0.1f, 0.15f, getResources(),
				new GameStateCallback(GameState.MENU));
		for (GameState state : GameState.values())
			if (!state.equals(GameState.MENU)) {
				gameHandler.add(button, state);
				gameHandler.update();
			}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d(TAG, "Try stopping application...");
		boolean retry = true;
		while (retry) {
			try {
				thread.setRunning(false);
				thread.join();
				retry = false;
			} catch (InterruptedException e) {
				Log.w(TAG, "Thread was not stopped.");
			}
		}
		Log.d(TAG, "Try stopping application...[Done]");
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		gameHandler.handleEvent(event, getContext(), getWidth(), getHeight());
		return super.onTouchEvent(event);
	}

	@Override
	protected void onDraw(Canvas canvas) {
	}

	public void updateGameState() {
		for (Updatable u : gameHandler.getAllUpdatableObjects()) {
			u.update(gameHandler);
		}
	}

	public void displayGameState(Canvas c) {

		if (c != null) {

			clearScreen(c);

			for (Drawable d : gameHandler.getAllDrawableObjects()) {
				d.draw(c);
			}

		}

	}

	private void clearScreen(Canvas c) {
		c.drawRect(new Rect(0, 0, c.getWidth(), c.getHeight()), backgroundPaint);
	}

	public void update() {
		gameHandler.update();
	}

}
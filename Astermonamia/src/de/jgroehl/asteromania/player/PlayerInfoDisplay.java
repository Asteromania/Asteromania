package de.jgroehl.asteromania.player;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import de.jgroehl.asteromania.control.GameHandler;
import de.jgroehl.asteromania.graphics.GameObject;

public class PlayerInfoDisplay extends GameObject {

	private final PlayerInfo playerInfo;

	private final Bitmap coin;
	private final Rect coinRect = new Rect();
	private RectF destRect = new RectF();
	private Paint textPaint = new Paint();

	public PlayerInfoDisplay(Context context, PlayerInfo playerInfo) {
		super(0.0f, 0.0f, context);
		this.playerInfo = playerInfo;

		coin = BitmapFactory.decodeResource(context.getResources(),
				de.jgroehl.asteromania.R.drawable.coin);
		coinRect.set(0, 0, coin.getWidth(), coin.getHeight());
		destRect.top = 0;
		destRect.left = 0;

		textPaint.setStyle(Style.FILL);
	}

	@Override
	public void draw(Canvas c) {
		destRect.bottom = 0.07f * c.getHeight();
		destRect.right = 0.05f * c.getWidth();
		c.drawBitmap(coin, coinRect, destRect, textPaint);
		textPaint.setTextSize(0.05f * c.getHeight());
		textPaint.setColor(Color.rgb(177, 125, 5));
		c.drawText(String.valueOf(playerInfo.getCoins()),
				0.06f * c.getWidth() + 1, 0.055f * c.getHeight() + 1, textPaint);
		textPaint.setColor(Color.rgb(249, 191, 48));
		c.drawText(String.valueOf(playerInfo.getCoins()), 0.06f * c.getWidth(),
				0.055f * c.getHeight(), textPaint);

		textPaint.setColor(Color.rgb(40, 40, 60));
		c.drawText("Level " + String.valueOf(playerInfo.getLevel()),
				0.45f * c.getWidth() + 1, 0.055f * c.getHeight() + 1, textPaint);
		textPaint.setColor(Color.rgb(190, 190, 220));
		c.drawText("Level " + String.valueOf(playerInfo.getLevel()),
				0.45f * c.getWidth(), 0.055f * c.getHeight(), textPaint);

		playerInfo.getHealthPoints().draw(c);
		float textX = (playerInfo.getHealthPoints().getX() + 0.01f)
				* c.getWidth();
		float textY = (playerInfo.getHealthPoints().getY() + playerInfo
				.getHealthPoints().getRelativeHeight() * 0.9f) * c.getHeight();
		textPaint.setTextSize(playerInfo.getHealthPoints().getRelativeHeight()
				* 0.8f * c.getHeight());
		textPaint.setColor(Color.BLACK);
		c.drawText("HP: " + playerInfo.getHealthPoints().getCurrentValue()
				+ "/" + playerInfo.getHealthPoints().getMaximum(), textX + 1,
				textY + 1, textPaint);
		textPaint.setColor(Color.GRAY);
		c.drawText("HP: " + playerInfo.getHealthPoints().getCurrentValue()
				+ "/" + playerInfo.getHealthPoints().getMaximum(), textX,
				textY, textPaint);

	}

	@Override
	public void update(GameHandler gameHandler) {
	}

}

package org.rexcellentgames.burningknight.game.state;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.Noise;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.util.ColorUtils;
import org.rexcellentgames.burningknight.util.Tween;

public class State {
	protected void renderPortal() {
		if (player == null) {
			player = Graphics.getTexture("props-gobbo_full");
		}

		Graphics.startAlphaShape();
		Graphics.shape.setProjectionMatrix(Camera.nil.combined);
		Color cl = ColorUtils.HSV_to_RGB(Dungeon.time * 20 % 360, 360, 360);
		Dungeon.setBackground2(new Color(cl.r * 0.4f, cl.g * 0.4f, cl.b * 0.4f, 1f));

		for (int i = 0; i < 65; i++) {
			float s = i * 0.015f;
			float mx = (Noise.instance.noise(Dungeon.time * 0.25f + s) * 96);
			float my = (Noise.instance.noise( 3 + Dungeon.time * 0.25f + s) * 96);
			float v = ((float) i) / 80f + 0.3f;

			Color color = ColorUtils.HSV_to_RGB((Dungeon.time * 20 - i * 1.4f) % 360, 360, 360);
			Graphics.shape.setColor(v * color.r, v * color.g, v * color.b, 0.5f);

			float a = (float) (Math.PI * i * 0.2f) + Dungeon.time * 2f;
			float w = i * 2 + 64;
			float d = i * 4f;
			float x = (float) (Math.cos(a) * d) + Display.GAME_WIDTH / 2 + mx * (((float) 56-i) / 56);
			float y = (float) (Math.sin(a) * d) + Display.GAME_HEIGHT / 2 + my * (((float) 56-i) / 56);

			Graphics.shape.rect(x - w / 2, y - w / 2, w / 2, w / 2, w, w, 1f, 1f, (float) Math.toDegrees(a + 0.1f));
			Graphics.shape.setColor(v * color.r, v * color.g, v * color.b, 0.9f);
			Graphics.shape.rect(x - w / 2, y - w / 2, w / 2, w / 2, w, w, 0.9f, 0.9f, (float) Math.toDegrees(a + 0.1f));
		}


		float i = 32;
		float mx = (Noise.instance.noise(Dungeon.time * 0.25f + i * 0.015f + 0.1f) * 128f) * (((float) 56-i) / 56);
		float my = (Noise.instance.noise( 3 + Dungeon.time * 0.25f + i * 0.015f + 0.1f) * 128f) * (((float) 56-i) / 56);

		Graphics.endAlphaShape();


		Graphics.batch.setProjectionMatrix(Camera.nil.combined);
		Graphics.render(player, Display.GAME_WIDTH / 2 + mx, Display.GAME_HEIGHT / 2 + my, Dungeon.time * 650, 8, 8,false, false);
	}

	private boolean paused;
	public static TextureRegion player;

	public void onPause() {

	}

	public void onUnpause() {

	}

	public boolean isPaused() {
		return paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;

		if (this.paused) {
			this.onPause();
		} else {
			this.onUnpause();
		}
	}

	public State() {

	}

	public void init() {

	}

	public void destroy() {

	}

	public void update(float dt) {

	}

	public void render() {

	}

	public void renderUi() {

	}

	public void resize(int width, int height) {

	}

	public static void transition(final Runnable runnable) {
		Tween.to(new Tween.Task(0, 0.2f) {
			@Override
			public float getValue() {
				return Dungeon.dark;
			}

			@Override
			public void setValue(float value) {
				Dungeon.dark = value;
			}

			@Override
			public void onEnd() {
				runnable.run();

				Tween.to(new Tween.Task(1, 0.2f) {
					@Override
					public float getValue() {
						return Dungeon.dark;
					}

					@Override
					public void setValue(float value) {
						Dungeon.dark = value;
					}
				});
			}

			@Override
			public boolean runWhenPaused() {
				return true;
			}
		});
	}
}
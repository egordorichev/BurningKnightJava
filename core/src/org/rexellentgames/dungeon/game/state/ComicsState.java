package org.rexellentgames.dungeon.game.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import org.rexellentgames.dungeon.Display;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.rooms.regular.ladder.CastleEntranceRoom;
import org.rexellentgames.dungeon.game.Ui;
import org.rexellentgames.dungeon.net.Network;
import org.rexellentgames.dungeon.util.Tween;

import java.util.Arrays;

public class ComicsState extends State {
	private static final float TIME_STEP = 1 / 45.f;
	private float accumulator;
	private int current;
	public static float[] alpha = new float[5];
	private static TextureRegion frame = Graphics.getTexture("comics");
	private static TextureRegion notice = Graphics.getTexture("notice_bg");

	@Override
	public void init() {
		super.init();
		Camera.instance.follow(null);
		Camera.instance.getCamera().position.set(
			CastleEntranceRoom.spawn.x * 16 + 8,
			CastleEntranceRoom.spawn.y * 16 + 8 + 56,
			0
		);

		Dungeon.showed = true;
		Camera.instance.getCamera().update();

		Arrays.fill(this.alpha, 1f);


		Tween.to(new Tween.Task(0, 1f) {
			@Override
			public float getValue() {
				return alpha[0];
			}

			@Override
			public void setValue(float value) {
				alpha[0] = value;
			}

			@Override
			public void onEnd() {
				Tween.to(new Tween.Task(0, 1f) {
					@Override
					public float getValue() {
						return alpha[1];
					}

					@Override
					public void setValue(float value) {
						alpha[1] = value;
					}

					@Override
					public void onEnd() {
						Tween.to(new Tween.Task(0, 1f) {
							@Override
							public float getValue() {
								return alpha[2];
							}

							@Override
							public void setValue(float value) {
								alpha[2] = value;
							}

							@Override
							public void onEnd() {
								Tween.to(new Tween.Task(0, 1f) {
									@Override
									public float getValue() {
										return alpha[3];
									}

									@Override
									public void setValue(float value) {
										alpha[3] = value;
									}

									@Override
									public void onEnd() {
										Tween.to(new Tween.Task(0, 1f) {
											@Override
											public float getValue() {
												return alpha[4];
											}

											@Override
											public void setValue(float value) {
												alpha[4] = value;
											}
										}.delay(1f));
									}
								}.delay(1f));
							}
						}.delay(1f));
					}
				}.delay(1f));
			}
		});
	}

	@Override
	public void render() {
		// Last frame
		clip(116, 3, 266, 130);
		Dungeon.area.render();
		pop();
	}

	private static void clip(int x, int y, int w, int h) {
		Rectangle scissors = new Rectangle();

		x += Camera.instance.getCamera().position.x - Display.GAME_WIDTH / 2;
		y += Camera.instance.getCamera().position.y - Display.GAME_HEIGHT / 2;

		Rectangle clipBounds = new Rectangle(x, y, w, h);
		ScissorStack.calculateScissors(Camera.instance.getCamera(), Graphics.batch.getTransformMatrix(), clipBounds, scissors);
		ScissorStack.pushScissors(scissors);
	}

	private static void pop() {
		Graphics.batch.flush();
		ScissorStack.popScissors();
	}

	@Override
	public void renderUi() {
		// Notice
		clip(281, 137, 101, 116);
		Graphics.render(notice, (float) (281 - 32 + Math.cos(Dungeon.time) * 16), 137);
		pop();

		Graphics.render(frame, 0, 0);

		Graphics.batch.end();
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Graphics.shape.setProjectionMatrix(Camera.ui.combined);
		Graphics.shape.begin(ShapeRenderer.ShapeType.Filled);

		if (this.alpha[0] > 0) {
			Graphics.shape.setColor(Level.LIGHT_R, Level.LIGHT_G, Level.LIGHT_B, this.alpha[0]);
			Graphics.shape.rect(3, 137, 219, 116);
		}

		if (this.alpha[1] > 0) {
			Graphics.shape.setColor(Level.LIGHT_R, Level.LIGHT_G, Level.LIGHT_B, this.alpha[1]);
			Graphics.shape.rect(226, 137, 51, 116);
		}

		if (this.alpha[2] > 0) {
			Graphics.shape.setColor(Level.LIGHT_R, Level.LIGHT_G, Level.LIGHT_B, this.alpha[2]);
			Graphics.shape.rect(281, 137, 101, 116);
		}

		if (this.alpha[3] > 0) {
			Graphics.shape.setColor(Level.LIGHT_R, Level.LIGHT_G, Level.LIGHT_B, this.alpha[3]);
			Graphics.shape.rect(3, 3, 109, 130);
		}

		if (this.alpha[4] > 0) {
			Graphics.shape.setColor(Level.LIGHT_R, Level.LIGHT_G, Level.LIGHT_B, this.alpha[4]);
			Graphics.shape.rect(116, 3, 266, 130);
		}

		Graphics.shape.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		Graphics.batch.begin();

		Ui.ui.render();
	}

	private void doPhysicsStep(float deltaTime) {
		float frameTime = Math.min(deltaTime, 0.25f);
		this.accumulator += frameTime;

		while (accumulator >= TIME_STEP) {
			Dungeon.world.step(TIME_STEP, 6, 2);
			this.accumulator -= TIME_STEP;
		}
	}

	@Override
	public void update(float dt) {
		this.doPhysicsStep(dt);
	}

	@Override
	public void destroy() {
		super.destroy();

		// Player.instance.tp(CastleEntranceRoom.spawn.x + 16, CastleEntranceRoom.spawn.y * 16);

		if (Network.SERVER || Network.NONE) {
			Dungeon.level.save(Level.DataType.PLAYER);
			Dungeon.level.save(Level.DataType.LEVEL);
		}

		if (Dungeon.area != null) {
			Dungeon.area.destroy();
		}
	}
}
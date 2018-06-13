package org.rexellentgames.dungeon.game.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import org.rexellentgames.dungeon.Display;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Assets;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.util.Tween;

public class AssetLoadState extends State {
	public static final boolean START_TO_MENU = false;
	public static boolean done = false;
	private static Texture region = new Texture(Gdx.files.internal("sprites_split/rexcellent_games.png"));
	private float a;

	@Override
	public void update(float dt) {
		super.update(dt);

		if (!tweened) {
			this.a = Math.min(1, a + dt);
		}

		if (!tweened && Assets.updateLoading()) {
			finish();
			tweened = true;
		}
	}

	@Override
	public void init() {
		super.init();

		if (!START_TO_MENU) {
			Assets.finishLoading();
			finish();
			tweened = true;
		}
	}

	private boolean tweened;

	private void finish() {
		done = true;
		Gdx.graphics.setTitle(Dungeon.title);

		if (!START_TO_MENU) {
			//Dungeon.goToLevel(Dungeon.depth);
		}

		Tween.to(new Tween.Task(0, 0.1f) {
			@Override
			public float getValue() {
				return a;
			}

			@Override
			public void setValue(float value) {
				a = value;
			}
		});

		Color color = Color.WHITE;
		float t = 0.1f;

		Tween.to(new Tween.Task(color.r, t) {
			@Override
			public float getValue() {
				return 0;
			}

			@Override
			public void setValue(float value) {
				Dungeon.background2.r = value;
			}

			@Override
			public void onEnd() {
				Color color = Color.valueOf("#323c39");
				float t = 0.5f;

				Tween.to(new Tween.Task(color.r, t) {
					@Override
					public float getValue() {
						return Dungeon.background2.r;
					}

					@Override
					public void setValue(float value) {
						Dungeon.background2.r = value;
					}

					@Override
					public void onEnd() {
						//if (START_TO_MENU) {
							Dungeon.game.setState(new MainMenuState());
						//}
					}
				});

				Tween.to(new Tween.Task(color.g, t) {
					@Override
					public float getValue() {
						return Dungeon.background2.g;
					}

					@Override
					public void setValue(float value) {
						Dungeon.background2.g = value;
					}
				});

				Tween.to(new Tween.Task(color.b, t) {
					@Override
					public float getValue() {
						return Dungeon.background2.b;
					}

					@Override
					public void setValue(float value) {
						Dungeon.background2.b = value;
					}
				});
			}
		});

		Tween.to(new Tween.Task(color.g, t) {
			@Override
			public float getValue() {
				return 0;
			}

			@Override
			public void setValue(float value) {
				Dungeon.background2.g = value;
			}
		});

		Tween.to(new Tween.Task(color.b, t) {
			@Override
			public float getValue() {
				return 0;
			}

			@Override
			public void setValue(float value) {
				Dungeon.background2.b = value;
			}
		});
	}

	@Override
	public void render() {
		super.render();

		Graphics.batch.setColor(1, 1, 1, this.a);
		Graphics.batch.draw(region, (Display.GAME_WIDTH - region.getWidth()) / 2, (Display.GAME_HEIGHT - region.getHeight()) / 2);

		Gdx.graphics.setTitle(Dungeon.title + " " + Math.floor(Assets.manager.getProgress() * 100) + "%");
	}
}
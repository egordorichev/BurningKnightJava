package org.rexcellentgames.burningknight.game.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.Version;
import org.rexcellentgames.burningknight.assets.Assets;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.level.save.GameSave;
import org.rexcellentgames.burningknight.entity.level.save.SaveManager;
import org.rexcellentgames.burningknight.util.Tween;

public class AssetLoadState extends State {
	public static final boolean START_TO_MENU = false;
	public static final boolean QUICK = true;
	public static boolean done = false;
	private static Texture region;
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

		if (QUICK || !START_TO_MENU) {
			Assets.finishLoading();
			finish();
			tweened = true;
		} else {
			region = new Texture(Gdx.files.internal("sprites_split/rexcellent_games.png"));
		}
	}

	private boolean tweened;

	private void finish() {
		done = true;

		if (!START_TO_MENU) {
			Gdx.graphics.setTitle(Dungeon.title);
			GameSave.Info info = GameSave.peek(SaveManager.slot);
			Dungeon.goToLevel(info.free ? -1 : info.depth);
			return;
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
				Dungeon.getBackground2().r = value;
			}

			@Override
			public void onEnd() {
				Color color = Color.valueOf("#1a1932");
				float t = 0.2f;

				Tween.to(new Tween.Task(color.r, t) {
					@Override
					public float getValue() {
						return Dungeon.getBackground2().r;
					}

					@Override
					public void setValue(float value) {
						Dungeon.getBackground2().r = value;
					}

					@Override
					public void onEnd() {
						Gdx.graphics.setTitle(Dungeon.title);

						if (START_TO_MENU) {
							if (Version.showAlphaWarning) {
								Dungeon.game.setState(new AlphaWarningState());
							} else {
								Dungeon.game.setState(new MainMenuState());
							}
						}
					}
				});

				Tween.to(new Tween.Task(color.g, t) {
					@Override
					public float getValue() {
						return Dungeon.getBackground2().g;
					}

					@Override
					public void setValue(float value) {
						Dungeon.getBackground2().g = value;
					}
				});

				Tween.to(new Tween.Task(color.b, t) {
					@Override
					public float getValue() {
						return Dungeon.getBackground2().b;
					}

					@Override
					public void setValue(float value) {
						Dungeon.getBackground2().b = value;
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
				Dungeon.getBackground2().g = value;
			}
		});

		Tween.to(new Tween.Task(color.b, t) {
			@Override
			public float getValue() {
				return 0;
			}

			@Override
			public void setValue(float value) {
				Dungeon.getBackground2().b = value;
			}
		});
	}

	@Override
	public void render() {
		super.render();

		if (QUICK) {
			return;
		}

		Graphics.batch.setColor(1, 1, 1, this.a);
		Graphics.batch.draw(region, (Display.GAME_WIDTH - region.getWidth()) / 2, (Display.GAME_HEIGHT - region.getHeight()) / 2);

		Gdx.graphics.setTitle(Dungeon.title + " " + Math.floor(Assets.manager.getProgress() * 100) + "%");
	}
}

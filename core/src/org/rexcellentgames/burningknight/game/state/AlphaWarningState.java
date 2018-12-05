package org.rexcellentgames.burningknight.game.state;

import com.badlogic.gdx.graphics.Color;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.util.Tween;

public class AlphaWarningState extends State {
	private float alpha = 0;
	private boolean did;

	@Override
	public void init() {
		super.init();
		Dungeon.setBackground(new Color(0, 0, 0, 1));
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		this.alpha += ((this.did ? 0 : 1) - this.alpha) * dt * 5;

		if (!did && (Input.instance.wasPressed("X"))) {
			did = true;

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
							Dungeon.game.setState(new MainMenuState());
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
			}).delay(0.5f);

			Tween.to(new Tween.Task(color.g, t) {
				@Override
				public float getValue() {
					return 0;
				}

				@Override
				public void setValue(float value) {
					Dungeon.getBackground2().g = value;
				}
			}).delay(0.5f);

			Tween.to(new Tween.Task(color.b, t) {
				@Override
				public float getValue() {
					return 0;
				}

				@Override
				public void setValue(float value) {
					Dungeon.getBackground2().b = value;
				}
			}).delay(0.5f);
		}
	}

	@Override
	public void renderUi() {
		super.renderUi();

		float v = did ? 0.7f : 1f;
		Graphics.small.setColor(v, v, v, this.alpha);

		int y = Display.UI_HEIGHT / 3 * 2;
		Graphics.print("Warning", Graphics.small, y);
		Graphics.print("This game is still in alpha", Graphics.small, y - 32);
		Graphics.print("This means, that it still might have bugs in it", Graphics.small, y - 48);
		Graphics.print("If you meet any, please report them", Graphics.small, y - 64);

		Graphics.print("Press X to continue", Graphics.small, y - 96);

		Graphics.small.setColor(1, 1, 1, 1);
	}
}
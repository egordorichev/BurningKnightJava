package org.rexcellentgames.burningknight.game.state;

import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.util.Tween;

public class WonState extends State {
	@Override
	public void init() {
		super.init();

		Dungeon.dark = 0;
		Dungeon.darkR = Dungeon.MAX_R;

		Tween.to(new Tween.Task(1, 0.3f) {
			@Override
			public float getValue() {
				return 0;
			}

			@Override
			public void setValue(float value) {
				Dungeon.dark = value;
			}
		});
	}

	@Override
	public void render() {
		super.render();
		renderPortal();

		if (Input.instance.wasPressed("X") && Dungeon.dark == 1) {
			Tween.to(new Tween.Task(0, 0.3f) {
				@Override
				public float getValue() {
					return 1;
				}

				@Override
				public void setValue(float value) {
					Dungeon.dark = value;
				}

				@Override
				public void onEnd() {
					MainMenuState.voidMusic.stop();
					Dungeon.newGame(false, -2);
				}
			});
		}
	}

	@Override
	public void renderUi() {
		super.renderUi();
		float s = 14;
		Graphics.printCenter("You won!", Graphics.medium, 0, Display.UI_HEIGHT / 2 + s * 2);
		Graphics.printCenter("(kind-of, that's only small part of the game)", Graphics.medium, 0, Display.UI_HEIGHT / 2 + s);
		Graphics.printCenter("Thanks so much for playing!", Graphics.medium, 0, Display.UI_HEIGHT / 2 - s);
		Graphics.printCenter("This was an alpha build of the game,", Graphics.medium, 0, Display.UI_HEIGHT / 2 - s * 2);
		Graphics.printCenter("so much more stuff will be added", Graphics.medium, 0, Display.UI_HEIGHT / 2 - s * 3);
		Graphics.printCenter("and fixed. Please come back later :)", Graphics.medium, 0, Display.UI_HEIGHT / 2 - s * 4);
		Graphics.printCenter("Press X", Graphics.medium, 0, Display.UI_HEIGHT / 2  - s * 6);
	}
}
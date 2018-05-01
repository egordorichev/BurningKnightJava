package org.rexellentgames.dungeon.game.state;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import org.rexellentgames.dungeon.Display;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.game.Area;
import org.rexellentgames.dungeon.net.Network;
import org.rexellentgames.dungeon.net.client.GameClient;
import org.rexellentgames.dungeon.ui.UiButton;
import org.rexellentgames.dungeon.ui.UiInput;
import org.rexellentgames.dungeon.util.Log;

import java.io.IOException;

public class LoginState extends State {
	@Override
	public void init() {
		this.setupUi();
	}

	@Override
	public void update(float dt) {
		Dungeon.ui.update(dt);
	}

	@Override
	public void renderUi() {
		Dungeon.ui.render();
	}

	@Override
	public void destroy() {
		Dungeon.ui.destroy();
	}

	private UiInput name;

	private void setupUi() {
		final UiButton start = new UiButton("Join!", 32, -1) {
			@Override
			public void onClick() {
				if (name.getInput().length() > 0) {
					// wdDungeon.world = new World(new Vector2(0, 0), true);

					Network.SERVER = false;
					Player.NAME = name.getInput();
					Log.info("Starting the client...");

					Network.client = new GameClient();

					try {
						Network.client.run(name.getInput());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		};

		Dungeon.ui.add(start);

		name = new UiInput() {
			@Override
			public void onEnter(String input) {
				start.onClick();
			}
		};

		name.setPlaceholder("Nickname");
		name.y = (Display.GAME_HEIGHT - name.h) / 2 + 16;

		Dungeon.ui.add(name);
	}
}
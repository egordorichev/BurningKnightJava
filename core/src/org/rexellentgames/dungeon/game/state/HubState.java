package org.rexellentgames.dungeon.game.state;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.UiLog;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.debug.Console;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.game.Area;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.net.Network;
import org.rexellentgames.dungeon.net.client.ClientHandler;
import org.rexellentgames.dungeon.util.Tween;

public class HubState extends State {
	private static final float TIME_STEP = 1 / 45.f;
	private float accumulator;
	private Console console;

	private void doPhysicsStep(float deltaTime) {
		float frameTime = Math.min(deltaTime, 0.25f);
		this.accumulator += frameTime;

		while (accumulator >= TIME_STEP) {
			Dungeon.world.step(TIME_STEP, 6, 2);
			this.accumulator -= TIME_STEP;
		}
	}

	@Override
	public void init() {
		if (Dungeon.world == null) {
			Dungeon.world = new World(new Vector2(0, 0), true);
		}

		Dungeon.area = new Area();
		Dungeon.area.add(new Camera());

		if (Dungeon.ui == null) {
			Dungeon.ui = new Area();
		}

		Dungeon.ui.add(new UiLog());

		console = new Console();

		if (!Network.SERVER) {
			for (Player player : ClientHandler.instance.getPlayers()) {
				Dungeon.area.add(player);
			}
		}
	}

	@Override
	public void update(float dt) {
		super.update(dt);
		this.doPhysicsStep(dt);
		console.update(dt);
		Dungeon.ui.update(dt);

		if (!Network.SERVER) {

			if (Input.instance.wasPressed("1")) {
				final OrthographicCamera cam = Camera.instance.getCamera();

				Tween.to(new Tween.Task(cam.zoom * 1.3f, 0.2f) {
					@Override
					public float getValue() {
						return cam.zoom;
					}

					@Override
					public void setValue(float value) {
						cam.zoom = value;
					}
				});
			} else if (Input.instance.wasPressed("2")) {
				final OrthographicCamera cam = Camera.instance.getCamera();

				Tween.to(new Tween.Task(cam.zoom * 0.7f, 0.2f) {
					@Override
					public float getValue() {
						return cam.zoom;
					}

					@Override
					public void setValue(float value) {
						cam.zoom = value;
					}
				});
			}
		}
	}

	@Override
	public void render() {
		super.render();

		Graphics.batch.setProjectionMatrix(Camera.ui.combined);
		Dungeon.ui.render();
		console.render();
	}

	@Override
	public void destroy() {
		Dungeon.area.destroy();
	}
}
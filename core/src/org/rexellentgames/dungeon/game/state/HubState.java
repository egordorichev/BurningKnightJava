package org.rexellentgames.dungeon.game.state;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.game.Area;
import org.rexellentgames.dungeon.net.Network;
import org.rexellentgames.dungeon.net.client.ClientHandler;

public class HubState extends State {
	private static final float TIME_STEP = 1 / 45.f;
	private float accumulator;

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
	}

	@Override
	public void destroy() {
		Dungeon.area.destroy();
	}
}
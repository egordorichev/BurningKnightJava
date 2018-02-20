package org.rexellentgames.dungeon.game;

import box2dLight.RayHandler;
import com.badlogic.gdx.physics.box2d.World;

public class State {
	protected World world;
	protected RayHandler light;

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

	public World getWorld() {
		return this.world;
	}

	public RayHandler getLight() {
		return this.light;
	}
}
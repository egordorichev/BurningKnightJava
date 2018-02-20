package org.rexellentgames.dungeon.game;

import org.rexellentgames.dungeon.entity.Entity;
import java.util.ArrayList;

public class Area {
	private ArrayList<Entity> entitys = new ArrayList<Entity>();
	private State state;

	public Area(State state) {
		this.state = state;
	}

	public Entity add(Entity entity) {
		this.entitys.add(entity);

		entity.setArea(this);
		entity.init();

		return entity;
	}

	public void remove(Entity entity) {
		this.entitys.remove(entity);
		entity.destroy();
	}

	public void update(float dt) {
		for (Entity entity : this.entitys) {
			entity.update(dt);
		}
	}

	public void render() {
		for (Entity entity : this.entitys) {
			entity.render();
		}
	}

	public State getState() {
		return this.state;
	}
}
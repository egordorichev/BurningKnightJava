package org.rexellentgames.dungeon.game;

import org.rexellentgames.dungeon.entity.Entity;
import java.util.ArrayList;

public class Area {
	private ArrayList<Entity> elements = new ArrayList<Entity>();
	private State state;

	public Area(State state) {
		this.state = state;
	}

	public Entity add(Entity entity) {
		this.elements.add(entity);

		entity.setArea(this);
		entity.init();

		return entity;
	}

	public void remove(Entity element) {
		this.elements.remove(element);
		element.destroy();
	}

	public void update(float dt) {
		for (Entity element : this.elements) {
			element.update(dt);
		}
	}

	public void render() {
		for (Entity element : this.elements) {
			element.render();
		}
	}

	public State getState() {
		return this.state;
	}
}
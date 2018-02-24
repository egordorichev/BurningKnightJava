package org.rexellentgames.dungeon.game;

import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.Random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Area {
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	private State state;
	private Comparator<Entity> comparator;

	public Area(State state) {
		this.state = state;

		this.comparator = new Comparator<Entity>() {
			@Override
			public int compare(Entity a, Entity b) {
				// -1 - less than, 1 - greater than, 0 - equal
				int ad = b.getDepth();
				int bd = a.getDepth();

				return ad > bd ? -1 : (ad < bd ? 1 : 0);
			}
		};
	}

	public Entity add(Entity entity) {
		this.entities.add(entity);

		entity.setArea(this);
		entity.init();

		return entity;
	}

	public void remove(Entity entity) {
		this.entities.remove(entity);
		entity.destroy();
	}

	public void update(float dt) {
		for (Entity entity : this.entities) {
			entity.update(dt);
		}
	}

	public void render() {
		Collections.sort(this.entities, this.comparator);

		for (Entity entity : this.entities) {
			entity.render();
		}
	}

	public Entity getRandomEntity(Class<? extends Entity> type) {
		ArrayList<Entity> list = new ArrayList<Entity>();

		for (Entity entity : this.entities) {
			if (type.isInstance(entity)) {
				list.add(entity);
			}
		}

		if (list.size() == 0) {
			return null;
		}

		return list.get(Random.newInt(list.size()));
	}

	public State getState() {
		return this.state;
	}
}
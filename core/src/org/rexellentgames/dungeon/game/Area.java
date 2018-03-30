package org.rexellentgames.dungeon.game;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.NetworkedEntity;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.level.SaveableEntity;
import org.rexellentgames.dungeon.net.Network;
import org.rexellentgames.dungeon.net.Packets;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.Random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Area {
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	private Comparator<Entity> comparator;

	public Area() {
		this.comparator = new Comparator<Entity>() {
			@Override
			public int compare(Entity a, Entity b) {
				// -1 - less than, 1 - greater than, 0 - equal
				float ad = b.getDepth();
				float bd = a.getDepth();

				if (ad == bd) {
					ad = a.y;
					bd = b.y;
				}

				return Float.compare(bd, ad);
			}
		};
	}

	public Entity add(Entity entity) {
		this.entities.add(entity);

		if (Network.SERVER && !(entity instanceof Player) && entity instanceof NetworkedEntity) {
			Network.server.getServerHandler().sendToAll(Packets.makeEntityAdded((Class<? extends NetworkedEntity>) entity.getClass(),
				entity.getId(), entity.x, entity.y, ""));
		}

		entity.setArea(this);
		entity.init();

		return entity;
	}

	public void remove(Entity entity) {
		this.entities.remove(entity);
	}

	public void update(float dt) {
		for (int i = this.entities.size() - 1; i >= 0; i--) {
			Entity entity = this.entities.get(i);

			entity.onScreen = entity.isOnScreen();

			if (entity.onScreen || entity.alwaysActive) {
				entity.update(dt);
			}

			if (entity.done) {
				if (entity instanceof SaveableEntity) {
					SaveableEntity saveableEntity = (SaveableEntity) entity;
					Dungeon.level.removeSaveable(saveableEntity);
				}

				entity.destroy();
				this.entities.remove(i);
			}
		}
	}

	public void render() {
		Collections.sort(this.entities, this.comparator);

		for (int i = 0; i < this.entities.size(); i++) {
			Entity entity = this.entities.get(i);

			if (entity.onScreen || entity.alwaysRender) {
				entity.render();
			}
		}

		for (int i = 0; i < this.entities.size(); i++) {
			Entity entity = this.entities.get(i);

			if (entity.onScreen || entity.alwaysRender) {
				entity.renderTop();
			}
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

	public void destroy() {
		for (int i = this.entities.size() - 1; i >= 0; i--) {
			this.entities.get(i).destroy();
		}

		this.entities.clear();
	}

	public ArrayList<Entity> getEntities() {
		return this.entities;
	}
}
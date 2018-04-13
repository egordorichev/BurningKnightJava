package org.rexellentgames.dungeon.entity.level.entities;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.MassData;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.creature.Creature;
import org.rexellentgames.dungeon.entity.creature.mob.Mob;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.item.Lamp;
import org.rexellentgames.dungeon.entity.level.SaveableEntity;
import org.rexellentgames.dungeon.entity.level.rooms.Room;
import org.rexellentgames.dungeon.entity.level.rooms.regular.FightRoom;
import org.rexellentgames.dungeon.entity.level.rooms.regular.LampRoom;
import org.rexellentgames.dungeon.entity.level.rooms.regular.ladder.ExitRoom;
import org.rexellentgames.dungeon.util.Animation;
import org.rexellentgames.dungeon.util.AnimationData;
import org.rexellentgames.dungeon.util.file.FileReader;
import org.rexellentgames.dungeon.util.file.FileWriter;

import java.io.IOException;

public class Door extends SaveableEntity {
	private boolean vertical;
	private Body body;
	public int numCollisions;
	private static Animation vertAnimation = Animation.make("actor-door-vertical");
	private static Animation horizAnimation = Animation.make("actor-door-horizontal");
	private AnimationData animation;
	public boolean autoLock;
	public boolean lock;
	public Room[] rooms = new Room[2];

	public Door(int x, int y, boolean vertical) {
		this.x = x * 16;
		this.y = y * 16;
		this.vertical = vertical;
		// todo; fix hitbox

		if (!this.vertical) {
			this.animation = vertAnimation.get("idle");
			this.y -= 8;
		} else {
			this.animation = horizAnimation.get("idle");
			this.x += 4;
		}

		this.animation.setPaused(true);
		this.animation.setAutoPause(true);
	}

	public Door() {

	}

	@Override
	public void update(float dt) {
		if (this.body == null) {
			this.body = this.createBody(this.vertical ? 2 : 0, this.vertical ? -4 : 8, this.vertical ? 4 : 16,
				this.vertical ? 20 : 4, BodyDef.BodyType.DynamicBody, !this.autoLock);
			this.body.setTransform(this.x, this.y, 0);

			if (this.autoLock) {
				MassData data = new MassData();
				data.mass = 10000000f;
				this.body.setMassData(data);

				this.animation.setPaused(false);
			}
		}

		if (this.autoLock) {
			boolean last = this.lock;
			this.lock = false;

			for (int i = 0; i < 2; i++) {
				if (this.rooms[i] instanceof ExitRoom && Mob.all.size() > 0) {
					this.lock = true;
					break;
				} else if (this.rooms[i] != null && this.rooms[i] == Player.instance.currentRoom
					&& Player.instance.currentRoom instanceof LampRoom
					&& !Player.instance.getInventory().find(Lamp.class)) {

					this.lock = true;
					break;
				}
			}

			if (this.lock && !last) {
				this.animation.setBack(true);
				this.animation.setPaused(false);
			} else if (!this.lock && last) {
				this.animation.setBack(false);
				this.animation.setPaused(false);
			}
		}

		super.update(dt);

		if (this.animation.update(dt)) {

		}
	}

	@Override
	public void onCollision(Entity entity) {
		if (entity instanceof Creature) {
			if (this.lock) {
				return;
			}

			this.numCollisions += 1;

			this.animation.setBack(false);
			this.animation.setPaused(false);
		}
	}

	@Override
	public void onCollisionEnd(Entity entity) {
		if (entity instanceof Creature) {
			if (this.lock) {
				return;
			}

			this.numCollisions -= 1;

			if (this.numCollisions <= 0) {
				this.numCollisions = 0; // to make sure

				if (!this.autoLock || this.lock) {
					this.animation.setBack(true);
					this.animation.setPaused(false);
				}
			}
		}
	}

	@Override
	public void render() {
		this.animation.render(this.x, this.y, false);
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);

		this.vertical = reader.readBoolean();

		if (!this.vertical) {
			this.animation = vertAnimation.get("idle");
		} else {
			this.animation = horizAnimation.get("idle");
		}

		this.animation.setPaused(true);
		this.animation.setAutoPause(true);

		this.autoLock = reader.readBoolean();

		if (this.autoLock) {
			this.rooms[0] = Dungeon.level.getRooms().get(reader.readInt16());
			this.rooms[1] = Dungeon.level.getRooms().get(reader.readInt16());
		}
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);

		writer.writeBoolean(this.vertical);
		writer.writeBoolean(this.autoLock);

		if (this.autoLock) {
			writer.writeInt16((short) Dungeon.level.getRooms().indexOf(this.rooms[0]));
			writer.writeInt16((short) Dungeon.level.getRooms().indexOf(this.rooms[1]));
		}
	}
}
package org.rexellentgames.dungeon.entity.level.entities;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.MassData;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.creature.Creature;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.entity.item.Lamp;
import org.rexellentgames.dungeon.entity.item.key.Key;
import org.rexellentgames.dungeon.entity.level.SaveableEntity;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.rooms.Room;
import org.rexellentgames.dungeon.entity.level.rooms.regular.LampRoom;
import org.rexellentgames.dungeon.physics.World;
import org.rexellentgames.dungeon.util.Animation;
import org.rexellentgames.dungeon.util.AnimationData;
import org.rexellentgames.dungeon.util.file.FileReader;
import org.rexellentgames.dungeon.util.file.FileWriter;

import java.io.IOException;

public class Door extends SaveableEntity {
	private boolean vertical;
	private Body body;
	private int numCollisions;
	private static Animation vertAnimation = Animation.make("actor-door-vertical");
	private static Animation horizAnimation = Animation.make("actor-door-horizontal");
	private static Animation lockAnimation = Animation.make("door-lock");
	private AnimationData animation;
	private AnimationData locked = lockAnimation.get("idle");
	private AnimationData unlock = lockAnimation.get("unlock");
	private AnimationData lk = lockAnimation.get("lock");
	private AnimationData lockAnim;

	public boolean autoLock;
	public boolean lockable;
	public boolean lock;
	public Room[] rooms = new Room[2];
	public Class<? extends Key> key;
	public boolean hidden;
	private int sx;
	private int sy;

	{
		alwaysActive = true;
	}

	public Door(int x, int y, boolean vertical) {
		this.x = x * 16;
		this.y = y * 16;
		this.sx = x;
		this.sy = y;
		this.vertical = vertical;
		// todo; fix hitbox

		if (!this.vertical) {
			this.animation = vertAnimation.get("idle");
			this.y -= 8;
		} else {
			this.animation = horizAnimation.get("idle");
			this.x += 4;
		}

		this.animation.setAutoPause(true);
		this.animation.setPaused(true);
	}

	public Door() {

	}

	@Override
	public void update(float dt) {
		if (this.body == null) {
			this.body = World.createSimpleBody(this, this.vertical ? 2 : 0, this.vertical ? -4 : 8, this.vertical ? 4 : 16,
				this.vertical ? 20 : 4, BodyDef.BodyType.DynamicBody, !(this.autoLock || this.lockable));
			this.body.setTransform(this.x, this.y, 0);

			MassData data = new MassData();
			data.mass = 10000000000f;
			this.body.setMassData(data);

			if (this.autoLock) {
				this.animation.setPaused(false);
			}
		}

		super.update(dt);

		if (this.hidden) {
			if (Dungeon.level.get(this.sx, this.sy) != Terrain.CRACK) {
				this.hidden = false;
			} else {
				return;
			}
		}

		if (this.animation.update(dt)) {
			if (this.animation.getFrame() == 2) {
				this.animation.setBack(true);
				this.animation.setPaused(false);
			}
		}

		if (this.lockAnim != null) {
			if (this.lockAnim.update(dt)) {
				if (this.lockAnim == this.unlock) {
					this.lock = false;
					this.lockAnim = null;
				} else if (this.lockAnim == this.lk) {
					this.lockAnim = this.locked;
				}
			}
		}
	}

	public boolean isOpen() {
		return this.animation.getFrame() != 0;
	}

	@Override
	public void destroy() {
		super.destroy();
		this.body = World.removeBody(this.body);
	}

	@Override
	public void onCollision(Entity entity) {
		if (entity instanceof Creature) {
			if (this.lock && this.lockable && entity instanceof Player) {
				Player player = (Player) entity;

				if (player.getInventory().find(this.key)) {
					Item key = player.getInventory().findItem(this.key);
					key.setCount(key.getCount() - 1);

					this.lock = false;
					this.animation.setBack(false);
					this.animation.setPaused(false);
					this.lockAnim = this.unlock;
				}
			}

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
			}
		}
	}

	@Override
	public void render() {
		if (this.hidden) {
			return;
		}

		if (this.lock && this.lockAnim == null) {
			this.lockAnim = this.lk;
		}

		boolean last = this.lock;

		if (this.autoLock) {
			this.lock = false;

			for (int i = 0; i < 2; i++) {
				/*if (this.rooms[i] instanceof ExitRoom && Mob.all.size() > 0) {
					this.lock = true;
					break;
				} else */

				if (Player.instance != null && this.rooms[i] == Player.instance.currentRoom) {
					if (Player.instance.currentRoom instanceof LampRoom
						&& !Player.instance.getInventory().find(Lamp.class)) {

						this.lock = true;
						break;
					} else if (Player.instance.currentRoom.numEnemies > 0) {
						this.lock = true;
						break;
					}
				}
			}
		}


		if (this.lock && !last) {
			this.lockAnim = this.lk;
			this.animation.setBack(true);
			this.animation.setPaused(false);
		} else if (!this.lock && last) {
			//this.animation.setBack(false);
			//this.animation.setPaused(false);
			this.lockAnim = this.unlock;
		}

		this.animation.render(this.x, this.y, false, false);

		if (this.lockAnim != null) {
			this.lockAnim.render(this.x + (this.vertical ? 0 : 3), this.y, false, false);
		}
	}

	@Override
	public void renderShadow() {
		if (this.hidden) {
			return;
		}

		Graphics.startShadows();
		this.animation.render(this.x, this.y - (this.vertical ? h / 2 - 2 : h), false, true, this.animation.getFrame(), false);
		Graphics.endShadows();
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
		this.lock = reader.readBoolean();
		this.lockable = reader.readBoolean();

		if (this.autoLock) {
			this.rooms[0] = Dungeon.level.getRooms().get(reader.readInt16());
			this.rooms[1] = Dungeon.level.getRooms().get(reader.readInt16());
		}

		if (reader.readBoolean()) {
			try {
				this.key = (Class<? extends Key>) Class.forName(reader.readString());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

		this.sx = reader.readInt16();
		this.sy = reader.readInt16();
		this.hidden = reader.readBoolean();
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);

		writer.writeBoolean(this.vertical);
		writer.writeBoolean(this.autoLock);
		writer.writeBoolean(this.lock);
		writer.writeBoolean(this.lockable);

		if (this.autoLock) {
			writer.writeInt16((short) Dungeon.level.getRooms().indexOf(this.rooms[0]));
			writer.writeInt16((short) Dungeon.level.getRooms().indexOf(this.rooms[1]));
		}

		writer.writeBoolean(this.lock && this.key != null);
		
		if (this.lock && this.key != null) {
			writer.writeString(this.key.getName());
		}

		writer.writeInt16((short) this.sx);
		writer.writeInt16((short) this.sy);
		writer.writeBoolean(this.hidden);
	}
}
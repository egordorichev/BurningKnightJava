package org.rexcellentgames.burningknight.entity.level.entities;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.*;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.buff.BurningBuff;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.fx.TerrainFlameFx;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.accessory.equipable.Lootpick;
import org.rexcellentgames.burningknight.entity.item.key.Key;
import org.rexcellentgames.burningknight.entity.item.pet.impl.PetEntity;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.SaveableEntity;
import org.rexcellentgames.burningknight.entity.level.entities.fx.PoofFx;
import org.rexcellentgames.burningknight.entity.level.rooms.Room;
import org.rexcellentgames.burningknight.entity.level.save.GlobalSave;
import org.rexcellentgames.burningknight.game.Achievements;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.*;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;

import java.io.IOException;

public class Door extends SaveableEntity {
	private boolean vertical;
	private Body body;
	private int numCollisions;
	private static Animation vertAnimation = Animation.make("actor-door-vertical", "-wooden");
	private static Animation horizAnimation = Animation.make("actor-door-horizontal", "-wooden");
	private static Animation ironLockAnimation = Animation.make("door-lock", "-iron");
	private static Animation bronzeLockAnimation = Animation.make("door-lock", "-bronze");
	private static Animation goldLockAnimation = Animation.make("door-lock", "-gold");
	private AnimationData animation;
	private AnimationData locked;
	private AnimationData unlock;
	private AnimationData lk;
	private AnimationData lockAnim;

	public boolean autoLock;
	public boolean lockable;
	public boolean lock;
	public Room[] rooms = new Room[2];
	public Class<? extends Key> key;
	private int sx;
	private int sy;

	{
		depth = -1;
		alwaysActive = true;
	}

	public Door() {

	}

	public Animation getAnimation() {
		if (this.autoLock) {
			return ironLockAnimation;
		}

		return goldLockAnimation;
	}

	public Door(int x, int y, boolean vertical) {
		this.x = x * 16;
		this.y = y * 16;
		this.sx = x;
		this.sy = y;
		this.vertical = vertical;

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

	private void setPas(boolean pas) {
		Dungeon.level.setPassable((int) Math.floor(this.x / 16), (int) Math.floor((this.y + 8) / 16), pas);
	}

	private float lastFlame;

	@Override
	public void update(float dt) {
		if (locked == null) {
			Animation animation = getAnimation();

			locked = animation.get("idle");
			unlock = animation.get("open");
			lk = animation.get("close");
		}

		if (this.numCollisions == 0 && this.animation.isPaused() && this.animation.getFrame() == 3) {
			this.clearT += dt;

			if (this.clearT > 0.5f) {
				this.animation.setBack(true);
				this.animation.setPaused(false);
				this.playSfx("door");
			}
		}

		if (this.body == null) {
			this.body = World.createSimpleBody(this, this.vertical ? 2 : 0, this.vertical ? -4 : 8, this.vertical ? 4 : 16,
				this.vertical ? 20 : 4, BodyDef.BodyType.DynamicBody, false);
			
			if (this.body != null) {
				this.body.setTransform(this.x, this.y, 0);
			}

			MassData data = new MassData();
			data.mass = 1000000000000000f;
			
			if (this.body != null) {
				this.body.setMassData(data);
			}
		}

		this.body.setTransform(this.x, this.y, 0);
		super.update(dt);

		if (this.animation.update(dt)) {
			if (this.animation.getFrame() == 3) {
				this.animation.setPaused(true);
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

		if (!this.burning) {
			int i = Level.toIndex((int) Math.floor(this.x / 16), (int) Math.floor((this.y + 8) / 16));
			byte info = Dungeon.level.getInfo(i);

			if (BitHelper.isBitSet(info, 0)) {
				// Burning
				this.damage = 0;
				this.burning = true;

				for (int j : PathFinder.NEIGHBOURS4) {
					Dungeon.level.setOnFire(i + j, true);
				}
			}
		} else {
			lastFlame += dt;

			if (this.lastFlame >= 0.05f) {
				this.lastFlame = 0;
				TerrainFlameFx fx = new TerrainFlameFx();
				fx.x = this.x + Random.newFloat(this.w);
				fx.y = this.y + Random.newFloat(this.h) - 4;
				fx.depth = 1;
				Dungeon.area.add(fx);
			}

			damage += dt / 5;

			if (damage >= 1f) {
				this.done = true;
				for (int i = 0; i < 5; i++) {
					PoofFx fx = new PoofFx();

					fx.x = this.x + this.w / 2;
					fx.y = this.y + this.h / 2;

					Dungeon.area.add(fx);
				}
			}
		}
	}

	private float damage;
	public boolean burning;

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
		if (entity instanceof Creature && !((Creature) entity).flying) {
			if (((Creature) entity).hasBuff(BurningBuff.class)) {
				this.burning = true;
			} else if (this.burning) {
				((Creature) entity).addBuff(new BurningBuff());
			}

			if (this.lock && this.lockable && entity instanceof Player) {
				Player player = (Player) entity;

				if (player.ui.hasEquipped(Lootpick.class)) {
					this.lock = false;
					this.animation.setBack(false);
					this.animation.setPaused(false);
					this.lockAnim = this.unlock;
				} else if (player.getInventory().find(this.key)) {
					Item key = player.getInventory().findItem(this.key);
					key.setCount(key.getCount() - 1);

					int num = GlobalSave.getInt("num_keys_used");
					GlobalSave.put("num_keys_used", num);

					if (num >= 10) {
						Achievements.unlock(Achievements.UNLOCK_LOOTPICK);
					}

					this.lock = false;
					this.animation.setBack(false);
					this.animation.setPaused(false);
					this.lockAnim = this.unlock;
				}
			}

			if (this.lock) {
				return;
			}

			if (this.numCollisions == 0) {
				this.playSfx("door");
			}

			this.numCollisions += 1;

			this.animation.setBack(false);
			this.animation.setPaused(false);
		}
	}

	@Override
	public void onCollisionEnd(Entity entity) {
		if (entity instanceof Creature && !((Creature) entity).flying) {
			if (this.lock) {
				return;
			}

			this.numCollisions -= 1;

			if (this.numCollisions <= 0) {
				this.numCollisions = 0; // to make sure
				this.clearT = 0;
			}
		}
	}

	private float clearT;

	@Override
	public void render() {
		if (this.lock && this.lockAnim == null) {
			this.lockAnim = this.locked;
			this.setPas(false);
		}

		boolean last = this.lock;

		if (this.autoLock) {
			this.lock = false;

			for (int i = 0; i < 2; i++) {
				if (Player.instance != null && this.rooms[i] == Player.instance.room) {
					/*if (Player.instance.room instanceof LampRoom
						&& !Player.instance.getInventory().find(Lamp.class)) {

						this.lock = true;
						break;
					} else */if (Player.instance.room.numEnemies > 0) {
						this.lock = true;
						break;
					}
				}
			}
		}


		if (this.lock && !last) {
			this.playSfx("door_lock");

			this.lockAnim = this.lk;
			this.animation.setBack(true);
			this.animation.setPaused(false);
			this.setPas(false);
		} else if (!this.lock && last) {
			this.playSfx("door_unlock");

			this.lockAnim = this.unlock;
			this.setPas(true);
		}

		this.animation.render(this.x, this.y, false);

		if (this.lockAnim != null) {
			this.lockAnim.render(this.x + (this.vertical ? -1 : 3), this.y + (this.vertical ? 2 : -2), false);
		}
	}

	@Override
	public void renderShadow() {
		Graphics.shape.end();
		Graphics.batch.begin();
		this.animation.render(this.x, this.y - (this.vertical ? h / 2 - 2 : h), false, true, this.animation.getFrame());
		Graphics.batch.end();
		Graphics.shape.begin(ShapeRenderer.ShapeType.Filled);
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);

		this.burning = reader.readBoolean();

		if (this.burning) {
			this.damage = reader.readFloat();
		}

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
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);

		writer.writeBoolean(this.burning);

		if (this.burning) {
			writer.writeFloat(this.damage);
		}

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
	}

	@Override
	public boolean shouldCollide(Object entity, Contact contact, Fixture fixture) {
		if (entity instanceof Creature || entity instanceof PetEntity) {
			if (!this.lock || entity instanceof PetEntity) {
				return false;
			}
		}

		return super.shouldCollide(entity, contact, fixture);
	}
}
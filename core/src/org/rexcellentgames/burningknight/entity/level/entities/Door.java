package org.rexcellentgames.burningknight.entity.level.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.*;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.buff.BurningBuff;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.mob.boss.Boss;
import org.rexcellentgames.burningknight.entity.creature.npc.Trader;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.fx.TerrainFlameFx;
import org.rexcellentgames.burningknight.entity.item.accessory.equippable.Lootpick;
import org.rexcellentgames.burningknight.entity.item.entity.BombEntity;
import org.rexcellentgames.burningknight.entity.item.key.*;
import org.rexcellentgames.burningknight.entity.item.pet.impl.PetEntity;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.SaveableEntity;
import org.rexcellentgames.burningknight.entity.level.entities.fx.PoofFx;
import org.rexcellentgames.burningknight.entity.level.rooms.Room;
import org.rexcellentgames.burningknight.entity.level.rooms.special.NpcSaveRoom;
import org.rexcellentgames.burningknight.entity.level.save.GlobalSave;
import org.rexcellentgames.burningknight.entity.trap.RollingSpike;
import org.rexcellentgames.burningknight.game.Achievements;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.game.state.InGameState;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.*;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;

import java.io.IOException;
import java.util.ArrayList;

public class Door extends SaveableEntity {
	private boolean vertical;
	private Body body;
	private int numCollisions;
	private static Animation vertAnimation = Animation.make("actor-door-vertical", "-wooden");
	private static Animation horizAnimation = Animation.make("actor-door-horizontal", "-wooden");
	private static Animation vertAnimationFire = Animation.make("actor-door-vertical", "-iron");
	private static Animation horizAnimationFire = Animation.make("actor-door-horizontal", "-iron");
	private static Animation ironLockAnimation = Animation.make("door-lock", "-iron");
	private static Animation bronzeLockAnimation = Animation.make("door-lock", "-bronze");
	public static Animation goldLockAnimation = Animation.make("door-lock", "-gold");
	private static Animation fireLockAnimation = Animation.make("door-lock", "-fire");
	private AnimationData animation;
	public AnimationData locked;
	private AnimationData unlock;
	private AnimationData lk;
	private AnimationData lockAnim;

	public boolean autoLock;
	public boolean lockable;
	public boolean lock;
	public Class<? extends Key> key;
	private TextureRegion keyRegion;
	private int sx;
	private int sy;
	private boolean collidingWithPlayer;
	private float al;

	public static ArrayList<Door> all = new ArrayList<>();

	{
		depth = -1;
		alwaysActive = true;
	}

	public Door() {

	}

	public Animation getAnimation() {
		if (this.key == BurningKey.class) {
			return fireLockAnimation;
		}

		if (this.key == KeyA.class) {
			return bronzeLockAnimation;
		}

		if (this.autoLock || this.key == KeyB.class) {
			return ironLockAnimation;
		}

		return goldLockAnimation;
	}

	@Override
	public void init() {
		super.init();
		all.add(this);

	}

	public Door(int x, int y, boolean vertical) {
		this.x = x * 16;
		this.y = y * 16;
		this.sx = x;
		this.sy = y;
		this.vertical = vertical;
	}

	private void setPas(boolean pas) {
		Dungeon.level.setPassable((int) Math.floor(this.x / 16), (int) Math.floor((this.y + 8) / 16), pas);
	}

	private float lastFlame;
	private Body sensor;
	public boolean bkDoor;

	@Override
	public void update(float dt) {
		if (this.animation == null) {
			if (!this.vertical) {
				this.animation = bkDoor ? vertAnimationFire.get("idle") : vertAnimation.get("idle");
				this.y -= 8;
			} else {
				this.animation = bkDoor ? horizAnimationFire.get("idle") : horizAnimation.get("idle");
				this.x += 4;
			}

			this.animation.setAutoPause(true);
			this.animation.setPaused(true);
		}

		if (this.sensor == null) {
			this.sensor = World.createSimpleBody(this, this.vertical ? 1 : -1, this.vertical ? -5 : 7, this.vertical ? 6 : 18,
				this.vertical ? 22 : 6, BodyDef.BodyType.DynamicBody, false);
			this.sensor.setSleepingAllowed(false);


			MassData data = new MassData();
			data.mass = 1000000000000000f;

			this.sensor.setMassData(data);
		}

		World.checkLocked(this.sensor).setTransform(this.x, this.y, 0);

		if (locked == null) {
			Animation animation = getAnimation();

			locked = animation.get("idle");
			unlock = animation.get("open");
			lk = animation.get("close");
		}

		if (this.noColl > 0) {
			this.noColl -= dt;

			if (this.noColl <= 0) {
				this.noColl = -1;
				this.collidingWithPlayer = false;
			}
		}

		if (this.collidingWithPlayer) {
			if (Player.instance.isRolling()) {
				this.collidingWithPlayer = false;
				onCollisionEnd(Player.instance);
			}
		}

		boolean last = this.lock;

		if (this.lock) {
			vt = Math.max(0, vt - dt);
		}

		if (Dungeon.depth == -2) {
			Room a = Dungeon.level.findRoomFor(this.x + (vertical ? 16 : 0), this.y + (vertical ? 0 : 16));
			Room b = Dungeon.level.findRoomFor(this.x - (vertical ? 16 : 0), this.y - (vertical ? 0 : 16));
			boolean found = false;

			for (Trader trader : Trader.all) {
				if (trader.room == a || trader.room == b) {
					found = true;
					break;
				}
			}

			lock = !found;
		} else if (this.autoLock) {
			this.lock = Player.instance != null && Player.instance.room != null && Player.instance.room.numEnemies > 0 && !Player.instance.hasBkKey;
		}

		this.setPas(false);

		if (this.lock && !last) {
			this.playSfx("door_lock");

			if (this.body != null) {
				this.body.getFixtureList().get(0).setSensor(false);
			}

			this.lockAnim = this.lk;
			this.animation.setBack(true);
			this.animation.setPaused(false);
		} else if (!this.lock && last) {
			this.playSfx("door_unlock");

			this.lockAnim = this.unlock;
			// this.setPas(true);
		}

		this.al += ((this.collidingWithPlayer ? 1 : 0) - this.al) * dt * 10;

		if (this.lock && this.onScreen && this.al >= 0.5f && Input.instance.wasPressed("interact")) {
			if (key != BurningKey.class && Player.instance.ui.hasEquipped(Lootpick.class)) {

				this.body = World.removeBody(this.body);
				this.lock = false;
				this.animation.setBack(false);
				this.animation.setPaused(false);
				this.lockAnim = this.unlock;
			} else if ((this.key == KeyC.class && Player.instance.getKeys() > 0) || Player.instance.getInventory().find(this.key)) {
				if (this.key == KeyC.class) {
					Player.instance.setKeys(Player.instance.getKeys() - 1);
				} else {
					Player.instance.getInventory().remove(BurningKey.class);
				}

				Player.instance.playSfx("unlock");

				int num = GlobalSave.getInt("num_keys_used");
				GlobalSave.put("num_keys_used", num);

				if (num >= 10) {
					Achievements.unlock(Achievements.UNLOCK_LOOTPICK);
				}

				this.body = World.removeBody(this.body);
				this.lock = false;
				this.animation.setBack(false);
				this.animation.setPaused(false);
				this.lockAnim = this.unlock;

				if (this.key == KeyC.class) {
					Room room = Dungeon.level.findRoomFor(this.x, this.y);

					for (Trader trader : Trader.all) {
						if (trader.room == room) {
							trader.saved = true;
							GlobalSave.put("npc_" + trader.id + "_saved", true);
							trader.become("thanks");

							if (trader.id != null && trader.id.equals(NpcSaveRoom.saveOrder[NpcSaveRoom.saveOrder.length - 1])) {
								Achievements.unlock(Achievements.SAVE_ALL);
								GlobalSave.put("all_npcs_saved", true);
							}
							break;
						}
					}
				}
			} else {
				vt = 1;
				this.playSfx("item_nocash");
				Camera.shake(3);
			}
		}

		if (this.numCollisions == 0 && this.animation.isPaused() && this.animation.getFrame() == 3) {
			this.clearT += dt;

			if (this.clearT > 0.5f) {
				this.animation.setBack(true);
				this.animation.setPaused(false);
				this.playSfx("door");
			}
		}

		if (this.body == null && lock) {
			this.body = World.createSimpleBody(null, this.vertical ? 2 : 0, this.vertical ? -4 : 8, this.vertical ? 4 : 16,
				this.vertical ? 20 : 4, BodyDef.BodyType.DynamicBody, false);

			if (this.body != null) {
				World.checkLocked(this.body).setTransform(this.x, this.y, 0);
			}

			MassData data = new MassData();
			data.mass = 1000000000000000f;
			
			if (this.body != null) {
				this.body.setMassData(data);
			}
		}

		if (this.body != null) {
			World.checkLocked(this.body).setTransform(this.x, this.y, 0);
		}

		super.update(dt);

		if (this.animation.update(dt)) {
			if (this.animation.getFrame() == 3) {
				this.animation.setPaused(true);
			}

			if (this.numCollisions == 0 && animation.getFrame() == 0) {
				Filter d = this.sensor.getFixtureList().get(0).getFilterData();
				d.maskBits = 0x0003;
				this.sensor.getFixtureList().get(0).setFilterData(d);
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
			if (Dungeon.depth != -3) {
				int i = Level.toIndex((int) Math.floor(this.x / 16), (int) Math.floor((this.y + 8) / 16));
				int info = Dungeon.level.getInfo(i);

				if (BitHelper.isBitSet(info, 0)) {
					// Burning
					this.damage = 0;
					this.burning = true;

					for (int j : PathFinder.NEIGHBOURS4) {
						Dungeon.level.setOnFire(i + j, true);
					}
				}
			}
		} else {
			if (Dungeon.depth == -3) {
				burning = false;
				return;
			}

			InGameState.burning = true;

			if (this.key == KeyA.class || this.key == BurningKey.class) {
				this.burning = false;
				return;
			}

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
				if (this.key == KeyA.class) {
					Room room = Dungeon.level.findRoomFor(this.x, this.y);

					for (Trader trader : Trader.all) {
						if (trader.room == room) {
							trader.saved = true;
							GlobalSave.put("npc_" + trader.id + "_saved", true);
							trader.become("thanks");

							if (trader.id.equals(NpcSaveRoom.saveOrder[NpcSaveRoom.saveOrder.length - 1])) {
								Achievements.unlock(Achievements.SAVE_ALL);
								GlobalSave.put("all_npcs_saved", true);
							}
							break;
						}
					}
				}

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
		this.sensor = World.removeBody(this.sensor);
		all.remove(this);
	}

	@Override
	public void onCollision(Entity entity) {
		if (entity instanceof Creature && !(entity instanceof Boss)) {
			if (((Creature) entity).hasBuff(BurningBuff.class)) {
				this.burning = true;
			} else if (this.burning) {
				((Creature) entity).addBuff(new BurningBuff());
			}

			if (this.lock && this.lockable && entity instanceof Player) {
				this.collidingWithPlayer = true;
			}

			if (this.lock || !(entity instanceof Player)) {
				return;
			}

			if (!this.isOpen()) {
				if (this.body != null) {
					this.body.getFixtureList().get(0).setSensor(true);
				}

				this.playSfx("door");
			}

			this.numCollisions += 1;

			if (this.sensor != null) {
				Filter d = this.sensor.getFixtureList().get(0).getFilterData();
				d.maskBits = 0x0002;
				this.sensor.getFixtureList().get(0).setFilterData(d);
			}

			this.animation.setBack(false);
			this.animation.setPaused(false);
		}
	}

	private float noColl = -1;

	@Override
	public void onCollisionEnd(Entity entity) {
		if (entity instanceof Creature && !((Creature) entity).isFlying()) {
			if (this.lock) {
				if (entity instanceof Player) {
					this.noColl = 0.1f;
				}

				return;
			}

			if (entity instanceof Player) {
				this.numCollisions = 0; // to make sure
				this.clearT = 0;
			}
		}
	}

	private float clearT;

	public void renderSigns() {


		if (lockAnim != null && keyRegion != null && al > 0) {
			float v = vt <= 0 ? 0 : (float) (Math.cos(Dungeon.time * 18f) * 5 * (vt));
			Graphics.batch.setColor(1, 1, 1, al);
			Graphics.render(keyRegion, this.x - 3 + (16 - this.keyRegion.getRegionWidth()) / 2 + v + (this.vertical ? 0f : 4f), this.y + 16);
			Graphics.batch.setColor(1, 1, 1, 1);
		}
	}

	private float vt;

	@Override
	public void render() {
		if (animation == null) {
			return;
		}

		if (this.key != null && this.key != KeyB.class && this.keyRegion == null) {
			try {
				Key key = this.key.newInstance();
				this.keyRegion = key.getSprite();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		if (this.lock && this.lockAnim == null) {
			this.lockAnim = this.locked;
			this.setPas(false);
		}

		this.animation.render(this.x, this.y, false);

		if (this.lockAnim != null) {
			if (this.al > 0) {
				Graphics.batch.end();
				Mob.shader.begin();
				Mob.shader.setUniformf("u_color", ColorUtils.WHITE);
				Mob.shader.setUniformf("u_a", al);
				Mob.shader.end();
				Graphics.batch.setShader(Mob.shader);
				Graphics.batch.begin();

				for (int yy = -1; yy < 2; yy++) {
					for (int xx = -1; xx < 2; xx++) {
						if (Math.abs(xx) + Math.abs(yy) == 1) {
							this.lockAnim.render(this.x + (this.vertical ? -1 : 3) + xx, this.y + (this.vertical ? 2 : -2) + yy, false);
						}
					}
				}

				Graphics.batch.end();
				Graphics.batch.setShader(null);
				Graphics.batch.begin();
			}

			this.lockAnim.render(this.x + (this.vertical ? -1 : 3), this.y + (this.vertical ? 2 : -2), false);
		}
	}

	@Override
	public void renderShadow() {
		if (this.animation == null) {
			return;
		}

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

		if (reader.readBoolean()) {
			try {
				this.key = (Class<? extends Key>) Class.forName(reader.readString());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

		this.sx = reader.readInt16();
		this.sy = reader.readInt16();
		this.bkDoor = reader.readBoolean();
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

		writer.writeBoolean(this.lock && this.key != null);
		
		if (this.lock && this.key != null) {
			writer.writeString(this.key.getName());
		}

		writer.writeInt16((short) this.sx);
		writer.writeInt16((short) this.sy);
		writer.writeBoolean(this.bkDoor);
	}

	@Override
	public boolean shouldCollide(Object entity, Contact contact, Fixture fixture) {
		if (entity instanceof RollingSpike || ((entity instanceof Mob || entity instanceof BombEntity))) {
			return true;
		}

		if ((entity instanceof Creature || entity instanceof PetEntity)) {
			if (!this.lock || entity instanceof PetEntity) {
				return false;
			}
		}

		return false;//return super.shouldCollide(entity, contact, fixture);
	}
}
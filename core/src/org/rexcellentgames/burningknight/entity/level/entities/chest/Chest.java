package org.rexcellentgames.burningknight.entity.level.entities.chest;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.MassData;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.buff.BurningBuff;
import org.rexcellentgames.burningknight.entity.creature.fx.HeartFx;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.fx.Confetti;
import org.rexcellentgames.burningknight.entity.fx.TerrainFlameFx;
import org.rexcellentgames.burningknight.entity.item.*;
import org.rexcellentgames.burningknight.entity.item.key.KeyB;
import org.rexcellentgames.burningknight.entity.item.key.KeyC;
import org.rexcellentgames.burningknight.entity.item.permanent.BetterChestChance;
import org.rexcellentgames.burningknight.entity.item.weapon.Weapon;
import org.rexcellentgames.burningknight.entity.item.weapon.WeaponBase;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.Projectile;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.SaveableEntity;
import org.rexcellentgames.burningknight.entity.level.entities.Door;
import org.rexcellentgames.burningknight.entity.level.entities.fx.PoofFx;
import org.rexcellentgames.burningknight.entity.level.save.GlobalSave;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.entity.pool.Pool;
import org.rexcellentgames.burningknight.game.Ui;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.game.state.InGameState;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.*;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Map;

public class Chest extends SaveableEntity {
	private AnimationData data;
	protected Body body;
	protected Body sensor;
	protected boolean open;
	protected Item item;
	protected boolean create;
	public static ArrayList<Chest> all = new ArrayList<>();
	public boolean weapon;
	public boolean locked = true;
	public boolean burning;

	public static TextureRegion keyRegion = Graphics.getTexture("item-key_c");
	public static Animation lockAnimations = Animation.make("door-lock", "-gold");

	private AnimationData unlock = lockAnimations.get("open");
	private AnimationData lockUnlock = Door.goldLockAnimation.get("open");
	private boolean renderUnlock;
	public static TextureRegion idleLock = lockAnimations.getFrames("idle").get(0).frame;
	private TextureRegion halfBroken = getAnim().getFrames("break").get(0).frame;
	private TextureRegion broken = getAnim().getFrames("break").get(1).frame;

	private byte hp = 14;
	private float last;

	{
		h = 13;
	}

	@Override
	public void init() {
		super.init();

		this.data = this.getClosedAnim();

		this.data.setAutoPause(true);
		this.body = World.createSimpleBody(this, 4, 8, 8, 1, BodyDef.BodyType.DynamicBody, false);
		this.sensor = World.createSimpleBody(this, 0, 0, 16, 12, BodyDef.BodyType.DynamicBody, true);

		World.checkLocked(this.body).setTransform(this.x, this.y, 0);
		this.sensor.setTransform(this.x, this.y, 0);

		MassData data = new MassData();
		data.mass = 1000000000000000f;

		this.body.setMassData(data);

		all.add(this);
	}

	public Item generate() {
		return null;
	}

	public void setItem(Item item) {
		this.item = item;
		this.item.generate();
	}

	public static Pool<Item> makePool(ItemRegistry.Quality quality, boolean weapon, boolean any) {
		Pool<Item> pool = new Pool<>();

		for (Map.Entry<String, ItemRegistry.Pair> both : ItemRegistry.INSTANCE.getItems().entrySet()) {
			ItemRegistry.Pair item = both.getValue();

			if (item.getQuality().check(quality) && (any || (weapon == WeaponBase.class.isAssignableFrom(item.getType())))
				&& item.unlocked(both.getKey()) && Player.instance.getInventory().findItem(item.getType()) == null) {

				pool.add(item.getType(), item.getChance() * (
					item.getWarrior() * Player.instance.getWarrior() +
						item.getMage() * Player.instance.getMage() +
						item.getRanged() * Player.instance.getRanger()
				));
			}
		}

		return pool;
	}

	public static Item generate(ItemRegistry.Quality quality, boolean weapon) {
		Pool<Item> pool = makePool(quality, weapon, false);
		Item item = pool.generate();

		if (item == null) {
			Log.error("Failed to generate item " + quality + " (weapon = " + weapon + ")");
			return new KeyB();
		}

		return item;
	}

	private boolean collided;

	@Override
	public void onCollision(Entity entity) {
		if (this.hp == 0) {
			return;
		}

		if (entity instanceof Creature) {
			if (((Creature) entity).hasBuff(BurningBuff.class)) {
				this.burning = true;
			} else if (this.burning) {
				((Creature) entity).addBuff(new BurningBuff());
			}
		}

		if (entity instanceof Player) {
			if (this.locked) {
				this.colliding = true;

				if (!collided && Dungeon.depth == -3 && Ui.controls.size() == 0) {
					Ui.ui.addControl("[white]" + Input.instance.getMapping("interact") + " [gray]" + Locale.get("interact"));
					collided = true;
				}
			} else if (!this.open) {
				this.locked = false;
				this.open = true;
				this.data = this.getOpenAnim();

				this.data.setListener(new AnimationData.Listener() {
					@Override
					public void onEnd() {
						create = true;
					}
				});
			}
		}

		if ((entity instanceof Projectile || entity instanceof Weapon)) {
			if (Dungeon.depth == -3) {
				return;
			}

			if (entity instanceof Projectile) {
				if (!(((Projectile) entity).owner instanceof Player)) {
					return;
				}

				((Projectile) entity).remove();
			} else if (entity instanceof Weapon) {
				if (!(((Weapon) entity).getOwner() instanceof Player)) {
					return;
				}
			}

			hit();
		}
	}

	public void explode() {
		if (this.hp == 0) {
			return;
		}

		this.hp = 0;
		this.burning = false;

		for (int i = 0; i < 10; i++) {
			PoofFx fx = new PoofFx();

			fx.x = this.x + this.w / 2;
			fx.y = this.y + this.h / 2;

			Dungeon.area.add(fx);
		}

		this.locked = false;
		this.createLoot = true;

		this.body = World.removeBody(this.body);
		this.sensor = World.removeBody(this.sensor);
	}

	private void hit() {
		if (this.hp == 0) {
			return;
		}

		this.hp -= 1;

		if (this.hp <= 0) {
			this.hp = 0;
			this.burning = false;

			for (int i = 0; i < 10; i++) {
				PoofFx fx = new PoofFx();

				fx.x = this.x + this.w / 2;
				fx.y = this.y + this.h / 2;

				Dungeon.area.add(fx);
			}

			this.locked = false;
			this.createLoot = true;

			this.body = World.removeBody(this.body);
			this.sensor = World.removeBody(this.sensor);
		}
	}

	private boolean createLoot;

	@Override
	public void onCollisionEnd(Entity entity) {
		super.onCollisionEnd(entity);

		if (entity instanceof Player) {
			this.colliding = false;
		}
	}

	private float al;
	private boolean colliding;

	@Override
	public void destroy() {
		super.destroy();
		this.body = World.removeBody(this.body);
		this.sensor = World.removeBody(this.sensor);
		all.remove(this);
	}

	public void toMimic() {
		Mimic chest = new Mimic();

		chest.x = this.x;
		chest.y = this.y;
		chest.weapon = this.weapon;
		chest.locked = this.locked;

		Dungeon.area.add(chest);
		LevelSave.add(chest);

		this.done = true;
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);

		this.open = reader.readBoolean();
		World.checkLocked(this.body).setTransform(this.x, this.y, 0);

		if (!this.open) {
			String name = reader.readString();

			try {
				Class<?> clazz = Class.forName(name);
				Constructor<?> constructor = clazz.getConstructor();
				Object object = constructor.newInstance();

				Item item = (Item) object;
				item.load(reader);

				this.item = item;
			} catch (Exception e) {
				Log.error(name);
				Dungeon.reportException(e);
			}
		}

		if (this.item == null && !this.open) {
			Log.error("Something wrong with chest");
		}

		if (this.open) {
			this.data = this.getOpenedAnim();
		}

		this.locked = reader.readBoolean();
		this.hp = reader.readByte();
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);

		writer.writeBoolean(this.open);

		if (!this.open && this.item == null) {
			Log.error("Something wrong when saving");
		}

		if (!this.open) {
			writer.writeString(this.item.getClass().getName());
			this.item.save(writer);
		}

		writer.writeBoolean(this.locked);
		writer.writeByte(this.hp);
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (renderUnlock) {
			if (lockUnlock.update(dt)) {
				renderUnlock = false;
			}
		}

		if (!this.open) {
			this.last += dt;

			if (locked) {
				this.vt = Math.max(0, vt - dt);
			}

			if (this.last >= 0.6f) {
				last = 0;
				Spark.randomOn(this);
			}
		}

		if (this.hp == 0) {
			this.burning = false;
		}

		if (createLoot) {
			if (Dungeon.depth != -3) {
				if (Random.chance(50)) {
					HeartFx fx = new HeartFx();

					fx.x = this.x + (this.w - fx.w) / 2;
					fx.y = this.y + (this.h - fx.h) / 2;

					Dungeon.area.add(fx.add());
				}

				if (Random.chance(30)) {
					ItemHolder fx = new ItemHolder(new KeyC());

					fx.x = this.x + (this.w - fx.w) / 2;
					fx.y = this.y + (this.h - fx.h) / 2;

					Dungeon.area.add(fx.add());
				}

				if (Random.chance(30)) {
					ItemHolder fx = new ItemHolder(new Gold());

					fx.getItem().generate();
					fx.x = this.x + (this.w - fx.w) / 2;
					fx.y = this.y + (this.h - fx.h) / 2;

					Dungeon.area.add(fx.add());
				}
			}

			createLoot = false;
		}

		this.t += dt;

		if (this.item != null && this.create) {
			this.open();
		}

		if (this.data != null && this.data.update(dt)) {
			if (this.data == this.getOpenAnim()) {
				this.data = this.getOpenedAnim();
			}
		}

		if (this.body != null) {
			this.sensor.setTransform(this.x, this.y, 0);
			World.checkLocked(this.body).setTransform(this.x, this.y, 0);
		}

		this.al += ((this.colliding ? 1f : 0f) - this.al) * dt * 8;

		if (this.al >= 0.5f && Input.instance.wasPressed("interact")) {
			Input.instance.putState("inventory", Input.State.UP);

			if (this.locked) {
				Item key = Player.instance.getInventory().findItem(KeyC.class);

				if (key == null) {
					this.colliding = true;
					vt = 1;
					Player.instance.playSfx("item_nocash");

					Camera.shake(6);
					return;
				}

				if (Dungeon.depth == -3) {
					Ui.ui.hideControlsFast();
				}

				Player.instance.playSfx("unlock");

				drawOpenAnim = true;
				renderUnlock = true;

				key.setCount(key.getCount() - 1);
				this.locked = false;

				this.open = true;
				this.data = this.getOpenAnim();

				this.data.setListener(new AnimationData.Listener() {
					@Override
					public void onEnd() {
						create = true;
					}
				});
			}
		}

		if (this.drawOpenAnim) {
			if (unlock.update(dt)) {
				this.drawOpenAnim = false;
			}
		}

		if (this.burning) {
			lastFlame += dt;
			InGameState.burning = true;

			if (this.lastFlame >= 0.05f) {
				this.lastFlame = 0;
				TerrainFlameFx fx = new TerrainFlameFx();
				fx.x = this.x + Random.newFloat(this.w);
				fx.y = this.y + Random.newFloat(this.h) - 4;
				fx.depth = 1;
				Dungeon.area.add(fx);
			}

			damage += dt * 2;

			if (damage >= 1f) {
				damage = 0f;
				hit();
			}
		} else {
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
	}

	private float damage;
	private float lastFlame;
	private boolean drawOpenAnim;

	public void open() {
		this.playSfx("chest_open");

		if (this.collided) {
			Ui.ui.hideControlsFast();
		}

		ItemHolder holder = new ItemHolder(this.item);

		holder.x = this.x + (this.w - this.item.getSprite().getRegionWidth()) / 2;
		holder.y = this.y - 3;

		Dungeon.area.add(holder);
		LevelSave.add(holder);

		this.item = null;
		this.create = false;

		this.data = this.getOpenedAnim();
		this.open = true;


		for (int i = 0; i < 15; i++) {
			Confetti c = new Confetti();

			c.x = this.x + Random.newFloat(this.w);
			c.y = this.y + Random.newFloat(this.h);
			c.vel.x = Random.newFloat(-30f, 30f);
			c.vel.y = Random.newFloat(30f, 40f);

			Dungeon.area.add(c);
		}
	}

	public static Chest random() {
		float r = Random.newFloat();

		if (GlobalSave.isTrue(BetterChestChance.ID)) {
			r += 0.1f;
		}

		if (r < 0.5f) {
			WoodenChest chest = new WoodenChest();

			if (Random.chance(30)) {
				chest.locked = false;
			}

			return chest;
		} else if (r < 0.8f) {
			return new IronChest();
		}

		return new GoldenChest();
	}

	@Override
	public void render() {
		if (this.data != null) {
			TextureRegion sprite;

			if (this.hp != 0 && (this.open || this.hp > 6)) {
				sprite = this.data.getCurrent().frame;
			} else if (this.hp == 0) {
				sprite = broken;
			} else {
				sprite = halfBroken;
			}

			int w = sprite.getRegionWidth();

			float sx = 1f;
			float sy = 1f;
			Graphics.render(sprite, this.x + w / 2 - 1, this.y, 0,
				w / 2, 0, false, false, sx, sy);
		}

		float x = this.x + (w - idleLock.getRegionWidth()) / 2;
		float y = this.y + (h - idleLock.getRegionHeight()) / 2 +
			(float) Math.sin(this.t) * 1.8f;

		if (this.locked) {
			if (al > 0) {
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
							Graphics.render(this.idleLock, x + xx, y + yy);
						}
					}
				}

				Graphics.batch.end();
				Graphics.batch.setShader(null);
				Graphics.batch.begin();
			}

			Graphics.render(this.idleLock, x, y);
		} else if (drawOpenAnim) {
			unlock.render(x, y, false);
		}

		if (this.locked || al > 0) {
			float v = vt <= 0 ? 0 : (float) (Math.cos(Dungeon.time * 18f) * 5 * (vt));
			Graphics.batch.setColor(1, 1, 1, al);
			Graphics.render(keyRegion, this.x + (16 - keyRegion.getRegionWidth()) / 2 + v, this.y + 12);
			Graphics.batch.setColor(1, 1, 1, 1);
		}

		if (renderUnlock) {
			lockUnlock.render(x, y, false);
		}
	}

	private float vt;

	@Override
	public void renderShadow() {
		Graphics.shadow(this.x, this.y, this.w, this.h);
	}

	protected AnimationData getClosedAnim() {
		return null;
	}

	protected Animation getAnim() {
		return null;
	}

	protected AnimationData getOpenedAnim() {
		return null;
	}

	protected AnimationData getOpenAnim() {
		return null;
	}
}
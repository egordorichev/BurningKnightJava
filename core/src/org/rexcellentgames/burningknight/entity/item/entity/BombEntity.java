package org.rexcellentgames.burningknight.entity.item.entity;

import box2dLight.PointLight;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.*;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.buff.Buff;
import org.rexcellentgames.burningknight.entity.creature.buff.BurningBuff;
import org.rexcellentgames.burningknight.entity.creature.buff.FrozenBuff;
import org.rexcellentgames.burningknight.entity.creature.buff.PoisonedBuff;
import org.rexcellentgames.burningknight.entity.creature.buff.fx.FlameFx;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.npc.Shopkeeper;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Explosion;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.BulletProjectile;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.NanoBullet;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.entities.SolidProp;
import org.rexcellentgames.burningknight.entity.level.entities.chest.Chest;
import org.rexcellentgames.burningknight.entity.level.rooms.Room;
import org.rexcellentgames.burningknight.entity.level.save.GlobalSave;
import org.rexcellentgames.burningknight.game.Achievements;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

import java.util.ArrayList;

public class BombEntity extends Entity {
	public static Animation normal = Animation.make("actor-bomb", "-normal");
	public static Animation iced = Animation.make("actor-bomb", "-ice_bomb");
	public static Animation poisoned = Animation.make("actor-bomb", "-poison");
	public static Animation tiny = Animation.make("actor-bomb", "-small");

	private AnimationData animation;
	private Body body;
	public Point vel = new Point();
	public Creature owner;
	public boolean bullets;
	public ArrayList<Buff> toApply = new ArrayList<>();

	{
		depth = -1;
		alwaysActive = true;
	}

	private PointLight light;

	public BombEntity(float x, float y) {
		this.x = x;
		this.y = y;
		this.fliped = Random.chance(50);
		light = World.newLight(32, new Color(1, 0, 0, 1), 64, 0, 0);
		light.setPosition(this.x + 8, this.y + 8);
	}

	private boolean burning;
	private boolean poison;
	private boolean ice;
	public boolean small;
	private boolean fliped;
	private float mod;

	@Override
	public void init() {
		super.init();
		mod = Random.newFloat(0.95f, 1.05f);

		for (Buff buff : this.toApply) {
			if (buff instanceof BurningBuff) {
				this.burning = true;
			} else if (buff instanceof PoisonedBuff) {
				this.poison = true;
			} else if (buff instanceof FrozenBuff) {
				this.ice = true;
			}
		}

		if (this.small) {
			w = 8;
			h = 8;
		} else {
			w = 10;
			h = 14;
		}

		if (this.ice) {
			this.animation = iced.get("idle");
		} else if (this.poison) {
			this.animation = poisoned.get("idle");
		} else if (this.small) {
			this.animation = tiny.get("idle");
		} else {
			this.animation = normal.get("idle");
		}

		this.body = World.createSimpleBody(this, 0, 0, w, h, BodyDef.BodyType.DynamicBody, false);
		MassData data = new MassData();
		data.mass = 0.1f;
		this.body.setMassData(data);

		World.checkLocked(this.body).setTransform(this.x, this.y, 0);

		this.playSfx("bomb_placed");

		Room room = Dungeon.level.findRoomFor(this.x, this.y);

		if (Shopkeeper.instance != null && Shopkeeper.instance.room == room) {
			Shopkeeper.instance.enrage();
		}
	}

	private float t;
	public boolean leaveSmall;

	@Override
	public void destroy() {
		super.destroy();
		World.removeLight(this.light);
		this.body = World.removeBody(this.body);
	}

	public BombEntity toMouseVel() {
		return this.velTo(Input.instance.worldMouse.x, Input.instance.worldMouse.y, 60f);
	}

	public BombEntity velTo(float x, float y, float f) {
		float a = (float) Math.atan2(y - this.y - 8, x - this.x - 8);
		this.vel = new Point((float) Math.cos(a) * f, (float) Math.sin(a) * f);

		this.x += Math.cos(a) * 5f;
		this.y += Math.sin(a) * 5f;

		return this;
	}


	private float lastFlame;

	@Override
	public void update(float dt) {
		this.t += dt;

		if (this.burning) {
			this.lastFlame += dt;

			if (this.lastFlame >= 0.1f) {
				this.lastFlame = 0;
				Dungeon.area.add(new FlameFx(this));
			}
		}

		this.x = this.body.getPosition().x;
		this.y = this.body.getPosition().y;

		this.vel.mul(0.95f);
		this.body.setLinearVelocity(this.vel);

		if (this.animation.update(dt * this.mod)) {
			if (this.bullets) {
				for (int i = 0; i < 8; i++) {
					BulletProjectile bullet = new NanoBullet();

					float f = 60;
					float a = (float) (i * (Math.PI / 4));

					bullet.damage = 10;
					bullet.bad = this.owner instanceof Mob;
					bullet.x = (float) (this.x + Math.cos(a) * 8);
					bullet.y = (float) (this.y + Math.sin(a) * 8);
					bullet.velocity.x = (float) (Math.cos(a) * f);
					bullet.velocity.y = (float) (Math.sin(a) * f);

					Dungeon.area.add(bullet);
				}
			}

			if (this.leaveSmall && !this.small) {
				for (int i = 0; i < 4; i++) {
					BombEntity e = new BombEntity(this.x + this.w - Random.newFloat(this.w), this.y + this.h - Random.newFloat(this.h));

					e.small = true;

					float a = (float) (i * Math.PI / 2);
					float f = 200f;
					e.vel = new Point((float) Math.cos(a) * f, (float) Math.sin(a) * f);

					e.x += Math.cos(a) * 5f;
					e.y += Math.sin(a) * 5f;

					Dungeon.area.add(e);
				}
			}

			this.playSfx("explosion");
			this.done = true;
			Explosion.make(this.x + w / 2, this.y + h / 2, !this.small);

			boolean fire = false;
			boolean ice = false;

			for (Buff add : toApply) {
				if (add instanceof BurningBuff) {
					fire = true;
				} else if (add instanceof FrozenBuff) {
					ice = true;
				}
			}

			for (int i = 0; i < Dungeon.area.getEntities().size(); i++) {
				Entity entity = Dungeon.area.getEntities().get(i);

				if (entity instanceof Creature) {
					Creature creature = (Creature) entity;

					if (creature.getDistanceTo(this.x + w / 2, this.y + h / 2) < 24f) {
						if (!creature.explosionBlock) {
							if (creature instanceof Player) {
								creature.modifyHp(-1000, this, true);
							} else {
								creature.modifyHp(-Math.round(Random.newFloatDice(20 / 3 * 2, 20)), this, true);
							}
						}

						float a = (float) Math.atan2(creature.y + creature.h / 2 - this.y - 8, creature.x + creature.w / 2 - this.x - 8);

						float knockbackMod = creature.knockbackMod;
						creature.knockback.x += Math.cos(a) * 10f * knockbackMod;
						creature.knockback.y += Math.sin(a) * 10f * knockbackMod;

						try {
							for (Buff buff : toApply) {
								creature.addBuff(buff.getClass().newInstance().setDuration(buff.getDuration()));
							}
						} catch (IllegalAccessException | InstantiationException e) {
							e.printStackTrace();
						}
					}
				} else if (entity instanceof Chest) {
					if (entity.getDistanceTo(this.x + w / 2, this.y + h / 2) < 24f) {
						((Chest) entity).explode();
					}
				} else if (entity instanceof BombEntity) {
					BombEntity b = (BombEntity) entity;

					float a = (float) Math.atan2(b.y - this.y, b.x - this.x) + Random.newFloat(-0.5f, 0.5f);

					b.vel.x += Math.cos(a) * 200f;
					b.vel.y += Math.sin(a) * 200f;
				}
			}

			int s = 3;

			if (!fire && !ice) {
				for (int yy = -s; yy <= s; yy++) {
					for (int xx = -s; xx <= s; xx++) {
						int x = (int) ((this.x + this.w / 2) / 16 + xx);
						int y = (int) ((this.y + this.h / 2) / 16 + yy);

						if (Math.sqrt(xx * xx + yy * yy) <= s - 1) {
							int t = Dungeon.level.get(x, y);

							if (t == Terrain.FLOOR_A || t == Terrain.FLOOR_B || t == Terrain.FLOOR_C || t == Terrain.FLOOR_D) {
								Dungeon.level.set(x, y, Terrain.DIRT);
								Dungeon.level.tileRegion(x, y);
							}
						}
					}
				}
			}

			for (int yy = -s; yy <= s; yy++) {
				for (int xx = -s; xx <= s; xx++) {
					if (Math.sqrt(xx * xx + yy * yy) <= s) {
						int x = (int) ((this.x + this.w / 2) / 16 + xx);
						int y = (int) ((this.y + this.h / 2) / 16 + yy);

						if (fire) {
							Dungeon.level.setOnFire(Level.toIndex(x, y), true);
						}

						if (ice) {
							Dungeon.level.freeze(Level.toIndex(x, y));
						}
					}
				}
			}

			boolean set = false;

			for (Room room : Dungeon.level.getRooms()) {
				if (room.hidden) {
					if (check(room)) {
						set =  true;
					}
				}
			}

			if (set) {
				Dungeon.level.loadPassable();
				Dungeon.level.addPhysics();
			}
		}

		light.setPosition(this.x + w / 2, this.y + h / 2);
	}

	public boolean check(Room room) {
		for (int x = room.left; x <= room.right; x++) {
			for (int y = room.top; y <= room.bottom; y++) {
				if (Dungeon.level.get(x, y) == Terrain.CRACK && this.getDistanceTo(x * 16 + w / 2, y * 16 + h / 2) <= 32f) {
					make(room);

					Achievements.unlock(Achievements.FIND_SECRET_ROOM);

					int num = GlobalSave.getInt("num_secret_rooms_found") + 1;
					GlobalSave.put("num_secret_rooms_found", num);

					if (num >= 3) {
						Achievements.unlock(Achievements.UNLOCK_SPECTACLES);
					}

					room.hidden = false;
					Dungeon.level.set(x, y, Terrain.FLOOR_A);

					return true;
				}
			}
		}

		return false;
	}

	public static void make(Room room) {
		Player.instance.playSfx("secret");
		Player.instance.playSfx("secret_room");

		for (int x = room.left; x <= room.right; x++) {
			for (int y = room.top; y <= room.bottom; y++) {
				if (Dungeon.level.isValid(x, y)) {
					Dungeon.level.set(x, y, (byte) -Dungeon.level.data[Level.toIndex(x, y)]);
				}
			}
		}
	}

	@Override
	public void renderShadow() {
		Graphics.shadow(this.x, this.y, this.w, this.h, (float) (Math.cos(this.t * 16 + Math.PI)) * 3);
	}

	@Override
	public void render() {
		// (float x, float y, boolean flip, boolean flipY, float ox, float oy, float a, float sx, float sy)

		float sx = (float) (Math.cos(this.t * 16) / 2f) + 1;
		float sy = (float) (Math.cos(this.t * 16 + Math.PI) / 3f) + 1;

		if (Math.cos(this.t * 16 + Math.PI) > 0) {
			Graphics.batch.end();
			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			Graphics.shape.setColor(1, 0, 0, 0.4f);
			Graphics.shape.begin(ShapeRenderer.ShapeType.Line);
			Graphics.shape.circle(this.x + w / 2, this.y + h / 2, 24);
			Graphics.shape.circle(this.x + w / 2, this.y + h / 2, 23);
			Graphics.shape.circle(this.x + w / 2, this.y + h / 2, 23.5f);
			Graphics.endAlphaShape();
		}

		this.animation.render(this.x, this.y, false, false, 5, 0, 0, this.fliped ? -sx : sx, sy);
	}

	@Override
	public boolean shouldCollide(Object entity, Contact contact, Fixture fixture) {
		if (entity == null) {
			return true;
		}

		if (entity != null && !((entity instanceof Player && !((Player) entity).isRolling()) || entity instanceof BombEntity || entity instanceof SolidProp)) {
			return false;
		}

		return super.shouldCollide(entity, contact, fixture);
	}
}
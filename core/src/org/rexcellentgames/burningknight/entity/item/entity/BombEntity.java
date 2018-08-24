package org.rexcellentgames.burningknight.entity.item.entity;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.MassData;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.buff.Buff;
import org.rexcellentgames.burningknight.entity.creature.npc.Shopkeeper;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Explosion;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
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
	public static Animation animations = Animation.make("actor-bomb", "-normal");
	private AnimationData animation = animations.get("idle");
	private Body body;
	private Point vel = new Point();
	public Creature owner;
	public ArrayList<Buff> toApply = new ArrayList<>();

	{
		alwaysActive = true;
		w = 10;
		h = 14;
	}

	public BombEntity(float x, float y) {
		this.x = x;
		this.y = y;
		this.fliped = Random.chance(50);
	}

	private boolean fliped;

	@Override
	public void init() {
		super.init();

		this.body = World.createSimpleBody(this, 2, 2, 12, 12, BodyDef.BodyType.DynamicBody, false);
		MassData data = new MassData();
		data.mass = 0.1f;
		this.body.setMassData(data);

		this.body.setTransform(this.x, this.y, 0);

		this.playSfx("bomb_placed");

		Room room = Dungeon.level.findRoomFor(this.x, this.y);

		if (Shopkeeper.instance != null && Shopkeeper.instance.room == room) {
			Shopkeeper.instance.enrage();
		}
	}

	private float t;

	@Override
	public void destroy() {
		super.destroy();
		this.body = World.removeBody(this.body);
	}

	public BombEntity randomVel() {
		float a = Random.newFloat((float) (Math.PI * 2));
		this.vel = new Point((float) Math.cos(a) * 50f, (float) Math.sin(a) * 50f);

		this.x += Math.cos(a) * 5f;
		this.y += Math.sin(a) * 5f;

		return this;
	}

	public BombEntity toMouseVel() {
		return this.velTo(Input.instance.worldMouse.x, Input.instance.worldMouse.y);
	}

	public BombEntity velTo(float x, float y) {
		float a = (float) Math.atan2(y - this.y - 8, x - this.x - 8);
		this.vel = new Point((float) Math.cos(a) * 50f, (float) Math.sin(a) * 50f);

		this.x += Math.cos(a) * 5f;
		this.y += Math.sin(a) * 5f;

		return this;
	}

	@Override
	public void update(float dt) {
		this.t += dt;

		this.x = this.body.getPosition().x;
		this.y = this.body.getPosition().y;

		this.vel.mul(0.9f);
		this.body.setLinearVelocity(this.vel);

		if (this.animation.update(dt)) {
			this.playSfx("explosion");
			this.done = true;
			Explosion.make(this.x + 8, this.y + 8);

			for (int i = 0; i < Dungeon.area.getEntities().size(); i++) {
				Entity entity = Dungeon.area.getEntities().get(i);

				if (entity instanceof Creature) {
					Creature creature = (Creature) entity;

					if (creature.getDistanceTo(this.x + 8, this.y + 8) < 24f) {
						if (!creature.explosionBlock) {
							if (creature instanceof Player) {
								creature.modifyHp(-1000, this.owner, true);
							} else {
								creature.modifyHp(-Math.round(Random.newFloatDice(20 / 3 * 2, 20)), this.owner, true);
							}
						}

							float a = (float) Math.atan2(creature.y + creature.h / 2 - this.y - 8, creature.x + creature.w / 2 - this.x - 8);

							float knockbackMod = creature.getStat("knockback");
							creature.vel.x += Math.cos(a) * 5000f * knockbackMod;
							creature.vel.y += Math.sin(a) * 5000f * knockbackMod;

						try {
							for (Buff buff : toApply) {
								creature.addBuff(buff.getClass().newInstance().setDuration(buff.getDuration()));
							}
						} catch (IllegalAccessException | InstantiationException e) {
							e.printStackTrace();
						}
					}
				}
			}

			int s = 2;

			for (int yy = -s; yy <= s; yy++) {
				for (int xx = -s; xx <= s; xx++) {
					int x = (int) ((this.x + this.w / 2) / 16 + xx);
					int y = (int) ((this.y + this.h / 2) / 16 + yy);

					if (Math.sqrt(xx * xx + yy * yy) <= s) {

						int t = Dungeon.level.get(x, y);

						if (t == Terrain.FLOOR_A || t == Terrain.FLOOR_B || t == Terrain.FLOOR_C || t == Terrain.FLOOR_D) {
							Dungeon.level.set(x, y, Terrain.DIRT);
							Dungeon.level.tileRegion(x, y);
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
	}

	public boolean check(Room room) {
		for (int x = room.left; x <= room.right; x++) {
			for (int y = room.top; y <= room.bottom; y++) {
				if (Dungeon.level.get(x, y) == Terrain.CRACK && this.getDistanceTo(x * 16 + 8, y * 16 + 8) <= 32f) {
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

		float sx = (float) (Math.cos(this.t * 16) / 4) + 1;
		float sy = (float) (Math.cos(this.t * 16 + Math.PI) / 5) + 1;

		this.animation.render(this.x, this.y, false, false, 5, 0, 0, this.fliped ? -sx : sx, sy);
	}

	@Override
	public boolean shouldCollide(Entity entity, Contact contact) {
		if (entity != null && !(entity instanceof Player)) {
			return false;
		}

		return super.shouldCollide(entity, contact);
	}
}
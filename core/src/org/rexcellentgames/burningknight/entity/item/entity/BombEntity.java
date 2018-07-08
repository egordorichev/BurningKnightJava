package org.rexcellentgames.burningknight.entity.item.entity;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.MassData;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.buff.Buff;
import org.rexcellentgames.burningknight.entity.creature.npc.Shopkeeper;
import org.rexcellentgames.burningknight.entity.item.Explosion;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.rooms.Room;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

import java.util.ArrayList;

public class BombEntity extends Entity {
	public static Animation animations = Animation.make("actor-bomb");
	private AnimationData animation = animations.get("idle");
	private Body body;
	private Point vel;
	public Creature owner;
	private boolean flip = Random.chance(50);
	public ArrayList<Buff> toApply = new ArrayList<>();

	{
		alwaysActive = true;
	}

	public BombEntity(float x, float y) {
		this.x = x;
		this.y = y;
	}

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
							creature.modifyHp(-Math.round(Random.newFloatDice(10 / 3 * 2, 10)), this.owner, true);
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

			for (int xx = -s; xx <= s; xx++) {
				for (int yy = -s; yy <= s; yy++) {
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
				Dungeon.level.set(x, y, (byte) -Dungeon.level.data[Level.toIndex(x, y)]);
			}
		}
	}

	@Override
	public void render() {
		this.animation.render(this.x, this.y, this.flip);
	}
}
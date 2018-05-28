package org.rexellentgames.dungeon.entity.item.entity;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.creature.Creature;
import org.rexellentgames.dungeon.entity.creature.mob.BurningKnight;
import org.rexellentgames.dungeon.entity.creature.mob.Mob;
import org.rexellentgames.dungeon.entity.item.Explosion;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.rooms.Room;
import org.rexellentgames.dungeon.entity.plant.Plant;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.physics.World;
import org.rexellentgames.dungeon.util.Animation;
import org.rexellentgames.dungeon.util.AnimationData;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.geometry.Point;

public class BombEntity extends Entity {
	public static Animation animations = Animation.make("actor-bomb");
	private AnimationData animation = animations.get("idle");
	private Body body;
	private Point vel;
	public Creature owner;
	private boolean flip = Random.chance(50);

	public BombEntity(float x, float y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public void init() {
		super.init();

		this.body = World.createSimpleBody(this, 2, 2, 12, 12, BodyDef.BodyType.DynamicBody, true);
		this.body.setTransform(this.x, this.y, 0);

		this.playSfx("bomb_placed");
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
			Dungeon.area.add(new Explosion(this.x + 8, this.y + 8));

			for (int i = 0; i < Dungeon.area.getEntities().size(); i++) {
				Entity entity = Dungeon.area.getEntities().get(i);

				if (entity instanceof Creature) {
					Creature creature = (Creature) entity;

					if (creature.getDistanceTo(this.x + 8, this.y + 8) < 24f) {
						creature.modifyHp(-Math.round(Random.newFloatDice(10 / 3 * 2, 10)), this.owner, true);

						float a = (float) Math.atan2(creature.y + creature.h / 2 - this.y - 8, creature.x + creature.w / 2 - this.x - 8);

						creature.vel.x += Math.cos(a) * 5000f;
						creature.vel.y += Math.sin(a) * 5000f;
					}
				} else if (entity instanceof Plant) {
					Plant creature = (Plant) entity;

					float dx = creature.x + creature.w / 2 - this.x - 8;
					float dy = creature.y + creature.h / 2 - this.y - 8;

					if (Math.sqrt(dx * dx + dy * dy) < 24f) {
						creature.startBurning();
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

	private boolean check(Room room) {
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

	private void make(Room room) {
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
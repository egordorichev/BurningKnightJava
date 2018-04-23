package org.rexellentgames.dungeon.entity.creature.fx;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.NetworkedEntity;
import org.rexellentgames.dungeon.entity.creature.Creature;
import org.rexellentgames.dungeon.entity.creature.buff.BurningBuff;
import org.rexellentgames.dungeon.entity.creature.mob.Mob;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.item.weapon.Weapon;
import org.rexellentgames.dungeon.entity.plant.Plant;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.util.Animation;
import org.rexellentgames.dungeon.util.AnimationData;
import org.rexellentgames.dungeon.util.Random;

public class Fireball extends NetworkedEntity implements WormholeFx.Suckable {
	private static Animation animations = Animation.make("fx-fireball");
	private AnimationData born;
	private AnimationData idle;
	private AnimationData dead;
	private AnimationData animation;
	private float t;
	private boolean flip;
	public Creature target;
	public boolean toMouse;
	private Body body;
	public boolean noMove;
	public boolean bad = true;
	public Vector2 vel;
	public static Sound cast = Graphics.getSound("sfx/fireball_cast_sfx.wav");
	public static Sound brk = Graphics.getSound("sfx/fireball_break_sfx.wav");

	@Override
	public Body getBody() {
		return body;
	}

	@Override
	public void init() {
		super.init();

		this.t = Random.newFloat(0.5f);

		this.playSfx(cast);

		WormholeFx.suck.add(this);

		this.depth = 11;
		this.flip = Random.chance(50);

		this.born = animations.get("appear");
		this.idle = animations.get("idle");
		this.dead = animations.get("dead");

		this.animation = this.born;

		this.y -= 4;

		this.body = this.createBody(0, 0, 16, 16, BodyDef.BodyType.DynamicBody, true);
		this.body.setTransform(this.x, this.y, 0);
		this.body.setBullet(true);

		if (this.vel == null) {
			this.vel = new Vector2();

			if (this.target != null) {
				float dx = this.target.x + this.target.w / 2 - this.x - 5;
				float dy = this.target.y + this.target.h / 2 - this.y - 5;
				float d = (float) Math.sqrt(dx * dx + dy * dy);

				this.vel.x = dx / d * 3;
				this.vel.y = dy / d * 3;

				this.body.setLinearVelocity(this.vel);
			}
		} else {
			this.body.setLinearVelocity(this.vel);
		}
	}

	@Override
	public void destroy() {
		super.destroy();
		WormholeFx.suck.remove(this);
		this.body.getWorld().destroyBody(this.body);
	}

	private boolean changed;

	@Override
	public void onCollision(Entity entity) {
		if (this.animation != this.idle) {
			return;
		}

		if (entity instanceof Mob && !((Mob) entity).isDead()) {
			((Mob) entity).modifyHp(Math.round(Random.newFloat(this.noMove ? -3 : -5 / 3 * 2, this.noMove ? -3 : -5)), true);
			this.animation = this.dead;
			this.playSfx(brk);
			((Mob) entity).addBuff(new BurningBuff().setDuration(3f));
		} else if (entity instanceof Player && this.bad) {
			((Player) entity).modifyHp(Math.round(Random.newFloat(this.noMove ? -3 : -5 / 3 * 2, this.noMove ? -3 : -5)), true);
			this.animation = this.dead;
			this.playSfx(brk);
			((Player) entity).addBuff(new BurningBuff().setDuration(3f));
		} else if (entity instanceof Weapon && this.bad) {
			if (((Weapon) entity).getOwner() instanceof Player) {
				if (this.target != null || this.toMouse) {
					this.animation = this.dead;
					this.playSfx(brk);
				} else if (!this.changed) {
					this.vel.x *= -1;
					this.vel.y *= -1;
					this.body.setLinearVelocity(this.vel);
					this.changed = true;
				}
			}
		} else if (entity instanceof Plant) {
			((Plant) entity).startBurning();
		}
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		this.x = this.body.getPosition().x;
		this.y = this.body.getPosition().y;

		if (!this.noMove) {
			this.vel = this.body.getLinearVelocity();
		}

		this.t += dt;

		if (Dungeon.level != null) {
			Dungeon.level.addLightInRadius(this.x + 8, this.y + 8, 3f, 0.8f, 0f, 2f, 2f, true);
		}

		if (this.animation.update(dt)) {
			if (this.animation == this.born) {
				this.animation = this.idle;
			} else if (this.animation == this.dead) {
				this.done = true;
			} else if (this.t >= 4f) {
				this.playSfx(brk);
				this.animation = this.dead;
			}
		}

		if (this.target != null) {
			float dx = this.target.x + this.target.w / 2 - this.x - 5;
			float dy = this.target.y + this.target.h / 2 - this.y - 5;
			float d = (float) Math.sqrt(dx * dx + dy * dy);

			this.vel.x += dx / d * 3;
			this.vel.y += dy / d * 3;
		} else if (this.toMouse) {
			float dx = Input.instance.worldMouse.x - this.x - 5;
			float dy = Input.instance.worldMouse.y - this.y - 5;
			float d = (float) Math.sqrt(dx * dx + dy * dy);

			this.vel.x += dx / d * 3;
			this.vel.y += dy / d * 3;
		}

		if (!this.noMove) {
			if (this.animation == this.idle) {
				this.body.setLinearVelocity(this.vel);
			} else {
				float s = 1.03f;
				this.body.setLinearVelocity(this.vel.x * s, this.vel.y * s);
			}
		}
	}

	@Override
	public void render() {
		this.animation.render(this.x, this.y, this.flip);
	}
}
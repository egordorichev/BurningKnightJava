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
import org.rexellentgames.dungeon.entity.item.weapon.Sword;
import org.rexellentgames.dungeon.entity.item.weapon.Weapon;
import org.rexellentgames.dungeon.entity.plant.Plant;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.util.Animation;
import org.rexellentgames.dungeon.util.AnimationData;
import org.rexellentgames.dungeon.util.Random;

public class Fireball extends NetworkedEntity {
	private static Animation animations = Animation.make("fireball");
	private AnimationData born;
	private AnimationData idle;
	private AnimationData dead;
	private AnimationData animation;
	private float t;
	private boolean flip;
	public Creature target;
	public float a;
	public boolean toMouse;
	private Body body;
	public boolean noMove;
	public boolean bad = true;
	public Vector2 vel;
	public static Sound cast = Graphics.getSound("sfx/fireball_cast_sfx.wav");
	public static Sound brk = Graphics.getSound("sfx/fireball_break_sfx.wav");

	@Override
	public void init() {
		super.init();

		cast.play();

		this.depth = 11;
		this.flip = Random.chance(50);

		this.born = animations.get("appear");
		this.idle = animations.get("idle");
		this.dead = animations.get("dead");

		this.animation = this.born;

		if (this.target != null) {
			float dx = this.target.x + this.target.w / 2 - this.x - 5;
			float dy = this.target.y + this.target.h / 2 - this.y - 5;
			this.a = (float) Math.atan2(dy, dx);
		}

		this.body = this.createBody(0, 0, 10, 10, BodyDef.BodyType.DynamicBody, true);
		this.body.setTransform(this.x, this.y, 0);
		this.body.setBullet(true);
	}

	@Override
	public void destroy() {
		super.destroy();
		this.body.getWorld().destroyBody(this.body);
	}

	@Override
	public void onCollision(Entity entity) {
		if (this.animation != this.idle) {
			return;
		}

		if (entity instanceof Mob && !this.bad && !((Mob) entity).isDead()) {
			((Mob) entity).modifyHp(this.noMove ? -3 : -5, true);
			this.animation = this.dead;
			brk.play();
			((Mob) entity).addBuff(new BurningBuff().setDuration(3f));
		} else if (entity instanceof Player && this.bad) {
			((Player) entity).modifyHp(this.noMove ? -3 : -5, true);
			this.animation = this.dead;
			brk.play();
			((Player) entity).addBuff(new BurningBuff().setDuration(3f));
		} else if (entity instanceof Weapon && this.bad) {
			if (((Weapon) entity).getOwner() instanceof Player) {
				this.animation = this.dead;
				brk.play();
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

		this.t += dt;

		if (Dungeon.level != null) {
			Dungeon.level.addLight(this.x + 5, this.y + 5, 3f, 0.8f, 0f, 2f, 3f);
		}

		if (this.animation.update(dt)) {
			if (this.animation == this.born) {
				this.animation = this.idle;
			} else if (this.animation == this.dead) {
				this.done = true;
			} else if (this.t >= 4f) {
				brk.play();
				this.animation = this.dead;
			}
		}

		float s = 60;

		if (this.target != null) {
			s = 30;

			float dx =  this.target.x + this.target.w / 2 - this.x - 5;
			float dy = this.target.y + this.target.h / 2 - this.y - 5;
			float d = (float) Math.atan2(dy, dx);

			this.a += (d - this.a) / 70f;
		}

		if (!this.noMove) {
			if (this.vel != null) {
				this.body.setLinearVelocity(this.vel);
			} else {
				this.body.setLinearVelocity((float) Math.cos(this.a) * s, (float) Math.sin(this.a) * s);
			}
		}
	}

	@Override
	public void render() {
		this.animation.render(this.x, this.y, this.flip);
	}
}
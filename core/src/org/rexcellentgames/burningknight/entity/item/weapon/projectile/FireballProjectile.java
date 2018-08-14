package org.rexcellentgames.burningknight.entity.item.weapon.projectile;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.buff.BurningBuff;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.fx.BKSFx;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class FireballProjectile extends Projectile {
	private static Animation animations = Animation.make("fx-fireball");
	private AnimationData born = animations.get("appear");
	private AnimationData idle = animations.get("idle");
	private AnimationData dead = animations.get("dead");
	private AnimationData animation = born;

	public Point tar;
	public Creature target;
	private float tt;

	{
		alwaysActive = true;
		depth = 12;
	}

	@Override
	protected boolean hit(Entity entity) {
		if (this.animation != idle) {
			return false;
		}

		if (this.bad) {
			if (entity instanceof Player) {
				this.doHit(entity);
				return true;
			}
		} else if (entity instanceof Mob) {
			this.doHit(entity);
			return true;
		}

		return false;
	}

	@Override
	protected void doHit(Entity entity) {
		super.doHit(entity);

		if (entity instanceof Creature) {
			((Creature) entity).addBuff(new BurningBuff());
		}
	}

	@Override
	protected void death() {
		this.animation = dead;
	}

	private float last;

	@Override
	public void update(float dt) {
		this.tt += dt;
		last += dt;

		if (last >= 0.2f) {
			last = 0;

			TextureRegion texture = this.animation.getCurrent().frame;

			float sx = (float) (1f + Math.cos(this.t * 7) / 6f);
			float sy = (float) (1f + Math.sin(this.t * 6f) / 6f);

			BKSFx fx = new BKSFx();

			fx.depth = this.depth - 1;
			fx.x = this.x;
			fx.y = this.y;
			fx.color = new Vector3(1, 0.1f + Random.newFloat(0.4f), 0.1f + Random.newFloat(0.2f));
			fx.region = texture;
			fx.ox = texture.getRegionWidth() / 2;
			fx.oy = texture.getRegionWidth() / 2;
			fx.sx = sx;
			fx.sy = sy;
			fx.speed = 2;

			Dungeon.area.add(fx);
		}

		if (this.animation.update(dt)) {
			if (this.animation == born) {
				this.animation = idle;
			} else if (this.animation == dead) {
				this.done = true;
			}
		}

		super.update(dt);

		Dungeon.level.addLightInRadius(this.x, this.y, 0.5f, 0.3f, 0, 2f, 3f, true);

		if (this.tt >= 10f) {
			this.animation = dead;
		}

		if (this.tar != null) {
			this.vel.x += (this.tar.x - this.vel.x) * dt;
			this.vel.y += (this.tar.y - this.vel.y) * dt;
		}
	}

	@Override
	public void init() {
		super.init();
		this.playSfx("fireball_cast");

		this.body = World.createCircleCentredBody(this, 0, 0, 6, BodyDef.BodyType.DynamicBody, true);
		this.body.setTransform(this.x, this.y, 0);
		this.body.setBullet(true);

		if (this.target != null) {
			float dx = this.target.x + this.target.w / 2 - this.x - 5;
			float dy = this.target.y + this.target.h / 2 - this.y - 5;
			float d = (float) Math.sqrt(dx * dx + dy * dy);

			this.vel.x = dx / d * 120;
			this.vel.y = dy / d * 120;

			this.body.setLinearVelocity(this.vel);
		}
	}

	@Override
	public void render() {
		TextureRegion texture = this.animation.getCurrent().frame;

		float sx = (float) (1f + Math.cos(this.t * 7) / 6f);
		float sy = (float) (1f + Math.sin(this.t * 6f) / 6f);

		Graphics.render(texture, this.x, this.y,
			0, texture.getRegionWidth() / 2, texture.getRegionHeight() / 2, false, false, sx, sy);
	}
}
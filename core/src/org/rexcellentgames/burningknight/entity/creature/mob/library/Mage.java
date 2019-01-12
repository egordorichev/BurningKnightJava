package org.rexcellentgames.burningknight.entity.creature.mob.library;

import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.BulletProjectile;
import org.rexcellentgames.burningknight.entity.pattern.BulletPattern;
import org.rexcellentgames.burningknight.entity.pattern.CircleBulletPattern;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;

public class Mage extends Mob {
	public static Animation animations = Animation.make("actor-mage", "-yellow");
	private AnimationData idle;
	private AnimationData killed;
	private AnimationData hurt;
	private AnimationData appear;
	private AnimationData dissappear;
	private AnimationData animation;

	public Animation getAnimation() {
		return animations;
	}

	{
		hpMax = 16;

		idle = getAnimation().get("idle");
		hurt = getAnimation().get("hurt");
		killed = getAnimation().get("dead");
		appear = getAnimation().get("appear");
		dissappear = getAnimation().get("dissappear");
		animation = idle;
		w = 21;
	}

	private PointLight light;

	@Override
	public void init() {
		super.init();

		flying = true;
		this.body = this.createSimpleBody(7, 0, 7, 14, BodyDef.BodyType.DynamicBody, false);
		World.checkLocked(this.body).setTransform(this.x, this.y, 0);

		light = World.newLight(32, new Color(1, 1, 1, 1f), 64, x, y);
		light.setIgnoreAttachedBody(true);
	}

	@Override
	public void destroy() {
		super.destroy();
		World.removeLight(light);
		this.body = World.removeBody(this.body);
	}

	@Override
	public void render() {
		if (this.target != null) {
			this.flipped = this.target.x < this.x;
		} else {
			if (Math.abs(this.velocity.x) > 1f) {
				this.flipped = this.velocity.x < 0;
			}
		}

		if (this.dead) {
			this.animation = killed;
		} else if (this.invt > 0) {
			this.animation = hurt;
		} else if (this.state.equals("dissappear")) {
			this.animation = dissappear;
		} else if (this.state.equals("appear")) {
			this.animation = appear;
		} else {
			this.animation = idle;
		}

		this.renderWithOutline(this.animation);
		Graphics.batch.setColor(1, 1, 1, 1);
		super.renderStats();
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		light.setActive(true);
		light.attachToBody(body, 4, 8, 0);
		light.setPosition(x + 10, y + 8);
		light.setDistance(64);

		animation.update(dt);
		super.common();
	}

	@Override
	protected void deathEffects() {
		super.deathEffects();

		this.playSfx("death_clown");
		deathEffect(killed);
	}

	@Override
	protected void onHurt(int a, Entity creature) {
		super.onHurt(a, creature);
		this.playSfx("damage_clown");
	}

	@Override
	protected State getAi(String state) {
		switch (state) {
			case "attack": return new AttackState();
			case "idle": case "roam": case "alerted": return new IdleState();
		}

		return super.getAi(state);
	}

	public class MageState extends Mob.State<Mage> {

	}

	public class AttackState extends MageState {
		@Override
		public void update(float dt) {
			super.update(dt);

			if (self.target.room != self.room) {
				self.become("idle");
			}

			if (t >= 4f) {
				t = 0;

				CircleBulletPattern pattern = new CircleBulletPattern();

				for (int i = 0; i < 8; i++) {
					pattern.addBullet(newProjectile());
				}

				BulletPattern.fire(pattern, self.x + 10, self.y + 8, self.getAngleTo(self.target.x + 8, self.target.y + 8), 60f);
			}
		}
	}

	public BulletProjectile newProjectile() {
		BulletProjectile bullet = new BulletProjectile();

		bullet.sprite = Graphics.getTexture("bullet-nano");

		bullet.damage = 1;
		bullet.letter = "bullet-nano";
		bullet.owner = this;
		bullet.bad = true;

		float a = 0; // getAngleTo(target.x + 8, target.y + 8);

		bullet.x = x;
		bullet.y = y;
		bullet.velocity.x = (float) (Math.cos(a));
		bullet.velocity.y = (float) (Math.sin(a));

		return bullet;
	}

	public class IdleState extends MageState {
		@Override
		public void update(float dt) {
			checkForPlayer();

			if (self.target != null && self.target.room == self.room) {
				self.become("attack");
			}
		}
	}
}
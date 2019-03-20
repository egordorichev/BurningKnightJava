package org.rexcellentgames.burningknight.entity.creature.mob.ice;

import box2dLight.PointLight;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.buff.FrozenBuff;
import org.rexcellentgames.burningknight.entity.creature.fx.Firefly;
import org.rexcellentgames.burningknight.entity.creature.fx.HpFx;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.BulletProjectile;
import org.rexcellentgames.burningknight.entity.level.entities.Door;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.Tween;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class IceElemental extends Mob {
	public static Animation animations = Animation.make("actor-ice-elemental", "-blue");
	private AnimationData idle;
	private AnimationData killed;
	private Body aura;

	public Animation getAnimation() {
		return animations;
	}

	{
		hpMax = 16;

		idle = getAnimation().get("idle");
		killed = getAnimation().get("dead");
	}

	private PointLight light;

	@Override
	public void knockBackFrom(Entity from, float force) {

	}

	@Override
	public void init() {
		super.init();

		idle.randomize();

		flying = true;
		this.body = World.createCircleCentredBody(this, 8, 8, 8, BodyDef.BodyType.DynamicBody, false);
		World.checkLocked(this.body).setTransform(this.x, this.y, 0);
		body.getFixtureList().get(0).setRestitution(1f);

		this.aura = World.createCircleCentredBody(this, 8, 8, 24, BodyDef.BodyType.DynamicBody, true);
		World.checkLocked(this.aura).setTransform(this.x, this.y, 0);

		float f = 32;
		this.velocity = new Point(f * (Random.chance(50) ? -1 : 1), f * (Random.chance(50) ? -1 : 1));

		body.setLinearVelocity(this.velocity);

		light = World.newLight(32, Firefly.colorIce, 64, x, y);
		light.setIgnoreAttachedBody(true);
	}

	@Override
	public HpFx modifyHp(int amount, Creature from) {
		return null;
	}

	@Override
	public void destroy() {
		super.destroy();
		this.body = World.removeBody(this.body);
		this.aura = World.removeBody(this.aura);
		World.removeLight(light);
	}

	private float rad = 24;

	@Override
	public float getOy() {
		return 16;
	}

	@Override
	public void render() {
		if (rad > 0) {
			Graphics.startAlphaShape();
			Graphics.shape.setColor(Firefly.colorIce.r, Firefly.colorIce.g, Firefly.colorIce.b, 0.3f);
			Graphics.shape.circle(x + 8, y + 8, rad);
			Graphics.endAlphaShape();
		}

		this.renderWithOutline(this.idle);
		Graphics.batch.setColor(1, 1, 1, 1);
		super.renderStats();
	}

	@Override
	public void die() {
		super.die();

		Tween.to(new Tween.Task(0, 0.1f) {
			@Override
			public float getValue() {
				return rad;
			}

			@Override
			public void setValue(float value) {
				rad = value;
			}
		});
	}

	@Override
	public void renderShadow() {
		Graphics.shadow(x, y, w, h, 10);
	}

	protected boolean stop;
	private Vector2 lastVel;

	@Override
	public void update(float dt) {
		super.update(dt);

		light.setActive(true);
		light.attachToBody(body, 0, 0, 0);
		light.setPosition(x + 8, y + 8);
		light.setDistance(64);

		if (this.body != null) {
			this.velocity.x = this.body.getLinearVelocity().x;
			this.velocity.y = this.body.getLinearVelocity().y;

			if (lastVel == null && stop) {
				lastVel = new Vector2(velocity.x, velocity.y);
			} else if (!stop && lastVel != null) {
				velocity.x = lastVel.x;
				velocity.y = lastVel.y;
				lastVel = null;
			}

			if (stop) {
				this.body.setLinearVelocity(0, 0);
			} else {
				float a = (float) Math.atan2(this.velocity.y, this.velocity.x);
				this.body.setLinearVelocity(((float) Math.cos(a)) * 32 * Mob.speedMod, ((float) Math.sin(a)) * 32 * Mob.speedMod);
			}

			World.checkLocked(this.aura).setTransform(this.x, this.y, 0);
		}

		idle.update(dt);
		super.common();

		if (!dd && room != null && room == Player.instance.room) {
			for (Mob mob : Mob.all) {
				if (!mob.isDead() && mob.room == room) {
					return;
				}
			}

			die();
		}
	}

	@Override
	public float getWeight() {
		return 0.5f;
	}

	@Override
	protected void deathEffects() {
		super.deathEffects();
		deathEffect(killed);
	}

	@Override
	public boolean shouldCollide(Object entity, Contact contact, Fixture fixture) {
		return entity == null || entity instanceof Player || entity instanceof Door || entity instanceof BulletProjectile;
	}


	@Override
	protected State getAi(String state) {
		return new IdleState();
	}

	public class IdleState extends Mob.State<IceElemental> {

	}

	@Override
	public void onCollision(Entity entity) {
		if (entity instanceof Player) {
			((Player) entity).addBuff(new FrozenBuff().setDuration(1f));
		} else if (entity instanceof BulletProjectile && target != null) {
			BulletProjectile bullet = (BulletProjectile) entity;

			float a = this.getAngleTo(target.x + 8, target.y + 8);
			float f = (float) Math.sqrt(bullet.velocity.x * bullet.velocity.x + bullet.velocity.y * bullet.velocity.y);

			bullet.velocity.x = (float) (Math.cos(a) * f);
			bullet.velocity.y = (float) (Math.sin(a) * f);
			bullet.bad = true;

			if (bullet.body != null) {
				bullet.body.setLinearVelocity(bullet.velocity);
			}

			bullet.angle = (float) Math.toDegrees(a);
			bullet.ra = a;
		}
	}
}
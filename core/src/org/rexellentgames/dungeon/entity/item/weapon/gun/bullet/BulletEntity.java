package org.rexellentgames.dungeon.entity.item.weapon.gun.bullet;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.creature.Creature;
import org.rexellentgames.dungeon.entity.creature.buff.Buff;
import org.rexellentgames.dungeon.entity.creature.fx.BloodFx;
import org.rexellentgames.dungeon.entity.creature.fx.HpFx;
import org.rexellentgames.dungeon.entity.creature.mob.Mob;
import org.rexellentgames.dungeon.entity.item.weapon.Weapon;
import org.rexellentgames.dungeon.entity.level.entities.Door;
import org.rexellentgames.dungeon.entity.level.entities.SolidProp;
import org.rexellentgames.dungeon.entity.trap.Turret;
import org.rexellentgames.dungeon.physics.World;
import org.rexellentgames.dungeon.util.Animation;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.geometry.Point;

public class BulletEntity extends Entity {
	public TextureRegion sprite;
	public float a;
	public Point vel;
	private Body body;
	private float ra;
	public int damage;
	public boolean remove;
	public float knockback = 50f;
	public boolean penetrates;
	private boolean auto;
	public String letter;
	private float t;
	private boolean rotate;
	public boolean crit;
	public Creature owner;
	private boolean bad;
	public static Animation animation = Animation.make("fx-badbullet");
	public Class<? extends Buff> toApply;
	public float duration = 1f;

	@Override
	public void init() {
		this.bad = this.letter.equals("bullet bad") || this.letter.equals("bad");
		this.rotate = this.letter.equals("star");
		this.alwaysActive = true;
		this.ra = (float) Math.toRadians(this.a);

		if (this.sprite == null) {
			this.sprite = Graphics.getTexture("bullet (bullet " + this.letter + ")");
		}

		this.w = sprite.getRegionWidth();
		this.h = sprite.getRegionHeight();

		if (sprite.getRegionWidth() == sprite.getRegionHeight()) {
			this.body = World.createCircleCentredBody(this, 0, 0, (float) Math.ceil(((float)sprite.getRegionWidth()) / 2), BodyDef.BodyType.DynamicBody, false);
		} else {
			this.body = World.createSimpleCentredBody(this, 0, 0, sprite.getRegionWidth(), sprite.getRegionHeight(), BodyDef.BodyType.DynamicBody, false);
		}

		this.body.setTransform(this.x, this.y, ra);
		this.body.setBullet(true);
		this.auto = this.letter.equals("C");
	}

	private Mob target;

	@Override
	public void onCollision(Entity entity) {
		if (entity == null || (entity instanceof Door && !((Door) entity).isOpen()) || entity instanceof Weapon || (entity instanceof SolidProp && !(entity instanceof Turret))) {

			this.remove = true;
		} else if (entity instanceof Creature && this.t >= 0.05f) {
			if (this.bad && entity instanceof Mob) {
				return;
			}

			Creature creature = ((Creature) entity);

			HpFx fx = creature.modifyHp(-this.damage, this.owner);

			if (fx != null && crit) {
				fx.crit = true;
			}

			this.remove = (!this.penetrates && (this.owner == null || !this.owner.penetrates));

			float a = (float) (this.getAngleTo(creature.x + creature.w / 2, creature.y + creature.h / 2) - Math.PI * 2);

			creature.vel.x += Math.cos(a) * this.knockback * creature.knockbackMod;
			creature.vel.y += Math.sin(a) * this.knockback * creature.knockbackMod;

			BloodFx.add(entity, 10);
			Camera.instance.shake(2);

			if(toApply != null) {
				try {
					creature.addBuff(toApply.newInstance().setDuration(this.duration));
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void destroy() {
		this.body = World.removeBody(this.body);
	}

	@Override
	public void render() {
		Graphics.render(sprite, this.x, this.y, this.a, sprite.getRegionWidth() / 2, sprite.getRegionHeight() / 2, false, false);
	}

	@Override
	public void renderShadow() {
		Graphics.shadow(this.x - this.w / 2, this.y - this.h / 2, this.w, this.h, 5);
	}

	protected void onDeath() {

	}

	private float last;

	@Override
	public void update(float dt) {
		super.update(dt);
		this.t += dt;

		if (this.bad) {
			this.last += dt;

			if (this.last > 0.08f) {
				this.last = 0;
				Part part = new Part();
				part.vel = new Point();

				part.x = this.x + Random.newFloat(this.sprite.getRegionWidth()) - this.sprite.getRegionWidth()/ 2 - 4;
				part.y = this.y + Random.newFloat(this.sprite.getRegionHeight()) - this.sprite.getRegionHeight() / 2 - 4;
				part.depth = -1;
				part.animation = animation.get("idle");

				Dungeon.area.add(part);
			}
		}

		boolean dd = this.done;
		this.done = this.remove;

		if (this.done && !dd) {
			this.onDeath();

			for (int i = 0; i < 20; i++) {
				Part part = new Part();

				part.x = this.x - this.vel.x * dt;
				part.y = this.y - this.vel.y * dt;

				Dungeon.area.add(part);
			}
		}

		if (this.target != null) {
			float dx = this.target.x + this.target.w / 2 - this.x;
			float dy = this.target.y + this.target.h / 2 - this.y;
			float d = (float) Math.sqrt(dx * dx + dy * dy);

			this.vel.x += dx / d;
			this.vel.y += dy / d;

			if (this.target.isDead()) {
				this.target = null;
			}
		} else if (this.auto) {
			float m = 512f;

			for (Mob mob : Mob.every) {
				float d = mob.getDistanceTo(this.x, this.y);

				if (d < m) {
					this.target = mob;
					m = d;
				}
			}
		}

		this.ra = (float) Math.atan2(this.vel.y, this.vel.x);


		if (this.rotate) {
			this.a += dt * 360 * 2;
		} else {
			this.a = (float) Math.toDegrees(this.ra);
		}

		this.x += this.vel.x * dt;
		this.y += this.vel.y * dt;

		this.body.setTransform(this.x, this.y, this.ra);
		this.body.setLinearVelocity(this.vel);
	}
}
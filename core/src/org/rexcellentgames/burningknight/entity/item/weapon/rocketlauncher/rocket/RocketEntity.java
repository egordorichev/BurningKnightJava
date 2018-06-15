package org.rexcellentgames.burningknight.entity.item.weapon.rocketlauncher.rocket;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.fx.BloodFx;
import org.rexcellentgames.burningknight.entity.creature.fx.HpFx;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.item.Explosion;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.bullet.Part;
import org.rexcellentgames.burningknight.entity.level.entities.Door;
import org.rexcellentgames.burningknight.entity.level.entities.SolidProp;
import org.rexcellentgames.burningknight.entity.plant.Plant;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class RocketEntity extends Entity {
	public TextureRegion sprite;
	public float a;
	public Point vel;
	private Body body;
	private float ra;
	public Creature owner;
	public int damage;
	private boolean remove;
	public float knockback = 50f;
	private boolean auto = true;
	public String letter;
	public boolean crit;

	@Override
	public void init() {
		this.alwaysActive = true;
		this.ra = (float) Math.toRadians(this.a);

		this.w = sprite.getRegionWidth();
		this.h = sprite.getRegionHeight();

		this.body = World.createSimpleCentredBody(this, 0, 0, sprite.getRegionWidth(), sprite.getRegionHeight(), BodyDef.BodyType.DynamicBody, false);
		
		if (this.body != null) {
			this.body.setTransform(this.x, this.y, ra);
			this.body.setBullet(true);
		}
	}

	private Mob target;

	@Override
	public void onCollision(Entity entity) {
		if (entity == null || (entity instanceof Door && !((Door) entity).isOpen()) || entity instanceof SolidProp) {
			this.remove = true;
			this.explode();
		} else if (entity instanceof Creature) {
			Creature creature = ((Creature) entity);

			this.explode();
			// creature.modifyHp(-this.damage);
			this.remove = true;

			float a = (float) (this.getAngleTo(creature.x + creature.w / 2, creature.y + creature.h / 2) - Math.PI * 2);

			creature.vel.x += Math.cos(a) * this.knockback * creature.knockbackMod;
			creature.vel.y += Math.sin(a) * this.knockback * creature.knockbackMod;

			BloodFx.add(entity, 10);
			Camera.shake(2);
		}
	}

	private void explode() {
		this.playSfx("explosion");
		this.done = true;
		Dungeon.area.add(new Explosion(this.x + 8, this.y + 8));

		for (int i = 0; i < Dungeon.area.getEntities().size(); i++) {
			Entity e = Dungeon.area.getEntities().get(i);

			if (e instanceof Creature) {
				Creature c = (Creature) e;

				if (c.getDistanceTo(this.x + 8, this.y + 8) < 24f) {
					if (!c.explosionBlock) {

						HpFx fx = c.modifyHp(-this.damage, this.owner, true);

						if (fx != null && crit) {
							fx.crit = true;
						}
					}

					float a = (float) Math.atan2(c.y + c.h / 2 - this.y - 8, c.x + c.w / 2 - this.x - 8);

					c.vel.x += Math.cos(a) * 5000f * c.knockbackMod;
					c.vel.y += Math.sin(a) * 5000f * c.knockbackMod;
				}
			} else if (e instanceof Plant) {
				Plant c = (Plant) e;

				float dx = c.x + c.w / 2 - this.x;
				float dy = c.y + c.h / 2 - this.y;

				if (Math.sqrt(dx * dx + dy * dy) < 24f) {
					c.startBurning();
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
		Graphics.shadow(this.x - this.w / 2, this.y - this.h / 2 - 5, this.w, this.h);
	}

	private float last;

	@Override
	public void update(float dt) {
		super.update(dt);

		this.last += dt;

		if (this.last >= 0.12f) {
			this.last = 0;

			Part part = new Part();

			part.x = this.x - 2.5f;
			part.y = this.y - 2.5f;
			part.depth = -1;
			part.speed = 3f;
			part.vel = new Point();

			Dungeon.area.add(part);
		}

		this.done = this.remove;

		if (this.done) {
			for (int i = 0; i < 20; i++) {
				Part part = new Part();

				part.x = this.x - this.vel.x;
				part.y = this.y - this.vel.y;

				Dungeon.area.add(part);
			}
		}

		if (this.target != null) {
			this.vel.mul(0.95f);

			double a = Math.atan2(this.vel.y, this.vel.x);

			this.vel.x += Math.cos(a) / 15;
			this.vel.y += Math.sin(a) / 15;

			float dx = this.target.x + this.target.w / 2 - this.x;
			float dy = this.target.y + this.target.h / 2 - this.y;
			float d = (float) Math.sqrt(dx * dx + dy * dy);

			this.vel.x += dx / d / 15;
			this.vel.y += dy / d / 15;

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
		this.a = (float) Math.toDegrees(this.ra);

		this.x += this.vel.x;
		this.y += this.vel.y;

		this.body.setTransform(this.x, this.y, this.ra);
		this.body.setLinearVelocity(this.vel);
	}
}
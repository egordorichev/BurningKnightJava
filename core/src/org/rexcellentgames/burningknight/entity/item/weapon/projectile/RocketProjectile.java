package org.rexcellentgames.burningknight.entity.item.weapon.projectile;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.fx.HpFx;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.item.Explosion;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.bullet.Part;
import org.rexcellentgames.burningknight.entity.level.entities.Door;
import org.rexcellentgames.burningknight.entity.level.entities.SolidProp;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class RocketProjectile extends Projectile {
	public TextureRegion sprite;
	public float a;
	public Point vel;
	private float ra;
	public String letter;

	{
		alwaysActive = true;
		depth = 1;
	}

	@Override
	public void init() {
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

	private void explode() {
		this.playSfx("explosion");
		this.done = true;
		Dungeon.area.add(new Explosion(this.x, this.y));

		for (int i = 0; i < Dungeon.area.getEntities().size(); i++) {
			Entity e = Dungeon.area.getEntities().get(i);

			if (e instanceof Creature) {
				Creature c = (Creature) e;

				if (c.getDistanceTo(this.x, this.y) < 24f) {
					if (!c.explosionBlock) {

						HpFx fx = c.modifyHp(-this.damage, this.owner, true);

						if (fx != null && crit) {
							fx.crit = true;
						}
					}

					float a = (float) Math.atan2(c.y + c.h / 2 - this.y - 8, c.x + c.w / 2 - this.x - 8);

					float knockbackMod = c.getStat("knockback");
					c.vel.x += Math.cos(a) * 5000f * knockbackMod;
					c.vel.y += Math.sin(a) * 5000f * knockbackMod;
				}
			}
		}
	}

	@Override
	protected boolean hit(Entity entity) {
		if (entity == null || entity instanceof Creature || entity instanceof SolidProp || entity instanceof Door) {
			this.explode();
			return true;
		}

		return false;
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
	public void logic(float dt) {
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
		} else {
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
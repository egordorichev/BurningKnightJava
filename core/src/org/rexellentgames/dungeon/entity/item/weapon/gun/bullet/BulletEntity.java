package org.rexellentgames.dungeon.entity.item.weapon.gun.bullet;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.creature.Creature;
import org.rexellentgames.dungeon.entity.creature.fx.BloodFx;
import org.rexellentgames.dungeon.entity.creature.mob.Mob;
import org.rexellentgames.dungeon.entity.level.entities.Door;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.physics.World;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.geometry.Point;

public class BulletEntity extends Entity {
	public TextureRegion sprite;
	public float a;
	public Point vel;
	private Body body;
	private float ra;
	public int damage;
	private boolean remove;
	public float knockback = 50f;
	private boolean auto;
	public String letter;

	@Override
	public void init() {
		this.alwaysActive = true;
		this.ra = (float) Math.toRadians(this.a);

		this.body = World.createSimpleCentredBody(this, 0, 0, sprite.getRegionWidth(), sprite.getRegionHeight(), BodyDef.BodyType.DynamicBody, false);
		this.body.setTransform(this.x, this.y, ra);
		this.body.setBullet(true);
		this.auto = this.letter.equals("C");
	}

	private Mob target;

	@Override
	public void onCollision(Entity entity) {
		if (entity == null || (entity instanceof Door && !((Door) entity).isOpen())) {
			this.remove = true;
		} else if (entity instanceof Creature) {
			if (this.letter.equals("bad") && entity instanceof Mob) {
				return;
			}

			Creature creature = ((Creature) entity);

			creature.modifyHp(-this.damage);
			this.remove = true;

			float a = (float) (this.getAngleTo(creature.x + creature.w / 2, creature.y + creature.h / 2) - Math.PI * 2);

			creature.vel.x += Math.cos(a) * this.knockback;
			creature.vel.y += Math.sin(a) * this.knockback;

			BloodFx.add(entity, 10);
			Camera.instance.shake(2);
		}
	}

	@Override
	public void destroy() {
		this.body = World.removeBody(this.body);
	}

	@Override
	public void render() {
		Graphics.startShadows();
		Graphics.render(sprite, this.x, this.y - 8, this.a, sprite.getRegionWidth() / 2, sprite.getRegionHeight() / 2, false, false);
		Graphics.endShadows();
		Graphics.render(sprite, this.x, this.y, this.a, sprite.getRegionWidth() / 2, sprite.getRegionHeight() / 2, false, false);
	}

	@Override
	public void update(float dt) {
		super.update(dt);

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
		this.a = (float) Math.toDegrees(this.ra);

		this.x += this.vel.x;
		this.y += this.vel.y;

		this.body.setTransform(this.x, this.y, this.ra);
		this.body.setLinearVelocity(this.vel);
	}
}
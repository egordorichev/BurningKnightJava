package org.rexellentgames.dungeon.entity.item.weapon.axe;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.creature.Creature;
import org.rexellentgames.dungeon.entity.creature.fx.BloodFx;
import org.rexellentgames.dungeon.entity.creature.fx.HpFx;
import org.rexellentgames.dungeon.entity.item.ItemHolder;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.physics.World;
import org.rexellentgames.dungeon.util.MathUtils;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.geometry.Point;

public class AxeFx extends Entity {
	private Point vel;
	private Body body;
	private float t;
	private float a;

	public boolean crit;
	public float damage;
	public TextureRegion region;
	public Creature owner;
	public boolean penetrates;
	public Class<? extends Axe> type;
	public int speed;
	public Axe axe;

	@Override
	public void init() {
		this.alwaysActive = true;
		float dx = Input.instance.worldMouse.x - this.x - 8;
		float dy = Input.instance.worldMouse.y - this.y - 8;
		float a = (float) Math.atan2(dy, dx);

		this.depth = 9;

		this.vel = new Point(
			(float) Math.cos(a) * this.speed,
			(float) Math.sin(a) * this.speed
		);

		this.body = World.createSimpleCentredBody(this, 0, 0, 16, 16, BodyDef.BodyType.DynamicBody, true);
		this.body.setTransform(this.x, this.y, 0);
		this.body.setBullet(true);

		this.a = Random.newFloat((float) (Math.PI * 2));
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		this.vel.mul(0.95f);

		this.t += dt;
		this.x += this.vel.x * dt;
		this.y += this.vel.y * dt;

		this.a += dt * 1000;

		this.body.setTransform(this.x, this.y, (float) Math.toRadians(this.a));

		float dx = this.owner.x + this.owner.w / 2 - this.x - 8;
		float dy = this.owner.y + this.owner.h / 2 - this.y - 8;
		float d = (float) Math.sqrt(dx * dx + dy * dy);

		if (d > 8) {
			float f = 10;

			if (d < 64 && this.t > 1) {
				f = MathUtils.clamp(1f, 10f, 64 - d);
			} else if (d > 150f) {
				f = 10f;
			}

			this.vel.x += dx / d * f;
			this.vel.y += dy / d * f;
		}

		this.body.setLinearVelocity(this.vel);
	}

	@Override
	public void render() {
		Graphics.render(this.region, this.x, this.y, this.a, 8, 8, false, false);
	}

	@Override
	public void onCollision(Entity entity) {
		super.onCollision(entity);

		if (entity == this.owner) {
			if (this.t >= 1f) {
				this.done = true;
			}
		} else if (entity instanceof Creature) {
			Creature creature = ((Creature) entity);

			creature.vel.x += this.vel.x;
			creature.vel.y += this.vel.y;

			HpFx fx = creature.modifyHp((int) -this.damage, this.owner);

			if (fx != null && crit) {
				fx.crit = true;
			}

			BloodFx.add(entity, 10);

			this.axe.onHit(creature);

			if (!this.penetrates) {
				this.done = true;
			}
		}
	}

	@Override
	public void destroy() {
		super.destroy();

		this.body = World.removeBody(this.body);
		ItemHolder holder = new ItemHolder();

		try {
			holder.setItem(this.type.newInstance());
			holder.x = this.x;
			holder.y = this.y;
			holder.auto = true;

			Dungeon.area.add(holder);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
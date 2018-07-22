package org.rexcellentgames.burningknight.entity.item.weapon.projectile;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.ItemHolder;
import org.rexcellentgames.burningknight.entity.item.weapon.axe.Axe;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.MathUtils;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class AxeProjectile extends Projectile {
	private float t;
	private float a;

	public TextureRegion region;
	public boolean penetrates;
	public Class<? extends Axe> type;
	public int speed;
	public Axe axe;

	{
		alwaysActive = true;
		depth = 9;
	}

	@Override
	public void init() {
		float dx = Input.instance.worldMouse.x - this.x - 8;
		float dy = Input.instance.worldMouse.y - this.y - 8;
		float a = (float) Math.atan2(dy, dx);

		this.vel = new Point(
			(float) Math.cos(a) * this.speed,
			(float) Math.sin(a) * this.speed
		);

		this.body = World.createSimpleCentredBody(this, 0, 0, 16, 16, BodyDef.BodyType.DynamicBody, true);
		this.body.setTransform(this.x, this.y, 0);
		this.body.setBullet(true);

		this.a = Random.newFloat((float) (Math.PI * 2));
	}

	private boolean did;

	@Override
	protected boolean hit(Entity entity) {
		if (!this.penetrates && this.did) {
			return false;
		}

		if (this.bad) {
			if (entity instanceof Player) {
				this.doHit(entity);
				this.did = true;
				return true;
			}
		} else if (entity instanceof Mob) {
			this.doHit(entity);
			this.did = true;
			return true;
		} else if (entity instanceof Player && this.t > 0.2f) {
			this.done = true;
		}

		return false;
	}

	@Override
	public void logic(float dt) {
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
	public void destroy() {
		super.destroy();

		ItemHolder holder = new ItemHolder();

		try {
			holder.setItem(this.type.newInstance());
			holder.x = this.x;
			holder.y = this.y;
			holder.auto = true;

			Dungeon.area.add(holder);
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
package org.rexcellentgames.burningknight.entity.item.weapon.projectile;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.weapon.bow.arrows.Arrow;
import org.rexcellentgames.burningknight.entity.level.entities.Door;
import org.rexcellentgames.burningknight.entity.level.entities.SolidProp;
import org.rexcellentgames.burningknight.entity.level.entities.fx.PoofFx;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class ArrowProjectile extends Projectile {
	public float a;
	public Class<? extends Arrow> type;
	public TextureRegion sprite;

	{
		alwaysActive = true;
		alwaysRender = true;
	}

	@Override
	public void destroy() {
		super.destroy();
		this.body = World.removeBody(this.body);
	}

	@Override
	public void init() {
		super.init();

		int w = sprite.getRegionWidth();
		int h = sprite.getRegionHeight();

		this.body = World.createSimpleCentredBody(this, 0, 0, w, h, BodyDef.BodyType.DynamicBody, true);
		this.body.setBullet(true);
		this.body.setTransform(this.x, this.y, this.a);

		float s = 5f * 60f;
		this.vel = new Point((float) Math.cos(this.a) * s, (float) Math.sin(this.a) * s);
	}

	public float charge;

	@Override
	protected boolean breaksFrom(Entity entity) {
		if (!this.done && (entity == null || entity instanceof SolidProp || entity instanceof Door)) {
			this.done = true;
			this.vel.mul(0);
			this.body.setLinearVelocity(this.vel.x, this.vel.y);

			for (int i = 0; i < 3; i++) {
				PoofFx fx = new PoofFx();

				fx.x = this.x;
				fx.y = this.y;

				Dungeon.area.add(fx);
			}
		}

		return false;
	}

	@Override
	protected boolean hit(Entity entity) {
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
	public void logic(float dt) {
		this.x += this.vel.x * dt;
		this.y += this.vel.y * dt;

		if (this.body != null) {
			this.body.setTransform(this.x, this.y, this.a);
			this.body.setLinearVelocity(this.vel.x, this.vel.y);
		}
	}

	@Override
	public void render() {
		Graphics.render(sprite, this.x, this.y, (float) Math.toDegrees(this.a), sprite.getRegionWidth() / 2, sprite.getRegionHeight() / 2, false, false);
	}

	@Override
	public void renderShadow() {
		Graphics.shadow(this.x - this.w / 2, this.y - this.h / 2 - 5, this.w, this.h, 2f);
	}
}
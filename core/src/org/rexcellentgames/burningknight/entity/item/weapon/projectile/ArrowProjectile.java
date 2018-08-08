package org.rexcellentgames.burningknight.entity.item.weapon.projectile;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.weapon.bow.arrows.Arrow;
import org.rexcellentgames.burningknight.entity.level.entities.Door;
import org.rexcellentgames.burningknight.entity.level.entities.SolidProp;
import org.rexcellentgames.burningknight.entity.level.entities.fx.PoofFx;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.geometry.Point;

import java.util.ArrayList;

public class ArrowProjectile extends Projectile {
	public float a;
	public Class<? extends Arrow> type;
	public TextureRegion sprite;
	private Creature stuck;
	private ArrayList<Point> positions = new ArrayList<>();

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

		float s = Math.max(10f, 14f * 60f * charge);
		this.vel = new Point((float) Math.cos(this.a) * s, (float) Math.sin(this.a) * s);
	}

	public float charge;

	@Override
	protected boolean breaksFrom(Entity entity) {
		if (!this.did && (entity == null || entity instanceof SolidProp || entity instanceof Door)) {
			this.did = true;
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
		if (this.did) {
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
	public void logic(float dt) {
		if (this.did) {
			if (this.positions.size() > 0) {
				this.positions.remove(0);
			} else {
				this.done = true;
			}

			return;
		}

		if (this.stuck != null) {
			this.x = (float) (this.stuck.x + this.stuck.w / 2 - Math.cos(this.a) * (this.stuck.w - this.w / 2) / 2);
			this.y = (float) (this.stuck.y + this.stuck.h / 2 - Math.sin(this.a) * (this.stuck.h - this.h / 2) / 2);

			if (this.stuck.isDead()) {
				this.broke = true;
			}

			if (this.positions.size() > 0) {
				this.positions.remove(0);
			}
		} else {
			this.positions.add(new Point(this.x, this.y));

			if (this.positions.size() > 10) {
				this.positions.remove(0);
			}

			this.x += this.vel.x * dt;
			this.y += this.vel.y * dt;

			if (this.body != null) {
				this.body.setTransform(this.x, this.y, this.a);
				this.body.setLinearVelocity(this.vel.x, this.vel.y);
			}
		}
	}

	@Override
	public void render() {
		if (this.positions.size() > 0) {
			Graphics.batch.end();
			Graphics.shape.begin(ShapeRenderer.ShapeType.Filled);
			Graphics.shape.setColor(1, 1, 1, 1);

			for (int i = 0; i < this.positions.size(); i++) {
				Point next = (i == positions.size() - 1 ? new Point(this.x + (float) Math.cos(this.a) * this.w / 2, this.y + (float) Math.sin(this.a) * this.h / 2)
					: this.positions.get(i + 1));
				Point pos = this.positions.get(i);

				Graphics.shape.line(next.x, next.y, pos.x, pos.y);
			}

			Graphics.shape.end();
			Graphics.batch.begin();
		}

		if (!this.did) {
			Graphics.render(sprite, this.x, this.y, (float) Math.toDegrees(this.a), sprite.getRegionWidth() / 2, sprite.getRegionHeight() / 2, false, false);
		}
	}

	private boolean did;

	@Override
	public void renderShadow() {
		if (this.stuck == null && !this.did) {
			Graphics.shadow(this.x - this.w / 2, this.y - this.h / 2 - 5, this.w, this.h, 2f);
		}
	}
}
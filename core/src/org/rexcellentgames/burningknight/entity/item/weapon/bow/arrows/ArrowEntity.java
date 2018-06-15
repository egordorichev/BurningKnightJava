package org.rexcellentgames.burningknight.entity.item.weapon.bow.arrows;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.fx.HpFx;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.bullet.Part;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.fx.BloodFx;
import org.rexcellentgames.burningknight.entity.creature.fx.HpFx;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.bullet.Part;
import org.rexcellentgames.burningknight.entity.level.entities.SolidProp;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.geometry.Point;

import java.util.ArrayList;

public class ArrowEntity extends Entity {
	public float a;
	private Body body;
	private Vector2 vel;
	public Creature owner;
	public int damage;
	public Class<? extends Arrow> type;
	public TextureRegion sprite;
	private Creature stuck;
	public boolean crit;
	public boolean bad;

	private ArrayList<Point> positions = new ArrayList<>();

	@Override
	public void destroy() {
		super.destroy();
		this.body = World.removeBody(this.body);
	}

	@Override
	public void init() {
		super.init();

		this.alwaysRender = true;
		this.alwaysActive = true;

		int w = sprite.getRegionWidth();
		int h = sprite.getRegionHeight();

		this.body = World.createSimpleCentredBody(this, 0, 0, w, h, BodyDef.BodyType.DynamicBody, true);

		if (this.body != null) {
			this.body.setBullet(true);
			this.body.setTransform(this.x, this.y, this.a);
		}

		// todo: charge == high speed
		float s = 14f * 60f;
		this.vel = new Vector2((float) Math.cos(this.a) * s, (float) Math.sin(this.a) * s);
	}

	@Override
	public void update(float dt) {
		if (this.did) {
			if (this.positions.size() > 0) {
				this.positions.remove(0);
			} else {
				this.done = true;
			}

			return;
		}

		super.update(dt);

		if (this.stuck != null) {
			this.x = (float) (this.stuck.x + this.stuck.w / 2 - Math.cos(this.a) * (this.stuck.w - this.w / 2) / 2);
			this.y = (float) (this.stuck.y + this.stuck.h / 2 - Math.sin(this.a) * (this.stuck.h - this.h / 2) / 2);

			// todo: offset from center

			if (this.stuck.isDead()) {
				this.done = true;
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
		}

		if (this.body != null) {
			this.body.setLinearVelocity(this.vel);
			this.body.setTransform(this.x, this.y, this.a);
		}
	}

	private boolean did;

	@Override
	public void onCollision(Entity entity) {
		if (this.did || this.stuck != null || entity == this.owner || this.done) {
			return;
		}

		if (entity instanceof Creature) {
			if (this.bad && entity instanceof Mob) {
				return;
			}

			Creature creature = ((Creature) entity);

			creature.vel.x += this.vel.x * 10f;
			creature.vel.y += this.vel.y * 10f;

			HpFx fx = creature.modifyHp(-this.damage, this.owner);

			if (fx != null && crit) {
				fx.crit = true;
			}
			this.stuck = creature;
			this.body = World.removeBody(this.body);
			this.depth = creature.depth;

			BloodFx.add(entity, 10);
		} else if (entity == null  || entity instanceof SolidProp) {
			this.did = true;
			this.vel.x *= 0;
			this.vel.y *= 0;
			this.body = World.removeBody(this.body);
			for (int i = 0; i < 20; i++) {
				Part part = new Part();

				float dt = Gdx.graphics.getDeltaTime();

				part.x = this.x - this.vel.x * dt;
				part.y = this.y - this.vel.y * dt;

				Dungeon.area.add(part);
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

		if (!did) {
			Graphics.render(sprite, this.x, this.y, (float) Math.toDegrees(this.a), sprite.getRegionWidth() / 2, sprite.getRegionHeight() / 2, false, false);
		}
	}

	@Override
	public void renderShadow() {
		if (this.stuck == null) {
			Graphics.shadow(this.x - this.w / 2, this.y - this.h / 2 - 5, this.w, this.h, 2f);
		}
	}
}
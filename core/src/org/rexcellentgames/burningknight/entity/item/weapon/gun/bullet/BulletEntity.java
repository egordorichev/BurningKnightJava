package org.rexcellentgames.burningknight.entity.item.weapon.gun.bullet;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.buff.Buff;
import org.rexcellentgames.burningknight.entity.creature.fx.BloodFx;
import org.rexcellentgames.burningknight.entity.creature.fx.HpFx;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.level.entities.Door;
import org.rexcellentgames.burningknight.entity.level.entities.SolidProp;
import org.rexcellentgames.burningknight.entity.trap.Turret;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

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
	public float time;
	public boolean circ;
	private boolean rotate;
	public boolean crit;
	public Creature owner;
	public boolean bad;
	public static Animation animation = Animation.make("fx-badbullet");
	public Class<? extends Buff> toApply;
	public float duration = 1f;
	public boolean parts;
	public int dir;
	public Point ivel;
	public float angle;
	public float dist;

	@Override
	public void init() {
		angle = (float) Math.atan2(this.vel.y, this.vel.x);
		dist = (float) Math.sqrt(vel.x * vel.x + vel.y * vel.y);

		this.ivel = new Point(this.vel.x, this.vel.y);
		this.dir = Random.chance(50) ? -1 : 1;
		this.parts = this.letter.equals("bullet bad") || this.letter.equals("bad");
		this.bad = this.letter.equals("bullet bad") || this.letter.equals("bad") || letter.equals("bone");
		this.rotate = this.letter.equals("star") || letter.equals("bone");
		this.alwaysActive = true;
		this.ra = (float) Math.toRadians(this.a);

		depth = letter.equals("bone") ? 6 : 0;

		if (this.sprite == null) {
			this.sprite = Graphics.getTexture("bullet (bullet " + this.letter + ")");
		}

		this.w = sprite.getRegionWidth();
		this.h = sprite.getRegionHeight();

		if (sprite.getRegionWidth() == sprite.getRegionHeight() || circ) {
			this.body = World.createCircleCentredBody(this, 0, 0, (float) Math.ceil(((float)sprite.getRegionWidth()) / 2), BodyDef.BodyType.DynamicBody, this.letter.equals("bone"));
		} else {
			this.body = World.createSimpleCentredBody(this, 0, 0, sprite.getRegionWidth(), sprite.getRegionHeight(), BodyDef.BodyType.DynamicBody, false);
		}

		if (this.body != null) {
			this.body.setTransform(this.x, this.y, ra);
			this.body.setBullet(true);
		}
		
		this.auto = this.letter.equals("C");
	}

	private Mob target;
	public boolean canBeRemoved;

	public void countRemove() {

	}

	@Override
	public void onCollision(Entity entity) {
		if (remove) {
			return;
		}

		if (entity == null || (entity instanceof Door && !((Door) entity).isOpen()) || (entity instanceof SolidProp && !(entity instanceof Turret))) {
			this.remove = canBeRemoved;
			if (this.remove) {
				countRemove();
			}
		} else if (entity instanceof Creature && this.time >= 0.05f) {
			if (this.bad && entity instanceof Mob) {
				return;
			}

			Creature creature = ((Creature) entity);

			HpFx fx = creature.modifyHp(-this.damage, this.owner);

			if (fx != null && crit) {
				fx.crit = true;
			}

			this.remove = (!this.penetrates && (this.owner == null || !this.owner.penetrates));
			if (this.remove) {
				countRemove();
			}

			float a = (float) (this.getAngleTo(creature.x + creature.w / 2, creature.y + creature.h / 2) - Math.PI * 2);

			creature.vel.x += Math.cos(a) * this.knockback * creature.knockbackMod;
			creature.vel.y += Math.sin(a) * this.knockback * creature.knockbackMod;

			BloodFx.add(entity, 10);
			Camera.shake(2);

			if(toApply != null) {
				try {
					creature.addBuff(toApply.newInstance().setDuration(this.duration));
				} catch (InstantiationException | IllegalAccessException e) {
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
		this.time += dt;

		if (this.parts) {
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
			this.a += dt * 360 * 2 * dir;
		} else {
			this.a = (float) Math.toDegrees(this.ra);
		}

		this.control();

		this.x += this.vel.x * dt;
		this.y += this.vel.y * dt;

		this.body.setTransform(this.x, this.y, this.ra);
		this.body.setLinearVelocity(this.vel);
	}

	public void control() {

	}
}
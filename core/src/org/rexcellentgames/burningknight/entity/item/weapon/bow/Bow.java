package org.rexcellentgames.burningknight.entity.item.weapon.bow;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.weapon.WeaponBase;
import org.rexcellentgames.burningknight.entity.item.weapon.bow.arrows.Arrow;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.Gun;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.ArrowProjectile;
import org.rexcellentgames.burningknight.entity.level.entities.Door;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class Bow extends WeaponBase {
	private float sx = 1f;
	private float sy = 1f;

	{
		auto = true;
		identified = true;
	}

	private float charge;
	private boolean beingUsed;

	@Override
	public void use() {
		super.use();

		beingUsed = true;
		// charge = 0;
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (!beingUsed) {
			sx += (1 - sx) * dt * 10;
			sy += (1 - sy) * dt * 10;

			return;
		}

		sx = Math.min(1.4f, sx + dt / 2);
		sy = Math.max(0.6f, sy - dt / 2);
		charge = Math.min(1, charge + dt * 1.5f);

		if (Input.instance.wasReleased("mouse0")) {
			beingUsed = false;
			sendArrow();
		}
	}

	private void sendArrow() {
		Arrow ar = (Arrow) this.owner.getAmmo("arrow");
		Point aim = this.owner.getAim();

		float a = (float) (this.owner.getAngleTo(aim.x, aim.y) - Math.PI);
		float s = 60f;

		float knockbackMod = owner.getStat("knockback");

		this.owner.vel.x += Math.cos(a) * s * knockbackMod;
		this.owner.vel.y += Math.sin(a) * s * knockbackMod;

		ArrowProjectile arrow = new ArrowProjectile();

		arrow.owner = this.owner;

		float dx = aim.x - this.owner.x - this.owner.w / 2;
		float dy = aim.y - this.owner.y - this.owner.h / 2;

		arrow.type = ar.getClass();
		arrow.sprite = ar.getSprite();
		arrow.a = (float) (Math.atan2(dy, dx) + Math.toRadians(Random.newFloat(-20, 20) * (1 - charge)));
		arrow.x = (float) (this.owner.x + this.owner.w / 2 + Math.cos(arrow.a) * 16);
		arrow.y = (float) (this.owner.y + this.owner.h / 2 + Math.sin(arrow.a) * 16);
		arrow.damage = rollDamage() + ar.damage;
		arrow.crit = lastCrit;
		arrow.bad = this.owner instanceof Mob;
		arrow.charge = charge;

		Dungeon.area.add(arrow);

		charge = 0;
	}

	private float lastAngle;

	@Override
	public void render(float x, float y, float w, float h, boolean flipped) {
		Point aim = this.owner.getAim();

		float an = this.owner.getAngleTo(aim.x, aim.y);
		an = Gun.angleLerp(this.lastAngle, an, 0.15f);
		this.lastAngle = an;
		float a = (float) Math.toDegrees(this.lastAngle);

		TextureRegion s = this.getSprite();

		float xx = x + w / 2;
		float yy = y + h / 2;

		this.renderAt(xx, yy, a, -4, s.getRegionHeight() / 2, false, false, sx, sy);

		if (this.owner instanceof Player && ((Player) this.owner).hasRedLine) {
			float d = Display.GAME_WIDTH * 10;
			closestFraction = 1f;
			float x2 = xx + (float) Math.cos(an) * d;
			float y2 = yy + (float) Math.sin(an) * d;

			if (xx != x2 || yy != y2) {
				World.world.rayCast(callback, xx, yy, x2, y2);
			}

			if (last != null) {
				Graphics.batch.end();
				Graphics.shape.setProjectionMatrix(Camera.game.combined);
				Graphics.shape.begin(ShapeRenderer.ShapeType.Filled);
				Graphics.shape.setColor(1, 0, 0, 0.7f);

				Graphics.shape.line(xx, yy, last.x, last.y);
				Graphics.shape.rect(last.x - 2, last.y - 2, 4, 4);

				Graphics.shape.end();
				Graphics.batch.begin();
			}
		}
	}


	private Vector2 last;
	private float closestFraction = 1.0f;

	private RayCastCallback callback = new RayCastCallback() {
		@Override
		public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
			if (fixture.isSensor()) {
				return 1;
			}

			Entity entity = (Entity) fixture.getBody().getUserData();

			if ((entity == null && !fixture.getBody().isBullet()) || (entity instanceof Door && !((Door) entity).isOpen()) || entity instanceof Player) {
				if (fraction < closestFraction) {
					closestFraction = fraction;
					last = point;
				}

				return fraction;
			}

			return 1;
		}
	};
}
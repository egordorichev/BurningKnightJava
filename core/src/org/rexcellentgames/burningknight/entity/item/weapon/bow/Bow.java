package org.rexcellentgames.burningknight.entity.item.weapon.bow;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.ItemHolder;
import org.rexcellentgames.burningknight.entity.item.weapon.WeaponBase;
import org.rexcellentgames.burningknight.entity.item.weapon.bow.arrows.Arrow;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.Gun;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.ArrowProjectile;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.entities.Door;
import org.rexcellentgames.burningknight.game.Achievements;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.geometry.Point;

import java.io.IOException;

public class Bow extends WeaponBase {
	private float sx = 1f;
	private float sy = 1f;

	{
		delay = 0.3f;
		setStats();
	}

	@Override
	public void onPickup() {
		super.onPickup();
		Achievements.unlock("UNLOCK_BOW");
	}

	private float charge;
	private boolean beingUsed;
	protected float accuracity = 10;

	protected void setStats() {
		String letter = this.level <= 2 ? "a" : (this.level <= 4 ? "b" : "c");

		name = Locale.get("bow_" + letter);
		sprite = "item-bow_" + letter;
		description = Locale.get("bow_desc");
		useTime = 0.45f - this.level * 0.01f;
		damage = 1 + this.level;
		useSpeedStr = this.getUseSpeedAsString();
		region = Graphics.getTexture(sprite);
	}

	@Override
	public void upgrade() {
		super.upgrade();
		setStats();
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);
		setStats();
	}

	public Bow() {
		setStats();
	}

	@Override
	public int getMaxLevel() {
		return 6;
	}

	@Override
	public void use() {
		super.use();

		beingUsed = true;
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (!beingUsed) {
			sx += (1 - sx) * dt * 20;
			sy += (1 - sy) * dt * 20;

			return;
		}

		sx = Math.min(1.4f, sx + dt * 10);
		sy = Math.max(0.6f, sy - dt * 10);
		charge = Math.min(1, charge + dt * 10f);

		if (charge == 1) {
			beingUsed = false;
			sendArrow();
		}
	}

	private void sendArrow() {
		this.owner.playSfx("bow");

		Arrow ar = (Arrow) this.owner.getAmmo("arrow");
		Point aim = this.owner.getAim();

		float a = (float) (this.owner.getAngleTo(aim.x, aim.y) - Math.PI);

		float knockbackMod = owner.getStat("knockback");

		this.owner.knockback.x += Math.cos(a) * 60 * knockbackMod;
		this.owner.knockback.y += Math.sin(a) * 60 * knockbackMod;

		ArrowProjectile arrow = new ArrowProjectile();

		arrow.owner = this.owner;

		float dx = aim.x - this.owner.x - this.owner.w / 2;
		float dy = aim.y - this.owner.y - this.owner.h / 2;

		double an = (Math.atan2(dy, dx));

		arrow.sprite = ar.getSprite();
		arrow.a = (float) Math.toDegrees(an);
		arrow.second = false;

		float s = 3 * 60f;
		double ann = an + Math.toRadians(Math.toRadians(Random.newFloat(-accuracity, accuracity)));

		arrow.velocity = new Point(
			(float) Math.cos(ann) * s, (float) Math.sin(ann) * s
		);

		arrow.x = (float) (this.owner.x + this.owner.w / 2 + Math.cos(an) * 8);
		arrow.y = (float) (this.owner.y + this.owner.h / 2 + Math.sin(an) * 8);
		arrow.damage = rollDamage() + ar.damage;
		arrow.crit = lastCrit;
		arrow.bad = this.owner instanceof Mob;

		Dungeon.area.add(arrow);

		charge = 0;
	}

	private float lastAngle;

	@Override
	public void render(float x, float y, float w, float h, boolean flipped) {
		Point aim = this.owner.getAim();

		float an = this.owner.getAngleTo(aim.x, aim.y);
		an = Gun.angleLerp(this.lastAngle, an, 0.15f, this.owner != null && this.owner.freezed);
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
			Object data = fixture.getBody().getUserData();

			if (!fixture.isSensor() && !(data instanceof Level || data instanceof ItemHolder || (data instanceof Door && ((Door) data).isOpen()))) {
				if (fraction < closestFraction) {
					closestFraction = fraction;
					last = point;
				}
			}

			return closestFraction;
		}
	};
}
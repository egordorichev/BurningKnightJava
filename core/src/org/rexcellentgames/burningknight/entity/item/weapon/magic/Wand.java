package org.rexcellentgames.burningknight.entity.item.weapon.magic;

import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.fx.ManaFx;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.weapon.WeaponBase;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.Gun;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.BulletProjectile;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.fx.RectFx;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.Tween;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class Wand extends WeaponBase {
	{
		useTime = 0.15f;
	}

	protected float speed = 120f;
	protected int mana = 1;
	protected Color color = Color.WHITE;
	protected Player owner;
	protected TextureRegion projectile;

	public void setOwner(Creature owner) {
		super.setOwner(owner);

		if (owner instanceof Player) {
			this.owner = (Player) owner;
		}
	}

	protected float lastAngle;

	@Override
	public void render(float x, float y, float w, float h, boolean flipped, boolean back) {
		if (this.owner != null) {
			Point aim = this.owner.getAim();

			float an = (float) (this.owner.getAngleTo(aim.x, aim.y) - Math.PI / 2);
			an = Gun.angleLerp(this.lastAngle, an, 0.15f, this.owner != null && this.owner.freezed);
			this.lastAngle = an;
		}

		TextureRegion s = this.getSprite();

		this.renderAt(x + w / 2, y + h / 4, back ? (flipped ? -45 : 45) : (float) Math.toDegrees(this.lastAngle), s.getRegionWidth() / 2, 0, false, false, sx, sy);
	}

	protected float sy = 1;
	protected float sx = 1;

	public int getManaUsage() {
		return Math.max(1, this.mana);
	}

	@Override
	public void use() {
		if (!canBeUsed()) {
			return;
		}

		int mn = getManaUsage();

		if (this.owner.getMana() < mn) {
			return;
		}

		this.owner.playSfx("fireball_cast");

		super.use();
		this.owner.modifyMana(-mn);
		this.sendProjectiles();

		Point aim = this.owner.getAim();
		float a = (float) (this.owner.getAngleTo(aim.x, aim.y) - Math.PI * 2);
		Camera.push(a, 16f);

		sx = 2f;
		sy = 0.5f;

		Tween.to(new Tween.Task(1f, 0.2f) {
			@Override
			public float getValue() {
				return sy;
			}

			@Override
			public void setValue(float value) {
				sy = value;
			}
		});

		Tween.to(new Tween.Task(1f, 0.2f) {
			@Override
			public float getValue() {
				return sx;
			}

			@Override
			public void setValue(float value) {
				sx = value;
			}
		});
	}

	protected void sendProjectiles() {
		float a = (float) Math.toDegrees(this.lastAngle);
		float h = this.region.getRegionHeight();
		double an = this.lastAngle + Math.PI / 2;

		this.owner.knockback.x -= Math.cos(an) * 40f;
		this.owner.knockback.y -= Math.sin(an) * 40f;

		this.spawnProjectile(this.owner.x + this.owner.w / 2 + h * (float) Math.cos(an),
			this.owner.y + this.owner.h / 4 + h * (float) Math.sin(an), a + 90);
	}

	public Color getColor() {
		return color;
	}

	protected TextureRegion getProjectile() {
		return null;
	}

	public void spawnProjectile(float x, float y, float a) {
		if (projectile == null) {
			projectile = getProjectile();
		}

		final int mana = getManaUsage();

		BulletProjectile missile = new BulletProjectile() {
			private int manaUsed;
			private boolean died;

			{
				ignoreArmor = true;
			}

			@Override
			protected void onDeath() {
				super.onDeath();

				if (died) {
					return;
				}

				died = true;
				int weight = manaUsed;

				while (weight > 0) {
					ManaFx fx = new ManaFx();

					fx.x = x;
					fx.y = y;

					fx.half = weight == 1;
					fx.poof();

					weight -= fx.half ? 1 : 2;
					Dungeon.area.add(fx);
					LevelSave.add(fx);
					fx.body.setLinearVelocity(new Vector2(-this.velocity.x * 0.5f, -this.velocity.y * 0.5f));
				}
			}

			@Override
			public void render() {
				Color color = getColor();

				Graphics.batch.setColor(color.r, color.g, color.b, 0.4f);
				Graphics.render(projectile, this.x, this.y, this.a, projectile.getRegionWidth() / 2, projectile.getRegionHeight() / 2, false, false, 2f, 2f);
				Graphics.batch.setColor(color.r, color.g, color.b, 0.8f);
				Graphics.render(projectile, this.x, this.y, this.a, projectile.getRegionWidth() / 2, projectile.getRegionHeight() / 2, false, false);
				Graphics.batch.setColor(1, 1, 1, 1);
			}

			private PointLight light;

			@Override
			public void init() {
				super.init();
				manaUsed = mana;
				light = World.newLight(32, new Color(1f, 1f, 1f, 1f), 64, x, y);
			}

			@Override
			public void destroy() {
				super.destroy();
				World.removeLight(light);
			}

			@Override
			public void logic(float dt) {
				super.logic(dt);

				light.setPosition(x, y);

				this.last += dt;

				if (this.last > 0.05f) {
					this.last = 0;
					RectFx fx = new RectFx();

					fx.depth = this.depth;
					fx.x = this.x + Random.newFloat(this.w) - this.w / 2;
					fx.y = this.y + Random.newFloat(this.h) - this.h / 2;
					fx.w = 4;
					fx.h = 4;

					Color color = getColor();

					fx.r = color.r;
					fx.g = color.g;
					fx.b = color.b;

					Dungeon.area.add(fx);
				}

				World.checkLocked(this.body).setTransform(this.x, this.y, (float) Math.toRadians(this.a));
			}
		};

		missile.depth = 1;
		missile.damage = this.rollDamage();

		missile.owner = this.owner;
		missile.x = x;
		missile.y = y - 3;
		missile.rectShape = true;
		missile.w = 6;
		missile.h = 6;
		missile.rotates = true;
		missile.noRotation = false;

		double ra = Math.toRadians(a);
		missile.velocity.x = (float) Math.cos(ra) * speed;
		missile.velocity.y = (float) Math.sin(ra) * speed;

		Dungeon.area.add(missile);
	}

	@Override
	public StringBuilder buildInfo() {
		StringBuilder builder = super.buildInfo();

		builder.append("\n[blue]Uses ");
		builder.append( getManaUsage());
		builder.append(" mana[gray]");

		return builder;
	}
}
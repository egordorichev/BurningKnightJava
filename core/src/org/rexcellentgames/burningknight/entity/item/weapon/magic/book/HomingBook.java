package org.rexcellentgames.burningknight.entity.item.weapon.magic.book;

import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.mob.boss.BurningKnight;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.Gun;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.BulletProjectile;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.fx.RectFx;
import org.rexcellentgames.burningknight.game.Achievements;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Random;

public class HomingBook extends Book {
	public static TextureRegion particle = Graphics.getTexture("particle-cool");
	public static TextureRegion big = Graphics.getTexture("particle-big");

	{
		name = Locale.get("book_of_magic_aim");
		description = Locale.get("book_of_magic_aim_desc");
		sprite = "item-book_a";
		mana = 2;
		damage = 4;
	}

	@Override
	public void onPickup() {
		super.onPickup();
		Achievements.unlock("UNLOCK_AIM_BOOK");
	}

	@Override
	public void spawnProjectile(float x, float y, float a) {
		BulletProjectile missile = new BulletProjectile() {
			{
				ignoreArmor = true;
				alwaysActive = true;
			}

			private Mob target;
			private PointLight light;

			@Override
			public void init() {
				super.init();

				float closest = 256;

				for (Mob mob : Mob.all) {
					if (mob.room == Player.instance.room && !mob.isDead() && (!(mob instanceof BurningKnight) || !((BurningKnight) mob).rage)) {
						float d = this.getDistanceTo(mob.x + mob.w / 2, mob.y + mob.h / 2);

						if (d < closest) {
							closest = d;
							target = mob;
						}
					}
				}

				light = World.newLight(32, new Color(1, 1f, 1f, 1f), 64, x, y);
			}

			@Override
			public void destroy() {
				super.destroy();
				World.removeLight(light);
			}

			@Override
			public void render() {
				Graphics.batch.setColor(1, 1, 1, 0.4f);
				Graphics.render(big, this.x, this.y, this.a, big.getRegionWidth() / 2, big.getRegionHeight() / 2, false, false, 2, 2);
				Graphics.batch.setColor(1, 1, 1, 0.8f);
				Graphics.render(particle, this.x, this.y, this.a, particle.getRegionWidth() / 2, particle.getRegionHeight() / 2, false, false);
				Graphics.batch.setColor(1, 1, 1, 1);
			}

			@Override
			public void logic(float dt) {
				super.logic(dt);

				light.setPosition(x, y);

				if (this.last > 0.03f) {
					this.last = 0;
					RectFx fx = new RectFx();

					fx.depth = this.depth;
					fx.x = this.x + Random.newFloat(this.w) - this.w / 2;
					fx.y = this.y + Random.newFloat(this.h) - this.h / 2;
					fx.a = Random.newFloat(0.6f, 0.9f);
					fx.w = 4;
					fx.h = 4;

					Dungeon.area.add(fx);
				}

				if (this.target != null) {
					float dx = this.target.x + this.target.w / 2 - this.x - this.w / 2;
					float dy = this.target.y + this.target.h / 2 - this.y - this.h / 2;
					float angle = (float) Math.atan2(dy, dx);

					this.angle = Gun.angleLerp(this.angle, angle, dt * 4f, false);

					float f = 60f;
					this.velocity.x = (float) (Math.cos(this.angle) * f);
					this.velocity.y = (float) (Math.sin(this.angle) * f);

					if (this.target.isDead()) {
						this.target = null;
					}
				}

				World.checkLocked(this.body).setTransform(this.x, this.y, (float) Math.toRadians(this.a));
			}
		};

		missile.depth = 1;
		missile.damage = this.rollDamage();

		missile.crit = this.lastCrit;
		missile.owner = this.owner;
		missile.x = x;
		missile.y = y - 3;
		missile.rectShape = true;
		missile.w = 6;
		missile.h = 6;
		missile.rotates = true;

		double ra = Math.toRadians(a);

		float s = 80f;

		missile.velocity.x = (float) Math.cos(ra) * s;
		missile.velocity.y = (float) Math.sin(ra) * s;

		Dungeon.area.add(missile);
	}
}
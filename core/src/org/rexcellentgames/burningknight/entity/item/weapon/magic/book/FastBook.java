package org.rexcellentgames.burningknight.entity.item.weapon.magic.book;

import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.BulletProjectile;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.fx.RectFx;
import org.rexcellentgames.burningknight.game.Achievements;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.ColorUtils;
import org.rexcellentgames.burningknight.util.Random;

public class FastBook extends Book {
	public static TextureRegion region = Graphics.getTexture("particle-thick");
	public static TextureRegion filled = Graphics.getTexture("particle-thick_filled");

	{
		name = Locale.get("book_of_green_energy");
		description = Locale.get("book_of_green_energy_desc");
		sprite = "item-book_e";
		damage = 3;
		mana = 1;
	}

	@Override
	public void spawnProjectile(float x, float y, float a) {
		this.spawnShot(x, y, a);
	}

	@Override
	public void onPickup() {
		super.onPickup();
		Achievements.unlock("UNLOCK_FAST_BOOK");
	}

	private void spawnShot(float x, float y, float a) {
		BulletProjectile missile = new BulletProjectile() {
			{
				ignoreArmor = true;
			}

			private PointLight light;

			@Override
			public void init() {
				super.init();
				light = World.newLight(32, new Color(1, 1f, 1f, 1f), 64, x, y);
			}

			@Override
			public void destroy() {
				super.destroy();
				World.removeLight(light);
			}

			@Override
			public void render() {
				Color color = ColorUtils.HSV_to_RGB(this.t * 180 % 360, 100, 100);

				light.setColor(color.r, color.g, color.b, 1);

				Graphics.batch.setColor(color.r, color.g, color.b, 0.4f);
				Graphics.render(filled, this.x, this.y, this.a, region.getRegionWidth() / 2, region.getRegionHeight() / 2, false, false, 2f, 2f);
				Graphics.batch.setColor(color.r, color.g, color.b, 0.8f);
				Graphics.render(region, this.x, this.y, this.a, region.getRegionWidth() / 2, region.getRegionHeight() / 2, false, false);
				Graphics.batch.setColor(1, 1, 1, 1);
			}

			@Override
			public void logic(float dt) {
				super.logic(dt);

				light.setPosition(x, y);

				if (this.last > 0.01f) {
					this.last = 0;
					RectFx fx = new RectFx();


					Color color = ColorUtils.HSV_to_RGB(this.t * 180 % 360, 100, 100);

					fx.depth = this.depth;
					fx.x = this.x + Random.newFloat(this.w) - this.w / 2;
					fx.y = this.y + Random.newFloat(this.h) - this.h / 2;
					fx.w = 4;
					fx.h = 4;
					fx.b = color.b;
					fx.r = color.r;
					fx.g = color.g;

					Dungeon.area.add(fx);
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
		missile.w = 6;
		missile.h = 6;
		missile.rotates = true;

		double ra = Math.toRadians(a);

		float s = 180f;

		missile.velocity.x = (float) Math.cos(ra) * s;
		missile.velocity.y = (float) Math.sin(ra) * s;

		Dungeon.area.add(missile);
	}
}
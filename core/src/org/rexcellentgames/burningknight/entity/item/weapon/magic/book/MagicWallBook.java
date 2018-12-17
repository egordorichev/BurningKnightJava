package org.rexcellentgames.burningknight.entity.item.weapon.magic.book;

import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.fx.ManaFx;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.BulletProjectile;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Random;

public class MagicWallBook extends Book {
	public static TextureRegion particle = Graphics.getTexture("particle-cool");
	public static TextureRegion big = Graphics.getTexture("particle-big");

	{
		name = Locale.get("book_of_magic_wall");
		description = Locale.get("book_of_magic_wall_desc");
		sprite = "item-book_c";
		damage = 6;
		mana = 4;
	}

	@Override
	public void spawnProjectile(float x, float y, float a) {
		float d = 8;

		double an = Math.toRadians(a);

		for (int i = 0; i < 10; i++) {
			float xx = x + (float) Math.cos(an) * i * d;
			float yy = y + (float) Math.sin(an) * i * d;

			this.spawnParticle(xx, yy, i == 0);
		}
	}

	private void spawnParticle(float x, float y, final boolean first) {
		final int mana = getManaUsage();

		BulletProjectile missile = new BulletProjectile() {
			@Override
			protected void onDeath() {
				super.onDeath();

				if (first) {
					int weight = mana;

					while (weight > 0) {
						ManaFx fx = new ManaFx();

						fx.x = x - velocity.x * 0.03f;
						fx.y = y - velocity.y * 0.03f;
						fx.half = weight == 1;
						fx.poof();

						weight -= fx.half ? 1 : 2;
						Dungeon.area.add(fx);
						LevelSave.add(fx);
					}
				}
			}

			{
				ignoreArmor = true;
			}

			@Override
			public void render() {
				float r = (float) Math.abs(Math.sin(this.t * 2.5f));
				float b = (float) Math.abs(Math.cos(this.t * 3f));

				light.setColor(r, 0, b, 1f);

				Graphics.batch.setColor(r, 0, b, 0.4f);
				Graphics.render(big, this.x, this.y, this.a, big.getRegionWidth() / 2, big.getRegionHeight() / 2, false, false, 2, 2);
				Graphics.batch.setColor(r, 0, b, 0.8f);
				Graphics.render(particle, this.x, this.y, this.a, particle.getRegionWidth() / 2, particle.getRegionHeight() / 2, false, false);
				Graphics.batch.setColor(1, 1, 1, 1);
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
			public void logic(float dt) {
				super.logic(dt);

				light.setPosition(x, y);

				/*if (this.last > 0.1f) {
					this.last = 0;
					RectFx fx = new RectFx();

					fx.depth = this.depth;
					fx.x = this.x + Random.newFloat(this.w) - this.w / 2;
					fx.y = this.y + Random.newFloat(this.h) - this.h / 2;
					fx.w = 4;
					fx.h = 4;
					fx.r = (float) Math.abs(Math.sin(this.t * 2.5f));
					fx.b = (float) Math.abs(Math.cos(this.t * 3f));
					fx.g = (float) Math.abs(Math.cos(this.t * 1.7f));

					Dungeon.area.add(fx);
				}*/

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
		missile.t = Random.newFloat(1f);
		missile.dissappearWithTime = true;
		missile.velocity.x = 0;
		missile.velocity.y = 0;

		Dungeon.area.add(missile);
	}
}
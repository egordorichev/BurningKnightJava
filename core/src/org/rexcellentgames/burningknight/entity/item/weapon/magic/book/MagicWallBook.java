package org.rexcellentgames.burningknight.entity.item.weapon.magic.book;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.BulletProjectile;
import org.rexcellentgames.burningknight.util.Random;

public class MagicWallBook extends Book {
	public static TextureRegion particle = Graphics.getTexture("particle-cool");
	public static TextureRegion big = Graphics.getTexture("particle-big");

	{
		name = Locale.get("book_of_magic_wall");
		description = Locale.get("book_of_magic_wall_desc");
		sprite = "item-book_c";
		damage = 4;
		mana = 4;
	}

	@Override
	public void spawnProjectile(float x, float y, float a) {
		float d = 8;

		double an = Math.toRadians(a);

		for (int i = 0; i < 10; i++) {
			float xx = x + (float) Math.cos(an) * i * d;
			float yy = y + (float) Math.sin(an) * i * d;

			this.spawnParticle(xx, yy);
		}
	}

	private void spawnParticle(float x, float y) {
		BulletProjectile missile = new BulletProjectile() {
			@Override
			public void render() {
				float r = (float) Math.abs(Math.sin(this.t * 2.5f));
				float b = (float) Math.abs(Math.cos(this.t * 3f));

				Graphics.batch.setColor(r, 0, b, 0.4f);
				Graphics.render(big, this.x, this.y, this.a, big.getRegionWidth() / 2, big.getRegionHeight() / 2, false, false, 2, 2);
				Graphics.batch.setColor(r, 0, b, 0.8f);
				Graphics.render(particle, this.x, this.y, this.a, particle.getRegionWidth() / 2, particle.getRegionHeight() / 2, false, false);
				Graphics.batch.setColor(1, 1, 1, 1);
			}

			@Override
			public void logic(float dt) {
				super.logic(dt);

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

				this.body.setTransform(this.x, this.y, (float) Math.toRadians(this.a));
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
		missile.vel.x = 0;
		missile.vel.y = 0;

		Dungeon.area.add(missile);
	}
}
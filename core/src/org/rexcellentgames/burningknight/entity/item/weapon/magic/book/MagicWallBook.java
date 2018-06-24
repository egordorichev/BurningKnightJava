package org.rexcellentgames.burningknight.entity.item.weapon.magic.book;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.BulletProjectile;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.fx.RectFx;
import org.rexcellentgames.burningknight.util.Random;

public class MagicWallBook extends Book {
	public static TextureRegion particle = Graphics.getTexture("particle-cool");

	{
		name = Locale.get("book_of_magic_wall");
		description = Locale.get("book_of_magic_wall_desc");
		sprite = "item-book_c";
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
				Graphics.batch.end();
				RectFx.shader.begin();
				RectFx.shader.setUniformf("r", (float) Math.abs(Math.sin(this.t * 2.5f)));
				RectFx.shader.setUniformf("g", 0f);
				RectFx.shader.setUniformf("b", (float) Math.abs(Math.cos(this.t * 3f)));
				RectFx.shader.setUniformf("a", 0.8f);
				RectFx.shader.end();
				Graphics.batch.setShader(RectFx.shader);
				Graphics.batch.begin();
				Graphics.render(particle, this.x, this.y, this.a, this.w / 2, this.h / 2, false, false);
				Graphics.batch.end();
				Graphics.batch.setShader(null);
				Graphics.batch.begin();
			}

			@Override
			public void logic(float dt) {
				super.logic(dt);

				if (this.last > 0.1f) {
					this.last = 0;
					RectFx fx = new RectFx();

					fx.depth = this.depth;
					fx.x = this.x + Random.newFloat(this.w);
					fx.y = this.y + Random.newFloat(this.h);
					fx.w = 4;
					fx.h = 4;
					fx.g = 0;
					fx.r = (float) Math.abs(Math.sin(this.t * 2.5f));
					fx.b = (float) Math.abs(Math.cos(this.t * 3f));

					Dungeon.area.add(fx);
				}

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
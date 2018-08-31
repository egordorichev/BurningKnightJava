package org.rexcellentgames.burningknight.entity.item.weapon.magic.book;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.BulletProjectile;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.fx.RectFx;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Random;

public class TripleShotBook extends Book {
	public static TextureRegion region = Graphics.getTexture("particle-empty");
	public static TextureRegion big = Graphics.getTexture("particle-big");

	{
		name = Locale.get("book_of_magic_trinity");
		description = Locale.get("book_of_magic_trinity_desc");
		sprite = "item-book_b";
		damage = 4;
		mana = 2;
	}

	@Override
	public void spawnProjectile(float x, float y, float a) {
		this.spawnShot(x, y, a);
		this.spawnShot(x, y, a - Random.newFloat(5f, 10f) * 2f);
		this.spawnShot(x, y, a + Random.newFloat(5f, 10f) * 2f);
	}

	private void spawnShot(float x, float y, float a) {
		BulletProjectile missile = new BulletProjectile() {
			@Override
			public void render() {
				Graphics.batch.setColor(1, 1, 0, 0.4f);
				Graphics.render(big, this.x, this.y, this.a, big.getRegionWidth() / 2, big.getRegionHeight() / 2, false, false, 2, 2);
				Graphics.batch.setColor(1, 1, 0, 0.8f);
				Graphics.render(region, this.x, this.y, this.a, region.getRegionWidth() / 2, region.getRegionHeight() / 2, false, false);
				Graphics.batch.setColor(1, 1, 1, 1);
			}

			@Override
			public void logic(float dt) {
				super.logic(dt);

				if (this.last > 0.03f) {
					this.last = 0;
					RectFx fx = new RectFx();

					fx.depth = this.depth;
					fx.x = this.x + Random.newFloat(this.w) - this.w / 2;
					fx.y = this.y + Random.newFloat(this.h) - this.h / 2;
					fx.w = 4;
					fx.h = 4;
					fx.g = Random.newFloat(0.5f, 1f);
					fx.b = 0f;

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
		missile.rectShape = true;
		missile.w = 6;
		missile.h = 6;
		missile.rotates = true;

		double ra = Math.toRadians(a);

		float s = 60f;

		missile.vel.x = (float) Math.cos(ra) * s;
		missile.vel.y = (float) Math.sin(ra) * s;

		Dungeon.area.add(missile);
	}
}
package org.rexcellentgames.burningknight.entity.item.weapon.magic;

import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.fx.ManaFx;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.BulletProjectile;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.fx.RectFx;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Random;

public class MagicMissileWand extends Wand {
	{
		name = Locale.get("magic_missile_wand");
		description = Locale.get("magic_missile_wand_desc");
		sprite = "item-wand_b";
		damage = 8;
		mana = 2;
	}

	public static TextureRegion region = Graphics.getTexture("particle-big");

	@Override
	public void spawnProjectile(float x, float y, float a) {
		final int mana = getManaUsage();

		BulletProjectile missile = new BulletProjectile() {
			@Override
			protected void onDeath() {
				super.onDeath();

				int weight = manaUsed;

				while (weight > 0) {
					ManaFx fx = new ManaFx();

					fx.x = x- velocity.x * 0.06f;
					fx.y = y - velocity.y * 0.06f;
					fx.half = weight == 1;
					fx.poof();

					weight -= fx.half ? 1 : 2;
					Dungeon.area.add(fx);
					LevelSave.add(fx);
				}
			}

			{
				ignoreArmor = true;
			}

			private int manaUsed;

			@Override
			public void render() {
				Graphics.batch.setColor(1, 1, 1, 0.4f);
				Graphics.render(region, this.x, this.y, this.a, region.getRegionWidth() / 2, region.getRegionHeight() / 2, false, false, 2f, 2f);
				Graphics.batch.setColor(1, 1, 1, 0.8f);
				Graphics.render(region, this.x, this.y, this.a, region.getRegionWidth() / 2, region.getRegionHeight() / 2, false, false);
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

				if (this.last > 0.05f) {
					this.last = 0;
					RectFx fx = new RectFx();

					fx.depth = this.depth;
					fx.x = this.x + Random.newFloat(this.w) - this.w / 2;
					fx.y = this.y + Random.newFloat(this.h) - this.h / 2;
					fx.w = 4;
					fx.h = 4;

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

		float s = 100f;

		missile.velocity.x = (float) Math.cos(ra) * s;
		missile.velocity.y = (float) Math.sin(ra) * s;

		Dungeon.area.add(missile);
	}
}
package org.rexcellentgames.burningknight.entity.item.weapon.magic;

import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.creature.fx.ManaFx;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.BulletProjectile;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.fx.RectFx;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Random;

public class EcoWand extends Wand {
	{
		sprite = "item-wand_c";
		damage = 1;
	}

	public static TextureRegion region = Graphics.getTexture("particle-big");

	@Override
	public void spawnProjectile(float x, float y, float a) {
		final int mana = getManaUsage();

		BulletProjectile missile = new BulletProjectile() {
			@Override
			protected void onDeath() {
				super.onDeath();

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

			{
				ignoreArmor = true;
			}

			@Override
			public void render() {
				Graphics.batch.setColor(0.3f, 1, 0.3f, 0.4f);
				Graphics.render(region, this.x, this.y, this.a, region.getRegionWidth() / 2, region.getRegionHeight() / 2, false, false, 2f, 2f);
				Graphics.batch.setColor(0.3f, 1, 0.3f, 0.8f);
				Graphics.render(region, this.x, this.y, this.a, region.getRegionWidth() / 2, region.getRegionHeight() / 2, false, false);
				Graphics.batch.setColor(1, 1, 1, 1);
			}

			private PointLight light;

			@Override
			public void init() {
				super.init();
				light = World.newLight(32, new Color(0.3f, 0.3f, 1f, 1f), 64, x, y);
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

				if (this.last > 0.03f) {
					this.last = 0;
					RectFx fx = new RectFx();

					fx.depth = this.depth;
					fx.x = this.x + Random.newFloat(this.w) - this.w / 2;
					fx.y = this.y + Random.newFloat(this.h) - this.h / 2;
					fx.w = 4;
					fx.h = 4;
					fx.r = 0.3f + Random.newFloat(0.3f);
					fx.b = 0.3f + Random.newFloat(0.3f);

					Dungeon.area.add(fx);

					int x = (int) Math.floor(this.x / 16);
					int y = (int) Math.floor((this.y + 8) / 16);
					int i = Level.toIndex(x, y);
					// byte t = Dungeon.level.get(i);
					byte l = Dungeon.level.liquidData[i];

					if (Dungeon.level.checkFor(i, Terrain.PASSABLE)) {
						if (l == Terrain.GRASS && Random.chance(30)) {
							Dungeon.level.set(i, Terrain.HIGH_GRASS);
						} else if (l == Terrain.DRY_GRASS) {
							Dungeon.level.set(i, Terrain.GRASS);
						} else if (l == Terrain.HIGH_DRY_GRASS) {
							Dungeon.level.set(i, Terrain.HIGH_GRASS);
						} else if (l != Terrain.HIGH_GRASS && l != Terrain.EXIT) {
							Dungeon.level.set(i, Terrain.GRASS);
						}

						Dungeon.level.info[i] = 0;
						Dungeon.level.updateTile(x, y);
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
		missile.y = y - 3; 		missile.rectShape = true; 		missile.w = 6; 		missile.h = 6;
		missile.rotates = true;

		double ra = Math.toRadians(a);

		float s = 60f;

		missile.velocity.x = (float) Math.cos(ra) * s;
		missile.velocity.y = (float) Math.sin(ra) * s;

		Dungeon.area.add(missile);
	}
}
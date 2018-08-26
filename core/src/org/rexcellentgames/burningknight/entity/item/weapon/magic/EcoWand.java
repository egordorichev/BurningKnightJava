package org.rexcellentgames.burningknight.entity.item.weapon.magic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.BulletProjectile;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.fx.RectFx;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.util.Random;

public class EcoWand extends Wand {
	{
		sprite = "item-wand_c";
		damage = 1;
	}

	public static TextureRegion region = Graphics.getTexture("particle-big");

	@Override
	public void spawnProjectile(float x, float y, float a) {
		BulletProjectile missile = new BulletProjectile() {
			@Override
			public void render() {
				Graphics.batch.end();
				RectFx.shader.begin();
				RectFx.shader.setUniformf("r", 0.3f);
				RectFx.shader.setUniformf("g", 1f);
				RectFx.shader.setUniformf("b", 0.3f);
				RectFx.shader.setUniformf("a", 0.8f);
				Texture texture = region.getTexture();

				RectFx.shader.setUniformf("pos", new Vector2(((float) region.getRegionX()) / texture.getWidth(), ((float) region.getRegionY()) / texture.getHeight()));
				RectFx.shader.setUniformf("size", new Vector2(((float) region.getRegionWidth()) / texture.getWidth(), ((float) region.getRegionHeight()) / texture.getHeight()));

				RectFx.shader.end();
				Graphics.batch.setShader(RectFx.shader);
				Graphics.batch.begin();
				Graphics.render(region, this.x, this.y, this.a, this.w / 2, this.h / 2, false, false);
				Graphics.batch.end();
				Graphics.batch.setShader(null);
				Graphics.batch.begin();
			}

			@Override
			public void logic(float dt) {
				super.logic(dt);

				if (this.last > 0.03f) {
					this.last = 0;
					RectFx fx = new RectFx();

					fx.depth = this.depth;
					fx.x = this.x + Random.newFloat(this.w) / 2 - this.w / 4 + this.w / 2;
					fx.y = this.y + Random.newFloat(this.h) / 2 - this.h / 4 + this.h / 2;
					fx.w = 4;
					fx.h = 4;
					fx.r = 0.3f + Random.newFloat(0.3f);
					fx.b = 0.3f + Random.newFloat(0.3f);

					Dungeon.area.add(fx);

					int x = Math.round(this.x / 16);
					int y = Math.round(this.y / 16);
					int i = Level.toIndex(x, y);
					byte t = Dungeon.level.get(i);
					byte l = Dungeon.level.liquidData[i];

					if (Dungeon.level.checkFor(i, Terrain.PASSABLE)) {
						if (l == Terrain.GRASS && Random.chance(30)) {
							Dungeon.level.set(i, Terrain.HIGH_GRASS);
						} else if (l == Terrain.DRY_GRASS) {
							Dungeon.level.set(i, Terrain.GRASS);
						} else if (l == Terrain.HIGH_DRY_GRASS) {
							Dungeon.level.set(i, Terrain.HIGH_GRASS);
						} else if (l != Terrain.HIGH_GRASS) {
							Dungeon.level.set(i, Terrain.GRASS);
						}

						Dungeon.level.updateTile(x, y);
					}
				}

				this.body.setTransform(this.x, this.y, (float) Math.toRadians(this.a));
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

		missile.vel.x = (float) Math.cos(ra) * s;
		missile.vel.y = (float) Math.sin(ra) * s;

		Dungeon.area.add(missile);
	}
}
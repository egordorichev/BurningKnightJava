package org.rexcellentgames.burningknight.entity.item.weapon.magic;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.buff.BurningBuff;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.BulletProjectile;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.fx.RectFx;
import org.rexcellentgames.burningknight.util.Random;

public class FireWand extends Wand {
	{
		name = Locale.get("fire_wand");
		description = Locale.get("fire_wand_desc");
		sprite = "item (wand G)";
		damage = 2;
	}

	public static TextureRegion region = Graphics.getTexture("particle-big");

	@Override
	public void spawnProjectile(float x, float y, float a) {
		BulletProjectile missile = new BulletProjectile() {
			@Override
			public void render() {
				Graphics.batch.end();
				RectFx.shader.begin();
				RectFx.shader.setUniformf("r", 1f);
				RectFx.shader.setUniformf("g", 0.3f);
				RectFx.shader.setUniformf("b", 0.3f);
				RectFx.shader.setUniformf("a", 0.8f);
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
					fx.x = this.x + Random.newFloat(this.w);
					fx.y = this.y + Random.newFloat(this.h);
					fx.w = 4;
					fx.h = 4;
					fx.g = 0.3f;
					fx.b = 0.3f;

					Dungeon.area.add(fx);
				}

				this.body.setTransform(this.x, this.y, (float) Math.toRadians(this.a));
			}

			@Override
			protected void onHit(Entity entity) {
				super.onHit(entity);

				if (entity instanceof Creature) {
					((Creature) entity).addBuff(new BurningBuff());
				}
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
package org.rexcellentgames.burningknight.entity.item.weapon.magic;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.buff.FreezeBuff;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.BulletProjectile;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.fx.RectFx;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.entities.Door;
import org.rexcellentgames.burningknight.util.Random;

public class IceWand extends Wand {
	{
		name = Locale.get("ice_wand");
		description = Locale.get("ice_wand_desc");
		sprite = "item-wand_f";
		damage = 1;
		mana = 2;
	}

	public static TextureRegion region = Graphics.getTexture("particle-big");

	@Override
	public void spawnProjectile(float x, float y, float a) {
		BulletProjectile missile = new BulletProjectile() {
			@Override
			public void render() {
				Graphics.batch.setColor(0.3f, 0.3f, 1, 0.4f);
				Graphics.render(region, this.x, this.y, this.a, region.getRegionWidth() / 2, region.getRegionHeight() / 2, false, false, 2f, 2f);
				Graphics.batch.setColor(0.3f, 0.3f, 1, 0.8f);
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
					fx.r = 0.3f;
					fx.g = 0.3f + Random.newFloat(0.6f);

					Dungeon.area.add(fx);

					int i = Level.toIndex((int) Math.floor(this.x / 16), (int) Math.floor(this.y / 16));

					Dungeon.level.setOnFire(i, false);
					Dungeon.level.freeze(i);
				}

				this.body.setTransform(this.x, this.y, (float) Math.toRadians(this.a));
			}

			@Override
			public void onCollision(Entity entity) {
				super.onCollision(entity);

				if (entity instanceof Door) {
					((Door) entity).burning = false;
				}
			}

			@Override
			protected void onHit(Entity entity) {
				super.onHit(entity);

				if (entity instanceof Creature) {
					((Creature) entity).addBuff(new FreezeBuff().setDuration(3));
				}
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
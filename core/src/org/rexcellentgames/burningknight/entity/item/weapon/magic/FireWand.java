package org.rexcellentgames.burningknight.entity.item.weapon.magic;

import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.buff.BurningBuff;
import org.rexcellentgames.burningknight.entity.creature.fx.ManaFx;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.BulletProjectile;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.fx.RectFx;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.entities.Door;
import org.rexcellentgames.burningknight.entity.level.entities.Tree;
import org.rexcellentgames.burningknight.entity.level.entities.chest.Chest;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.game.Achievements;
import org.rexcellentgames.burningknight.game.Ui;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Log;
import org.rexcellentgames.burningknight.util.Random;

public class FireWand extends Wand {
	{
		name = Locale.get("fire_wand");
		description = Locale.get("fire_wand_desc");
		sprite = "item-wand_g";
		damage = 2;
		mana = 2;
	}

	public static TextureRegion region = Graphics.getTexture("particle-big");

	private boolean showed;
	private boolean hidden;

	@Override
	public void onPickup() {
		super.onPickup();
		Log.error("On pickup " + showed);

		if (Dungeon.depth == -3 && !showed) {
			showed = true;

			Ui.ui.addControl("[white]" + Input.instance.getMapping("use") + " [gray]" + Locale.get("use"));
			Ui.ui.addControl("[white]" + Input.instance.getMapping("switch") + " [gray]" + Locale.get("select_item"));
		}

		if (Dungeon.depth != -3) {
			Achievements.unlock("UNLOCK_FIRE_WAND");
		}
	}

	@Override
	public void use() {
		super.use();

		if (!hidden) {
			hidden = true;
			Ui.ui.hideControls();
		}
	}

	@Override
	public void spawnProjectile(float x, float y, float a) {
		final int mana = getManaUsage();

		BulletProjectile missile = new BulletProjectile() {
			@Override
			protected void death() {
				super.death();

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
				Graphics.batch.setColor(1, 0.3f, 0.3f, 0.4f);
				Graphics.render(region, this.x, this.y, this.a, region.getRegionWidth() / 2, region.getRegionHeight() / 2, false, false, 2f, 2f);
				Graphics.batch.setColor(1, 0.3f, 0.3f, 0.8f);
				Graphics.render(region, this.x, this.y, this.a, region.getRegionWidth() / 2, region.getRegionHeight() / 2, false, false);
				Graphics.batch.setColor(1, 1, 1, 1);
			}

			private PointLight light;

			@Override
			public void init() {
				super.init();
				light = World.newLight(32, new Color(1, 0.3f, 0.3f, 1f), 64, x, y);
			}

			@Override
			public void destroy() {
				super.destroy();
				World.removeLight(light);
			}

			@Override
			public void logic(float dt) {
				super.logic(dt);

				light.setPosition(this.x, this.y);

				if (this.last > 0.03f) {
					this.last = 0;
					RectFx fx = new RectFx();

					fx.depth = this.depth;

					fx.x = this.x + Random.newFloat(this.w) - this.w / 2;
					fx.y = this.y + Random.newFloat(this.h) - this.h / 2;
					fx.w = 4;
					fx.h = 4;
					fx.g = 0.3f + Random.newFloat(0.5f);
					fx.b = 0.3f;

					Dungeon.area.add(fx);
					Dungeon.level.setOnFire(Level.toIndex((int) Math.floor(this.x / 16), (int) Math.floor((this.y + 8) / 16)), true);
				}

				World.checkLocked(this.body).setTransform(this.x, this.y, (float) Math.toRadians(this.a));
			}

			@Override
			protected void onHit(Entity entity) {
				super.onHit(entity);

				if (entity instanceof Creature) {
					((Creature) entity).addBuff(new BurningBuff());
				}
			}

			@Override
			public void onCollision(Entity entity) {
				super.onCollision(entity);

				if (entity instanceof Door) {
					((Door) entity).burning = true;
				} else if (entity instanceof Chest) {
					((Chest) entity).burning = true;
				} else if (entity instanceof Tree) {
					((Tree) entity).burning = true;
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

		missile.velocity.x = (float) Math.cos(ra) * s;
		missile.velocity.y = (float) Math.sin(ra) * s;

		Dungeon.area.add(missile);
	}
}
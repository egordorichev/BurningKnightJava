package org.rexcellentgames.burningknight.entity.item.weapon.magic.book;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.BulletProjectile;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.fx.RectFx;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.game.Achievements;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Log;
import org.rexcellentgames.burningknight.util.Random;

public class SuperCrazyBook extends Book {
	public static TextureRegion big = Graphics.getTexture("particle-giant");
	public static TextureRegion particle = Graphics.getTexture("particle-large");
	public static TextureRegion small = Graphics.getTexture("particle-cool");
	public static TextureRegion tiny = Graphics.getTexture("particle-triangle");

	{
		name = Locale.get("book_of_crash");
		description = Locale.get("book_of_crash_desc");
		sprite = "item-book_g";
		mana = 6;
		damage = 4;
	}

	@Override
	public void onPickup() {
		super.onPickup();
		Achievements.unlock(Achievements.FIND_CRASH_BOOK);
	}

	public void spawnMicroChild(float x, float y) {
		for (int i = 0; i < 8; i++) {
			float a = (float) (i * Math.PI / 4);

			BulletProjectile missile = new BulletProjectile() {
				@Override
				public void render() {
					float g = (float) Math.abs(Math.cos(this.t * 2f - 0.1f));
					float b = (float) Math.abs(Math.sin(this.t * 2.7f));

					Graphics.batch.setColor(1f, g, b, 0.4f);
					Graphics.render(tiny, this.x, this.y, this.a, tiny.getRegionWidth() / 2, tiny.getRegionHeight() / 2, false, false, 2f, 2f);
					Graphics.batch.setColor(1f, g, b, 0.8f);
					Graphics.render(tiny, this.x, this.y, this.a, tiny.getRegionWidth() / 2, tiny.getRegionHeight() / 2, false, false);
					Graphics.batch.setColor(1, 1, 1, 1);
				}

				@Override
				public void logic(float dt) {
					super.logic(dt);
					World.checkLocked(this.body).setTransform(this.x, this.y, (float) Math.toRadians(this.a));
				}
			};

			missile.depth = 1;
			missile.damage = this.rollDamage();

			missile.crit = this.lastCrit;
			missile.owner = this.owner;
			missile.x = x;
			missile.y = y;
			missile.rectShape = true;
			missile.w = 6;
			missile.h = 6;
			missile.rotates = true;

			double ra = a;
			float s = 120f;

			missile.velocity.x = (float) Math.cos(ra) * s;
			missile.velocity.y = (float) Math.sin(ra) * s;

			Dungeon.area.add(missile);
		}
	}

	public static TextureRegion full = Graphics.getTexture("particle-big");

	public void spawnChild(float x, float y) {
		for (int i = 0; i < 8; i++) {
			float a = (float) (i * Math.PI / 4);

			BulletProjectile missile = new BulletProjectile() {
				@Override
				public void render() {
					float r = (float) Math.abs(Math.cos(this.t * 3.5f));
					float g = (float) Math.abs(Math.cos(this.t * 1.4f - 0.1f));
					float b = (float) Math.abs(Math.sin(this.t * 1.7f));

					Graphics.batch.setColor(r, g, b, 0.4f);
					Graphics.render(full, this.x, this.y, this.a, full.getRegionWidth() / 2, full.getRegionHeight() / 2, false, false, 2f, 2f);
					Graphics.batch.setColor(r, g, b, 0.8f);
					Graphics.render(small, this.x, this.y, this.a, small.getRegionWidth() / 2, small.getRegionHeight() / 2, false, false);
					Graphics.batch.setColor(1, 1, 1, 1);
				}

				@Override
				protected void onDeath() {
					super.onDeath();
					spawnMicroChild(this.x, this.y);
				}

				@Override
				public void logic(float dt) {
					super.logic(dt);
					World.checkLocked(this.body).setTransform(this.x, this.y, (float) Math.toRadians(this.a));
				}
			};

			missile.depth = 1;
			missile.damage = this.rollDamage();

			missile.crit = this.lastCrit;
			missile.owner = this.owner;
			missile.x = x;
			missile.y = y;
			missile.rectShape = true;
			missile.w = 6;
			missile.h = 6;
			missile.rotates = true;

			double ra = a;
			float s = 60f;

			missile.velocity.x = (float) Math.cos(ra) * s;
			missile.velocity.y = (float) Math.sin(ra) * s;

			Dungeon.area.add(missile);
		}
	}

	@Override
	public void spawnProjectile(float x, float y, float a) {
		y += 8;

		if (Dungeon.level.checkFor(Math.round(x / 16), Math.round(y / 16), Terrain.SOLID) ||
			Dungeon.level.checkFor(Math.round(x / 16), Math.round((y - 16) / 16), Terrain.SOLID)) {
			Log.error("In Wall!");
			return;
		}

		BulletProjectile missile = new BulletProjectile() {
			@Override
			public void render() {
				float r = (float) Math.abs(Math.cos(this.t * 1.5f));
				float g = (float) Math.abs(Math.cos(this.t * 2f - 0.1f));
				float b = (float) Math.abs(Math.sin(this.t * 2.7f));

				Graphics.batch.setColor(r, g, b, 0.4f);
				Graphics.render(big, this.x, this.y, this.a, big.getRegionWidth() / 2, big.getRegionHeight() / 2, false, false, 1.5f, 1.5f);
				Graphics.batch.setColor(r, g, b, 0.8f);
				Graphics.render(big, this.x, this.y, this.a, big.getRegionWidth() / 2, big.getRegionHeight() / 2, false, false);
				Graphics.batch.setColor(1, 1, 1, 1);
			}

			@Override
			protected void onDeath() {
				super.onDeath();
				spawnMain(this.x, this.y);
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
					fx.r = (float) Math.abs(Math.cos(this.t * 1.5f));
					fx.g = (float) Math.abs(Math.cos(this.t * 2f - 0.1f));
					fx.b = (float) Math.abs(Math.sin(this.t * 2.7f));

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
		missile.y = y - 8;
		missile.rectShape = true;
		missile.w = 16;
		missile.h = 16;
		missile.rotates = true;
		missile.rotationSpeed = 0.3f;

		double ra = Math.toRadians(a);
		float s = 30f;

		missile.velocity.x = (float) Math.cos(ra) * s;
		missile.velocity.y = (float) Math.sin(ra) * s;

		Dungeon.area.add(missile);
	}

	public void spawnMain(float x, float y) {
		for (int i = 0; i < 8; i++) {
			float a = (float) (i * Math.PI / 4);


			BulletProjectile missile = new BulletProjectile() {
				@Override
				public void render() {
					float r = (float) Math.abs(Math.cos(this.t * 1.5f));
					float g = (float) Math.abs(Math.cos(this.t * 2f - 0.1f));
					float b = (float) Math.abs(Math.sin(this.t * 2.7f));

					Graphics.batch.setColor(r, g, b, 0.4f);
					Graphics.render(particle, this.x, this.y, this.a, particle.getRegionWidth() / 2, particle.getRegionHeight() / 2, false, false, 2f, 2f);
					Graphics.batch.setColor(r, g, b, 0.8f);
					Graphics.render(particle, this.x, this.y, this.a, particle.getRegionWidth() / 2, particle.getRegionHeight() / 2, false, false);
					Graphics.batch.setColor(1, 1, 1, 1);
				}

				@Override
				protected void onDeath() {
					super.onDeath();
					spawnChild(this.x, this.y);
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
						fx.r = (float) Math.abs(Math.cos(this.t * 1.5f));
						fx.g = (float) Math.abs(Math.cos(this.t * 2f - 0.1f));
						fx.b = (float) Math.abs(Math.sin(this.t * 2.7f));

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
			missile.y = y;
			missile.rectShape = true;
			missile.w = 8;
			missile.h = 8;
			missile.rotates = true;
			missile.rotationSpeed = 0.3f;

			double ra = a;
			float s = 50f;

			missile.velocity.x = (float) Math.cos(ra) * s;
			missile.velocity.y = (float) Math.sin(ra) * s;

			Dungeon.area.add(missile);
		}
	}
}
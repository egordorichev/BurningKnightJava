package org.rexcellentgames.burningknight.entity.plant;


import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.buff.BurningBuff;
import org.rexcellentgames.burningknight.entity.creature.buff.fx.FlameFx;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.BulletProjectile;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.ItemHolder;
import org.rexcellentgames.burningknight.entity.item.weapon.Weapon;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.AxeProjectile;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.ArrowProjectile;
import org.rexcellentgames.burningknight.entity.level.SaveableEntity;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.Tween;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;

import java.io.IOException;
import java.util.ArrayList;

public class Plant extends SaveableEntity {
	protected float growSpeed = 1f;
	protected float growProgress;
	protected Body body;
	protected AnimationData animation;
	protected boolean broke;
	protected boolean canBurn = true;
	protected boolean burning;
	protected float health;

	public void startBurning() {
		this.burning = this.canBurn;
	}

	public AnimationData getWiltAnimation() {
		return null;
	}

	private boolean dead;

	@Override
	public void init() {
		super.init();

		this.health = 3f;

		this.t = Random.newFloat(128);
		this.body = World.createSimpleBody(this, 3, 3, 10, 10, BodyDef.BodyType.DynamicBody, true);
		
		if (this.body != null) {
			this.body.setTransform(this.x, this.y, 0);
		}
	}

	private float lastFlame;

	@Override
	public void update(float dt) {
		super.update(dt);

		if (this.dead) {
			if (this.growProgress == 1f && !Dungeon.reset && this.broke) {
				ArrayList<Item> drops = this.getDrops();

				for (Item item : drops) {
					ItemHolder holder = new ItemHolder();

					holder.setItem(item);
					holder.x = this.x + Random.newInt(-8, 8);
					holder.y = this.y + Random.newInt(-8, 8);
					holder.randomVel();

					Dungeon.area.add(holder.add());
				}

				for (int i = 0; i < 10; i++) {
					PlantFx fx = new PlantFx();

					fx.x = this.x + Random.newInt(-4, 4) + 8;
					fx.y = this.y + Random.newInt(-4, 4) + 8;

					Dungeon.area.add(fx);
				}

				this.broke = false;
			}

			this.sz = Math.max(1, this.sz - this.sz * dt * 2);
			return;
		}

		if (this.burning) {
			this.health -= dt;
			Dungeon.level.addLightInRadius(this.x + 8, this.y + 8, 1f, 0.9f, 0f, 0.9f, 3f, false);

			this.lastFlame += dt;

			if (this.lastFlame >= 0.1f) {
				this.lastFlame = 0;
				Dungeon.area.add(new FlameFx(this));
			}

			if (this.health <= 0) {
				this.dead = true;
				this.animation = this.getWiltAnimation();
			}
		}

		if (this.growProgress < 1) {
			float a = Dungeon.depth > 0 ? Dungeon.level.getLight((int) this.x / 16, (int) this.y / 16) : 1;
			this.growProgress += this.growSpeed * a * dt * 0.05f;

			if (this.growProgress > 1) {
				this.growProgress = 1;
			}
		}
	}

	private float sz = 1f;

	@Override
	public void render() {
		TextureRegion sprite = this.animation.getFrames().get(
			this.animation == this.getWiltAnimation() ? 0 : (int) Math.floor(this.growProgress * 2)).frame;

		float a = (float) (Math.sin(this.t * 2.5f * 2) * Math.cos(this.t * 1.5f * 2) * 5) * this.sz;

		Graphics.render(sprite, this.x + 8,
			this.y + (16 - sprite.getRegionHeight()) / 2, a, sprite.getRegionWidth() / 2, 0, false, false);
	}

	@Override
	public void renderShadow() {
		TextureRegion sprite = this.animation.getFrames().get(
			this.animation == this.getWiltAnimation() ? 0 : (int) Math.floor(this.growProgress * 2)).frame;

		Graphics.shadow(this.x, this.y + sprite.getRegionHeight() / 4, sprite.getRegionWidth(), sprite.getRegionHeight());
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);
		writer.writeFloat(this.growProgress);
		writer.writeBoolean(this.dead);
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);

		this.body.setTransform(this.x, this.y, 0);
		this.growProgress = reader.readFloat();

		this.dead = reader.readBoolean();

		if (this.dead) {
			this.animation = this.getWiltAnimation();
		}

		this.t = Random.newFloat(128);
	}

	@Override
	public void onCollision(Entity entity) {
		super.onCollision(entity);

		if ((entity instanceof Weapon || entity instanceof ArrowProjectile || entity instanceof BulletProjectile || entity instanceof AxeProjectile) && this.growProgress == 1f && !this.dead) {
			this.dead = true;
			this.animation = this.getWiltAnimation();
			this.broke = true;
			// Dungeon.level.set((int) this.x / 16, (int) (this.y + 8) / 16, Terrain.DIRT);
		} else if (entity instanceof Creature) {
			Tween.to(new Tween.Task(5f, 0.1f) {
				@Override
				public float getValue() {
					return sz;
				}

				@Override
				public void setValue(float value) {
					sz = value;
				}
			});

			if (((Creature) entity).hasBuff(BurningBuff.class)) {
				this.startBurning();
			}
		}
	}

	public ArrayList<Item> getDrops() {
		return new ArrayList<>();
	}

	@Override
	public void destroy() {
		super.destroy();
		this.body = World.removeBody(this.body);
	}

	public void grow() {
		this.growProgress = 1;
	}

	public static Plant random() {
		int random = Random.newInt(3);

		switch (random) {
			case 0: if (Dungeon.depth > 0) { return new Lightroom(); } break;
			// case 1: return new PotionGrass();
		}

		return new Cabbage();
	}
}
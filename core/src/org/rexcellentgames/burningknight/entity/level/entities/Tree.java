package org.rexcellentgames.burningknight.entity.level.entities;

import com.badlogic.gdx.math.Rectangle;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.fx.TerrainFlameFx;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.entities.fx.PoofFx;
import org.rexcellentgames.burningknight.game.state.InGameState;
import org.rexcellentgames.burningknight.util.BitHelper;
import org.rexcellentgames.burningknight.util.PathFinder;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.Tween;

public class Tree extends SolidProp {
	{
		sprite = "props-tree";
		collider = new Rectangle(4, 8, 30 - 4 * 2, 30 - 8 * 2);
		w = 30;
		h = 30;
	}

	private float am = 1f;
	private float damage;
	public boolean burning;
	private float lastFlame;

	@Override
	public void onCollision(Entity entity) {
		super.onCollision(entity);

		if (entity instanceof Creature) {
			Tween.to(new Tween.Task(4, 0.2f) {
				@Override
				public float getValue() {
					return am;
				}

				@Override
				public void setValue(float value) {
					am = value;
				}
			});
		}
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		am = Math.max(1, am - dt * 2f);

		if (!burning) {
			int i = Level.toIndex((int) Math.floor((this.x + 15) / 16), (int) Math.floor((this.y) / 16));
			int info = Dungeon.level.getInfo(i);

			if (BitHelper.isBitSet(info, 0)) {
				// Burning
				this.damage = 0;
				this.burning = true;

				for (int j : PathFinder.NEIGHBOURS4) {
					Dungeon.level.setOnFire(i + j, true);
				}
			}
		} else {
			InGameState.burning = true;
			Dungeon.level.setOnFire(Level.toIndex((int) Math.floor((this.x + 15) / 16), (int) Math.floor((this.y) / 16)), true);

			this.damage += dt * 0.8f;
			lastFlame += dt;

			if (this.lastFlame >= 0.05f) {
				this.lastFlame = 0;
				TerrainFlameFx fx = new TerrainFlameFx();
				fx.x = this.x + Random.newFloat(this.w);
				fx.y = this.y + Random.newFloat(this.h) - 4;
				fx.depth = 1;
				Dungeon.area.add(fx);
			}

			if (this.damage >= 1f) {
				this.done = true;
				for (int i = 0; i < 10; i++) {
					PoofFx fx = new PoofFx();

					fx.x = this.x + this.w / 2;
					fx.y = this.y + this.h / 2;

					Dungeon.area.add(fx);
				}
			}
		}
	}

	@Override
	public void init() {
		super.init();
	}

	@Override
	public void renderShadow() {
		Graphics.shadow(x, y + 8, w, h, 14);
	}

	@Override
	public void render() {
		float a = (float) (Math.cos(this.t + this.y * 0.1f + this.x * 0.2f) * am * 6);
		Graphics.render(region, this.x + 15, this.y + 2, a, 15, 2, false, false);
	}
}
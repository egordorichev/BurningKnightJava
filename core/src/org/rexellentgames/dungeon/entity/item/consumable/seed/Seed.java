package org.rexellentgames.dungeon.entity.item.consumable.seed;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.item.consumable.Consumable;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.plant.Plant;
import org.rexellentgames.dungeon.util.Tween;

public class Seed extends Consumable {
	protected float added;

	@Override
	public void render(float x, float y, float w, float h, boolean flipped) {
		if (this.added != 0) {
			float angle = (flipped ? this.added : -this.added);
			TextureRegion sprite = this.getSprite();

			Graphics.render(sprite, x + (flipped ? w / 3 : w / 3 * 2), y + h / 4, angle, sprite.getRegionWidth() / 2,
				0, false,
				false);
		}
	}

	@Override
	public void use() {
		super.use();
		this.identify();

		Tween.to(new Tween.Task(120, 0.2f) {
			@Override
			public float getValue() {
				return added;
			}

			@Override
			public void setValue(float value) {
				added = value;
			}

			@Override
			public void onEnd() {
				Tween.to(new Tween.Task(0, 0.1f) {
					@Override
					public float getValue() {
						return added;
					}

					@Override
					public void setValue(float value) {
						added = value;
					}
				});

				endUse();
			}
		});
	}

	@Override
	public void endUse() {
		super.endUse();

		int x = Math.round(this.owner.x / 16) + (this.owner.isFlipped() ? -1 : 1);
		int y = Math.round(this.owner.y / 16);

		if (Dungeon.level.get(x, y) == Terrain.DIRT) {
			Plant plant = this.createPlant();

			if (plant != null) {
				plant.x = x * 16;
				plant.y = y * 16 - 4;

				Dungeon.area.add(plant);
				Dungeon.level.addSaveable(plant);
				Dungeon.level.set(x, y, Terrain.PLANTED_DIRT);

				this.count -= 1;
			}
		}
	}

	protected Plant createPlant() {
		return null;
	}
}

/*
 non-combat utility plants

 - cabbage you can eat or sell
 - bulb that let's you respawn when killed
 - mushroom that produces a little light and allow other plants to grow, albeit slowly
 - exploding shitshroom that makes enemies dirty so they will ignore you and go for a bath in the nearest pond.
 - flower which enemies will pick and bring it to their colleges as a present (all the while ignoring you or walking into your traps)
 - thorns that can expand by themselves and hinder enemies (but they can grow out of hand and become an issue for you too)
 - weed that can expand by itself and is pretty much useless, but can catch wire easily, and make enemies dizzy in the area
 - fruit that spawns a random unidentified potion when harvested
 - arrow grass that can be harvested for well, arrows
 - gold nuts that lure enemies to harvest them, and can be traded in shops
 - crystals that can collect light when lit, and release it when unlit (like the lamp)
 - tome bush that spawns a random scroll when harvested.

 combat / trap

 - snapjaw flower that can trap and kill one regular enemy instantly
 - wines that can trap and kill one regular enemy, unless other enemies are there to cut him free
 - seedpod that can shoot at enemies but it's weak and just serves to aggro them.
 - bomb plant that explodes for damage, but set's things on fire (so you cannot really grow them in large number where enemies are around)
*/
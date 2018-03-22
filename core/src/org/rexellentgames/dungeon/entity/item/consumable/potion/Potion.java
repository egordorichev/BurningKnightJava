package org.rexellentgames.dungeon.entity.item.consumable.potion;

import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.item.ChangableRegistry;
import org.rexellentgames.dungeon.entity.item.consumable.Consumable;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.Tween;

public class Potion extends Consumable {
	protected float added;
	protected ChangableRegistry.Type type;

	{
		useTime = 10f;
	}

	public Potion() {
		this.type = ChangableRegistry.types.get(this.getClass().getSimpleName());
		this.sprite = this.type.getSprite();
		this.identified = ChangableRegistry.identified.get(this.type);
	}

	@Override
	public void render(float x, float y, boolean flipped) {
		if (this.added != 0) {
			float angle = (flipped ? this.added : -this.added);

			Graphics.render(this.getSprite(), x + (flipped ? -3 : 3), y - 4, angle, 8, 10, false,
				false);
		}
	}

	@Override
	public void use() {
		super.use();
		this.identify();

		Tween.to(new Tween.Task(-70, 0.3f) {
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
				count -= 1;
				added = 0;
			}
		});
	}

	@Override
	public String getName() {
		if (this.identified) {
			return super.getName();
		} else {
			String name = this.type.toString().toLowerCase();
			name = name.substring(0, 1).toUpperCase() + name.substring(1);

			return name + " Potion";
		}
	}

	@Override
	public void identify() {
		super.identify();
		ChangableRegistry.identified.put(this.type, true);
	}

	public static Potion random() {
		int random = Random.newInt(8);

		switch (random) {
			case 0: case 1:
				return new RegenerationPotion();
			case 2:
				return new PoisonPotion();
			case 3:
				return new DefensePotion();
			case 4: case 5:
				return new SunPotion();
			case 6:
				return new InvisibilityPotion();
			case 7: case 8: default:
				return new SpeedPotion();
		}
	}
}
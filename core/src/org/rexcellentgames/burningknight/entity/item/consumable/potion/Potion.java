package org.rexcellentgames.burningknight.entity.item.consumable.potion;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.assets.Audio;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.item.ChangableRegistry;
import org.rexcellentgames.burningknight.entity.item.consumable.Consumable;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.Tween;

public class Potion extends Consumable {
	protected float added;
	protected ChangableRegistry.Type type;

	{
		useTime = 10f;
	}

	public Potion() {
		this.onPickup();
	}

	@Override
	public void onPickup() {
		super.onPickup();

		this.type = ChangableRegistry.types.get(this.getClass().getSimpleName());
		this.sprite = this.type.getSprite();
		this.identified = ChangableRegistry.identified.get(this.type);
	}

	@Override
	public boolean isIdentified() {
		if (super.isIdentified()) {
			return true;
		}

		if (ChangableRegistry.identified.get(this.type)) {
			this.identified = true;
			return true;
		}

		return false;
	}

	@Override
	public void render(float x, float y, float w, float h, boolean flipped) {
		if (this.added != 0) {
			float angle = (flipped ? this.added : -this.added);
			TextureRegion sprite = this.getSprite();

			Graphics.render(sprite, x + (flipped ? w / 3 : w / 3 * 2), y + h / 3, angle, sprite.getRegionWidth() / 2,
				sprite.getRegionHeight() - 4, false,
				false);
		}
	}

	@Override
	public void use() {
		super.use();
		this.identify();

		Audio.playSfx("potion");

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
		if (this.isIdentified()) {
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
			case 6: default:
				return new InvisibilityPotion();
		}
	}
}
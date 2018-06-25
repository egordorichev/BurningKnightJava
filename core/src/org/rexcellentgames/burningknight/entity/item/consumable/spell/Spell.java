package org.rexcellentgames.burningknight.entity.item.consumable.spell;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.assets.Audio;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.item.ChangableRegistry;
import org.rexcellentgames.burningknight.entity.item.consumable.Consumable;
import org.rexcellentgames.burningknight.util.Random;

public class Spell extends Consumable {
	protected ChangableRegistry.Type type;

	{
		useTime = 1f;
	}

	public Spell() {
		this.onPickup();
	}

	@Override
	public void onPickup() {
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
		if (this.delay > 0) {
			TextureRegion sprite = this.getSprite();

			Graphics.render(sprite, x + (flipped ? -w / 3 : w / 3), y + h / 3, 0, sprite.getRegionWidth() / 2,sprite.getRegionHeight() / 2,
				!flipped, false);
		}
	}

	@Override
	public void use() {
		Audio.playSfx("scroll");
		this.identify();
		this.count -= 1;
		super.use();
	}

	@Override
	public String getName() {
		if (this.isIdentified()) {
			return super.getName();
		} else {
			String name = this.type.toString().toLowerCase();
			name = name.substring(0, 1).toUpperCase() + name.substring(1);

			return name;
		}
	}

	@Override
	public void identify() {
		super.identify();
		ChangableRegistry.identified.put(this.type, true);
	}

	public static Spell random() {
		int random = Random.newInt(3);

		switch (random) {
			case 0: case 1:
				return new SpellOfTeleportation();
			case 2: default:
				return new SpellOfDamage();
		}
	}
}
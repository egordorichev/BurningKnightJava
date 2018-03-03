package org.rexellentgames.dungeon.entity.item.consumable.spell;

import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.item.ChangableRegistry;
import org.rexellentgames.dungeon.entity.item.consumable.Consumable;

public class Spell extends Consumable {
	protected ChangableRegistry.Type type;

	{
		useTime = 1f;
	}

	public Spell() {
		this.type = ChangableRegistry.types.get(this.getClass().getSimpleName());
		this.sprite = (short) this.type.getSprite();
		this.identified = ChangableRegistry.identified.get(this.type);
	}

	@Override
	public void render(float x, float y, boolean flipped) {
		if (this.delay > 0) {
			Graphics.render(Graphics.items, this.sprite, x + (flipped ? -8 : 8), y, 1, 1, 0, 8,8, !flipped, false);
		}
	}

	@Override
	public void use() {
		this.identify();
		this.count -= 1;
		super.use();
	}

	@Override
	public String getName() {
		if (this.identified) {
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
}
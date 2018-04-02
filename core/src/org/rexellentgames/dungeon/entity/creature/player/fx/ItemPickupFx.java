package org.rexellentgames.dungeon.entity.creature.player.fx;

import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.item.ItemHolder;
import org.rexellentgames.dungeon.game.input.Input;

public class ItemPickupFx extends Entity {
	private String text;
	private ItemHolder item;
	private Player player;

	public ItemPickupFx(ItemHolder item, Player player) {
		this.depth = 10;
		this.text = item.getItem().getName();
		this.item = item;
		this.player = player;

		Graphics.layout.setText(Graphics.medium, this.text);
		this.x = item.x + 8 - Graphics.layout.width / 2;
		this.y = item.y + 16;
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (Input.instance.wasPressed("pickup")) {
			if (this.player.tryToPickup(this.item)) {
				this.done = true;
				this.area.add(new ItemPickedFx("+" + this.text, this));
			}
		}
	}

	@Override
	public void render() {
		Graphics.medium.draw(Graphics.batch, this.text, this.x, this.y);
	}
}
package org.rexellentgames.dungeon.entity.creature.player.fx;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.item.ItemHolder;
import org.rexellentgames.dungeon.game.input.Input;

public class ItemPickupFx extends Entity {
	private String text;
	public ItemHolder item;
	private Player player;

	public ItemPickupFx(ItemHolder item, Player player) {
		this.depth = 10;
		this.text = item.getItem().getName();

		if (this.text == null) {
			this.text = "Missing item name";
		}

		this.item = item;
		this.player = player;

		Graphics.layout.setText(Graphics.medium, this.text);
		this.x = item.x + item.hw / 2 - Graphics.layout.width / 2;
		this.y = item.y + item.hh + 16;
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (Input.instance.wasPressed("pickup")) {
			if (this.player.tryToPickup(this.item)) {
				this.done = true;
				this.area.add(new ItemPickedFx(item));
				Dungeon.level.removeSaveable(item);
			}
		}
	}

	@Override
	public void render() {
		Graphics.medium.draw(Graphics.batch, this.text, this.x, this.y);
	}
}
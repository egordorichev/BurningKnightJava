package org.rexellentgames.dungeon.entity.creature.player.fx;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.entity.item.ItemHolder;

public class ItemPickupFx extends Entity {
	private String text;

	public ItemPickupFx(ItemHolder item, Player player) {
		this.depth = 10;
		this.text = item.getItem().getName();

		GlyphLayout layout = new GlyphLayout(Graphics.medium, this.text);

		this.x = item.x + 8 - layout.width / 2;
		this.y = item.y + 32;
	}

	@Override
	public void render() {
		Graphics.medium.draw(Graphics.batch, this.text, this.x, this.y);
	}
}
package org.rexcellentgames.burningknight.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.game.state.ItemSelectState;

public class StartingItem extends UiButton {
	public Item item;
	public Player.Type type;

	public StartingItem() {
		super("", 0, 0);
	}

	@Override
	public void init() {
		super.init();
		this.w = item.getSprite().getRegionWidth() * 3;
		this.h = item.getSprite().getRegionHeight() * 3;
	}

	@Override
	public void onClick() {
		super.onClick();
		ItemSelectState.pick(this.item, this.type);
	}

	@Override
	public void render() {
		TextureRegion region = item.getSprite();
		float a = (float) (Math.cos(this.y / 12 + Dungeon.time * 6) * (this.mx / this.w * 20));

		Graphics.render(region, this.x, this.y, a, region.getRegionWidth() / 2, region.getRegionHeight() / 2, false, false, this.scale * 2, this.scale * 2);
	}
}
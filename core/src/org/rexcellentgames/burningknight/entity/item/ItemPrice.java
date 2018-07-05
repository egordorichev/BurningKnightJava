package org.rexcellentgames.burningknight.entity.item;

import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;

public class ItemPrice extends Entity {
	public byte price;
	private String text;

	{
		depth = -1;
	}

	@Override
	public void init() {
		super.init();

		this.text = String.valueOf(this.price);
		Graphics.layout.setText(Graphics.medium, text);

		this.w = Graphics.layout.width;
		this.h = Graphics.layout.height * 2;
		this.x -= this.w / 2;
		this.y -= 16;
	}

	@Override
	public void render() {
		super.render();

		Graphics.medium.draw(Graphics.batch, text, this.x, this.y + 16);
	}
}
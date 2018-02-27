package org.rexellentgames.dungeon.entity.creature.player.fx;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.entity.item.ItemHolder;
import org.rexellentgames.dungeon.util.Tween;

public class ItemPickedFx extends Entity {
	private String text;
	private float t = 0;

	public ItemPickedFx(ItemHolder item) {
		Item i = item.getItem();
		this.text = i.getName();

		if (i.getCount() > 1) {
			this.text += " (" + i.getCount() + ")";
		}

		GlyphLayout layout = new GlyphLayout(Graphics.medium, this.text);

		this.x = item.x + 8 - layout.width / 2;
		this.y = item.y + 32;

		this.depth = 10;
		this.tween();
	}

	public ItemPickedFx(String text, Entity fx) {
		this.text = text;
		this.depth = 10;

		this.x = fx.x;
		this.y = fx.y;

		this.tween();
	}

	private void tween() {
		Tween.to(new Tween.Task(5, 1f) {
			@Override
			public float getValue() {
				return t;
			}

			@Override
			public void setValue(float value) {
				t = value;
			}

			@Override
			public void onEnd() {
				done = true;
			}
		});
	}

	@Override
	public void update(float dt) {
		super.update(dt);
	}

	@Override
	public void render() {
		if (this.t % 1f < 0.5f) {
			Graphics.medium.draw(Graphics.batch, this.text, this.x, this.y);
		}
	}
}
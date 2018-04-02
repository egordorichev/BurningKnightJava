package org.rexellentgames.dungeon.entity.creature.player.fx;

import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.entity.item.ItemHolder;
import org.rexellentgames.dungeon.util.Tween;

public class ItemPickedFx extends Entity {
	private String text;
	private float t = 0;
	private float a;

	public ItemPickedFx(ItemHolder item) {
		Item i = item.getItem();
		this.text = i.getName();

		this.a = 1f;

		if (i.getCount() > 1) {
			this.text += " (" + i.getCount() + ")";
		}

		Graphics.layout.setText(Graphics.medium, this.text);
		this.x = item.x + 8 - Graphics.layout.width / 2;
		this.y = item.y + 32;

		this.depth = 10;
		this.tween();
	}

	public ItemPickedFx(String text, Entity fx) {
		this.text = text;
		this.depth = 10;

		this.x = fx.x;
		this.y = fx.y;
		this.a = 1f;

		this.tween();
	}

	private void tween() {
		Tween.to(new Tween.Task(0, 2f) {
			@Override
			public float getValue() {
				return a;
			}

			@Override
			public void setValue(float value) {
				a = value;
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
		this.t += dt;
	}

	@Override
	public void render() {
		Graphics.medium.setColor(1, 1, 1, this.a);
		Graphics.medium.draw(Graphics.batch, this.text, this.x, this.y + this.t * 10);
		Graphics.medium.setColor(1, 1, 1, 1);
	}
}
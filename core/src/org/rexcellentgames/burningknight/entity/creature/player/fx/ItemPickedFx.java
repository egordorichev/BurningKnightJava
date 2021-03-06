package org.rexcellentgames.burningknight.entity.creature.player.fx;

import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.ItemHolder;
import org.rexcellentgames.burningknight.util.Tween;

public class ItemPickedFx extends Entity {
	private String text;
	private float a;

	{
		depth = 15;
		alwaysActive = true;
	}

	public ItemPickedFx(ItemHolder item) {
		Item i = item.getItem();
		this.text = "+" + i.getName();

		this.a = 1f;

		Graphics.layout.setText(Graphics.medium, this.text);
		this.x = item.x + item.w / 2 - Graphics.layout.width / 2;
		this.y = item.y + item.h + 4;

		this.tween();
	}

	private void tween() {
		Tween.to(new Tween.Task(this.y + 10, 2f, Tween.Type.QUAD_OUT) {
			@Override
			public float getValue() {
				return y;
			}

			@Override
			public void setValue(float value) {
				y = value;
			}
		});

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
				setDone(true);
			}
		});
	}

	@Override
	public void render() {
		//Graphics.medium.setColor(1, 1, 1, this.a);
		Graphics.print(this.text, Graphics.medium, this.x, this.y);
		//Graphics.medium.setColor(1, 1, 1, 1);
	}
}
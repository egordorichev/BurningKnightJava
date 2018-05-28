package org.rexellentgames.dungeon.entity.creature.player.fx;

import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.entity.item.ItemHolder;
import org.rexellentgames.dungeon.util.Tween;

public class ItemPickedFx extends Entity {
	private String text;
	private float t = 0;
	private float a;

	{
		depth = 15;
	}

	public ItemPickedFx(ItemHolder item) {
		Item i = item.getItem();
		this.text = "+" + i.getName();

		this.a = 1f;

		Graphics.layout.setText(Graphics.medium, this.text);
		this.x = item.x + item.w / 2 - Graphics.layout.width / 2;
		this.y = item.y + item.h + 4;

		this.tween();
		if (Player.instance.ui != null) {
			Player.instance.ui.forceT = 1f;
		}
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
		Graphics.print(this.text, Graphics.medium, this.x, this.y);
		Graphics.medium.setColor(1, 1, 1, 1);
	}
}
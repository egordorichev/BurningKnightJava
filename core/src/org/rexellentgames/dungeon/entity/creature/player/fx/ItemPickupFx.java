package org.rexellentgames.dungeon.entity.creature.player.fx;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.item.ItemHolder;
import org.rexellentgames.dungeon.entity.level.entities.fx.LadderFx;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.util.Dialog;
import org.rexellentgames.dungeon.util.Tween;

public class ItemPickupFx extends Entity {
	private String text;
	public ItemHolder item;
	private Player player;
	private float a;
	private boolean go;


	{
		depth = 15;
	}

	public ItemPickupFx(ItemHolder item, Player player) {
		this.text = item.getItem().getName();

		if (this.text == null) {
			this.text = "Missing item name";
		}

		this.item = item;
		this.player = player;

		Graphics.layout.setText(Graphics.medium, this.text);
		this.x = item.x + item.w / 2 - Graphics.layout.width / 2;
		this.y = item.y + item.h + 4;
		Tween.to(new Tween.Task(1, 0.1f, Tween.Type.QUAD_OUT) {
			@Override
			public float getValue() {
				return a;
			}

			@Override
			public void setValue(float value) {
				a = value;
			}
		});
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (this.go) {
			return;
		}

		if (Input.instance.wasPressed("pickup") && Dialog.active == null) {
			if (this.player.tryToPickup(this.item)) {
				this.remove();
				this.area.add(new ItemPickedFx(item));
				Dungeon.level.removeSaveable(item);
			}
		} else if (this.item.done) {
			this.remove();
			this.area.add(new ItemPickedFx(item));
			Dungeon.level.removeSaveable(item);
		}
	}

	public void remove() {
		ItemPickupFx self = this;
		this.go = true;
		Tween.to(new Tween.Task(0, 0.2f, Tween.Type.QUAD_IN) {
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
				super.onEnd();

				self.done = true;
			}
		});
	}

	@Override
	public void render() {
		float c = (float) (0.8f + Math.cos(Dungeon.time * 10) / 5f);

		Graphics.medium.setColor(c, c, c, this.a);
		Graphics.print(this.text, Graphics.medium, this.x, this.y);
		Graphics.medium.setColor(1, 1, 1, 1);
	}
}
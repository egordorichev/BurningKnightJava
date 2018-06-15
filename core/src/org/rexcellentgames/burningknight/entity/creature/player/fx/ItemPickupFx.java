package org.rexcellentgames.burningknight.entity.creature.player.fx;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.ItemHolder;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.util.Dialog;
import org.rexcellentgames.burningknight.util.Tween;

public class ItemPickupFx extends Entity {
	private String text;
	public ItemHolder item;
	private Player player;
	private float a;
	private boolean go;

	{
		depth = 15;
		alwaysActive = true;
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
				LevelSave.remove(item);
			}
		} else if (this.item.done) {
			this.remove();
			this.area.add(new ItemPickedFx(item));
			LevelSave.remove(item);
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
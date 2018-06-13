package org.rexcellentgames.burningknight.entity.creature.npc;

import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.util.Dialog;

public class Npc extends Creature {
	private boolean active;
	private NpcDialog dialog;

	@Override
	public void update(float dt) {
		super.update(dt);

		if (Player.instance != null && Dialog.active == null) {
			float d = Player.instance.getDistanceTo(this.x + this.w / 2, this.y + this.h / 2);

			if (d < 32f && !this.active) {
				this.active = true;
				String s = "Hi!\nHow are you doing?\nWanna something?";

				if (this.dialog == null || this.dialog.done) {
					this.dialog = new NpcDialog(this, s);
					Dungeon.area.add(dialog);
				} else {
					this.dialog.setMessage(s);
					this.dialog.open();
				}
			} else if (d >= 48f && this.active) {
				this.active = false;
				this.dialog.remove();
			}
		}
	}

	@Override
	public void render() {
		Graphics.render(Item.missing, this.x, this.y);
	}

	@Override
	public void renderShadow() {
		Graphics.shadow(this.x, this.y, this.w, this.h);
	}
}
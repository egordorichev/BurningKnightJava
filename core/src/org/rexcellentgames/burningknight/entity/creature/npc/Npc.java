package org.rexcellentgames.burningknight.entity.creature.npc;

import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.item.Item;

public class Npc extends Mob {
	{
		hp = 10;
		hpMax = 10;
		friendly = true;
	}

	@Override
	public void init() {
		super.init();
		friendly = true;
		depth = 0;
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		/*
		if (Player.instance != null && Dialog.activeController == null) {
			float d = Player.instance.getDistanceTo(this.x + this.w / 2, this.y + this.h / 2);

			if (d < 32f && !this.activeController) {
				this.activeController = true;
				String s = "Hi!\nHow are you doing?\nWanna something?";

				if (this.dialog == null || this.dialog.done) {
					this.dialog = new NpcDialog(this, s);
					Dungeon.area.add(dialog);
				} else {
					this.dialog.setMessage(s);
					this.dialog.open();
				}
			} else if (d >= 48f && this.activeController) {
				this.activeController = false;
				this.dialog.remove();
			}
		}*/

		super.common();
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
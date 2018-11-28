package org.rexcellentgames.burningknight.entity.creature.npc;

import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.util.Dialog;
import org.rexcellentgames.burningknight.util.DialogData;

public class Npc extends Mob {
	{
		hp = 10;
		hpMax = 10;
		friendly = true;
	}

	protected float al;
	protected boolean talking;
	protected boolean lastWhite;
	public static Npc active;

	@Override
	public void init() {
		super.init();
		friendly = true;
		depth = 0;
	}

	protected DialogData selectDialog() {
		return null;
	}

	protected void setupDialog(DialogData dialog) {

	}


	public class TalkDialogState extends Mob.State {

	}

	public boolean wantToTalk() {
		return true;
	}

	@Override
	protected State getAi(String state) {
		if (state.equals("talk_dialog")) {
			return new TalkDialogState();
		}

		return super.getAi(state);
	}

	@Override
	public void update(float dt) {
		super.update(dt);


		lastWhite = (active == null && Upgrade.Companion.getActiveUpgrade() == null && !this.talking && Player.instance.pickupFx == null && Player.instance.room == this.room && wantToTalk() && Player.instance.getDistanceTo(this.x + this.w / 2, this.y + this.h / 2) < 10);

		if (lastWhite && Input.instance.wasPressed("interact")) {
			Dialog.active = selectDialog();

			if (Dialog.active == null) {
				return;
			}

			active = this;
			talking = true;
			Camera.follow(this, false);
			this.become("talk_dialog");

			Dialog.active.start();
			setupDialog(Dialog.active);

			Dialog.active.onEnd(new Runnable() {
				@Override
				public void run() {
					talking = false;
					active = null;
					Camera.follow(Player.instance, false);
					become("idle");
				}
			});
		}

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
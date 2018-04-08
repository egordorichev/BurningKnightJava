package org.rexellentgames.dungeon.entity.creature.player;

import org.rexellentgames.dungeon.entity.creature.buff.InvisibilityBuff;
import org.rexellentgames.dungeon.util.Log;

public class GhostPlayer extends Player {
	/*@Override
	public void init() {
		super.init();
		this.ghost = true;
		this.alwaysActive = true;
	}

	@Override
	public void onHurt() {
		super.onHurt();

		Log.info(this.hp + " " + Player.instance + " " + this);

		if (this.hp <= 0 && Player.instance != null) {
			Log.info("remove");
			Player.instance.removeBuff(InvisibilityBuff.class);
		}
	}

	private boolean tpd;

	@Override
	public void update(float dt) {
		if (!this.tpd) {
			tpd = true;
			tp(Player.instance.x, Player.instance.y);
		}

		super.update(dt);
	}

	@Override
	public void render() {
		super.render();
	}*/

	// todo
}
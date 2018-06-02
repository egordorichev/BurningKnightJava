package org.rexellentgames.dungeon.entity.level.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.level.entities.fx.WellFx;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.Random;

public class MagicWell extends UsableProp {
	private static TextureRegion[] water = new TextureRegion[] {
		Graphics.getTexture("prop (well_water_none)"),
		Graphics.getTexture("prop (well_water_heal)")
	};

	{
		sprite = "prop (tub)";
		collider = new Rectangle(4, 5, 30 - 8, 12);
	}

	private boolean s;

	@Override
	public boolean use() {
		this.used = true;

		// todo: particles

		int r = Random.newInt(3);

		switch (r) {
			case 0: default:
				Player.instance.modifyHp(Player.instance.getHpMax() - Player.instance.getHp(), null);
				Log.info("[green]You take a sip and feel refreshed!");
				break;
			/*case 1:

				break;
			case 2:
				break;*/
		}
		return true;
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (!s) {
			s = true;
			for (int x = (int) (this.x / 16); x < Math.ceil((this.x + 32) / 16); x++) {
				Dungeon.level.setPassable(x, (int) ((this.y + 11) / 16), false);
				Dungeon.level.setPassable(x, (int) ((this.y) / 16), false);
			}
		}
	}

	private WellFx fx;

	@Override
	public void onCollision(Entity entity) {
		if (entity instanceof Player && !this.used) {
			this.fx = new WellFx(this, "take_a_sip");
			Dungeon.area.add(fx);
		}
	}

	@Override
	public void onCollisionEnd(Entity entity) {
		if (this.fx != null && entity instanceof Player) {
			this.fx.remove();
			this.fx = null;
		}
	}

	@Override
	public void render() {
		super.render();

		// FIXME!
		// Graphics.render(water[this.used ? 0 : 1], this.x + 5, this.y - 8 - 0.3f, 0, 0, 0, false, false, 1, -1f);

		// Graphics.render(water[this.used ? 0 : 1], this.x + 5, this.y + 8);
	}
}
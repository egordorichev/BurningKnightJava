package org.rexellentgames.dungeon.entity.level.entities;

import com.badlogic.gdx.math.Rectangle;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.entity.item.weapon.WeaponBase;
import org.rexellentgames.dungeon.entity.level.entities.fx.WellFx;
import org.rexellentgames.dungeon.ui.UiLog;

public class WeaponAltar extends UsableProp {
	{
		sprite = "biome-0 (slab C)";
		collider = new Rectangle(1, 10, 26, 6);
	}

	private boolean s;

	@Override
	public void update(float dt) {
		super.update(dt);

		if (!s) {
			s = true;
			Dungeon.level.setPassable((int) (this.x / 16), (int) ((this.y + 8) / 16), false);
		}
	}

	private WellFx fx;

	@Override
	public boolean use() {
		Item item = Player.instance.getInventory().getSlot(Player.instance.getInventory().active);

		if (!(item instanceof WeaponBase)) {
			UiLog.instance.print("[orange]Item must be a weapon!");
			return false;
		}

		return true;
	}

	@Override
	public void onCollision(Entity entity) {
		if (entity instanceof Player && !this.used) {
			this.fx = new WellFx(this, "bless_a_weapon");
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
}
package org.rexellentgames.dungeon.entity.item.weapon.ranged;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.game.input.Input;

public class Bow extends Item {
	protected int damage;

	{
		auto = true;
		identified = true;
	}

	@Override
	public void use() {
		if (!(this.owner instanceof Player)) {
			return;
		}

		float a = (float) (this.owner.getAngleTo(Input.instance.worldMouse.x, Input.instance.worldMouse.y) - Math.PI);
		float s = 60f;

		this.owner.vel.x += Math.cos(a) * s;
		this.owner.vel.y += Math.sin(a) * s;

		Player player = (Player) this.owner;

		if (!player.getInventory().find(Arrow.class)) {
			return;
		}

		player.getInventory().remove(Arrow.class);
		super.use();

		ArrowEntity arrow = new ArrowEntity();

		arrow.owner = this.owner;

		float dx = Input.instance.worldMouse.x - this.owner.x - this.owner.w / 2;
		float dy = Input.instance.worldMouse.y - this.owner.y - this.owner.h / 2;

		arrow.a = (float) Math.atan2(dy, dx);
		arrow.x = (float) (this.owner.x + this.owner.w / 2 + Math.cos(arrow.a) * 16);
		arrow.y = (float) (this.owner.y + this.owner.h / 2 + Math.sin(arrow.a) * 16);
		arrow.damage = this.damage;

		Dungeon.area.add(arrow);
	}
}
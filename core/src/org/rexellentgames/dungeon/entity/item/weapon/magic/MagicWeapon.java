package org.rexellentgames.dungeon.entity.item.weapon.magic;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.creature.Creature;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.game.input.Input;

public class MagicWeapon extends Item {
	protected int damage = 1;
	protected float mana;
	protected Player owner;

	{
		identified = true;
	}

	public void setOwner(Creature owner) {
		this.owner = (Player) owner;
	}

	@Override
	public void render(float x, float y, float w, float h, boolean flipped) {
		float dx = Input.instance.worldMouse.x - this.owner.x - this.owner.w / 2;
		float dy = Input.instance.worldMouse.y - this.owner.y - this.owner.h / 2;
		double a = Math.atan2(dy, dx);

		TextureRegion s = this.getSprite();

		Graphics.render(s, x + w / 2, y + h / 4, (float) Math.toDegrees(a) - 90, s.getRegionWidth() / 2, 0, false, false);
	}

	@Override
	public void use() {
		if (this.owner.getMana() < this.mana) {
			return;
		}

		super.use();

		this.owner.modifyMana(-this.mana);
	}

	@Override
	public StringBuilder buildInfo() {
		StringBuilder builder = super.buildInfo();

		builder.append("\nUses ");
		builder.append((int) this.mana);
		builder.append(" mana\n");
		builder.append(this.damage);
		builder.append(" damage\n");

		return builder;
	}
}
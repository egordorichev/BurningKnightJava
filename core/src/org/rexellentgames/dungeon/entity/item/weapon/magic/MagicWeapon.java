package org.rexellentgames.dungeon.entity.item.weapon.magic;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexellentgames.dungeon.entity.creature.Creature;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.item.ChangableRegistry;
import org.rexellentgames.dungeon.entity.item.weapon.WeaponBase;
import org.rexellentgames.dungeon.entity.item.weapon.gun.Gun;
import org.rexellentgames.dungeon.util.geometry.Point;

public class MagicWeapon extends WeaponBase {
	protected int damage = 1;
	protected float mana;
	protected Player owner;
	protected ChangableRegistry.Type type;

	public MagicWeapon() {
		this.onPickup();
		this.identified = true;
	}

	public void setOwner(Creature owner) {
		this.owner = (Player) owner;
	}

	@Override
	public void onPickup() {
		this.type = ChangableRegistry.types.get(this.getClass().getSimpleName());
		this.sprite = this.type.getSprite();
		this.identified = ChangableRegistry.identified.get(this.type);
	}

	private float lastAngle;

	@Override
	public void render(float x, float y, float w, float h, boolean flipped) {
		if (this.owner != null) {
			Point aim = this.owner.getAim();

			float an = (float) (this.owner.getAngleTo(aim.x, aim.y) - Math.PI / 2);
			an = Gun.angleLerp(this.lastAngle, an, 0.15f);
			this.lastAngle = an;
		}

		TextureRegion s = this.getSprite();

		this.renderAt(x + w / 2, y + h / 4, (float) Math.toDegrees(this.lastAngle), s.getRegionWidth() / 2, 0, false, false);
	}

	@Override
	public void use() {
		if (this.owner.getMana() < this.mana) {
			return;
		}

		super.use();
		this.identify();

		this.owner.modifyMana(-this.mana);
	}

	@Override
	public StringBuilder buildInfo() {
		StringBuilder builder = super.buildInfo();

		builder.append("\n[blue]Uses ");
		builder.append((int) this.mana);
		builder.append(" mana[gray]\n");
		builder.append(this.damage);
		builder.append(" damage\n");

		return builder;
	}
}
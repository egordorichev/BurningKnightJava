package org.rexcellentgames.burningknight.entity.item.weapon.magic;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.weapon.WeaponBase;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.Gun;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class Wand extends WeaponBase {
	protected int mana = 1;
	protected Player owner;

	public void setOwner(Creature owner) {
		super.setOwner(owner);
		this.owner = (Player) owner;
	}

	protected float lastAngle;

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
		this.owner.modifyMana(-this.mana);
		this.sendProjectiles();
	}

	protected void sendProjectiles() {
		float a = (float) Math.toDegrees(this.lastAngle);
		float h = this.region.getRegionHeight();
		double an = this.lastAngle + Math.PI / 2;

		this.spawnProjectile(this.owner.x + this.owner.w / 2 + h * (float) Math.cos(an),
			this.owner.y + this.owner.h / 2 + h * (float) Math.sin(an), a + 90);
	}

	public void spawnProjectile(float x, float y, float a) {

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
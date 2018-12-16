package org.rexcellentgames.burningknight.entity.item.weapon.magic;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.weapon.WeaponBase;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.Gun;
import org.rexcellentgames.burningknight.util.Tween;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class Wand extends WeaponBase {
	{
		useTime = 0.15f;
	}

	protected int mana = 1;
	protected Player owner;

	public void setOwner(Creature owner) {
		super.setOwner(owner);

		if (owner instanceof Player) {
			this.owner = (Player) owner;
		}
	}

	protected float lastAngle;

	@Override
	public void render(float x, float y, float w, float h, boolean flipped) {
		if (this.owner != null) {
			Point aim = this.owner.getAim();

			float an = (float) (this.owner.getAngleTo(aim.x, aim.y) - Math.PI / 2);
			an = Gun.angleLerp(this.lastAngle, an, 0.15f, this.owner != null && this.owner.freezed);
			this.lastAngle = an;
		}

		TextureRegion s = this.getSprite();

		this.renderAt(x + w / 2, y + h / 4, (float) Math.toDegrees(this.lastAngle), s.getRegionWidth() / 2, 0, false, false, sx, sy);
	}

	protected float sy = 1;
	protected float sx = 1;

	public int getManaUsage() {
		return (int) Math.max(1, this.mana - this.level + 1);
	}

	@Override
	public void use() {
		int mn = getManaUsage();

		if (this.owner.getMana() < mn) {
			return;
		}

		this.owner.playSfx("fireball_cast");

		super.use();
		this.owner.modifyMana(-mn);
		this.sendProjectiles();

		Point aim = this.owner.getAim();
		float a = (float) (this.owner.getAngleTo(aim.x, aim.y) - Math.PI * 2);
		Camera.push(a, 8f);

		sx = 2f;
		sy = 0.5f;

		Tween.to(new Tween.Task(1f, 0.2f) {
			@Override
			public float getValue() {
				return sy;
			}

			@Override
			public void setValue(float value) {
				sy = value;
			}
		});

		Tween.to(new Tween.Task(1f, 0.2f) {
			@Override
			public float getValue() {
				return sx;
			}

			@Override
			public void setValue(float value) {
				sx = value;
			}
		});
	}

	protected void sendProjectiles() {
		float a = (float) Math.toDegrees(this.lastAngle);
		float h = this.region.getRegionHeight();
		double an = this.lastAngle + Math.PI / 2;

		this.owner.knockback.x -= Math.cos(an) * 40f;
		this.owner.knockback.y -= Math.sin(an) * 40f;

		this.spawnProjectile(this.owner.x + this.owner.w / 2 + h * (float) Math.cos(an),
			this.owner.y + this.owner.h / 4 + h * (float) Math.sin(an), a + 90);
	}

	public void spawnProjectile(float x, float y, float a) {

	}

	@Override
	public StringBuilder buildInfo() {
		StringBuilder builder = super.buildInfo();

		builder.append("\n[blue]Uses ");
		builder.append( getManaUsage());
		builder.append(" mana[gray]");

		return builder;
	}
}
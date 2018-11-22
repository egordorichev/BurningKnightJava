package org.rexcellentgames.burningknight.entity.item.weapon.laser;

import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.fx.Laser;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.Revolver;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.game.input.Input;

public class LaserGun extends Revolver {
	{
		useTime = 1f;
		damage = 4;
		hole.y -= 1.5f;
		hole.x += 2f;
	}

	private Laser laser;
	private float last;
	private boolean inHands;

	@Override
	public void updateInHands(float dt) {
		super.updateInHands(dt);

		inHands = true;

		if (laser != null) {
			if (ammoLeft > 0 && !laser.dead) {
				last += dt;

				if (last >= 0.2f) {
					last = 0;
					// FIXME
					// ammoLeft -= 1;

					if (ammoLeft == 0) {
						laser.remove();
					}
				}
			}

			// FIXME: wont detect key up
			if (!Input.instance.isDown("use") || Player.instance.isRolling()) {
				laser.remove();
				endUse();
			}

			double an = this.lastAngle;

			float x = this.owner.x + this.owner.w / 2 + (this.owner.isFlipped() ? -7 : 7);
			float y = this.owner.y + this.owner.h / 4 + this.owner.z;
			float xx = x + getAimX(0, 0);
			float yy = y + getAimY(0, 0);

			laser.x = xx;
			laser.y = yy;
			laser.a = (float) Math.toDegrees(an - Math.PI / 2);
			laser.recalc();

			if (laser.done || laser.dead) {
				laser = null;
			}
		}
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (!inHands && laser != null) {
			laser.remove();
		}

		inHands = false;
	}

	@Override
	protected void sendBullets() {
		this.owner.playSfx("laser");

		Laser laser = new Laser();
		float x = this.owner.x + this.owner.w / 2 + (this.owner.isFlipped() ? -7 : 7);
		float y = this.owner.y + this.owner.h / 4 + this.owner.z;
		float xx = x + getAimX(0, 0);
		float yy = y + getAimY(0, 0);
		double an = this.owner.getAngleTo(this.owner.getAim().x, this.owner.getAim().y);

		laser.x = xx;
		laser.y = yy;
		laser.huge = false;

		laser.a = (float) Math.toDegrees(an - Math.PI / 2);
		laser.w = 32f;
		laser.damage = this.rollDamage();
		laser.crit = this.lastCrit;
		laser.owner = this.owner;

		this.laser = laser;

		Dungeon.area.add(laser);
	}
}
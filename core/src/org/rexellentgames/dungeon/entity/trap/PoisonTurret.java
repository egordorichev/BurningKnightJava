package org.rexellentgames.dungeon.entity.trap;

import org.rexellentgames.dungeon.entity.creature.buff.PoisonBuff;
import org.rexellentgames.dungeon.entity.item.weapon.gun.bullet.BulletEntity;

public class PoisonTurret extends Turret {
	@Override
	protected void modify(BulletEntity entity) {
		super.modify(entity);

		entity.toApply = PoisonBuff.class;
	}
}
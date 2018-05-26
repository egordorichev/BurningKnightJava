package org.rexellentgames.dungeon.entity.trap;

import org.rexellentgames.dungeon.entity.creature.buff.FreezeBuff;
import org.rexellentgames.dungeon.entity.item.weapon.gun.bullet.BulletEntity;

public class IceTurret extends Turret {
	@Override
	protected void modify(BulletEntity entity) {
		super.modify(entity);

		entity.toApply = FreezeBuff.class;
	}
}
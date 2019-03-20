
package org.rexcellentgames.burningknight.entity.creature.buff;

public class FrozenBuff extends Buff {
	{
		id = Buffs.FROZEN;
		name = "Frozen";
		duration = 5f;
	}

	@Override
	public void onStart() {
		this.owner.freezed = true;
		this.owner.removeBuff(Buffs.BURNING);
	}

	@Override
	public void onEnd() {
		this.owner.freezed = false;
	}
}
package org.rexcellentgames.burningknight.entity.item.consumable.potion;

import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.util.file.FileReader;

import java.io.IOException;

public class HealingPotion extends Potion {
	{
		
		
	}

	@Override
	public void onPickup() {
		super.onPickup();

		if (!this.identified && Player.instance != null && Player.instance.getType() == Player.Type.WARRIOR) {
			this.identify();
		}
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);

		if (!this.identified && Player.instance != null && Player.instance.getType() == Player.Type.WARRIOR) {
			this.identify();
		}
	}

	@Override
	public void use() {
		if (this.owner.getHpMax() == this.owner.getHp()) {
			return;
		}

		super.use();
		this.owner.modifyHp(this.owner.getHpMax() - this.owner.getHp(), null);
	}
}
package org.rexellentgames.dungeon.entity.item.consumable.potion;

import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.util.file.FileReader;

import java.io.IOException;

public class HealingPotion extends Potion {
	{
		name = Locale.get("healing_potion");
		description = Locale.get("healing_potion_desc");
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
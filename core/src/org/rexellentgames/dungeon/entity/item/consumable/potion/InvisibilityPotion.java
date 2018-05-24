package org.rexellentgames.dungeon.entity.item.consumable.potion;

import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.creature.buff.InvisibilityBuff;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.util.file.FileReader;

import java.io.IOException;

public class InvisibilityPotion extends Potion {
	{
		name = Locale.get("invis_potion");
		description = Locale.get("invis_potion_desc");
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);

		if (!this.identified && Player.instance.getType() == Player.Type.ROGUE) {
			this.identify();
		}
	}

	@Override
	public void init() {
		super.init();

		if (!this.identified && Player.instance.getType() == Player.Type.ROGUE) {
			this.identify();
		}
	}

	@Override
	public void use() {
		super.use();
		this.owner.addBuff(new InvisibilityBuff());
	}
}
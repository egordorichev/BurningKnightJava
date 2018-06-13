package org.rexcellentgames.burningknight.entity.item.consumable.potion;

import org.rexcellentgames.burningknight.entity.creature.buff.InvisibilityBuff;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.buff.InvisibilityBuff;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.util.file.FileReader;

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
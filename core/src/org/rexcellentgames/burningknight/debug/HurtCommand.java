package org.rexcellentgames.burningknight.debug;

import org.jetbrains.annotations.NotNull;
import org.rexcellentgames.burningknight.entity.creature.player.Player;

public class HurtCommand extends ConsoleCommand {
	public HurtCommand() {
		super("/hurt", "/h", "Ouch");
	}

	@Override
	public void run(@NotNull Console console, @NotNull String[] args) {
		super.run(console, args);
		Player.instance.modifyHp(-1, null, true);
	}
}
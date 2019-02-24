package org.rexcellentgames.burningknight.debug;

import org.rexcellentgames.burningknight.entity.creature.player.Player;

public class GodModeCommand extends ConsoleCommand {
	{
		name = "/gm";
		shortName = "/g";
	}

	@Override
	public void run(Console console, String[] args) {
		Player.instance.setUnhittable(!Player.instance.isUnhittable());
		console.print(Player.instance.isUnhittable() ? "[green]God mode on" : "[red]God mode off");
	}
}
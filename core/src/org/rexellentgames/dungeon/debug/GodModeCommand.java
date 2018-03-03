package org.rexellentgames.dungeon.debug;

import org.rexellentgames.dungeon.UiLog;
import org.rexellentgames.dungeon.entity.creature.player.Player;

public class GodModeCommand extends ConsoleCommand {
	{
		name = "/godmode";
		shortName = "/gm";
		description = "makes you unkillable";
	}

	@Override
	public void run(Console console, String[] args) {
		Player.instance.setUnhittable(!Player.instance.isUnhittable());

		if (Player.instance.isUnhittable()) {
			UiLog.instance.print("[green]You are now unkillable!");
		} else {
			UiLog.instance.print("[red]You are now killable!");
		}
	}
}
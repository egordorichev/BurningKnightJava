package org.rexellentgames.dungeon.debug;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.UiLog;

public class LevelCommand extends ConsoleCommand {
	{
		name = "/level";
		shortName = "/lvl";
		description = "Switches to given depth";
	}

	@Override
	public void run(Console console, String[] args) {
		if (args.length == 1) {
			Dungeon.goToLevel(Integer.valueOf(args[0]));
		} else {
			UiLog.instance.print("/level [id]");
		}
	}
}
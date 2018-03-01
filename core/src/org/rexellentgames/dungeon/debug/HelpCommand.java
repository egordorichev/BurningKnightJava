package org.rexellentgames.dungeon.debug;

import org.rexellentgames.dungeon.UiLog;

public class HelpCommand extends ConsoleCommand {
	{
		name = "/help";
		shortName = "/h";
		description = "prints help";
	}

	@Override
	public void run(Console console, String[] args) {
		for (ConsoleCommand command : console.getCommands()) {
			UiLog.instance.print(command.getName() + " " + command.getDescription());
		}
	}
}
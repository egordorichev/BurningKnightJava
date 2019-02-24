package org.rexcellentgames.burningknight.debug;

import org.rexcellentgames.burningknight.entity.level.Level;

public class RoomDebugCommand extends ConsoleCommand {
	{
		name = "/room";
		shortName = "/r";
	}

	@Override
	public void run(Console console, String[] args) {
		Level.RENDER_ROOM_DEBUG = !Level.RENDER_ROOM_DEBUG;
	}
}
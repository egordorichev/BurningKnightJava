package org.rexcellentgames.burningknight.debug;

import org.jetbrains.annotations.NotNull;
import org.rexcellentgames.burningknight.entity.level.Level;

public class RoomDebugCommand extends ConsoleCommand {
	public RoomDebugCommand() {
		super("/room", "/r", "Some room debug");
	}

	@Override
	public void run(@NotNull Console console, @NotNull String[] args) {
		Level.RENDER_ROOM_DEBUG = !Level.RENDER_ROOM_DEBUG;
	}
}
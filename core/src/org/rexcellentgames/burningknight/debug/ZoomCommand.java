package org.rexcellentgames.burningknight.debug;


import com.badlogic.gdx.Gdx;
import org.jetbrains.annotations.NotNull;
import org.rexcellentgames.burningknight.Display;

public class ZoomCommand extends ConsoleCommand {
	public ZoomCommand() {
		super("/zoom", "/z", "Zoom [int]");
	}

	@Override
	public void run(@NotNull Console console, @NotNull String[] args) {
		super.run(console, args);

		if (args.length < 1) {
			return;
		}

		int zoom = Math.max(0, Integer.valueOf(args[0]));

		Gdx.graphics.setWindowedMode(Display.GAME_WIDTH * zoom, Display.GAME_HEIGHT * zoom);
	}
}
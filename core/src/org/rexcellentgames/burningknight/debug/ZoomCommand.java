package org.rexcellentgames.burningknight.debug;

import com.badlogic.gdx.Gdx;
import org.rexcellentgames.burningknight.Display;

public class ZoomCommand extends ConsoleCommand {
	{
		name = "/zoom";
		shortName = "/z";
	}

	@Override
	public void run(Console console, String[] args) {
		if (args.length == 0) {
			return;
		}

		float zoom = Math.max(0, Float.valueOf(args[0]));
		Gdx.graphics.setWindowedMode((int) (Display.GAME_WIDTH * zoom), (int) (Display.GAME_HEIGHT * zoom));
	}
}
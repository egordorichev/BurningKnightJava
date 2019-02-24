package org.rexcellentgames.burningknight.debug;

import com.badlogic.gdx.graphics.Color;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.level.entities.Entrance;

public class LevelCommand extends ConsoleCommand {
	{
		name = "/lvl";
		shortName = "/l";
	}

	@Override
	public void run(Console console, String[] args) {
		if (args.length == 1) {
			Player.instance.setUnhittable(true);
			Camera.follow(null);

			Dungeon.loadType = Entrance.LoadType.GO_DOWN;
			Dungeon.goToLevel(Integer.valueOf(args[0]));
			Dungeon.setBackground2(new Color(0f, 0f, 0f, 1f));
		}
	}
}
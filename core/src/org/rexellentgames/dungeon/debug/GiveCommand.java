package org.rexellentgames.dungeon.debug;

import org.rexellentgames.dungeon.UiLog;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.entity.item.ItemHolder;
import org.rexellentgames.dungeon.entity.item.ItemRegistry;

import java.lang.reflect.Constructor;

public class GiveCommand extends ConsoleCommand {
	{
		shortName = "/gv";
		name = "/give";
		description = "[item] (count) gives an item";
	}

	@Override
	public void run(Console console, String[] args) {
		int count = 1;

		if (args.length == 2) {
			count = Integer.valueOf(args[1]);
		}

		if (args.length < 3) {
			String name = args[0];

			try {
				Class<?> clazz = ItemRegistry.items.get(name);

				if (clazz == null) {
					UiLog.instance.print("[red]Unknown item");
					return;
				}

				Constructor<?> constructor = clazz.getConstructor();
				Item item = (Item) constructor.newInstance(new Object[] { });
				ItemHolder itemHolder = new ItemHolder();
				itemHolder.setItem(item);

				if (item.isStackable()) {
					item.setCount(count);
				}

				Player.instance.getInventory().add(itemHolder);
			} catch (Exception e) {
				UiLog.instance.print("[red]Failed to create item, consult @egordorichev");
				e.printStackTrace();
			}
		} else {
			UiLog.instance.print("/give [item] (count)");
		}
	}
}
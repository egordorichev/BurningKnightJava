package org.rexcellentgames.burningknight.debug;

import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.ItemHolder;
import org.rexcellentgames.burningknight.entity.item.ItemRegistry;

public class GiveCommand extends ConsoleCommand {
	{
		shortName = "/gv";
		name = "/give";
	}

	@Override
	public void run(Console console, String[] args) {
		int count = 1;

		if (args.length == 2) {
			count = Integer.valueOf(args[1]);
		}

		if (args.length > 0 && args.length < 3) {
			String name = args[0];
			Item item;

			try {
				ItemRegistry.Pair clazz = ItemRegistry.INSTANCE.getItems().get(name);

				if (clazz == null) {
					console.print("[red]Unknown item $name");
					return;
				}

				item = clazz.getType().newInstance();

				if (item.isStackable()) {
					item.setCount(count);
				}

				ItemHolder itemHolder = new ItemHolder();
				itemHolder.setItem(item);

				Player.instance.tryToPickup(itemHolder);
				console.print("[green]Gave " + name + " (" + count + ")");
			} catch (Exception e) {
				console.print("[red]Failed to create item");
				e.printStackTrace();
			}
		}
	}
}
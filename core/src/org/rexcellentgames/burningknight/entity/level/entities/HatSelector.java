package org.rexcellentgames.burningknight.entity.level.entities;

import org.rexcellentgames.burningknight.entity.creature.npc.Upgrade;
import org.rexcellentgames.burningknight.entity.item.ItemHolder;
import org.rexcellentgames.burningknight.entity.item.ItemRegistry;

import java.util.ArrayList;
import java.util.Map;

public class HatSelector extends ItemHolder {
	public static ArrayList<HatSelector> all = new ArrayList<>();

	@Override
	public void init() {
		super.init();
		all.add(this);
	}

	@Override
	public void destroy() {
		super.destroy();
		all.remove(this);
	}

	private boolean show;
	public String id;
	private boolean set;

	@Override
	public void render() {
		super.render();

		if (!set && this.getItem() == null) {
			set = true;

			for (Map.Entry<String, ItemRegistry.Pair> pair : ItemRegistry.INSTANCE.getItems().entrySet()) {
				if (!pair.getValue().getShown() && pair.getValue().getPool() == Upgrade.Type.DECOR) {
					this.id = pair.getKey();

					try {
						this.setItem(ItemRegistry.INSTANCE.getItems().get(this.id).getType().newInstance());
					} catch (InstantiationException | IllegalAccessException e) {
						e.printStackTrace();
					}

					show = true;
					break;
				}
			}
		}
	}
}
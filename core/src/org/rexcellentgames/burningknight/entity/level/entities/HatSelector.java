package org.rexcellentgames.burningknight.entity.level.entities;

import org.jetbrains.annotations.NotNull;
import org.rexcellentgames.burningknight.entity.creature.npc.Upgrade;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.ItemHolder;
import org.rexcellentgames.burningknight.entity.item.ItemRegistry;
import org.rexcellentgames.burningknight.entity.item.NullItem;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;

import java.io.IOException;
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
		if (this.getItem() instanceof NullItem) {
			nullGot = false;
		}

		if (pair != null) {
			pair.shown = false;
		}

		all.remove(this);
		super.destroy();
	}

	public boolean set;

	public static boolean nullGot;
	private ItemRegistry.Pair pair;
	private String key;

	@Override
	public void render() {
		super.render();

		if ((!set && this.getItem() == null) || Upgrade.Companion.getUpdateEvent()) {
			for (Map.Entry<String, ItemRegistry.Pair> pair : ItemRegistry.items.entrySet()) {
				if (!pair.getValue().shown && pair.getValue().pool == Upgrade.Type.DECOR && !pair.getKey().equals(this.key) && pair.getValue().unlocked(pair.getKey())) {
					pair.getValue().shown = true;
					this.pair = pair.getValue();
					key = pair.getKey();

					try {
						this.setItem((Item) ItemRegistry.items.get(pair.getKey()).type.newInstance());
					} catch (InstantiationException | IllegalAccessException e) {
						e.printStackTrace();
					}

					set = true;
					break;
				}
			}

			if (!set && !nullGot && (Player.hatId != null &&
				Player.hatId.equals("gobbo_head"))) {
				set = true;
				key = "null";
				this.setItem(new NullItem());
				nullGot = true;
			}
		}
	}

	@Override
	public void save(@NotNull FileWriter writer) throws IOException {
		super.save(writer);
		writer.writeString(key);
	}

	@Override
	public void load(@NotNull FileReader reader) throws IOException {
		super.load(reader);
		key = reader.readString();
	}

	@Override
	public void renderShadow() {
		if (set) {
			super.renderShadow();
		}
	}
}
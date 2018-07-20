package org.rexcellentgames.burningknight.entity.level.entities.chest;

import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.ItemRegistry;
import org.rexcellentgames.burningknight.entity.item.accessory.Accessory;
import org.rexcellentgames.burningknight.entity.item.weapon.WeaponBase;
import org.rexcellentgames.burningknight.entity.pool.Pool;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;

public class IronChest extends Chest {
	public static Animation animation = Animation.make("chest", "-iron");
	private static AnimationData closed = animation.get("idle");
	private static AnimationData open = animation.get("opening");
	private static AnimationData openend = animation.get("open");

	@Override
	public Item generate() {
		Pool<Item> pool = new Pool<>();

		for (ItemRegistry.Pair item : ItemRegistry.INSTANCE.getItems().values()) {
			if (item.getQuality() == ItemRegistry.Quality.IRON && item.getType().isAssignableFrom(weapon ? WeaponBase.class : Accessory.class)) {

				pool.add(item.getType(), item.getChance() * (
					item.getWarrior() * Player.instance.getWarrior() +
						item.getMage() * Player.instance.getManaMax() +
						item.getRanged() * Player.instance.getRanger()
				));
			}
		}

		return pool.generate();
	}

	@Override
	protected AnimationData getClosedAnim() {
		return closed;
	}

	@Override
	protected AnimationData getOpenAnim() {
		return open;
	}

	@Override
	protected AnimationData getOpenedAnim() {
		return openend;
	}
}
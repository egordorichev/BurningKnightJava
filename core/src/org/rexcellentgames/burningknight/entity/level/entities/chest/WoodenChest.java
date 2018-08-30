package org.rexcellentgames.burningknight.entity.level.entities.chest;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.creature.fx.HeartFx;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Gold;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.ItemHolder;
import org.rexcellentgames.burningknight.entity.item.ItemRegistry;
import org.rexcellentgames.burningknight.entity.item.accessory.Accessory;
import org.rexcellentgames.burningknight.entity.item.key.KeyC;
import org.rexcellentgames.burningknight.entity.item.weapon.WeaponBase;
import org.rexcellentgames.burningknight.entity.pool.Pool;
import org.rexcellentgames.burningknight.game.Achievements;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.Random;

public class WoodenChest extends Chest {
	public static Animation animation = Animation.make("chest", "-wooden");
	private static AnimationData closed = animation.get("idle");
	private static AnimationData open = animation.get("opening");
	private static AnimationData openend = animation.get("open");

	@Override
	protected Animation getAnim() {
		return animation;
	}

	@Override
	public Item generate() {
		Pool<Item> pool = new Pool<>();
		weapon = Random.chance(50);

		for (ItemRegistry.Pair item : ItemRegistry.INSTANCE.getItems().values()) {
			if (item.getQuality() == ItemRegistry.Quality.WOODEN && Achievements.unlocked(item.getUnlock()) && (weapon ? WeaponBase.class : Accessory.class).isAssignableFrom(item.getType()) && Player.instance.getInventory().findItem(item.getType()) == null) {

				pool.add(item.getType(), item.getChance() * (
					item.getWarrior() * Player.instance.getWarrior() +
					item.getMage() * Player.instance.getMage() +
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

	@Override
	public void open() {
		super.open();

		if (Random.chance(50)) {
			HeartFx fx = new HeartFx();

			fx.x = this.x + (this.w - fx.w) / 2;
			fx.y = this.y + (this.h - fx.h) / 2;

			Dungeon.area.add(fx);
		}


		if (Random.chance(10)) {
			ItemHolder fx = new ItemHolder();

			fx.setItem(new KeyC());
			fx.x = this.x + (this.w - fx.w) / 2;
			fx.y = this.y + (this.h - fx.h) / 2;

			Dungeon.area.add(fx);
		}

		if (Random.chance(10)) {
			ItemHolder fx = new ItemHolder();

			fx.setItem(new Gold());
			fx.getItem().generate();
			fx.x = this.x + (this.w - fx.w) / 2;
			fx.y = this.y + (this.h - fx.h) / 2;

			Dungeon.area.add(fx);
		}
	}
}
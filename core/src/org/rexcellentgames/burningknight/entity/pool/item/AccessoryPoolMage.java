package org.rexcellentgames.burningknight.entity.pool.item;

import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.accessory.equipable.*;
import org.rexcellentgames.burningknight.entity.item.autouse.ManaHeart;
import org.rexcellentgames.burningknight.entity.item.consumable.food.ManaInABottle;
import org.rexcellentgames.burningknight.entity.item.pet.orbital.FlyingStar;
import org.rexcellentgames.burningknight.entity.item.weapon.dagger.ManaKnife;
import org.rexcellentgames.burningknight.entity.pool.Pool;

public class AccessoryPoolMage extends Pool<Item> {
	public static AccessoryPoolMage instance = new AccessoryPoolMage();

	public AccessoryPoolMage() {
		add(ManaRing.class, 1f);
		add(ManaBottle.class, 1f);
		add(ManaHeart.class, 1f);
		add(ManaKnife.class, 1f);

		add(BlueBomb.class, 1f);
		add(BlueBook.class, 1f);
		add(GreenBook.class, 1f);
		add(RedBook.class, 1f);
		add(YellowBook.class, 1f);
		add(FlyingStar.class, 1f);
		add(ManaShield.class, 1f);
		add(ManaBoots.class, 1f);
		add(ArcaneBattery.class, 1f);
		add(BlueCoin.class, 1f);
		add(BlueHeart.class, 1f);
		add(ManaInABottle.class, 2f);
	}
}
package org.rexellentgames.dungeon.entity.item.pool;

import org.rexellentgames.dungeon.entity.item.accessory.hat.*;

public class ShopHatPool extends Pool<Hat> {
	public static ShopHatPool instance = new ShopHatPool();

	public ShopHatPool() {
		add(RaveHat.class, 1f);
		add(DunceHat.class, 1f);
		add(GoldHat.class, 0.2f);
		add(RubyHat.class, 0.1f);
		add(CoboiHat.class, 0.8f);
		add(FungiHat.class, 1f);
		add(MoaiHat.class, 0.5f);
		add(ShroomHat.class, 0.3f);
		add(SkullHat.class, 0.2f);
		add(UshankaHat.class, 0.3f);
		add(ValkyreHat.class, 1f);
		add(VikingHat.class, 1f);
	}
}
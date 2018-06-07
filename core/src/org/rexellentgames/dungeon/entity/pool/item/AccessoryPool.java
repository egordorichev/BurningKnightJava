package org.rexellentgames.dungeon.entity.pool.item;

import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.entity.item.accessory.equipable.*;
import org.rexellentgames.dungeon.entity.item.pet.Bumbo;
import org.rexellentgames.dungeon.entity.item.pet.LibGDX;
import org.rexellentgames.dungeon.entity.item.pet.Pico8;
import org.rexellentgames.dungeon.entity.item.pet.StrawberryPet;
import org.rexellentgames.dungeon.entity.item.pet.orbital.GooOrbital;
import org.rexellentgames.dungeon.entity.item.reference.Dendy;
import org.rexellentgames.dungeon.entity.item.reference.MagicMushroom;
import org.rexellentgames.dungeon.entity.item.reference.MeetBoy;
import org.rexellentgames.dungeon.entity.item.reference.Switch;
import org.rexellentgames.dungeon.entity.pool.Pool;

public class AccessoryPool extends Pool<Item> {
	public static AccessoryPool instance = new AccessoryPool();

	public AccessoryPool() {
		add(FireFlower.class, 0.3f);
		add(StrawberryPet.class, 0.1f);
		add(Pico8.class, 0.1f);
		add(LibGDX.class, 0.1f);
		add(FireRing.class, 1f);
		add(IceRing.class, 1f);
		add(ThornRing.class, 1f);
		add(MetalRing.class, 1f);
		add(PoisonRing.class, 1f);
		add(BloodRing.class, 1f);
		add(MeetBoy.class, 1f);
		add(Dendy.class, 1f);
		add(MagicMushroom.class, 1f);
		add(Switch.class, 0.1f);
		add(FortuneRing.class, 1f);
		add(GoldRing.class, 1f);
		add(Bumbo.class, 0.5f);
		add(GooOrbital.class, 1f);
		add(VampireRing.class, 1f);
		add(FireExtinguisher.class, 1f);
		add(ObsidianBoots.class, 1f);
		add(CampfireInABottle.class, 1f);
		add(Antidote.class, 1f);
	}
}
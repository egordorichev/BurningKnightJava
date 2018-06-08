package org.rexellentgames.dungeon.entity.pool.item;

import org.rexellentgames.dungeon.entity.item.Compass;
import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.entity.item.accessory.equipable.*;
import org.rexellentgames.dungeon.entity.item.pet.Bumbo;
import org.rexellentgames.dungeon.entity.item.pet.LibGDX;
import org.rexellentgames.dungeon.entity.item.pet.Pico8;
import org.rexellentgames.dungeon.entity.item.pet.StrawberryPet;
import org.rexellentgames.dungeon.entity.item.pet.orbital.*;
import org.rexellentgames.dungeon.entity.item.reference.*;
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
		add(VampireOrbital.class, 1f);
		add(NanoOrbital.class, 1f);
		add(AmmoOrbital.class, 1f);
		add(BombOrbital.class, 1f);
		add(FireExtinguisher.class, 1f);
		add(ObsidianBoots.class, 1f);
		add(CampfireInABottle.class, 1f);
		add(Antidote.class, 1f);
		add(Spectacles.class, 0.1f);
		add(CobaltShield.class, 1f);
		add(MagicShield.class, 1f);
		add(Diamond.class, 0.5f);
		add(Compass.class, 1f);
		add(Star.class, 1f);
		add(PenetrationRune.class, 1f);
		add(BlueWatch.class, 1f);
		add(StopWatch.class, 1f);
		add(TheEye.class, 1f);
		add(LuckyCube.class, 1f);
		add(FortuneArmor.class, 1f);
		add(StopAndPlay.class, 1f);
		add(SwordOrbital.class, 1f);
		add(DewVial.class, 1f);
		add(ChallengeRune.class, 1f);
		add(LuckRune.class, 1f);
		add(StoneHeartRune.class, 1f);
		add(RageRune.class, 1f);
		add(GravityBooster.class, 1f);
		add(Wings.class, 1f);
	}
}
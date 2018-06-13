package org.rexcellentgames.burningknight.entity.level.entities.chest;

import org.rexcellentgames.burningknight.entity.item.consumable.potion.HealingPotion;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.GunB;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.GunC;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.item.Compass;
import org.rexcellentgames.burningknight.entity.item.Gold;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.accessory.hat.SkullHat;
import org.rexcellentgames.burningknight.entity.item.accessory.hat.ValkyreHat;
import org.rexcellentgames.burningknight.entity.item.accessory.hat.VikingHat;
import org.rexcellentgames.burningknight.entity.item.consumable.potion.HealingPotion;
import org.rexcellentgames.burningknight.entity.item.weapon.axe.AxeA;
import org.rexcellentgames.burningknight.entity.item.weapon.axe.AxeB;
import org.rexcellentgames.burningknight.entity.item.weapon.axe.AxeC;
import org.rexcellentgames.burningknight.entity.item.weapon.axe.AxeD;
import org.rexcellentgames.burningknight.entity.item.weapon.bow.BowA;
import org.rexcellentgames.burningknight.entity.item.weapon.bow.BowB;
import org.rexcellentgames.burningknight.entity.item.weapon.dagger.DaggerB;
import org.rexcellentgames.burningknight.entity.item.weapon.dagger.DaggerC;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.GunB;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.GunC;
import org.rexcellentgames.burningknight.entity.item.weapon.rocketlauncher.RocketLauncherB;
import org.rexcellentgames.burningknight.entity.item.weapon.rocketlauncher.RocketLauncherC;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.SwordA;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.SwordB;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.SwordC;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.butcher.ButcherA;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.butcher.ButcherB;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.butcher.ButcherC;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.morning.MorningStarA;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.morning.MorningStarB;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.morning.MorningStarC;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.Log;
import org.rexcellentgames.burningknight.util.Random;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class IronChest extends Chest {
	private static Animation animation = Animation.make("actor-iron-chest");
	private static AnimationData closed = animation.get("closed");
	private static AnimationData open = animation.get("anim");
	private static AnimationData openend = animation.get("open");

	@SuppressWarnings("unchecked")
	private static ArrayList<Class<? extends Item>>[] items = new ArrayList[] {
		new ArrayList(Collections.singletonList(Gold.class)),
		new ArrayList(Arrays.asList(SwordA.class, DaggerB.class, SwordB.class, AxeA.class, ButcherA.class, MorningStarA.class, Compass.class, ButcherB.class, MorningStarB.class, GunB.class, HealingPotion.class, BowA.class, VikingHat.class, RocketLauncherB.class)),
		new ArrayList(Arrays.asList(SwordB.class, DaggerC.class, SwordC.class, AxeB.class, ButcherB.class, MorningStarB.class, Compass.class, AxeC.class, ButcherC.class, MorningStarC.class, GunC.class, HealingPotion.class, BowB.class, ValkyreHat.class, RocketLauncherC.class)),
		new ArrayList(Arrays.asList(AxeD.class, SwordC.class, AxeC.class, ButcherC.class, MorningStarC.class, Compass.class, GunC.class, HealingPotion.class, BowB.class, SkullHat.class, RocketLauncherC.class)),
		new ArrayList(Arrays.asList(AxeD.class, Compass.class))
	};

	private static float[][] chances = new float[][] {
		{ 1 },
		{ 0.4f, 0.3f, 1f, 1.3f, 1, 0.8f, 0.3f, 0.4f, 0.4f, 2f, 1f, 1f, 1f, 1f },
		{ 0.4f, 0.3f, 1f, 1.3f, 1f, 1f, 1f, 0.4f, 0.4f, 0.4f, 2f, 1f, 1f, 1f, 1f },
		{ 1.4f, 2f, 0.1f, 1.3f, 1.2f, 1f, 2f, 1f, 1f, 1f, 1f },
		{ 1, 1f }
	};

	@Override
	public Item generate() {
		try {
			int i = Random.chances(chances[Dungeon.depth]);

			if (i == -1) {
				Log.error("Failed to generate chest item!");
				return new Compass();
			}

			return items[Dungeon.depth].get(i).newInstance();
		} catch (IllegalAccessException | InstantiationException e) {
			e.printStackTrace();
		}

		return new Gold();
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
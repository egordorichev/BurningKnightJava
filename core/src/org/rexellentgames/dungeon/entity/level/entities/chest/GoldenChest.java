package org.rexellentgames.dungeon.entity.level.entities.chest;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.item.Compass;
import org.rexellentgames.dungeon.entity.item.Gold;
import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.entity.item.consumable.potion.HealingPotion;
import org.rexellentgames.dungeon.entity.item.weapon.axe.AxeA;
import org.rexellentgames.dungeon.entity.item.weapon.axe.AxeB;
import org.rexellentgames.dungeon.entity.item.weapon.axe.AxeC;
import org.rexellentgames.dungeon.entity.item.weapon.axe.AxeD;
import org.rexellentgames.dungeon.entity.item.weapon.bow.BowA;
import org.rexellentgames.dungeon.entity.item.weapon.bow.BowB;
import org.rexellentgames.dungeon.entity.item.weapon.dagger.DaggerB;
import org.rexellentgames.dungeon.entity.item.weapon.dagger.DaggerC;
import org.rexellentgames.dungeon.entity.item.weapon.gun.GunC;
import org.rexellentgames.dungeon.entity.item.weapon.sword.SwordA;
import org.rexellentgames.dungeon.entity.item.weapon.sword.SwordB;
import org.rexellentgames.dungeon.entity.item.weapon.sword.SwordC;
import org.rexellentgames.dungeon.entity.item.weapon.sword.butcher.ButcherA;
import org.rexellentgames.dungeon.entity.item.weapon.sword.butcher.ButcherB;
import org.rexellentgames.dungeon.entity.item.weapon.sword.butcher.ButcherC;
import org.rexellentgames.dungeon.entity.item.weapon.sword.morning.MorningStarA;
import org.rexellentgames.dungeon.entity.item.weapon.sword.morning.MorningStarB;
import org.rexellentgames.dungeon.entity.item.weapon.sword.morning.MorningStarC;
import org.rexellentgames.dungeon.util.Animation;
import org.rexellentgames.dungeon.util.AnimationData;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.Random;

import java.util.ArrayList;
import java.util.Arrays;

public class GoldenChest extends Chest {
	private static Animation animation = Animation.make("actor-golden-chest");
	private static AnimationData closed = animation.get("closed");
	private static AnimationData open = animation.get("anim");
	private static AnimationData openend = animation.get("open");

	private static ArrayList<Class<? extends Item>>[] items = new ArrayList[]{
		new ArrayList(Arrays.asList(Gold.class)),
		new ArrayList(Arrays.asList(SwordC.class, DaggerC.class, SwordC.class, AxeB.class, ButcherB.class, MorningStarB.class, Compass.class, GunC.class, HealingPotion.class, BowA.class)),
		new ArrayList(Arrays.asList(SwordC.class, DaggerC.class, SwordC.class, AxeC.class, ButcherC.class, MorningStarC.class, Compass.class, GunC.class, HealingPotion.class, BowB.class)),
		new ArrayList(Arrays.asList(AxeD.class, SwordC.class, AxeC.class, ButcherC.class, MorningStarC.class, Compass.class, GunC.class, HealingPotion.class, BowB.class)),
		new ArrayList(Arrays.asList(AxeD.class, Compass.class))
	};

	private static float[][] chances = new float[][]{
		{1},
		{1, 0.3f, 0.1f, 1, 1, 0.8f, 0.3f, 1f, 1f, 1f},
		{1, 0.3f, 0.1f, 1.2f, 1, 1f, 1f, 1f, 1f, 1f},
		{0.1f, 1, 1.2f, 1, 1f, 1f, 1f, 1f, 1f},
		{1, 1f}
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
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
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
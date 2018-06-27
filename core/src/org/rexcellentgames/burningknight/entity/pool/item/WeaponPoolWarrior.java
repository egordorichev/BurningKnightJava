package org.rexcellentgames.burningknight.entity.pool.item;

import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.reference.GravelordSword;
import org.rexcellentgames.burningknight.entity.item.weapon.axe.*;
import org.rexcellentgames.burningknight.entity.item.weapon.dagger.DaggerA;
import org.rexcellentgames.burningknight.entity.item.weapon.dagger.DaggerB;
import org.rexcellentgames.burningknight.entity.item.weapon.dagger.DaggerC;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.*;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.butcher.ButcherA;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.butcher.ButcherB;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.butcher.ButcherC;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.morning.MorningStarA;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.morning.MorningStarB;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.morning.MorningStarC;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.starwars.LightsaberA;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.starwars.LightsaberB;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.starwars.LightsaberC;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.starwars.LightsaberD;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.tool.*;
import org.rexcellentgames.burningknight.entity.item.weapon.throwing.ThrowingDaggerA;
import org.rexcellentgames.burningknight.entity.item.weapon.throwing.ThrowingDaggerB;
import org.rexcellentgames.burningknight.entity.item.weapon.throwing.ThrowingDaggerC;
import org.rexcellentgames.burningknight.entity.item.weapon.yoyo.YoyoA;
import org.rexcellentgames.burningknight.entity.item.weapon.yoyo.YoyoB;
import org.rexcellentgames.burningknight.entity.item.weapon.yoyo.YoyoC;
import org.rexcellentgames.burningknight.entity.pool.Pool;

public class WeaponPoolWarrior extends Pool<Item> {
	public static WeaponPoolWarrior instance = new WeaponPoolWarrior();

	public WeaponPoolWarrior() {
		add(SwordA.class, 1f);
		add(SwordB.class, 1f);
		add(SwordC.class, 1f);
		add(ButcherA.class, 1f);
		add(ButcherB.class, 1f);
		add(ButcherC.class, 1f);
		add(MorningStarA.class, 1f);
		add(MorningStarB.class, 1f);
		add(MorningStarC.class, 1f);
		add(GravelordSword.class, 1f);
		add(LightsaberA.class, 1f);
		add(LightsaberB.class, 1f);
		add(LightsaberC.class, 1f);
		add(LightsaberD.class, 1f);
		add(ChickenSword.class, 1f);
		add(Shovel.class, 1f);
		add(YoyoA.class, 1f);
		add(YoyoB.class, 1f);
		add(YoyoC.class, 1f);
		add(ThrowingDaggerA.class, 1f);
		add(ThrowingDaggerB.class, 1f);
		add(DaggerA.class, 1f);
		add(DaggerB.class, 1f);
		add(DaggerC.class, 1f);
		add(ThrowingDaggerA.class, 1f);
		add(ThrowingDaggerB.class, 1f);
		add(ThrowingDaggerC.class, 1f);
		add(PickaxeA.class, 1f);
		add(PickaxeB.class, 1f);
		add(PickaxeC.class, 1f);
		add(PickaxeD.class, 1f);
		add(ShovelA.class, 1f);
		add(ShovelB.class, 1f);
		add(ShovelC.class, 1f);
		add(AxeA.class, 1f);
		add(AxeB.class, 1f);
		add(AxeC.class, 1f);
		add(AxeD.class, 1f);
		add(MeetboyAxe.class, 1);
		add(DiamondSword.class, 0.6f);
	}
}
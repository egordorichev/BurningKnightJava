package org.rexcellentgames.burningknight.entity.pool.item;

import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.reference.BlueBoomerang;
import org.rexcellentgames.burningknight.entity.item.reference.IsaacHead;
import org.rexcellentgames.burningknight.entity.item.reference.StarCannon;
import org.rexcellentgames.burningknight.entity.item.weapon.axe.*;
import org.rexcellentgames.burningknight.entity.item.weapon.bow.BowA;
import org.rexcellentgames.burningknight.entity.item.weapon.bow.BowB;
import org.rexcellentgames.burningknight.entity.item.weapon.bow.BowC;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.*;
import org.rexcellentgames.burningknight.entity.item.weapon.throwing.ThrowingDaggerA;
import org.rexcellentgames.burningknight.entity.item.weapon.throwing.ThrowingDaggerB;
import org.rexcellentgames.burningknight.entity.item.weapon.throwing.ThrowingDaggerC;
import org.rexcellentgames.burningknight.entity.pool.Pool;

public class WeaponPoolRanger extends Pool<Item> {
	public static WeaponPoolRanger instance = new WeaponPoolRanger();

	public WeaponPoolRanger() {
		add(BowA.class, 1f);
		add(BowB.class, 1f);
		add(BowC.class, 1f);
		add(AxeA.class, 1f);
		add(AxeB.class, 1f);
		add(AxeC.class, 1f);
		add(AxeD.class, 1f);
		add(MeetboyAxe.class, 1f);
		add(BlueBoomerang.class, 1f);
		add(Revolver.class, 1f);
		add(MachineGun.class, 1f);
		add(TripleMachineGun.class, 1f);
		add(BackGun.class, 1f);
		add(IsaacHead.class, 1f);
		add(StarCannon.class, 1f);
		add(ThrowingDaggerA.class, 1f);
		add(ThrowingDaggerB.class, 1f);
		add(ThrowingDaggerC.class, 1f);
		add(Pistol.class, 1f);
		add(Chopper.class, 1f);
		add(Riffle.class, 1f);
	}
}
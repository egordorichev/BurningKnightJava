package org.rexcellentgames.burningknight.entity.level.rooms.special;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.ItemHolder;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.entities.Slab;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.entity.pool.item.AccessoryPool;
import org.rexcellentgames.burningknight.entity.pool.item.ShopHatPool;
import org.rexcellentgames.burningknight.entity.pool.item.ShopWeaponPool;
import org.rexcellentgames.burningknight.util.Random;

import java.util.ArrayList;

public class ShopRoom extends LockedRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		Painter.fill(level, this, 1, Terrain.randomFloor());

		int c = (this.getWidth() - 2) / 2;

		switch (Random.newInt(6)) {
			case 0: paintArmor(c); break;
			case 1: paintWeapon(c); break;
			case 2: paintAccessory(c); break;
			case 3: case 4: case 5: paintMixed(c); break;
		}
	}

	@Override
	public int getMinWidth() {
		return 12;
	}

	@Override
	public int getMaxWidth() {
		return 18;
	}

	@Override
	public int getMinHeight() {
		return 6;
	}

	@Override
	public int getMaxHeight() {
		return 7;
	}

	private void paintArmor(int c) {
		ArrayList<Item> items = new ArrayList<>();

		for (int i = 0; i < c; i++) {
			items.add(ShopHatPool.instance.generate());
		}

		placeItems(items);
	}

	private void paintWeapon(int c) {
		ArrayList<Item> items = new ArrayList<>();

		for (int i = 0; i < c; i++) {
			items.add(ShopWeaponPool.instance.generate());
		}

		placeItems(items);
	}

	private void paintAccessory(int c) {
		ArrayList<Item> items = new ArrayList<>();

		for (int i = 0; i < c; i++) {
			items.add(AccessoryPool.instance.generate());
		}

		placeItems(items);
	}

	private void paintMixed(int c) {
		ArrayList<Item> items = new ArrayList<>();

		for (int i = 0; i < c; i++) {
			switch (Random.newInt(3)) {
				case 0: items.add(ShopWeaponPool.instance.generate()); break;
				case 1: items.add(ShopHatPool.instance.generate()); break;
				case 2: items.add(AccessoryPool.instance.generate()); break;
			}
		}

		placeItems(items);
	}

	private void placeItems(ArrayList<Item> items) {
		int i = 0;

		for (int x = 2; x < items.size() * 2; x += 2) {
			Slab slab = new Slab();

			slab.x = (this.left + x) * 16 + 1;
			slab.y = (this.top + 3) * 16 - 4;

			LevelSave.add(slab);
			Dungeon.area.add(slab);

			ItemHolder holder = new ItemHolder();

			holder.x = (this.left + x) * 16 + 4;
			holder.y = (this.top + 3) * 16;
			holder.setItem(items.get(i));

			LevelSave.add(holder);
			Dungeon.area.add(holder);

			i++;
		}
	}
}
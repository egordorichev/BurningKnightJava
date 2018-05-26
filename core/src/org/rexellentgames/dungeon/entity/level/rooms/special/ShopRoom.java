package org.rexellentgames.dungeon.entity.level.rooms.special;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.entity.item.ItemHolder;
import org.rexellentgames.dungeon.entity.pool.AccessoryPool;
import org.rexellentgames.dungeon.entity.pool.ShopHatPool;
import org.rexellentgames.dungeon.entity.pool.ShopWeaponPool;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.entities.Slab;
import org.rexellentgames.dungeon.entity.level.painters.Painter;
import org.rexellentgames.dungeon.util.Random;

import java.util.ArrayList;

public class ShopRoom extends LockedRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		Painter.fill(level, this, 1, Terrain.FLOOR_B);

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

			Dungeon.level.addSaveable(slab);
			Dungeon.area.add(slab);

			ItemHolder holder = new ItemHolder();

			holder.x = (this.left + x) * 16 + 4;
			holder.y = (this.top + 3) * 16;
			holder.setItem(items.get(i));

			Dungeon.level.addSaveable(holder);
			Dungeon.area.add(holder);

			i++;
		}
	}
}
package org.rexcellentgames.burningknight.entity.level.rooms.shop;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.creature.npc.Shopkeeper;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Gold;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.ItemHolder;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.entities.Slab;
import org.rexcellentgames.burningknight.entity.level.features.Door;
import org.rexcellentgames.burningknight.entity.level.rooms.special.LockedRoom;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.entity.pool.Pool;
import org.rexcellentgames.burningknight.entity.pool.item.*;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

import java.util.ArrayList;

public class ShopRoom extends LockedRoom {
	protected void placeItems() {
		int c = getItemCount();

		switch (Random.newInt(6)) {
			case 0: paintArmor(c); break;
			case 1: paintWeapon(c); break;
			case 2: paintAccessory(c); break;
			case 3: case 4: case 5: paintMixed(c); break;
		}

		Point point = getSpawn();

		Shopkeeper npc = new Shopkeeper();

		npc.x = point.x * 16;
		npc.generate();
		npc.y = point.y * 16;

		LevelSave.add(npc);
		Dungeon.area.add(npc);

		if (Random.chance(30)) {
			for (int i = 0; i < Random.newInt(1, 4); i++) {
				ItemHolder holder = new ItemHolder();
				holder.setItem(new Gold()).getItem().generate();

				Point p = this.getRandomFreeCell();

				holder.x = p.x * 16 + (16 - holder.getItem().getSprite().getRegionWidth()) / 2;
				holder.y = p.y * 16 + (16 - holder.getItem().getSprite().getRegionHeight()) / 2;

				Dungeon.area.add(holder);
				LevelSave.add(holder);
			}
		}
	}

	protected Point getSpawn() {
		return this.getRandomFreeCell();
	}

	@Override
	public void paint(Level level) {
		super.paint(level);

		if (Random.chance(25)) {
			for (Door door : this.connected.values()) {
				door.setType(Door.Type.SECRET);
			}

			hidden = true;
		}
	}

	protected int getItemCount() {
		return (this.getWidth() - 1) / 2;
	}

	@Override
	protected int validateWidth(int w) {
		return (int) (Math.floor(w / 2) * 2);
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
		return 8;
	}

	@Override
	public int getMaxHeight() {
		return 9;
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

		Pool pool = getWeaponPool();

		for (int i = 0; i < c; i++) {
			items.add((Item) pool.generate());
		}

		placeItems(items);
	}

	private void paintAccessory(int c) {
		ArrayList<Item> items = new ArrayList<>();

		Pool all = new Pool<Item>();
		Pool pool = getAccessoryPool();

		all.addFrom(pool);
		all.addFrom(AccessoryPoolAll.instance);

		for (int i = 0; i < c; i++) {
			items.add((Item) all.generate());
		}

		placeItems(items);
	}

	private Pool getAccessoryPool() {
		switch (Player.instance.getType()) {
			case WARRIOR: default: return AccessoryPoolWarrior.instance;
			case WIZARD: return AccessoryPoolMage.instance;
			case RANGER: return AccessoryPoolRanger.instance;
		}
	}

	private Pool getWeaponPool() {
		switch (Player.instance.getType()) {
			case WARRIOR: default: return WeaponPoolWarrior.instance;
			case WIZARD: return WeaponPoolMage.instance;
			case RANGER: return WeaponPoolRanger.instance;
		}
	}

	private void paintMixed(int c) {
		ArrayList<Item> items = new ArrayList<>();

		Pool weapon = getWeaponPool();
		Pool accessory = getAccessoryPool();

		for (int i = 0; i < c; i++) {
			switch (Random.newInt(3)) {
				case 0: items.add((Item) weapon.generate()); break;
				case 1: items.add(ShopHatPool.instance.generate()); break;
				case 2: items.add((Item) accessory.generate()); break;
			}
		}

		placeItems(items);
	}

	protected void placeItems(ArrayList<Item> items) {
		int i = 0;

		for (int x = 2; x < items.size() * 2; x += 2) {
			placeItem(items.get(i), (this.left + x) * 16 + 1, (this.top + 3) * 16 - 4);
			i++;
		}
	}

	protected void placeItem(Item item, int x, int y) {
		Slab slab = new Slab();

		slab.x = x + 1;
		slab.y = y - 4;

		LevelSave.add(slab);
		Dungeon.area.add(slab);

		ItemHolder holder = new ItemHolder();

		holder.setItem(item);
		holder.x = x + (16 - holder.w) / 2;
		holder.y = y + (16 - holder.h) / 2;
		holder.getItem().shop = true;

		int cn = (int) Player.instance.getStat("sale");

		if (cn == 0 && Random.chance(33)) {
			cn ++;
		}

		for (int j = 0; j < cn; j++) {
			holder.sale();
		}

		LevelSave.add(holder);
		Dungeon.area.add(holder);
	}
}
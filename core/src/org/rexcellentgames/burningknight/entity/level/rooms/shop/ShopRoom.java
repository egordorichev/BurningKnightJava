package org.rexcellentgames.burningknight.entity.level.rooms.shop;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.creature.npc.BlueShopkeeper;
import org.rexcellentgames.burningknight.entity.creature.npc.OrangeShopkeeper;
import org.rexcellentgames.burningknight.entity.creature.npc.Shopkeeper;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Gold;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.ItemHolder;
import org.rexcellentgames.burningknight.entity.item.ItemRegistry;
import org.rexcellentgames.burningknight.entity.item.accessory.Accessory;
import org.rexcellentgames.burningknight.entity.item.weapon.WeaponBase;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.entities.Slab;
import org.rexcellentgames.burningknight.entity.level.features.Door;
import org.rexcellentgames.burningknight.entity.level.rooms.Room;
import org.rexcellentgames.burningknight.entity.level.rooms.connection.ConnectionRoom;
import org.rexcellentgames.burningknight.entity.level.rooms.special.LockedRoom;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.entity.pool.Pool;
import org.rexcellentgames.burningknight.entity.pool.item.ShopHatPool;
import org.rexcellentgames.burningknight.game.Achievements;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

import java.util.ArrayList;

public class ShopRoom extends LockedRoom {
	private boolean hd;

	public ShopRoom() {
		hd = Random.chance(30);
	}

	private boolean doublePrice;

	protected void placeItems() {
		int c = getItemCount();

		Shopkeeper npc = null;

		switch (Random.newInt(4)) {
			case 0: case 1: npc = new Shopkeeper(); break;
			case 2: npc = new BlueShopkeeper(); break;
			case 3: npc = new OrangeShopkeeper(); doublePrice = true;
		}

		switch (Random.newInt(6)) {
			// case 0: paintArmor(c); break;
			// case 1: paintWeapon(c); break;
			// case 2: paintAccessory(c); break;
			// case 3: case 4: case 5: paintMixed(c); break;
		}

		paintMixed(c);

		Point point = getSpawn();


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

		if (hd) {
			for (Door door : this.connected.values()) {
				door.setType(Door.Type.SECRET);
			}

			hidden = true;
		}
	}

	@Override
	public boolean canConnect(Room r) {
		if ((this.hd && r instanceof ConnectionRoom)) {
			return false;
		}

		return super.canConnect(r);
	}


	protected int getItemCount() {
		return (this.getWidth() - 2) / 2;
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

		Pool<Item> pool = getWeaponPool();

		for (int i = 0; i < c; i++) {
			items.add(pool.generate());
		}

		placeItems(items);
	}

	private void paintAccessory(int c) {
		ArrayList<Item> items = new ArrayList<>();

		Pool pool = getAccessoryPool();

		for (int i = 0; i < c; i++) {
			items.add((Item) pool.generate());
		}

		placeItems(items);
	}

	public static Pool<Item> getAccessoryPool() {
		Pool<Item> pool = new Pool<>();

		for (ItemRegistry.Pair item : ItemRegistry.INSTANCE.getItems().values()) {
			if (Accessory.class.isAssignableFrom(item.getType()) && Achievements.unlocked(item.getUnlock()) && Player.instance.getInventory().findItem(item.getType()) == null) {
				pool.add(item.getType(), item.getChance() * (
					item.getWarrior() * Player.instance.getWarrior() +
						item.getMage() * Player.instance.getMage() +
						item.getRanged() * Player.instance.getRanger()
				));
			}
		}

		return pool;
	}

	public static Pool<Item> getWeaponPool() {
		Pool<Item> pool = new Pool<>();

		for (ItemRegistry.Pair item : ItemRegistry.INSTANCE.getItems().values()) {
			if (WeaponBase.class.isAssignableFrom(item.getType()) && Achievements.unlocked(item.getUnlock()) && Player.instance.getInventory().findItem(item.getType()) == null) {
				pool.add(item.getType(), item.getChance() * (
					item.getWarrior() * Player.instance.getWarrior() +
						item.getMage() * Player.instance.getMage() +
						item.getRanged() * Player.instance.getRanger()
				));
			}
		}

		return pool;
	}

	private void paintMixed(int c) {
		ArrayList<Item> items = new ArrayList<>();

		Pool weapon = getWeaponPool();
		Pool accessory = getAccessoryPool();

		for (int i = 0; i < c; i++) {
			switch (Random.newInt(2)) {
				case 0: items.add((Item) weapon.generate()); break;
				case 1: items.add((Item) accessory.generate()); break;

				// case 2: items.add(ShopHatPool.instance.generate()); break;
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

		if (this.doublePrice) {
			holder.getItem().price += 5;
		}

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
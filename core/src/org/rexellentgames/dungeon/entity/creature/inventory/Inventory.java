package org.rexellentgames.dungeon.entity.creature.inventory;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.creature.Creature;
import org.rexellentgames.dungeon.entity.creature.fx.TextFx;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.item.Gold;
import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.entity.item.ItemHolder;
import org.rexellentgames.dungeon.entity.level.SaveableEntity;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.file.FileReader;
import org.rexellentgames.dungeon.util.file.FileWriter;

import java.io.IOException;
import java.lang.reflect.Constructor;

public class Inventory {
	private Creature creature;
	private Item[] slots;
	public int active;

	public Inventory(Creature creature, int size) {
		this.creature = creature;
		this.slots = new Item[size];
	}

	public void load(FileReader reader) throws IOException {
		this.active = reader.readByte();

		for (int i = 0; i < this.getSize(); i++) {
			if (reader.readBoolean()) {
				String type = reader.readString();

				try {
					Class<?> clazz = Class.forName(type);
					Constructor<?> constructor = clazz.getConstructor();
					Object object = constructor.newInstance(new Object[]{});

					Item item = (Item) object;
					item.load(reader);
					item.setOwner(Player.instance);

					this.setSlot(i, item);
				} catch (Exception e) {
					Dungeon.reportException(e);
				}
			}
		}
	}

	public void save(FileWriter writer) throws IOException {
		writer.writeByte((byte) this.active);

		for (int i = 0; i < this.getSize(); i++) {
			Item slot = this.getSlot(i);

			if (slot == null) {
				writer.writeBoolean(false);
			} else {
				writer.writeBoolean(true);
				writer.writeString(slot.getClass().getName());

				slot.save(writer);
			}
		}
	}

	public boolean add(ItemHolder holder) {
		Item item = holder.getItem();

		if (item instanceof Gold) {
			Item slot = this.getSlot(11);

			Log.info("Gold " + item.getCount());

			if (slot == null) {
				this.setSlot(11, item);
			} else {
				slot.setCount(slot.getCount() + item.getCount());
			}

			slot = this.getSlot(11);
			Log.info("Ended with " + slot.getCount());

			item.onPickup();
			holder.done = true;

			item.setOwner(Player.instance);
			return true;
		}

		if (item.isStackable()) {
			for (int i = 0; i < this.getSize(); i++) {
				Item slot = this.getSlot(i);

				if (slot != null && slot.getClass() == item.getClass() && UiSlot.canAccept(i, slot)) {
					slot.setCount(slot.getCount() + item.getCount());
					item.onPickup();
					holder.done = true;

					item.setOwner(Player.instance);
					return true;
				}
			}
		}

		for (int i = 0; i < this.getSize(); i++) {
			if (this.isEmpty(i) && UiSlot.canAccept(i, null)) {
				this.setSlot(i, item);
				item.onPickup();
				holder.done = true;

				item.setOwner(Player.instance);
				return true;
			}
		}

		Dungeon.area.add(new TextFx("No Space", Player.instance).setColor(Dungeon.ORANGE));

		return false;
	}

	public boolean find(Class<? extends Item> clazz) {
		for (int i = 0; i < this.getSize(); i++) {
			if (!this.isEmpty(i)) {
				if (clazz.isInstance(this.getSlot(i))) {
					return true;
				}
			}
		}

		return false;
	}

	public Item findItem(Class<? extends Item> clazz) {
		for (int i = 0; i < this.getSize(); i++) {
			if (!this.isEmpty(i)) {
				if (clazz.isInstance(this.getSlot(i))) {
					return this.getSlot(i);
				}
			}
		}

		return null;
	}

	public Item remove(Class<? extends Item> clazz) {
		for (int i = 0; i < this.getSize(); i++) {
			if (!this.isEmpty(i)) {
				Item item = this.getSlot(i);

				if (clazz.isInstance(item)) {
					item.setCount(item.getCount() - 1);

					if (item.getCount() == 0) {
						this.setSlot(i, null);
					}

					return item;
				}
			}
		}

		return null;
	}

	public boolean isEmpty(int i) {
		return this.getSlot(i) == null || this.getSlot(i).getCount() == 0;
	}

	public Item getSlot(int i) {
		return this.slots[i];
	}

	public void setSlot(int i, Item item) {
		this.slots[i] = item;
	}

	public int getSize() {
		return this.slots.length;
	}

	public Creature getCreature() {
		return this.creature;
	}
}
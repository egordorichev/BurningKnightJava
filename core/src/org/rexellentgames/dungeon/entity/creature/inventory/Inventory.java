package org.rexellentgames.dungeon.entity.creature.inventory;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.creature.Creature;
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

	public Inventory(Creature creature, int size) {
		this.creature = creature;
		this.slots = new Item[size];
	}

	public void load(FileReader reader) throws IOException {
		for (int i = 0; i < this.getSize(); i++) {
			if (reader.readBoolean()) {
				String type = reader.readString();
				
				try {
					Class<?> clazz = Class.forName(type);
					Constructor<?> constructor = clazz.getConstructor();
					Object object = constructor.newInstance(new Object[]{});

					Item item = (Item) object;
					item.load(reader);

					this.setSlot(i, item);
				} catch (Exception e) {
					Dungeon.reportException(e);
				}
			}
		}
	}

	public void save(FileWriter writer) throws IOException {
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

		if (item.isStackable()) {
			for (int i = 0; i < this.getSize(); i++) {
				Item slot = this.getSlot(i);

				if (slot != null && slot.getClass() == item.getClass()) {
					slot.setCount(slot.getCount() + item.getCount());
					holder.done = true;

					return true;
				}
			}
		}

		for (int i = 0; i < this.getSize(); i++) {
			if (this.isEmpty(i)) {
				this.setSlot(i, item);
				holder.done = true;

				return true;
			}
		}

		return false;
	}

	public boolean isEmpty(int i) {
		return this.getSlot(i) == null;
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
package org.rexcellentgames.burningknight.entity.creature.inventory;

import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.ItemHolder;
import org.rexcellentgames.burningknight.entity.item.accessory.equippable.Equippable;
import org.rexcellentgames.burningknight.entity.item.autouse.Autouse;
import org.rexcellentgames.burningknight.entity.item.entity.PickupFx;
import org.rexcellentgames.burningknight.entity.item.weapon.WeaponBase;
import org.rexcellentgames.burningknight.util.Tween;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;
import org.rexcellentgames.burningknight.util.geometry.Point;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;

public class Inventory {
	private Creature creature;
	private Item[] slots;
	private ArrayList<Item> spaces = new ArrayList<>();
	public int active;

	public Inventory(Creature creature) {
		this.creature = creature;
		this.slots = new Item[3];
	}

	public void clear() {
		this.slots = new Item[3];
		spaces.clear();
	}

	public void load(FileReader reader) throws IOException {
		this.active = reader.readByte();

		for (int i = 0; i < 3; i++) {
			if (reader.readBoolean()) {
				String type = reader.readString();

				try {
					Class<?> clazz = Class.forName(type);
					Constructor<?> constructor = clazz.getConstructor();
					Object object = constructor.newInstance();

					Item item = (Item) object;
					item.load(reader);
					item.setOwner(Player.instance);

					this.setSlot(i, item);
				} catch (Exception e) {
					Dungeon.reportException(e);
				}
			}
		}

		short spaces = reader.readInt16();

		for (int i = 0; i < spaces; i++) {
			String type = reader.readString();

			try {
				Class<?> clazz = Class.forName(type);
				Constructor<?> constructor = clazz.getConstructor();
				Object object = constructor.newInstance();

				Item item = (Item) object;
				item.load(reader);
				item.setOwner(Player.instance);

				if (item instanceof Equippable) {
					((Equippable) item).onEquip(true);
				}

				this.spaces.add(item);
			} catch (Exception e) {
				Dungeon.reportException(e);
			}
		}
	}

	public void save(FileWriter writer) throws IOException {
		writer.writeByte((byte) this.active);

		for (int i = 0; i < slots.length; i++) {
			Item slot = this.getSlot(i);

			if (slot == null) {
				writer.writeBoolean(false);
			} else {
				writer.writeBoolean(true);
				writer.writeString(slot.getClass().getName());

				slot.save(writer);
			}
		}

		writer.writeInt16((short) this.spaces.size());

		for (int i = 0; i < spaces.size(); i++) {
			Item slot = spaces.get(i);

			writer.writeString(slot.getClass().getName());
			slot.save(writer);
		}
	}

	public boolean add(ItemHolder holder) {
		Item item = holder.getItem();

		if (item == null) {
			return false;
		}

		if (item instanceof WeaponBase) {
			for (int i = 0; i < 3; i++) {
				if (this.isEmpty(i) && (i != 2)) {
					this.setSlot(i, item);
					item.setOwner(Player.instance);
					item.onPickup();
					holder.done = true;

					if (item instanceof Equippable) {
						((Equippable) item).onEquip(false);
					}

					this.onAdd(holder, i);
					return true;
				}
			}
		}

		if (item instanceof Autouse) {
			int c = item.getCount();
			for (int j = 0; j < c; j++) {
				item.use();
			}
		} else if (item instanceof Equippable) {
			((Equippable) item).onEquip(false);
		}


		this.onAdd(holder, 0);
		this.spaces.add(holder.getItem());
		return true;
	}

	private void onAdd(ItemHolder holder, int slot) {
		final Item item = holder.getItem();

		item.setOwner(Player.instance);
		item.a = 0;

		Tween.to(new Tween.Task(1, 0.3f) {
			@Override
			public float getValue() {
				return item.a;
			}

			@Override
			public void setValue(float value) {
				item.a = value;
			}
		});

		PickupFx fx = new PickupFx();

		fx.x = holder.x + holder.w / 2;
		fx.y = holder.y + holder.h / 2;
		fx.region = item.getSprite();
		fx.target = new Point(Camera.game.position.x - Display.GAME_WIDTH / 2 * Camera.game.zoom, Camera.game.position.y - Display.GAME_HEIGHT / 2 * Camera.game.zoom); // todo: fix

		Dungeon.area.add(fx);
	}

	public boolean find(Class clazz) {
		for (int i = 0; i < 3; i++) {
			if (!this.isEmpty(i)) {
				if (clazz.isInstance(this.getSlot(i))) {
					return true;
				}
			}
		}

		return findEquipped(clazz);
	}

	public boolean findEquipped(Class clazz) {
		for (Item item : spaces) {
			if (clazz.isInstance(item)) {
				return true;
			}
		}

		return false;
	}

	public Item findItem(Class clazz) {
		for (int i = 0; i < 3; i++) {
			if (!this.isEmpty(i)) {
				if (clazz.isInstance(this.getSlot(i))) {
					return this.getSlot(i);
				}
			}
		}

		for (Item item : spaces) {
			if (clazz.isInstance(item)) {
				return item;
			}
		}

		return null;
	}

	public Item remove(Class clazz) {
		for (int i = 0; i < 3; i++) {
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

		for (int i = spaces.size() - 1; i >= 0; i--) {
			Item item = spaces.get(i);

			if (clazz.isInstance(item)) {
				item.setCount(item.getCount() - 1);

				if (item.getCount() == 0) {
					spaces.remove(item);
				}

				return item;
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

	public int getSpace() {
		return spaces.size();
	}

	public Item getSpace(int i) {
		return this.spaces.get(i);
	}

	public void setSlot(int i, Item item) {
		this.slots[i] = item;
	}

	public int getSize() {
		return 3;
	}

	public Creature getCreature() {
		return this.creature;
	}
}
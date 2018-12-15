package org.rexcellentgames.burningknight.entity.creature.inventory;

import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Gold;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.ItemHolder;
import org.rexcellentgames.burningknight.entity.item.accessory.equippable.Equippable;
import org.rexcellentgames.burningknight.entity.item.entity.PickupFx;
import org.rexcellentgames.burningknight.util.Log;
import org.rexcellentgames.burningknight.util.Tween;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;
import org.rexcellentgames.burningknight.util.geometry.Point;

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

	public void clear() {
		this.slots = new Item[this.slots.length];
	}

	public void resize(int size) {
		Item[] old = this.slots;
		this.slots = new Item[size];

		System.arraycopy(old, 0, this.slots, 0, old.length);
	}

	public int getGold() {
		int c = 0;

		for (int i = 0; i < getSize(); i++) {
			Item item = this.getSlot(i);

			if (item instanceof Gold) {
				c += item.getCount();
			}
		}

		return c;
	}

	public void removeGold(int c) {
		for (int i = 0; i < getSize(); i++) {
			Item item = this.getSlot(i);

			if (item instanceof Gold) {
				int r = item.getCount();

				if (r == c) {
					this.setSlot(i, null);
					break;
				} else if (r < c) {
					this.setSlot(i, null);
					c -= r;
				} else {
					item.setCount(r - c);
					return;
				}
			}
		}
	}

	public void load(FileReader reader) throws IOException {
		this.active = reader.readByte();

		for (int i = 0; i < this.getSize(); i++) {
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
			/*
			Item slot = this.getSlot(11);

			if (slot == null) {
				this.setSlot(11, item);
			} else {
				slot.setCount(slot.getCount() + item.getCount());
			}

			item.onPickup();
			holder.done = true;

			this.onAdd(holder, 11);

			return true;*/
		}

		if (item.isStackable()) {
			for (int i = 0; i < this.getSize(); i++) {
				Item slot = this.getSlot(i);

				if (slot != null && slot.getClass() == item.getClass() && UiSlot.canAccept(i, slot)) {
					slot.setCount(slot.getCount() + item.getCount());
					item.setOwner(Player.instance);
					item.onPickup();
					holder.done = true;

					this.onAdd(holder, i);
					return true;
				}
			}
		}

		for (int i = 0; i < this.getSize(); i++) {
			if (this.isEmpty(i) && UiSlot.canAccept(i, item)) {
				boolean found = false;

				if (i > 3 && i < 8 && item instanceof Equippable) {
					for (int j = 4; j < 8; j++) {
						Item sl = getSlot(j);

						if (sl != null && sl.getClass().isInstance(item)) {
							found = true;
							break;
						}
					}

					if (found) {
						continue;
					}

					((Equippable) item).onEquip(false);
				}

				this.setSlot(i, item);
				item.setOwner(Player.instance);
				item.onPickup();
				holder.done = true;

				this.onAdd(holder, i);

				return true;
			}
		}

		Log.error("Can't pickup item " + item);
		Player.instance.playSfx("item_nocash");
		// Dungeon.area.add(new TextFx("No Space", Player.instance).setColor(Dungeon.ORANGE));

		return false;
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


	public boolean findEquipped(Class<? extends Item> clazz) {
		for (int i = 4; i < 8; i++) {
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
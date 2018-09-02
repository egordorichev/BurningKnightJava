package org.rexcellentgames.burningknight.entity.creature.npc;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.ItemRegistry;
import org.rexcellentgames.burningknight.entity.level.SaveableEntity;
import org.rexcellentgames.burningknight.game.Achievements;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;

import java.io.IOException;
import java.util.Map;

public class Upgrade extends SaveableEntity {
	{
		depth = -1;
	}

	public enum Type {
		CONSUMABLE(0),
		WEAPON(1),
		ACCESSORY(2),

		NONE(3);

		Type(int id) {
			this.id = (byte) id;
		}

		byte id;
	}

	public Type type = Type.CONSUMABLE;
	private Item item;
	private String str;

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);
		type = Type.values()[reader.readByte()];

		try {
			this.str = reader.readString();
			this.item = ItemRegistry.INSTANCE.getItems().get(str).getType().newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	private boolean hidden;

	protected Item generateItem() {
		for (Map.Entry<String, ItemRegistry.Pair> entry : ItemRegistry.INSTANCE.getItems().entrySet()) {
			if (!entry.getValue().getBusy() && entry.getValue().getPool() == this.type && !Achievements.unlocked("SHOP_" + entry.getKey().toUpperCase())) {
				this.str = entry.getKey();
				entry.getValue().setBusy(true);

				try {
					return entry.getValue().getType().newInstance();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);

		this.checkItem();

		writer.writeByte(type.id);
		writer.writeString(this.str);
	}

	@Override
	public void update(float dt) {
		if (this.hidden) {
			return;
		}

		this.checkItem();

		super.update(dt);
	}

	private void checkItem() {
		if (this.item == null) {
			this.item = this.generateItem();

			if (this.item == null) {
				this.hidden = true;
			}
		}
	}

	@Override
	public void render() {
		if (hidden || item == null) {
			return;
		}

		TextureRegion reg = item.getSprite();

		Graphics.render(reg, this.x + (16 - reg.getRegionWidth()) / 2, this.y + (16 - reg.getRegionHeight()) / 2);
	}
}
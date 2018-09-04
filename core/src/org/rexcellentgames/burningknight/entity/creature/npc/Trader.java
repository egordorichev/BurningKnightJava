package org.rexcellentgames.burningknight.entity.creature.npc;

import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;

import java.io.IOException;
import java.util.ArrayList;

public class Trader extends Npc {
	public static ArrayList<Trader> all = new ArrayList<>();

	public boolean saved = false;
	public String id;

	@Override
	public void init() {
		super.init();
		all.add(this);
	}

	@Override
	public void destroy() {
		super.destroy();
		all.remove(this);
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);
		writer.writeBoolean(this.saved);
		writer.writeString(this.id);
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);
		this.saved = reader.readBoolean();
		this.id = reader.readString();
	}

	@Override
	public void update(float dt) {
		if (!this.saved) {
			return;
		}

		super.update(dt);
	}

	@Override
	public void render() {
		if (!this.saved) {
			return;
		}

		Graphics.render(Item.missing, this.x, this.y);
	}
}
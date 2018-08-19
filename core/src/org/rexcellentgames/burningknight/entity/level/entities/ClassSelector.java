package org.rexcellentgames.burningknight.entity.level.entities;

import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.ItemHolder;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.Revolver;
import org.rexcellentgames.burningknight.entity.item.weapon.magic.MagicMissileWand;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.SwordA;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;

import java.io.IOException;

public class ClassSelector extends ItemHolder {
	public String id;

	public ClassSelector(String id) {
		this.id = id;
		this.parseId();
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);
		this.id = reader.readString();
		this.parseId();
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);
		writer.writeString(this.id);
	}

	public ClassSelector() {

	}

	public void parseId() {
		switch (this.id) {
			case "ranger": this.setItem(new Revolver()); break;
			case "warrior": this.setItem(new SwordA()); break;
			case "wizard": this.setItem(new MagicMissileWand()); break;
		}
	}

	public boolean same(Player.Type type) {
		switch (type) {
			case WARRIOR: return this.id.equals("warrior");
			case WIZARD: return this.id.equals("wizard");
			case RANGER: default: return this.id.equals("ranger");
		}
	}
}